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
public class RestaurantListDto {
  private Long id;
  private String restaurantName;
  private String foodCategory;
  private String locationCategory;
  private String locationTag;
  private String imageUrl;
  private Double latitude;
  private Double longitude;
  private Double rating;
  private Long reviewCount;
  public static RestaurantListDto fromEntity(Restaurant restaurant){
    String image = null;
    if (restaurant.getImageUrl() != null){
      image = restaurant.getImageUrl().getImageUrl();
    }
    return RestaurantListDto.builder()
        .id(restaurant.getId())
        .restaurantName(restaurant.getRestaurantName())
        .foodCategory(restaurant.getFoodCategory().getName())
        .locationCategory(restaurant.getLocationCategory().getName())
        .locationTag(restaurant.getLocationTag().getName())
        .imageUrl(image)
        .latitude(restaurant.getLatitude())
        .longitude(restaurant.getLongitude())
        .rating(restaurant.getRating())
        .reviewCount(restaurant.getReviewCount())
        .build();
  }

}
