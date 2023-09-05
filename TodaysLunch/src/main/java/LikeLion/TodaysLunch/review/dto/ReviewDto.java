package LikeLion.TodaysLunch.review.dto;

import LikeLion.TodaysLunch.review.domain.Review;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {

  private Long id;
  private ReviewProfileDto member;
  @NotBlank(message = "리뷰 내용은 Null과 공백일 수 없습니다!")
  private String reviewContent;
  @NotNull(message = "별점은 Null과 공백일 수 없습니다!")
  private Integer rating;
  private LocalDate createdDate;
  private Long likeCount;
  private Boolean liked;

  public Review toEntity(){
    return Review.builder()
        .rating(rating)
        .reviewContent(reviewContent)
        .build();
  }

  public static ReviewDto fromEntity(Review review, Boolean liked){
    return ReviewDto.builder()
        .id(review.getId())
        .rating(review.getRating())
        .reviewContent(review.getReviewContent())
        .createdDate(review.getCreatedDate())
        .likeCount(review.getLikeCount())
        .member(ReviewProfileDto.fromEntity(review.getMember()))
        .liked(liked)
        .build();
  }

}
