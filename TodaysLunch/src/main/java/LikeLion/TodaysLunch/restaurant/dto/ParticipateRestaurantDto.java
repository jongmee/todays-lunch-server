package LikeLion.TodaysLunch.restaurant.dto;

import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipateRestaurantDto {
  private Long id;
  private String restaurantName;
  private String imageUrl;
  private String foodCategory;
  private String locationCategory;
  private Double rating;
  private String bestReview;
  private Boolean liked;

  public static ParticipateRestaurantDto fromEntity(Restaurant restaurant, Boolean liked){
    String image = null;
    if (restaurant.getImageUrl() != null){
      image = restaurant.getImageUrl().getImageUrl();
    }
    return ParticipateRestaurantDto.builder()
        .id(restaurant.getId())
        .restaurantName(restaurant.getRestaurantName())
        .imageUrl(image)
        .foodCategory(restaurant.getFoodCategory().getName())
        .locationCategory(restaurant.getLocationCategory().getName())
        .rating(restaurant.getRating())
        .bestReview(restaurant.getBestReview())
        .liked(liked)
        .build();
  }
}
