package LikeLion.TodaysLunch.restaurant.service;

import static LikeLion.TodaysLunch.restaurant.repository.SpecificationBuilder.buildSpecification;

import LikeLion.TodaysLunch.customized.domain.MemberLocationCategory;
import LikeLion.TodaysLunch.customized.repository.MemberLocationCategoryRepository;
import LikeLion.TodaysLunch.category.domain.FoodCategory;
import LikeLion.TodaysLunch.category.domain.LocationCategory;
import LikeLion.TodaysLunch.category.domain.LocationTag;
import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.category.domain.RecommendCategory;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import LikeLion.TodaysLunch.customized.domain.MyStore;
import LikeLion.TodaysLunch.restaurant.domain.RestaurantContributor;
import LikeLion.TodaysLunch.restaurant.dto.ContributorDto;
import LikeLion.TodaysLunch.restaurant.dto.ParticipateRestaurantDto;
import LikeLion.TodaysLunch.restaurant.dto.RestaurantDto;
import LikeLion.TodaysLunch.restaurant.dto.RestaurantListDto;
import LikeLion.TodaysLunch.exception.NotFoundException;
import LikeLion.TodaysLunch.restaurant.dto.RestaurantRecommendDto;
import LikeLion.TodaysLunch.restaurant.repository.DataJpaRestaurantRepository;
import LikeLion.TodaysLunch.category.repository.FoodCategoryRepository;
import LikeLion.TodaysLunch.image.repository.ImageUrlRepository;
import LikeLion.TodaysLunch.category.repository.LocationCategoryRepository;
import LikeLion.TodaysLunch.category.repository.LocationTagRepository;
import LikeLion.TodaysLunch.customized.repository.MyStoreRepository;
import LikeLion.TodaysLunch.category.repository.RecommendCategoryRepository;
import LikeLion.TodaysLunch.restaurant.repository.RestaurantContributorRepository;
import LikeLion.TodaysLunch.restaurant.repository.RestaurantSpecification;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantService {

  private final DataJpaRestaurantRepository restaurantRepository;
  private final FoodCategoryRepository foodCategoryRepository;
  private final LocationTagRepository locationTagRepository;
  private final LocationCategoryRepository locationCategoryRepository;
  private final ImageUrlRepository imageUrlRepository;
  private final RecommendCategoryRepository recommendCategoryRepository;
  private final RestaurantContributorRepository restaurantContributorRepository;
  private final MyStoreRepository myStoreRepository;
  private final MemberLocationCategoryRepository memberLocationCategoryRepository;

  public HashMap<String, Object> restaurantList(
      String foodCategoryName, String locationCategoryName,
      String locationTagName, Long recommendCategoryId, String keyword,
      int page, int size, String sort, String order, Member member) {

    Pageable pageable = determineSort(page, size, sort, order);
    Specification<Restaurant> spec = determineFilterCondition(foodCategoryName, locationCategoryName, locationTagName, recommendCategoryId, keyword);

    Page<Restaurant> restaurantList = restaurantRepository.findAll(spec, pageable);
    /* Todo: 하나의 쿼리로 조회하기 */
    List<RestaurantListDto> restaurantDtos = new ArrayList<>(restaurantList.getSize());
    for(Restaurant restaurant: restaurantList){
      Boolean liked = isMyStore(member, restaurant);
      restaurantDtos.add(RestaurantListDto.fromEntity(restaurant, liked));
    }

    HashMap<String, Object> responseMap = new HashMap<>();
    responseMap.put("data", restaurantDtos);
    responseMap.put("totalPages", restaurantList.getTotalPages());
    return responseMap;
  }

  private Specification<Restaurant> determineFilterCondition(
          String foodCategoryName, String locationCategoryName, String locationTagName, Long recommendCategoryId, String keyword) {
    FoodCategory foodCategory = foodCategoryRepository.findByName(foodCategoryName).orElseGet(null);
    LocationCategory locationCategory = locationCategoryRepository.findByName(locationCategoryName).orElseGet(null);
    LocationTag locationTag = locationTagRepository.findByName(locationTagName).orElseGet(null);
    RecommendCategory recommendCategory = recommendCategoryRepository.findById(recommendCategoryId).orElseGet(null);
    return buildSpecification(foodCategory, locationCategory, locationTag, recommendCategory, keyword, false, null);
  }

  public RestaurantDto restaurantDetail(Long id, Member member){
    Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new NotFoundException("맛집"));

    List<ContributorDto> contributors =
        restaurantContributorRepository.findAllByRestaurant(restaurant)
        .stream()
        .map(RestaurantContributor::getMember)
        .map(ContributorDto::fromEntity)
        .collect(Collectors.toList());

    Boolean liked = isMyStore(member, restaurant);
    /* Todo: 하나의 쿼리로 조회하기 */
    String bestReview = null;
    if(restaurant.getBestReview() != null) {
      bestReview = restaurant.getBestReview().getReviewContent();
    }
    return RestaurantDto.fromEntity(restaurant, contributors, liked, bestReview);
  }

  /* Todo: 리팩토링하기 */
  public List<RestaurantRecommendDto> recommendation(Member member){
    List<LocationCategory> locationCategories = memberLocationCategoryRepository.findAllByMember(member)
        .stream().map(MemberLocationCategory::getLocationCategory).collect(Collectors.toList());

    Specification<Restaurant> spec =(root, query, criteriaBuilder) -> null;
    for(LocationCategory locationCategory: locationCategories){
      spec = spec.or(RestaurantSpecification.equalLocationCategory(locationCategory));
    }
    spec = spec.and(RestaurantSpecification.equalJudgement(false));

    List<Restaurant> pool = restaurantRepository.findAll(spec);
    int poolSize = pool.size();

    int index = 0;
    List<Restaurant> recommend = new ArrayList<>();

    int today = LocalDate.now().getDayOfMonth();
    int size = 5 > poolSize ? poolSize:5;
    if(today % poolSize == 0) today++;

    while(size>0){
      index = (index + today) % poolSize;
      recommend.add(pool.get(index));
      size--;
    }

    List<RestaurantRecommendDto> recommendDtos = new ArrayList<>(recommend.size());

    String bestReview;
    for(Restaurant restaurant: recommend){
      bestReview = null;
      if(restaurant.getBestReview() != null) {
        bestReview = restaurant.getBestReview().getReviewContent();
      }
      Boolean liked = isMyStore(member, restaurant);
      recommendDtos.add(RestaurantRecommendDto.fromEntity(restaurant, liked, bestReview));
    }
    return recommendDtos;
  }

  @Transactional
  public void addMyStore(Long restaurantId, Member member) {
    Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new NotFoundException("맛집"));
    if(isMyStore(member, restaurant)) {
      /* Todo: isMyStore()와 아래 코드에서 두 번 조회됨. 개선하기 */
      MyStore myStore = myStoreRepository.findByMemberAndRestaurant(member, restaurant).get();
      myStoreRepository.delete(myStore);
      member.decreaseMyStoreCount();
      restaurant.decreaseLikeCount();
    } else {
      MyStore myStore = MyStore.builder().member(member).restaurant(restaurant).build();
      myStoreRepository.save(myStore);
      member.increaseMyStoreCount();
      restaurant.increaseLikeCount();
    }
  }

  public HashMap<String, Object> myStoreList(int page, int size, Member member) {
    Pageable pageable = PageRequest.of(page, size);

    Page<MyStore> myStores = myStoreRepository.findAllByMember(member,pageable);
    List<Restaurant> restaurantList = myStores.stream().map(MyStore::getRestaurant).collect(Collectors.toList());

    List<RestaurantListDto> restaurantDtos = new ArrayList<>(restaurantList.size());
    for(Restaurant restaurant: restaurantList){
      Boolean liked = isMyStore(member, restaurant);
      restaurantDtos.add(RestaurantListDto.fromEntity(restaurant, liked));
    }

    HashMap<String, Object> responseMap = new HashMap<>();
    responseMap.put("data", restaurantDtos);
    responseMap.put("totalPages", myStores.getTotalPages());
    return responseMap;
  }

  public HashMap<String, Object> participateRestaurantList(Member member, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);

    Page<Restaurant> participateRestaurant = restaurantRepository.findAllByRegistrantAndJudgement(member, false, pageable);
    long participationCount = participateRestaurant.getTotalElements();

    List<ParticipateRestaurantDto> participation = new ArrayList<>((int)participationCount);
    for(Restaurant restaurant: participateRestaurant){
      Boolean liked = isMyStore(member, restaurant);
      participation.add(ParticipateRestaurantDto.fromEntity(restaurant, liked));
    }

    HashMap response = new HashMap<>();
    response.put("participation", participation);
    response.put("participationCount", participationCount);
    response.put("totalPages", participateRestaurant.getTotalPages());
    return response;
  }

  public HashMap<String, Object> contributeRestaurantList(Member member, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);

    Page<RestaurantContributor> relations = restaurantContributorRepository.findAllByMember(member, pageable);
    List<Restaurant> contributionRestaurant = relations.stream().map(RestaurantContributor::getRestaurant).collect(Collectors.toList());
    long contributionCount = relations.getTotalElements();

    List<ParticipateRestaurantDto> contribution = new ArrayList<>((int)contributionCount);
    for(Restaurant restaurant: contributionRestaurant){
      Boolean liked = isMyStore(member, restaurant);
      contribution.add(ParticipateRestaurantDto.fromEntity(restaurant, liked));
    }

    HashMap response = new HashMap<>();
    response.put("contribution", contribution);
    response.put("contributionCount", contributionCount);
    response.put("totalPages", relations.getTotalPages());
    return response;
  }

  private Pageable determineSort(int page, int size, String sort, String order){
    Pageable pageable = PageRequest.of(page, size);
    if(order.equals("ascending")){
      pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
    } else if(order.equals("descending")){
      pageable = PageRequest.of(page, size, Sort.by(sort).descending());
    }
    return pageable;
  }

  public void deleteRestaurant(Long restaurantId){
    Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new NotFoundException("맛집"));
    /* Todo: S3에서도 이미지 파일 지우기 */
    if(restaurant.getImageUrl() != null) {
      imageUrlRepository.delete(restaurant.getImageUrl());
    }
    restaurantRepository.delete(restaurant);
  }

  /* Todo: 조회 최적화할 수 있는 방법 찾아보기 */
  private boolean isMyStore(Member member, Restaurant restaurant){
    return !myStoreRepository.findByMemberAndRestaurant(member, restaurant).isEmpty();
  }
}