package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.domain.relation.RestaurantRecommendCategoryRelation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestRecmdRelRepository extends JpaRepository<RestaurantRecommendCategoryRelation,Long> {
  List<RestaurantRecommendCategoryRelation> findAllByRestaurant(Restaurant restaurant);
}
