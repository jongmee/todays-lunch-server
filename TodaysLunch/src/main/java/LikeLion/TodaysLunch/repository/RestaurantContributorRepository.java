package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.relation.RestaurantContributor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantContributorRepository extends JpaRepository<RestaurantContributor, Long> {

}
