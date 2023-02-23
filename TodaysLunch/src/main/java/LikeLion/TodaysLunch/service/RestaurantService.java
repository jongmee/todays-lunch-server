package LikeLion.TodaysLunch.service;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.ImageUrl;
import LikeLion.TodaysLunch.domain.LocationCategory;
import LikeLion.TodaysLunch.domain.LocationTag;
import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.dto.JudgeDto;
import LikeLion.TodaysLunch.repository.DataJpaRestaurantRepository;
import LikeLion.TodaysLunch.repository.FoodCategoryRepository;
import LikeLion.TodaysLunch.repository.ImageUrlRepository;
import LikeLion.TodaysLunch.repository.LocationCategoryRepository;
import LikeLion.TodaysLunch.repository.LocationTagRepository;
import LikeLion.TodaysLunch.repository.RestaurantSpecification;
import LikeLion.TodaysLunch.s3.S3UploadService;
import java.io.IOException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

@Transactional
public class RestaurantService {
  private final DataJpaRestaurantRepository restaurantRepository;
  private final FoodCategoryRepository foodCategoryRepository;
  private final LocationTagRepository locationTagRepository;
  private final LocationCategoryRepository locationCategoryRepository;
  private final ImageUrlRepository imageUrlRepository;

  @Autowired
  private S3UploadService s3UploadService;

  public RestaurantService(DataJpaRestaurantRepository restaurantRepository,
      FoodCategoryRepository foodCategoryRepository,
      LocationTagRepository locationTagRepository,
      LocationCategoryRepository locationCategoryRepository,
      ImageUrlRepository imageUrlRepository
      ) {
    this.restaurantRepository = restaurantRepository;
    this.foodCategoryRepository = foodCategoryRepository;
    this.locationTagRepository = locationTagRepository;
    this.locationCategoryRepository = locationCategoryRepository;
    this.imageUrlRepository = imageUrlRepository;
  }

  public Page<Restaurant> restaurantList(
      String foodCategory, String locationCategory,
      String locationTag, String keyword,
      int page, int size, String sort, String order) {

    Pageable pageable = determineSort(page, size, sort, order);

    FoodCategory foodCategoryObj = new FoodCategory();
    LocationCategory locationCategoryObj = new LocationCategory();
    LocationTag locationTagObj = new LocationTag();

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

    return restaurantRepository.findAll(spec, pageable);
  }

  public Restaurant restaurantDetail(Long id){
    return restaurantRepository.findById(id).get();
  }

  public Restaurant createJudgeRestaurant(Member member, String address, String restaurantName, String foodCategoryName,
      String locationCategoryName, String locationTagName, String introduction,  MultipartFile restaurantImage)
      throws IOException {
    FoodCategory foodCategory = foodCategoryRepository.findByName(foodCategoryName)
        .orElseThrow(() -> new IllegalArgumentException("음식 카테고리 "+foodCategoryName+" 찾기 실패! 심사 맛집을 등록할 수 없습니다."));
    LocationCategory locationCategory = locationCategoryRepository.findByName(locationCategoryName)
        .orElseThrow(() -> new IllegalArgumentException("위치 카테고리 "+locationCategoryName+" 찾기 실패! 심사 맛집을 등록할 수 없습니다."));
    LocationTag locationTag = locationTagRepository.findByName(locationTagName)
        .orElseThrow(() -> new IllegalArgumentException("위치 태그 "+locationTagName+" 찾기 실패! 심사 맛집을 등록할 수 없습니다."));

    JudgeDto judgeDto = new JudgeDto(restaurantName, foodCategory,
        locationCategory, locationTag, address, introduction);
    Restaurant restaurant = judgeDto.toEntity();
    restaurant.setMember(member);

    if(!restaurantImage.isEmpty()) {
      ImageUrl imageUrl = new ImageUrl();
      String originalName = restaurantImage.getOriginalFilename();
      String savedUrl = s3UploadService.upload(restaurantImage, "judge_restaurant");
      imageUrl.setOriginalName(originalName);
      imageUrl.setImageUrl(savedUrl);
      imageUrlRepository.save(imageUrl);
      restaurant.setImageUrl(imageUrl);
    }

    return restaurantRepository.save(restaurant);
  }

  public Page<Restaurant> judgeRestaurantList(Pageable pageable){
    Specification<Restaurant> spec =(root, query, criteriaBuilder) -> null;
    spec = spec.and(RestaurantSpecification.equalJudgement(true));
    return restaurantRepository.findAll(spec, pageable);
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