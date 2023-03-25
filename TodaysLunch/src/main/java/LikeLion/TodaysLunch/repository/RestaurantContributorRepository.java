package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.domain.relation.RestaurantContributor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantContributorRepository extends JpaRepository<RestaurantContributor, Long> {
    Optional<RestaurantContributor> findByRestaurantAndMember(Restaurant restaurant, Member member);
}
