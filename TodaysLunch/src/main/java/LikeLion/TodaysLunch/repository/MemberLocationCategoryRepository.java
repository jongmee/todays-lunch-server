package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.LocationCategory;
import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.relation.MemberLocationCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberLocationCategoryRepository extends JpaRepository<MemberLocationCategory, Long> {
  List<MemberLocationCategory> findAllByMember(Member member);
  Optional<MemberLocationCategory> findByLocationCategoryAndMember(LocationCategory locationCategory, Member member);
}
