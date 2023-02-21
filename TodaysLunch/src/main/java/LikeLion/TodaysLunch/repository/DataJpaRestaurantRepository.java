package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.LocationCategory;
import LikeLion.TodaysLunch.domain.LocationTag;
import LikeLion.TodaysLunch.domain.Restaurant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DataJpaRestaurantRepository extends JpaRepository <Restaurant, Long>,
    JpaSpecificationExecutor<Restaurant> {
  Page<Restaurant> findAll(Specification<Restaurant> spec, Pageable pageable);
}
