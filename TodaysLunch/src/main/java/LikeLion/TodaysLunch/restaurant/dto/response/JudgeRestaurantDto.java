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
public class JudgeRestaurantDto {

  private Long id;
  private String restaurantName;
  private String introduction;
  private String registrant;
  private String imageUrl;
  private Double latitude;
  private Double longitude;
  private Long agreementCount;
  private String foodCategory;
  private String locationCategory;
  private String locationTag;
  private Boolean agreed;

  public static JudgeRestaurantDto fromEntity(Restaurant restaurant, Boolean agreed){
    String image = null;
    if (restaurant.getImageUrl() != null)
      image = restaurant.getImageUrl().getImageUrl();

    String registrant = null;
    if(restaurant.getRegistrant() != null)
      registrant = restaurant.getRegistrant().getNickname();

    return JudgeRestaurantDto.builder()
        .id(restaurant.getId())
        .restaurantName(restaurant.getRestaurantName())
        .introduction(restaurant.getIntroduction())
        .registrant(registrant)
        .imageUrl(image)
        .latitude(restaurant.getLatitude())
        .longitude(restaurant.getLongitude())
        .agreementCount(restaurant.getAgreementCount())
        .foodCategory(restaurant.getFoodCategory().getName())
        .locationCategory(restaurant.getLocationCategory().getName())
        .locationTag(restaurant.getLocationTag().getName())
        .agreed(agreed)
        .build();
  }
}
