package LikeLion.TodaysLunch.customized.domain;

import LikeLion.TodaysLunch.category.domain.LocationCategory;
import LikeLion.TodaysLunch.member.domain.Member;
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
  @JoinColumn(nullable = false)
  private Member member;

  @ManyToOne
  @JoinColumn(nullable = false)
  private LocationCategory locationCategory;

  @Builder
  public MemberLocationCategory(Member member, LocationCategory locationCategory){
    this.member = member;
    this.locationCategory = locationCategory;
  }
}
