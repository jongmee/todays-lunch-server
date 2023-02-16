package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.relation.LocationRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRelationRepository extends JpaRepository<LocationRelation, Long> {

}
