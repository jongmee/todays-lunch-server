package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.relation.MemberFoodCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberFoodCategoryRepository extends JpaRepository<MemberFoodCategory, Long> {
  List<MemberFoodCategory> findAllByMember(Member member);
  Optional<MemberFoodCategory> findByFoodCategoryAndMember(FoodCategory foodCategory, Member member);
}
