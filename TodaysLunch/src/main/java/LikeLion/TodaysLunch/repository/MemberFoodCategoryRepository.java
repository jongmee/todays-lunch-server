package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.relation.MemberFoodCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberFoodCategoryRepository extends JpaRepository<MemberFoodCategory, Long> {
  List<MemberFoodCategory> findAllByMember(Member member);
}
