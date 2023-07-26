package LikeLion.TodaysLunch.category.repository;
import LikeLion.TodaysLunch.category.domain.LocationTag;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationTagRepository extends JpaRepository<LocationTag, Long> {
  Optional<LocationTag> findByName(String name);
}
