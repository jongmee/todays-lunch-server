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
public class JudgeRestaurantListDto {

  private Long id;
  private String restaurantName;
  private String introduction;
  private String registrant;
  private String imageUrl;
  private String foodCategory;
  private String locationCategory;
  private String locationTag;
  private Long agreementCount;
  private Boolean agreed;

  public static JudgeRestaurantListDto fromEntity(Restaurant restaurant, Boolean agreed){
    String image = null;
    if (restaurant.getImageUrl() != null)
      image = restaurant.getImageUrl().getImageUrl();

    String registrant = null;
    if(restaurant.getRegistrant() != null)
      registrant = restaurant.getRegistrant().getNickname();

    return JudgeRestaurantListDto.builder()
        .id(restaurant.getId())
        .restaurantName(restaurant.getRestaurantName())
        .introduction(restaurant.getIntroduction())
        .registrant(registrant)
        .imageUrl(image)
        .foodCategory(restaurant.getFoodCategory().getName())
        .locationCategory(restaurant.getLocationCategory().getName())
        .locationTag(restaurant.getLocationTag().getName())
        .agreementCount(restaurant.getAgreementCount().get())
        .agreed(agreed)
        .build();
  }
}
