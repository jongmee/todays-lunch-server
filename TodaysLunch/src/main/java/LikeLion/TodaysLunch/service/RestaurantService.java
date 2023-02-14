package LikeLion.TodaysLunch.service;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.LocationCategory;
import LikeLion.TodaysLunch.domain.LocationTag;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.repository.DataJpaRestaurantRepository;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Transactional
public class RestaurantService {
  private final DataJpaRestaurantRepository restaurantRepository;
  public RestaurantService(DataJpaRestaurantRepository restaurantRepository){
    this.restaurantRepository = restaurantRepository;
  }
  public Page<Restaurant> restaurantList(int page, int size, String sort, String order){
    Pageable pageable = determineSort(page, size, sort ,order);
    return restaurantRepository.findAll(pageable);
  }

  public Restaurant restaurantDetail(Long id){
    return restaurantRepository.findById(id).get();
  }

  public Page<Restaurant> filterByFoodCategory(FoodCategory foodCategory, int page, int size, String sort, String order){
    Pageable pageable = determineSort(page, size, sort ,order);
    return restaurantRepository.findAllByFoodCategory(foodCategory, pageable);
  }

  public Page<Restaurant> filterByLocationCategory(LocationCategory locationCategory, int page, int size, String sort, String order){
    Pageable pageable = determineSort(page, size, sort ,order);
    return restaurantRepository.findAllByLocationCategory(locationCategory, pageable);
  }

  public Page<Restaurant> filterByLocationTag(LocationTag locationTag, int page, int size, String sort, String order){
    Pageable pageable = determineSort(page, size, sort ,order);
    return restaurantRepository.findAllByLocationTag(locationTag, pageable);
  }

  public Page<Restaurant> filterByLocationTagAndFoodCategory(LocationTag locationTag, FoodCategory foodCategory, int page, int size, String sort, String order){
    Pageable pageable = determineSort(page, size, sort ,order);
    return restaurantRepository.findAllByLocationTagAndFoodCategory(locationTag, foodCategory, pageable);
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
