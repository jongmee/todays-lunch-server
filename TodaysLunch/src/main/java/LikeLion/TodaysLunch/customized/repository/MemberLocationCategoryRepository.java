package LikeLion.TodaysLunch.customized.repository;

import LikeLion.TodaysLunch.category.domain.LocationCategory;
import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.customized.domain.MemberLocationCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberLocationCategoryRepository extends JpaRepository<MemberLocationCategory, Long> {
  List<MemberLocationCategory> findAllByMember(Member member);
  Optional<MemberLocationCategory> findByLocationCategoryAndMember(LocationCategory locationCategory, Member member);
}
