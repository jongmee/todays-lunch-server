package LikeLion.TodaysLunch.dto;

import LikeLion.TodaysLunch.domain.Review;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewDto {
  @NotBlank(message = "리뷰 내용은 Null과 공백일 수 없습니다!")
  private String reviewContent;
  @NotBlank(message = "별점은 Null과 공백일 수 없습니다!")
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
