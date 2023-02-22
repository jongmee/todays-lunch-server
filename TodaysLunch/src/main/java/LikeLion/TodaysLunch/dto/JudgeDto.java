package LikeLion.TodaysLunch.dto;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.LocationCategory;
import LikeLion.TodaysLunch.domain.LocationTag;
import LikeLion.TodaysLunch.domain.Restaurant;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class JudgeDto {
  private String restaurantName;
  private FoodCategory foodCategory;
  private LocationCategory locationCategory;
  private LocationTag locationTag;
  private String address;
  private String introduction;
  @Builder
  public JudgeDto(String restaurantName, FoodCategory foodCategory,
      LocationCategory locationCategory, LocationTag locationTag, String address, String introduction) {
    this.restaurantName = restaurantName;
    this.foodCategory = foodCategory;
    this.locationCategory = locationCategory;
    this.locationTag = locationTag;
    this.address = address;
    this.introduction = introduction;
  }

  public Restaurant toEntity(){
    return Restaurant.builder().foodCategory(foodCategory).locationCategory(locationCategory).locationTag(locationTag)
        .address(address).restaurantName(restaurantName).introduction(introduction).build();
  }
}
