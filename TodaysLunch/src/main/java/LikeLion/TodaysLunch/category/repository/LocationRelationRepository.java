package LikeLion.TodaysLunch.category.repository;

import LikeLion.TodaysLunch.category.domain.LocationRelation;
import LikeLion.TodaysLunch.category.domain.LocationTag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRelationRepository extends JpaRepository<LocationRelation, Long> {
 Optional<LocationRelation> findByLocationTag(LocationTag locationTag);
}
