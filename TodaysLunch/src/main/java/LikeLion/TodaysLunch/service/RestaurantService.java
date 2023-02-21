package LikeLion.TodaysLunch.service;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.LocationCategory;
import LikeLion.TodaysLunch.domain.LocationTag;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.repository.DataJpaRestaurantRepository;
import LikeLion.TodaysLunch.repository.FoodCategoryRepository;
import LikeLion.TodaysLunch.repository.LocationCategoryRepository;
import LikeLion.TodaysLunch.repository.LocationTagRepository;
import LikeLion.TodaysLunch.repository.RestaurantSpecification;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@Transactional
public class RestaurantService {
  private final DataJpaRestaurantRepository restaurantRepository;
  private final FoodCategoryRepository foodCategoryRepository;
  private final LocationTagRepository locationTagRepository;
  private final LocationCategoryRepository locationCategoryRepository;

  public RestaurantService(DataJpaRestaurantRepository restaurantRepository,
      FoodCategoryRepository foodCategoryRepository,
      LocationTagRepository locationTagRepository,
      LocationCategoryRepository locationCategoryRepository
      ) {
    this.restaurantRepository = restaurantRepository;
    this.foodCategoryRepository = foodCategoryRepository;
    this.locationTagRepository = locationTagRepository;
    this.locationCategoryRepository = locationCategoryRepository;
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

    return restaurantRepository.findAll(spec, pageable);
  }

  public Restaurant restaurantDetail(Long id){
    return restaurantRepository.findById(id).get();
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
