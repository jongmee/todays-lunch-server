package LikeLion.TodaysLunch.restaurant.domain;

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
public class RestaurantContributor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Restaurant restaurant;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Member member;

  @Builder
  public RestaurantContributor(Restaurant restaurant, Member member){
    this.restaurant = restaurant;
    this.member = member;
  }
}
