package LikeLion.TodaysLunch.category.repository;

import LikeLion.TodaysLunch.category.domain.LocationCategory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationCategoryRepository extends JpaRepository<LocationCategory, Long> {
  Optional<LocationCategory> findByName(String name);
}
