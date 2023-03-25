package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.domain.relation.MyStore;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyStoreRepository extends JpaRepository<MyStore, Long> {
  Optional<MyStore> findByMemberAndRestaurant(Member member, Restaurant restaurant);
}
