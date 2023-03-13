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
public class JudgeRestaurantDto {
  private Long id;
  private String restaurantName;
  private String introduction;
  private String nickname;
  private String imageUrl;
  private Double latitude;
  private Double longitude;
  private Long agreementCount;
  public static JudgeRestaurantDto fromEntity(Restaurant restaurant){
    String image = null;
    if (restaurant.getImageUrl() != null){
      image = restaurant.getImageUrl().getImageUrl();
    }
    return JudgeRestaurantDto.builder()
        .id(restaurant.getId())
        .restaurantName(restaurant.getRestaurantName())
        .introduction(restaurant.getIntroduction())
        .nickname(restaurant.getMember().getNickname())
        .imageUrl(image)
        .latitude(restaurant.getLatitude())
        .longitude(restaurant.getLongitude())
        .agreementCount(restaurant.getAgreementCount().get())
        .build();
  }
}
