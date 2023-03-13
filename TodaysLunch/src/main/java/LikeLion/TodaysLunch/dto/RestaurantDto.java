package LikeLion.TodaysLunch.dto;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.Restaurant;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDto {
  private Long id;
  private String restaurantName;
  private String foodCategory;
  private String locationCategory;
  private String locationTag;
  private Set<String> recommendCategoryList;
  private String imageUrl;
  private Double latitude;
  private Double longitude;
  private String address;
  private Double rating;
  private Long reviewCount;
  private String nickname;
  public static RestaurantDto fromEntity(Restaurant restaurant){
    String image = null;
    if (restaurant.getImageUrl() != null){
      image = restaurant.getImageUrl().getImageUrl();
    }
    String nickname = null;
    if(restaurant.getMember() != null){
      nickname = restaurant.getMember().getNickname();
    }
    return RestaurantDto.builder()
        .id(restaurant.getId())
        .restaurantName(restaurant.getRestaurantName())
        .foodCategory(restaurant.getFoodCategory().getName())
        .locationCategory(restaurant.getLocationCategory().getName())
        .locationTag(restaurant.getLocationTag().getName())
        .recommendCategoryList(
            restaurant.getRecommendCategoryRelations().stream()
            .map(rel -> rel.getRecommendCategory().getName())
            .collect(Collectors.toSet())
        )
        .imageUrl(image)
        .latitude(restaurant.getLatitude())
        .longitude(restaurant.getLongitude())
        .address(restaurant.getAddress())
        .rating(restaurant.getRating())
        .reviewCount(restaurant.getReviewCount())
        .nickname(nickname).build();
  }
}
