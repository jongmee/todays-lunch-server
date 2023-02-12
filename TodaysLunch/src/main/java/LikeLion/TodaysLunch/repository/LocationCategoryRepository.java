package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.LocationCategory;
import LikeLion.TodaysLunch.domain.LocationTag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationCategoryRepository extends JpaRepository<LocationCategory, Long> {
  Optional<LocationCategory> findByName(String name);
}
