package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.Agreement;
import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.Restaurant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {
  Optional<Agreement> findByMemberAndRestaurant(Member member, Restaurant restaurant);
}
