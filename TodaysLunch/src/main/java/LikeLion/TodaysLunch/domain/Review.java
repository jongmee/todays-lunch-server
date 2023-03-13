package LikeLion.TodaysLunch.domain;

import LikeLion.TodaysLunch.dto.ReviewDto;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import lombok.Builder;
import lombok.Data;
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
  private AtomicLong likeCount;
  @ManyToOne
  @JoinColumn
  private Restaurant restaurant;
  @OneToOne
  @JoinColumn(nullable = false)
  Member member;

  @Builder
  public Review(String reviewContent, Integer rating) {
    this.reviewContent = reviewContent;
    this.rating = rating;
    this.likeCount = new AtomicLong(0);
  }

  public void setRestaurant(Restaurant restaurant) {
    this.restaurant = restaurant;
  }
  public void setMember(Member member) { this.member = member; }
  public void setLikeCount(AtomicLong likeCount) { this.likeCount = likeCount; }

  public void update(ReviewDto reviewDto) {
    if (reviewDto.getReviewContent() != null) {
      this.reviewContent = reviewDto.getReviewContent();
    }
    if (reviewDto.getRating() != null) {
      this.rating = reviewDto.getRating();
    }
  }

}
