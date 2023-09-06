package LikeLion.TodaysLunch.review.dto;

import LikeLion.TodaysLunch.review.domain.Review;
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
public class MyReviewDto {

  private Long reviewId;
  private Long restaurantId;
  private String restaurantName;
  private String imageUrl;
  private String reviewContent;
  private Integer rating;
  private LocalDate createdDate;
  private Long likeCount;
  private Boolean liked;

  public static MyReviewDto fromEntity(Review review, Boolean liked) {
    String image = null;
    if(review.getRestaurant().getImageUrl() != null)
      image = review.getRestaurant().getImageUrl().getImageUrl();

    return MyReviewDto.builder()
        .reviewId(review.getId())
        .restaurantId(review.getRestaurant().getId())
        .restaurantName(review.getRestaurant().getRestaurantName())
        .imageUrl(image)
        .reviewContent(review.getReviewContent())
        .rating(review.getRating())
        .createdDate(review.getCreatedDate())
        .likeCount(review.getLikeCount())
        .liked(liked)
        .build();
  }
}
