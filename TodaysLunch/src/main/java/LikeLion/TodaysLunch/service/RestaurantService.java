package LikeLion.TodaysLunch.service;

import LikeLion.TodaysLunch.domain.Agreement;
import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.ImageUrl;
import LikeLion.TodaysLunch.domain.LocationCategory;
import LikeLion.TodaysLunch.domain.LocationTag;
import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.RecommendCategory;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.domain.relation.RestaurantRecommendCategoryRelation;
import LikeLion.TodaysLunch.dto.JudgeRestaurantCreateDto;
import LikeLion.TodaysLunch.dto.JudgeRestaurantDto;
import LikeLion.TodaysLunch.dto.JudgeRestaurantListDto;
import LikeLion.TodaysLunch.dto.RestaurantDto;
import LikeLion.TodaysLunch.dto.RestaurantListDto;
import LikeLion.TodaysLunch.exception.NotFoundException;
import LikeLion.TodaysLunch.repository.AgreementRepository;
import LikeLion.TodaysLunch.repository.DataJpaRestaurantRepository;
import LikeLion.TodaysLunch.repository.FoodCategoryRepository;
import LikeLion.TodaysLunch.repository.ImageUrlRepository;
import LikeLion.TodaysLunch.repository.LocationCategoryRepository;
import LikeLion.TodaysLunch.repository.LocationTagRepository;
import LikeLion.TodaysLunch.repository.MemberRepository;
import LikeLion.TodaysLunch.repository.RecommendCategoryRepository;
import LikeLion.TodaysLunch.repository.RestRecmdRelRepository;
import LikeLion.TodaysLunch.repository.RestaurantSpecification;
import LikeLion.TodaysLunch.s3.S3UploadService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

  @Autowired
  private S3UploadService s3UploadService;


  public Page<RestaurantListDto> restaurantList(
      String foodCategory, String locationCategory,
      String locationTag, String keyword,
      int page, int size, String sort, String order) {

    Pageable pageable = determineSort(page, size, sort, order);

    FoodCategory foodCategoryObj;
    LocationCategory locationCategoryObj;
    LocationTag locationTagObj;

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
    if (keyword != null) {
      spec = spec.and(RestaurantSpecification.likeRestaurantName(keyword));
    }

    spec = spec.and(RestaurantSpecification.equalJudgement(false));

    return restaurantRepository.findAll(spec, pageable).map(RestaurantListDto::fromEntity);
  }

  public RestaurantDto restaurantDetail(Long id){
    Restaurant restaurant = restaurantRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("맛집"));
    return RestaurantDto.fromEntity(restaurant);
  }

  public Restaurant createJudgeRestaurant(JudgeRestaurantCreateDto createDto,
      MultipartFile restaurantImage, Member member)
      throws IOException {

    FoodCategory foodCategory = foodCategoryRepository.findByName(createDto.getFoodCategoryName())
        .orElseThrow(() -> new NotFoundException("음식 카테고리"));
    LocationCategory locationCategory = locationCategoryRepository.findByName(createDto.getLocationCategoryName())
        .orElseThrow(() -> new NotFoundException("위치 카테고리"));
    LocationTag locationTag = locationTagRepository.findByName(createDto.getLocationTagName())
        .orElseThrow(() -> new NotFoundException("위치 태그"));

    Restaurant restaurant = Restaurant.builder()
        .foodCategory(foodCategory)
        .locationCategory(locationCategory)
        .locationTag(locationTag)
        .address(createDto.getAddress())
        .restaurantName(createDto.getRestaurantName())
        .introduction(createDto.getIntroduction())
        .longitude(createDto.getLongitude())
        .latitude(createDto.getLatitude())
        .member(member)
        .build();

    if(!restaurantImage.isEmpty()) {
      ImageUrl imageUrl = new ImageUrl();
      String originalName = restaurantImage.getOriginalFilename();
      String savedUrl = s3UploadService.upload(restaurantImage, "judge_restaurant");
      imageUrl.setOriginalName(originalName);
      imageUrl.setImageUrl(savedUrl);
      imageUrlRepository.save(imageUrl);
      restaurant.setImageUrl(imageUrl);
    }

    Restaurant result = restaurantRepository.save(restaurant);

    List<Long> recommendCategoryIds= createDto.getRecommendCategoryIds();
    for(int i = 0; i < recommendCategoryIds.size(); i++){
      RecommendCategory recommendCategory = recommendCategoryRepository.findById(recommendCategoryIds.get(i))
          .orElseThrow(() -> new NotFoundException("추천 카테고리"));
      RestaurantRecommendCategoryRelation relation = new RestaurantRecommendCategoryRelation(restaurant, recommendCategory);
      restRecmdRelRepository.save(relation);
      restaurant.addRecommendCategoryRelation(relation);
    }

    if (recommendCategoryIds.size() > 0){
      result = restaurantRepository.save(restaurant);
    }

    return result;
  }

  public Page<JudgeRestaurantListDto> judgeRestaurantList(
      String foodCategory, String locationCategory, String locationTag, Long recommendCategoryId,
      int page, int size, String sort, String order) {

    Pageable pageable = determineSort(page, size, sort, order);

    FoodCategory foodCategoryObj;
    LocationCategory locationCategoryObj;
    LocationTag locationTagObj;
    RecommendCategory recommendCategoryObj;

    Specification<Restaurant> spec =(root, query, criteriaBuilder) -> null;
    if (foodCategory != null) {
      foodCategoryObj = foodCategoryRepository.findByName(foodCategory)
          .orElseThrow(() -> new NotFoundException("음식 카테고리"));
      spec = spec.and(RestaurantSpecification.equalFoodCategory(foodCategoryObj));
    }
    if (locationCategory != null) {
      locationCategoryObj = locationCategoryRepository.findByName(locationCategory)
          .orElseThrow(() -> new NotFoundException("위치 카테고리"));
      spec = spec.and(RestaurantSpecification.equalLocationCategory(locationCategoryObj));
    }
    if (locationTag != null) {
      locationTagObj = locationTagRepository.findByName(locationTag)
          .orElseThrow(() -> new NotFoundException("위치 태그"));
      spec = spec.and(RestaurantSpecification.equalLocationTag(locationTagObj));
    }
    if (recommendCategoryId != null) {
      recommendCategoryObj = recommendCategoryRepository.findById(recommendCategoryId)
          .orElseThrow(() -> new NotFoundException("추천 카테고리"));
      spec = spec.and(RestaurantSpecification.equalRecommendCategory(recommendCategoryObj));
    }
    spec = spec.and(RestaurantSpecification.equalJudgement(true));

    return restaurantRepository.findAll(spec, pageable).map(JudgeRestaurantListDto::fromEntity);
  }

  public JudgeRestaurantDto judgeRestaurantDetail(Long id){
    return JudgeRestaurantDto.fromEntity(restaurantRepository.findById(id).get());
  }

  public String addOrCancelAgreement(Member member, Long restaurantId){
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new IllegalArgumentException("맛집 심사 동의를 위한 대상 맛집 찾기 실패!"));

    if(isNotAlreadyAgree(member, restaurant)){
      agreementRepository.save(new Agreement(member, restaurant));

      AtomicLong agreementCount = restaurant.getAgreementCount();
      agreementCount.incrementAndGet();
      restaurant.setAgreementCount(agreementCount);

      if (agreementCount.get() > 4L)
        restaurant.setJudgement(false);

      restaurantRepository.save(restaurant);
      return "맛집 심사 동의 성공";
    } else {
      Agreement agreement = agreementRepository.findByMemberAndRestaurant(member, restaurant).get();
      AtomicLong agreementCount = restaurant.getAgreementCount();
      agreementCount.decrementAndGet();
      restaurant.setAgreementCount(agreementCount);
      restaurantRepository.save(restaurant);
      agreementRepository.delete(agreement);
      return "맛집 심사 동의 취소 성공";
    }
  }

  public String isAlreadyAgree(Member member, Long restaurantId){
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new IllegalArgumentException("맛집 심사 동의를 위한 대상 맛집 찾기 실패!"));

    if(isNotAlreadyAgree(member, restaurant))
      return "false";
    else
      return "true";
  }

  // 유저가 이미 동의한 심사 레스토랑인지 체크
  private boolean isNotAlreadyAgree(Member member, Restaurant restaurant){
    return agreementRepository.findByMemberAndRestaurant(member, restaurant).isEmpty();
  }


  // 추후 개선 사항 : 새로 고침할 때마다 추천 메뉴가 바뀌도록 구현하기 (랜덤으로?)
  public List<Restaurant> recommendation(Long userId){
    Member member = memberRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("맛집 추천을 위한 대상 유저 찾기 실패!"));

    FoodCategory foodCategory = member.getFoodCategory();
    LocationCategory locationCategory = member.getLocationCategory();
    Specification<Restaurant> spec =(root, query, criteriaBuilder) -> null;
    if (foodCategory != null){
      spec = spec.and(RestaurantSpecification.equalFoodCategory(foodCategory));
    }
    if (locationCategory != null){
      spec = spec.and(RestaurantSpecification.equalLocationCategory(locationCategory));
    }
    Pageable pageable = determineSort(0, 5, "rating", "descending");
    Page<Restaurant> recmdResult = restaurantRepository.findAll(spec, pageable);

    // 추천 맛집 수가 5개보다 작을 경우, 전체 맛집에서 평점 높은 순으로 모자른 맛집 수를 채운다.
    List<Restaurant> rest =  new ArrayList<>();
    if(recmdResult.getNumberOfElements() < 5){
      pageable = determineSort(0, 5-recmdResult.getNumberOfElements(), "rating", "descending");
      rest = restaurantRepository.findAll((root, query, criteriaBuilder) -> null, pageable).getContent();
    }
    List<Restaurant> finalList =  new ArrayList<>();
    finalList.addAll(recmdResult.getContent());
    finalList.addAll(rest);
    return finalList;
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
}