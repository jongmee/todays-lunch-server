package LikeLion.TodaysLunch.domain.relation;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.LocationCategory;
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

@Entity
@Getter
@NoArgsConstructor
public class MemberLocationCategory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  @JoinColumn
  private Member member;
  @ManyToOne
  @JoinColumn
  private LocationCategory locationCategory;
  @Builder
  public MemberLocationCategory(Member member, LocationCategory locationCategory){
    this.member = member;
    this.locationCategory = locationCategory;
  }
}
