package LikeLion.TodaysLunch.customized.repository;

import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import LikeLion.TodaysLunch.customized.domain.MyStore;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyStoreRepository extends JpaRepository<MyStore, Long> {
  Optional<MyStore> findByMemberAndRestaurant(Member member, Restaurant restaurant);
  List<MyStore> findAllByMember(Member member);
  Page<MyStore> findAllByMember(Member member, Pageable pageable);
}
