package LikeLion.TodaysLunch.service;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.LocationTag;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.repository.RestaurantRepository;
import java.util.List;
import javax.transaction.Transactional;

@Transactional
public class RestaurantService {
  private final RestaurantRepository restaurantRepository;
  public RestaurantService(RestaurantRepository restaurantRepository){
    this.restaurantRepository = restaurantRepository;
  }

  public Restaurant restaurantDetail(Long id){
    return restaurantRepository.findById(id).get();
  }

  public List<Restaurant> filterByFoodCategory(FoodCategory foodCategory){
    return restaurantRepository.findAllByFoodCategory(foodCategory);
  }

  public List<Restaurant> filterByLocationTag(LocationTag locationTag){
    return restaurantRepository.findAllByLocationTag(locationTag);
  }


//  public Restaurant saveRestaurant(Restaurant restaurant){
//    return restaurantRepository.save(restaurant);
//  }

}
