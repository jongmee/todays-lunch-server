package LikeLion.TodaysLunch.restaurant.dto.request;

import LikeLion.TodaysLunch.category.domain.FoodCategory;
import LikeLion.TodaysLunch.category.domain.LocationCategory;
import LikeLion.TodaysLunch.category.domain.LocationTag;
import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import java.util.List;
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
public class JudgeRestaurantCreateDto {

  @NotBlank(message = "맛집 이름은 Null, 공백일 수 없습니다.")
  private String restaurantName;
  @NotBlank(message = "맛집의 도로명 주소는 Null, 공백일 수 없습니다.")
  private String address;
  private String introduction;
  @NotBlank(message = "음식 카테고리 이름은 Null, 공백일 수 없습니다.")
  private String foodCategoryName;
  private List<Long> recommendCategoryIds;
  @NotNull(message = "위도는 Null, 공백일 수 없습니다.")
  private Double latitude;
  @NotNull(message = "경도는 Null, 공백일 수 없습니다.")
  private Double longitude;

  public Restaurant toEntity(FoodCategory foodCategory, LocationTag locationTag, LocationCategory locationCategory, Member registrant) {
    return Restaurant.builder()
            .foodCategory(foodCategory)
            .locationCategory(locationCategory)
            .locationTag(locationTag)
            .address(address)
            .restaurantName(restaurantName)
            .introduction(introduction)
            .longitude(longitude)
            .latitude(latitude)
            .registrant(registrant)
            .build();
  }
}
