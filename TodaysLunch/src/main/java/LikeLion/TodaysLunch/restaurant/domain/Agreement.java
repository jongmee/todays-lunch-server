package LikeLion.TodaysLunch.restaurant.domain;

import LikeLion.TodaysLunch.member.domain.Member;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Agreement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Member member;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Restaurant restaurant;

  public Agreement(Member member, Restaurant restaurant) {
    this.member = member;
    this.restaurant = restaurant;
  }
}
