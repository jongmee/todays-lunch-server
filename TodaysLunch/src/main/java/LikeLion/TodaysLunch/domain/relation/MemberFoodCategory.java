package LikeLion.TodaysLunch.domain.relation;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.Member;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MemberFoodCategory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  @JoinColumn
  private Member member;
  @ManyToOne
  @JoinColumn
  private FoodCategory foodCategory;
  @Builder
  public MemberFoodCategory(Member member, FoodCategory foodCategory){
    this.member = member;
    this.foodCategory = foodCategory;
  }
}