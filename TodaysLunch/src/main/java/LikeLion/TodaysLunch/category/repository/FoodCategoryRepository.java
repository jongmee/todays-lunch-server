package LikeLion.TodaysLunch.category.repository;

import LikeLion.TodaysLunch.category.domain.FoodCategory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodCategoryRepository extends JpaRepository<FoodCategory, Long> {
  Optional<FoodCategory> findByName(String name);
}
