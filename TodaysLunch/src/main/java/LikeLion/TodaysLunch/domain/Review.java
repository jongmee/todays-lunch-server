package LikeLion.TodaysLunch.domain;

import LikeLion.TodaysLunch.dto.ReviewDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Review {

  @PrePersist
  public void prePersist() {
    this.reviewDecmd = this.reviewDecmd == null ? 0L : reviewDecmd;
    this.reviewRecmd = this.reviewRecmd == null ? 0L : reviewRecmd;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(length = 200, nullable = false)
  private String reviewContent;
  @Column(nullable = false)
  private Integer rating;
  private Long reviewRecmd;
  private Long reviewDecmd;
  @ManyToOne
  @JoinColumn
  private Restaurant restaurant;

  @Builder
  public Review(String reviewContent, Integer rating) {
    this.reviewContent = reviewContent;
    this.rating = rating;
  }

  public void setRestaurant(Restaurant restaurant) {
    this.restaurant = restaurant;
  }

  public void update(ReviewDto reviewDto) {
    if (reviewDto.getReviewContent() != null) {
      this.reviewContent = reviewDto.getReviewContent();
    }
    if (reviewDto.getRating() != null) {
      this.rating = reviewDto.getRating();
    }
  }

//  @ManyToOne
//  @JoinColumn(name="group")
//  private Member member;

}
