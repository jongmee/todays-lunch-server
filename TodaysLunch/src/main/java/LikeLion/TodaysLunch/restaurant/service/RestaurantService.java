package LikeLion.TodaysLunch.restaurant.service;

import LikeLion.TodaysLunch.restaurant.domain.Agreement;
import LikeLion.TodaysLunch.category.domain.FoodCategory;
import LikeLion.TodaysLunch.image.domain.ImageUrl;
import LikeLion.TodaysLunch.category.domain.LocationCategory;
import LikeLion.TodaysLunch.category.domain.LocationTag;
import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.category.domain.RecommendCategory;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import LikeLion.TodaysLunch.customized.domain.MyStore;
import LikeLion.TodaysLunch.restaurant.domain.RestaurantContributor;
import LikeLion.TodaysLunch.restaurant.domain.RestaurantRecommendCategoryRelation;
import LikeLion.TodaysLunch.restaurant.dto.ContributorDto;
import LikeLion.TodaysLunch.restaurant.dto.JudgeRestaurantCreateDto;
import LikeLion.TodaysLunch.restaurant.dto.JudgeRestaurantDto;
import LikeLion.TodaysLunch.restaurant.dto.JudgeRestaurantListDto;
import LikeLion.TodaysLunch.restaurant.dto.ParticipateRestaurantDto;
import LikeLion.TodaysLunch.restaurant.dto.RestaurantDto;
import LikeLion.TodaysLunch.restaurant.dto.RestaurantListDto;
import LikeLion.TodaysLunch.exception.NotFoundException;
import LikeLion.TodaysLunch.restaurant.repository.AgreementRepository;
import LikeLion.TodaysLunch.restaurant.repository.DataJpaRestaurantRepository;
import LikeLion.TodaysLunch.category.repository.FoodCategoryRepository;
import LikeLion.TodaysLunch.image.repository.ImageUrlRepository;
import LikeLion.TodaysLunch.category.repository.LocationCategoryRepository;
import LikeLion.TodaysLunch.category.repository.LocationTagRepository;
import LikeLion.TodaysLunch.member.repository.MemberRepository;
import LikeLion.TodaysLunch.customized.repository.MyStoreRepository;
import LikeLion.TodaysLunch.category.repository.RecommendCategoryRepository;
import LikeLion.TodaysLunch.restaurant.repository.RestRecmdRelRepository;
import LikeLion.TodaysLunch.restaurant.repository.RestaurantContributorRepository;
import LikeLion.TodaysLunch.external.S3UploadService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@RequiredArgsConstructor
public class
RestaurantService {
  private final DataJpaRestaurantRepository restaurantRepository;
  private final FoodCategoryRepository foodCategoryRepository;
  private final LocationTagRepository locationTagRepository;
  private final LocationCategoryRepository locationCategoryRepository;
  private final ImageUrlRepository imageUrlRepository;
  private final MemberRepository memberRepository;
  private final AgreementRepository agreementRepository;
  private final RecommendCategoryRepository recommendCategoryRepository;
  private final RestRecmdRelRepository restRecmdRelRepository;
  private final RestaurantContributorRepository restaurantContributorRepository;
  private final MyStoreRepository myStoreRepository;

  @Autowired
  private S3UploadService s3UploadService;

  private final Double EARTH_RADIUS = 6370d; // 지구 반지름(km)

  public HashMap<String, Object> restaurantList(
      String foodCategory, String locationCategory,
      String locationTag, Long recommendCategoryId, String keyword,
      int page, int size, String sort, String order, Member member) {

    Pageable pageable = determineSort(page, size, sort, order);

    Specification<Restaurant> spec = determineSpecification(foodCategory, locationCategory, locationTag, recommendCategoryId, keyword, false, null);

    Page<Restaurant> restaurantList = restaurantRepository.findAll(spec, pageable);
    List<RestaurantListDto> restaurantDtos = new ArrayList<>(restaurantList.getSize());
    Boolean liked;
    for(Restaurant restaurant: restaurantList){
      if(isNotAlreadyMyStore(member, restaurant))
        liked = false;
      else
        liked = true;
      restaurantDtos.add(RestaurantListDto.fromEntity(restaurant, liked));
    }

    HashMap<String, Object> responseMap = new HashMap<>();
    responseMap.put("data", restaurantDtos);
    responseMap.put("totalPages", restaurantList.getTotalPages());
    return responseMap;
  }

  public RestaurantDto restaurantDetail(Long id, Member member){
    Restaurant restaurant = restaurantRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("맛집"));

    List<ContributorDto> contributors =
        restaurantContributorRepository.findAllByRestaurant(restaurant)
        .stream()
        .map(c->c.getMember())
        .map(ContributorDto::fromEntity)
        .collect(Collectors.toList());

    Boolean liked;
    if(isNotAlreadyMyStore(member, restaurant))
      liked = false;
    else
      liked = true;

    return RestaurantDto.fromEntity(restaurant, contributors, liked);
  }

  public void createJudgeRestaurant(JudgeRestaurantCreateDto createDto,
      MultipartFile restaurantImage, Member member)
      throws IOException {

    FoodCategory foodCategory = foodCategoryRepository.findByName(createDto.getFoodCategoryName())
        .orElseThrow(() -> new NotFoundException("음식 카테고리"));

    Double latitude = createDto.getLatitude();
    Double longitude = createDto.getLongitude();
    Double diff = 10000d; // 좌표 상 거리 차이

    List<LocationTag> locationTagList = locationTagRepository.findAll();
    LocationTag locationTag = new LocationTag();
    for(LocationTag tag: locationTagList){
      Double tmpDiff = getDistance(latitude, longitude, tag.getLatitude(), tag.getLongitude());
      if(diff > tmpDiff) {
        diff = tmpDiff;
        locationTag = tag;
      }
    }

    diff = 10000d;
    List<LocationCategory> locationCategoryList = locationCategoryRepository.findAll();
    LocationCategory locationCategory = new LocationCategory();
    for(LocationCategory category: locationCategoryList){
      Double tmpDiff = getDistance(latitude, longitude, category.getLatitude(), category.getLongitude());
      if(diff > tmpDiff){
        diff = tmpDiff;
        locationCategory = category;
      }
    }

    Restaurant restaurant = Restaurant.builder()
        .foodCategory(foodCategory)
        .locationCategory(locationCategory)
        .locationTag(locationTag)
        .address(createDto.getAddress())
        .restaurantName(createDto.getRestaurantName())
        .introduction(createDto.getIntroduction())
        .longitude(longitude)
        .latitude(latitude)
        .registrant(member)
        .build();

    if(restaurantImage != null && !restaurantImage.isEmpty()) {
      ImageUrl imageUrl = new ImageUrl();
      String originalName = restaurantImage.getOriginalFilename();
      String savedUrl = s3UploadService.upload(restaurantImage, "judge_restaurant");
      imageUrl.setOriginalName(originalName);
      imageUrl.setImageUrl(savedUrl);
      imageUrlRepository.save(imageUrl);
      restaurant.setImageUrl(imageUrl);
    }

    restaurantRepository.save(restaurant);

    List<Long> recommendCategoryIds= createDto.getRecommendCategoryIds();
    for(int i = 0; i < recommendCategoryIds.size(); i++){
      RecommendCategory recommendCategory = recommendCategoryRepository.findById(recommendCategoryIds.get(i))
          .orElseThrow(() -> new NotFoundException("추천 카테고리"));
      RestaurantRecommendCategoryRelation relation = new RestaurantRecommendCategoryRelation(restaurant, recommendCategory);
      restRecmdRelRepository.save(relation);
      restaurant.addRecommendCategoryRelation(relation);
    }

    if (recommendCategoryIds.size() > 0){
      restaurantRepository.save(restaurant);
    }
  }

  public HashMap<String, Object> judgeRestaurantList(
      String foodCategory, String locationCategory, String locationTag, Long recommendCategoryId,
      int page, int size, String sort, String order, Long registrantId, Member member) {

    Member registrant = null;
    if (registrantId != null){
      registrant = memberRepository.findById(registrantId).get();
    }

    Pageable pageable = determineSort(page, size, sort, order);

    Specification<Restaurant> spec = determineSpecification(foodCategory, locationCategory, locationTag, recommendCategoryId, null, true, registrant);

    Page<Restaurant> restaurantList = restaurantRepository.findAll(spec, pageable);
    List<JudgeRestaurantListDto> restaurantDtos = new ArrayList<>(restaurantList.getSize());
    Boolean agreed;
    for(Restaurant restaurant: restaurantList){
      if(isNotAlreadyAgree(member, restaurant))
        agreed = false;
      else
        agreed = true;
      restaurantDtos.add(JudgeRestaurantListDto.fromEntity(restaurant, agreed));
    }

    HashMap<String, Object> responseMap = new HashMap<>();
    responseMap.put("data", restaurantDtos);
    responseMap.put("totalPages", restaurantList.getTotalPages());

    return responseMap;
  }

  public JudgeRestaurantDto judgeRestaurantDetail(Long id, Member member){
    Restaurant restaurant = restaurantRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("맛집"));
    Boolean agreed;
    if(isNotAlreadyAgree(member, restaurant))
      agreed = false;
    else
      agreed = true;
    return JudgeRestaurantDto.fromEntity(restaurant, agreed);
  }

  public void addOrCancelAgreement(Member member, Long restaurantId){
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new NotFoundException("맛집"));

    AtomicLong agreementCount = restaurant.getAgreementCount();

    if(isNotAlreadyAgree(member, restaurant)){
      agreementRepository.save(new Agreement(member, restaurant));

      agreementCount.incrementAndGet();
      restaurant.setAgreementCount(agreementCount);

      if (agreementCount.get() > 4L)
        restaurant.setJudgement(false);

      restaurantRepository.save(restaurant);
    } else {
      Agreement agreement = agreementRepository.findByMemberAndRestaurant(member, restaurant).get();
      agreementCount.decrementAndGet();
      restaurant.setAgreementCount(agreementCount);
      restaurantRepository.save(restaurant);
      agreementRepository.delete(agreement);
    }
  }

  // 추후 개선 사항 : 새로 고침할 때마다 추천 메뉴가 바뀌도록 구현하기 (랜덤으로?)
