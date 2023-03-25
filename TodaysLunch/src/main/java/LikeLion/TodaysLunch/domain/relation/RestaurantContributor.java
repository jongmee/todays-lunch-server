package LikeLion.TodaysLunch.domain.relation;

import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.Restaurant;
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
public class RestaurantContributor {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  @JoinColumn
  private Restaurant restaurant;
  @ManyToOne
  @JoinColumn
  private Member member;
  @Builder
  public RestaurantContributor(Restaurant restaurant, Member member){
    this.restaurant = restaurant;
    this.member = member;
  }
}
