package LikeLion.TodaysLunch.restaurant.repository;

import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import LikeLion.TodaysLunch.restaurant.domain.RestaurantContributor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantContributorRepository extends JpaRepository<RestaurantContributor, Long> {
    Optional<RestaurantContributor> findByRestaurantAndMember(Restaurant restaurant, Member member);
    List<RestaurantContributor> findAllByRestaurant(Restaurant restaurant);
    List<RestaurantContributor> findAllByMember(Member member);
}
