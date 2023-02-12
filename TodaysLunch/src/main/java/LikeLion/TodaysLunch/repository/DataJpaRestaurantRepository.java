package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.LocationTag;
import LikeLion.TodaysLunch.domain.Restaurant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataJpaRestaurantRepository extends JpaRepository <Restaurant, Long>, RestaurantRepository {
  List<Restaurant> findAllByFoodCategory(FoodCategory foodCategory);
  List<Restaurant> findAllByLocationTag(LocationTag locationTag);
}
