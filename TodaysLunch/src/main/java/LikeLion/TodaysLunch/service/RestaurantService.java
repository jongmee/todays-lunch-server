package LikeLion.TodaysLunch.service;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.LocationCategory;
import LikeLion.TodaysLunch.domain.LocationTag;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.repository.DataJpaRestaurantRepository;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Transactional
public class RestaurantService {
  private final DataJpaRestaurantRepository restaurantRepository;
  public RestaurantService(DataJpaRestaurantRepository restaurantRepository){
    this.restaurantRepository = restaurantRepository;
  }
  public Page<Restaurant> restaurantList(Pageable pageable){
    return restaurantRepository.findAll(pageable);
  }

  public Restaurant restaurantDetail(Long id){
    return restaurantRepository.findById(id).get();
  }

  public Page<Restaurant> filterByFoodCategory(FoodCategory foodCategory, Pageable pageable){
    return restaurantRepository.findAllByFoodCategory(foodCategory, pageable);
  }

  public Page<Restaurant> filterByLocationCategory(LocationCategory locationCategory, Pageable pageable){
    return restaurantRepository.findAllByLocationCategory(locationCategory, pageable);
  }

  public Page<Restaurant> filterByLocationTag(LocationTag locationTag, Pageable pageable){
    return restaurantRepository.findAllByLocationTag(locationTag, pageable);
  }

  public Page<Restaurant> filterByLocationTagAndFoodCategory(LocationTag locationTag, FoodCategory foodCategory, Pageable pageable){
    return restaurantRepository.findAllByLocationTagAndFoodCategory(locationTag, foodCategory, pageable);
  }


//  public Restaurant saveRestaurant(Restaurant restaurant){
//    return restaurantRepository.save(restaurant);
//  }

}
