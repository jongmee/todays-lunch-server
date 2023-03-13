package LikeLion.TodaysLunch.dto;

import LikeLion.TodaysLunch.domain.Review;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
  @NotBlank(message = "리뷰 내용은 Null과 공백일 수 없습니다!")
  private String reviewContent;
  @NotBlank(message = "별점은 Null과 공백일 수 없습니다!")
  private Integer rating;
  private LocalDate createdDate;
  private Long likeCount;
  @Builder
  public ReviewDto(String reviewContent, Integer rating){
    this.rating = rating;
    this.reviewContent = reviewContent;
  }

  public Review toEntity(){
    return Review.builder().rating(rating).reviewContent(reviewContent).build();
  }

  public static ReviewDto fromEntity(Review review){
    return ReviewDto.builder()
        .rating(review.getRating())
        .reviewContent(review.getReviewContent())
        .createdDate(review.getCreatedDate())
        .likeCount(review.getLikeCount().get())
        .build();
  }

}
