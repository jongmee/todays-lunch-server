package LikeLion.TodaysLunch.category.repository;

import LikeLion.TodaysLunch.category.domain.RecommendCategory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendCategoryRepository extends JpaRepository<RecommendCategory, Long> {
  Optional<RecommendCategory> findByName(String name);
}
