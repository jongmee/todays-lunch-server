package LikeLion.TodaysLunch.service;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.LocationCategory;
import LikeLion.TodaysLunch.domain.LocationTag;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.repository.DataJpaRestaurantRepository;
import LikeLion.TodaysLunch.repository.FoodCategoryRepository;
import LikeLion.TodaysLunch.repository.LocationCategoryRepository;
import LikeLion.TodaysLunch.repository.LocationTagRepository;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Transactional
public class RestaurantService {
  private final DataJpaRestaurantRepository restaurantRepository;
  private final FoodCategoryRepository foodCategoryRepository;
  private final LocationTagRepository locationTagRepository;
  private final LocationCategoryRepository locationCategoryRepository;

  public RestaurantService(DataJpaRestaurantRepository restaurantRepository,
      FoodCategoryRepository foodCategoryRepository, LocationTagRepository locationTagRepository,
      LocationCategoryRepository locationCategoryRepository) {
    this.restaurantRepository = restaurantRepository;
    this.foodCategoryRepository = foodCategoryRepository;
    this.locationTagRepository = locationTagRepository;
    this.locationCategoryRepository = locationCategoryRepository;
  }

  public Page<Restaurant> restaurantList(int page, int size, String sort, String order){
    Pageable pageable = determineSort(page, size, sort ,order);
    return restaurantRepository.findAll(pageable);
  }

  public Restaurant restaurantDetail(Long id){
    return restaurantRepository.findById(id).get();
  }

  public Page<Restaurant> filterByFoodCategory(String categoryName, int page, int size, String sort, String order){
    Pageable pageable = determineSort(page, size, sort ,order);
    FoodCategory foodCategory = foodCategoryRepository.findByName(categoryName)
        .orElseThrow(() -> new IllegalArgumentException("맛집 리스트 반환 실패! 해당 음식 카테고리가 없습니다."));
    return restaurantRepository.findAllByFoodCategory(foodCategory, pageable);
  }

  public Page<Restaurant> filterByLocationCategory(String categoryName, int page, int size, String sort, String order){
    Pageable pageable = determineSort(page, size, sort ,order);
    LocationCategory locationCategory = locationCategoryRepository.findByName(categoryName)
        .orElseThrow(() -> new IllegalArgumentException("맛집 리스트 반환 실패! 해당 위치 카테고리가 없습니다."));
    return restaurantRepository.findAllByLocationCategory(locationCategory, pageable);
  }

  public Page<Restaurant> filterByLocationTag(String tagName, int page, int size, String sort, String order){
    Pageable pageable = determineSort(page, size, sort ,order);
    LocationTag locationTag = locationTagRepository.findByName(tagName)
        .orElseThrow(() -> new IllegalArgumentException("맛집 리스트 반환 실패! 해당 위치 태그가 없습니다."));
    return restaurantRepository.findAllByLocationTag(locationTag, pageable);
  }

  public Page<Restaurant> filterByLocationTagAndFoodCategory(String tagName, String categoryName, int page, int size, String sort, String order){
    Pageable pageable = determineSort(page, size, sort ,order);
    LocationTag locationTag = locationTagRepository.findByName(tagName)
        .orElseThrow(() -> new IllegalArgumentException("맛집 리스트 반환 실패! 해당 위치 태그가 없습니다."));
    FoodCategory foodCategory = foodCategoryRepository.findByName(categoryName)
        .orElseThrow(() -> new IllegalArgumentException("맛집 리스트 반환 실패! 해당 음식 카테고리가 없습니다."));
    return restaurantRepository.findAllByLocationTagAndFoodCategory(locationTag, foodCategory, pageable);
  }

  public Page<Restaurant> searchRestaurantName(String keyword, Pageable pageable){
    return restaurantRepository.findByRestaurantNameContaining(keyword, pageable);
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
