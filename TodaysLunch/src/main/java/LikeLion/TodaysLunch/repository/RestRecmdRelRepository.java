package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.relation.RestaurantRecommendCategoryRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestRecmdRelRepository extends JpaRepository<RestaurantRecommendCategoryRelation,Long> {

}
