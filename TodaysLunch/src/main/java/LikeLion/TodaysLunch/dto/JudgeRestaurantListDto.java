package LikeLion.TodaysLunch.dto;

import LikeLion.TodaysLunch.domain.Restaurant;
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
  private String nickname;
  private String imageUrl;
  private String foodCategory;
  private String locationCategory;
  private String locationTag;
  private Long agreementCount;
  public static JudgeRestaurantListDto fromEntity(Restaurant restaurant){
    String image = null;
    if (restaurant.getImageUrl() != null){
      image = restaurant.getImageUrl().getImageUrl();
    }
    return JudgeRestaurantListDto.builder()
        .id(restaurant.getId())
        .restaurantName(restaurant.getRestaurantName())
        .introduction(restaurant.getIntroduction())
        .nickname(restaurant.getMember().getNickname())
        .imageUrl(image)
        .foodCategory(restaurant.getFoodCategory().getName())
        .locationCategory(restaurant.getLocationCategory().getName())
        .locationTag(restaurant.getLocationTag().getName())
        .agreementCount(restaurant.getAgreementCount().get())
        .build();
  }
}
