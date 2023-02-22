package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.LocationCategory;
import LikeLion.TodaysLunch.domain.LocationTag;
import LikeLion.TodaysLunch.domain.Restaurant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataJpaRestaurantRepository extends JpaRepository <Restaurant, Long> {
  Page<Restaurant> findAll(Pageable pageable);
  Page<Restaurant> findAllByFoodCategory(FoodCategory foodCategory, Pageable pageable);
  Page<Restaurant> findAllByLocationCategory(LocationCategory locationCategory, Pageable pageable);
  Page<Restaurant> findAllByLocationTag(LocationTag locationTag, Pageable pageable);
  Page<Restaurant> findAllByLocationTagAndFoodCategory(LocationTag locationTag, FoodCategory foodCategory, Pageable pageable);
  Page<Restaurant> findByRestaurantNameContaining(String keyword, Pageable pageable);
}
