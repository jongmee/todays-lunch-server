package LikeLion.TodaysLunch.restaurant.dto.response;

import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantRecommendDto {

  private Long id;
  private String restaurantName;
  private String foodCategory;
  private String locationCategory;
  private String locationTag;
  private String imageUrl;
  private Double rating;
  private Long lowestPrice;
  private Long reviewCount;
  private Boolean liked;
  private String bestReview;

  public static RestaurantRecommendDto fromEntity(Restaurant restaurant, Boolean liked, String bestReview){
    String image = null;
    if (restaurant.getImageUrl() != null)
      image = restaurant.getImageUrl().getImageUrl();

    return RestaurantRecommendDto.builder()
        .id(restaurant.getId())
        .restaurantName(restaurant.getRestaurantName())
        .foodCategory(restaurant.getFoodCategory().getName())
        .locationCategory(restaurant.getLocationCategory().getName())
        .locationTag(restaurant.getLocationTag().getName())
        .imageUrl(image)
        .rating(restaurant.getRating())
        .lowestPrice(restaurant.getLowestPrice())
        .reviewCount(restaurant.getReviewCount())
        .liked(liked)
        .bestReview(bestReview)
        .build();
  }
}
