package LikeLion.TodaysLunch.review.domain;

import LikeLion.TodaysLunch.common.BaseTimeEntity;
import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.review.dto.ReviewDto;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Review extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 200, nullable = false)
  private String reviewContent;

  @Column(nullable = false)
  private Integer rating;

  @Column(nullable = false)
  private Long likeCount;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Restaurant restaurant;

  @OneToOne
  @JoinColumn(nullable = false)
  Member member;

  @Builder
  public Review(String reviewContent, Integer rating) {
    this.reviewContent = reviewContent;
    this.rating = rating;
    this.likeCount = 0L;
  }

  public void setRestaurant(Restaurant restaurant) {
    this.restaurant = restaurant;
  }
  public void setMember(Member member) { this.member = member; }
  public void setLikeCount(Long likeCount) { this.likeCount = likeCount; }

  public void update(ReviewDto reviewDto) {
    if (reviewDto.getReviewContent() != null)
      this.reviewContent = reviewDto.getReviewContent();

    if (reviewDto.getRating() != null)
      this.rating = reviewDto.getRating();
  }
}
