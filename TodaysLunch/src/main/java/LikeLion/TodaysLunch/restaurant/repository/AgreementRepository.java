package LikeLion.TodaysLunch.restaurant.repository;

import LikeLion.TodaysLunch.restaurant.domain.Agreement;
import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {
  Optional<Agreement> findByMemberAndRestaurant(Member member, Restaurant restaurant);
}
