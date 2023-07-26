package LikeLion.TodaysLunch.restaurant.repository;

import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import LikeLion.TodaysLunch.restaurant.domain.RestaurantRecommendCategoryRelation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestRecmdRelRepository extends JpaRepository<RestaurantRecommendCategoryRelation,Long> {
  List<RestaurantRecommendCategoryRelation> findAllByRestaurant(Restaurant restaurant);
}
