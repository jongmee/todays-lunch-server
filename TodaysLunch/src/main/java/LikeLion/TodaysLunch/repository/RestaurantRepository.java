package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.LocationTag;
import LikeLion.TodaysLunch.domain.Restaurant;
import java.util.*;

public interface RestaurantRepository {
  Restaurant save(Restaurant restaurant);
  Optional<Restaurant> findById(Long id);
  List<Restaurant> findAll();
  List<Restaurant> findAllByFoodCategory(FoodCategory foodCategory);
  List<Restaurant> findAllByLocationTag(LocationTag locationTag);
}
