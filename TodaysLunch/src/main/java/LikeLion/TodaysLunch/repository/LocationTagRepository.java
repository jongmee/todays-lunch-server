package LikeLion.TodaysLunch.repository;
import LikeLion.TodaysLunch.domain.LocationTag;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationTagRepository extends JpaRepository<LocationTag, Long> {
  Optional<LocationTag> findByName(String name);
}
