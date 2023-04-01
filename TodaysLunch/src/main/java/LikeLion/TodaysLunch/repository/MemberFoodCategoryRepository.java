package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.relation.MemberFoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberFoodCategoryRepository extends JpaRepository<MemberFoodCategory, Long> {

}