//  public List<Restaurant> recommendation(Long userId){
//    Member member = memberRepository.findById(userId)
//        .orElseThrow(() -> new IllegalArgumentException("맛집 추천을 위한 대상 유저 찾기 실패!"));
//
//    FoodCategory foodCategory = member.getFoodCategory();
//    LocationCategory locationCategory = member.getLocationCategory();
//    Specification<Restaurant> spec =(root, query, criteriaBuilder) -> null;
//    if (foodCategory != null){
//      spec = spec.and(RestaurantSpecification.equalFoodCategory(foodCategory));
//    }
//    if (locationCategory != null){
//      spec = spec.and(RestaurantSpecification.equalLocationCategory(locationCategory));
//    }
//    Pageable pageable = determineSort(0, 5, "rating", "descending");
//    Page<Restaurant> recmdResult = restaurantRepository.findAll(spec, pageable);
//
//    // 추천 맛집 수가 5개보다 작을 경우, 전체 맛집에서 평점 높은 순으로 모자른 맛집 수를 채운다.
//    List<Restaurant> rest =  new ArrayList<>();
//    if(recmdResult.getNumberOfElements() < 5){
//      pageable = determineSort(0, 5-recmdResult.getNumberOfElements(), "rating", "descending");
//      rest = restaurantRepository.findAll((root, query, criteriaBuilder) -> null, pageable).getContent();
//    }
//    List<Restaurant> finalList =  new ArrayList<>();
//    finalList.addAll(recmdResult.getContent());
//    finalList.addAll(rest);
//    return finalList;
//  }

  public void addMyStore(Long restaurantId, Member member) {
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new NotFoundException("맛집"));
    if (isNotAlreadyMyStore(member, restaurant)) {
      Long count = member.getMyStoreCount();
      member.setMyStoreCount(++count);
      memberRepository.save(member);

      myStoreRepository.save(new MyStore(member, restaurant));
    } else {
      MyStore myStore = myStoreRepository.findByMemberAndRestaurant(member, restaurant).get();
      myStoreRepository.delete(myStore);

      Long count = member.getMyStoreCount();
      member.setMyStoreCount(--count);
      memberRepository.save(member);
    }
  }

  public HashMap<String, Object> myStoreList(
      int page, int size, Member member) {
    Pageable pageable = PageRequest.of(page, size);
    Page<MyStore> myStores = myStoreRepository.findAllByMember(member,pageable);
    List<Restaurant> restaurantList = myStores.stream().map(s->s.getRestaurant()).collect(Collectors.toList());
    List<RestaurantListDto> restaurantDtos = new ArrayList<>(restaurantList.size());
    Boolean liked;
    for(Restaurant restaurant: restaurantList){
      if(isNotAlreadyMyStore(member, restaurant))
        liked = false;
      else
        liked = true;
      restaurantDtos.add(RestaurantListDto.fromEntity(restaurant, liked));
    }
    HashMap<String, Object> responseMap = new HashMap<>();
    responseMap.put("data", restaurantDtos);
    responseMap.put("totalPages", myStores.getTotalPages());
    return responseMap;

  }

  public HashMap<String, Object> participateRestaurantList(Member member) {
    List<Restaurant> participateRestaurant = restaurantRepository.findAllByRegistrantAndJudgement(member, false);
    Integer participationCount = participateRestaurant.size();
    List<ParticipateRestaurantDto> participation = new ArrayList<>(participationCount);
    Boolean liked;
    for(Restaurant restaurant: participateRestaurant){
      if(isNotAlreadyMyStore(member, restaurant))
        liked = false;
      else
        liked = true;
      participation.add(ParticipateRestaurantDto.fromEntity(restaurant, liked));
    }

    List<Restaurant> contributionRestaurant = restaurantContributorRepository.findAllByMember(member)
        .stream().map(RestaurantContributor::getRestaurant).collect(Collectors.toList());
    Integer contributionCount = contributionRestaurant.size();
    List<ParticipateRestaurantDto> contribution = new ArrayList<>(contributionCount);
    for(Restaurant restaurant: contributionRestaurant){
      if(isNotAlreadyMyStore(member, restaurant))
        liked = false;
      else
        liked = true;
      contribution.add(ParticipateRestaurantDto.fromEntity(restaurant, liked));
    }

    HashMap response = new HashMap<>();
    response.put("participation", participation);
    response.put("participationCount", participationCount);
    response.put("contribution", contribution);
    response.put("contributionCount", contributionCount);

    return response;
  }

  private boolean isNotAlreadyMyStore(Member member, Restaurant restaurant){
    return myStoreRepository.findByMemberAndRestaurant(member, restaurant).isEmpty();
  }

  private boolean isNotAlreadyAgree(Member member, Restaurant restaurant){
    return agreementRepository.findByMemberAndRestaurant(member, restaurant).isEmpty();
  }

  public Pageable determineSort(int page, int size, String sort, String order){
    Pageable pageable = PageRequest.of(page, size);
    if(order.equals("ascending")){
      pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
    } else if(order.equals("descending")){
      pageable = PageRequest.of(page, size, Sort.by(sort).descending());
    }
    return pageable;
  }

  public Specification<Restaurant> determineSpecification(String foodCategory, String locationCategory,
      String locationTag, Long recommendCategoryId, String keyword, Boolean judgement, Member member){
    FoodCategory foodCategoryObj;
    LocationCategory locationCategoryObj;
    LocationTag locationTagObj;
    RecommendCategory recommendCategoryObj;

    Specification<Restaurant> spec =(root, query, criteriaBuilder) -> null;
    if (foodCategory != null) {
      foodCategoryObj = foodCategoryRepository.findByName(foodCategory).get();
      spec = spec.and(RestaurantSpecification.equalFoodCategory(foodCategoryObj));
    }
    if (locationCategory != null) {
      locationCategoryObj = locationCategoryRepository.findByName(locationCategory).get();
      spec = spec.and(RestaurantSpecification.equalLocationCategory(locationCategoryObj));
    }
    if (locationTag != null) {
      locationTagObj = locationTagRepository.findByName(locationTag).get();
      spec = spec.and(RestaurantSpecification.equalLocationTag(locationTagObj));
    }
    if (recommendCategoryId != null) {
      recommendCategoryObj = recommendCategoryRepository.findById(recommendCategoryId)
          .orElseThrow(() -> new NotFoundException("추천 카테고리"));
      spec = spec.and(RestaurantSpecification.equalRecommendCategory(recommendCategoryObj));
    }
    if (keyword != null) {
      spec = spec.and(RestaurantSpecification.likeRestaurantName(keyword));
    }
    if (member != null) {
      spec = spec.and(RestaurantSpecification.equalRegistrant(member));
    }

    return spec.and(RestaurantSpecification.equalJudgement(judgement));
  }

  private Double getDistance(Double latitude1, Double longitude1, Double latitude2, Double longitude2){
    Double diffLat = Math.toRadians(latitude2-latitude1);
    Double diffLon = Math.toRadians(longitude2-longitude1);
    Double raw = Math.pow(Math.sin(diffLat/2), 2)
        +Math.cos(Math.toRadians(latitude1))*Math.cos(Math.toRadians(latitude2))*Math.pow(Math.sin(diffLon),2);
    return EARTH_RADIUS*2*Math.atan2(Math.sqrt(raw), Math.sqrt(1-raw));
  }
}