package LikeLion.TodaysLunch.review.domain;

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
public class ReviewLike {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  @JoinColumn
  private Member member;
  @ManyToOne
  @JoinColumn
  private Review review;
  public ReviewLike(Member member, Review review) {
    this.member = member;
    this.review = review;
  }

}
