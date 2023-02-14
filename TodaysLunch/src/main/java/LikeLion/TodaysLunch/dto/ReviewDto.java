package LikeLion.TodaysLunch.dto;

import LikeLion.TodaysLunch.domain.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewDto {
  private String reviewContent;
  private Integer rating;
  @Builder
  public ReviewDto(String reviewContent, Integer rating){
    this.rating = rating;
    this.reviewContent = reviewContent;
  }

  public Review toEntity(){
    return Review.builder().rating(rating).reviewContent(reviewContent).build();
  }

}
