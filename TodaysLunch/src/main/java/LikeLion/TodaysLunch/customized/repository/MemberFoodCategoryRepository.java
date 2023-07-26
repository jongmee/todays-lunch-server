package LikeLion.TodaysLunch.customized.repository;

import LikeLion.TodaysLunch.category.domain.FoodCategory;
import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.customized.domain.MemberFoodCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberFoodCategoryRepository extends JpaRepository<MemberFoodCategory, Long> {
  List<MemberFoodCategory> findAllByMember(Member member);
  Optional<MemberFoodCategory> findByFoodCategoryAndMember(FoodCategory foodCategory, Member member);
}
