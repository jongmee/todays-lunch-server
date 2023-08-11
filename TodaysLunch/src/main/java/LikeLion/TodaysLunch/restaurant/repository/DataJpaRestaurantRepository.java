package LikeLion.TodaysLunch.restaurant.repository;

import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DataJpaRestaurantRepository extends JpaRepository <Restaurant, Long>,
    JpaSpecificationExecutor<Restaurant> {
  Page<Restaurant> findAll(Specification<Restaurant> spec, Pageable pageable);
  List<Restaurant> findAll(Specification<Restaurant> spec);
  List<Restaurant> findAllByRegistrantAndJudgement(Member registrant, Boolean judgement);
  Page<Restaurant> findAllByRegistrantAndJudgement(Member registrant, Boolean judgement, Pageable pageable);
  Optional<Restaurant> findByRestaurantName(String restaurantName);
}
