package LikeLion.TodaysLunch.dto;

import LikeLion.TodaysLunch.domain.FoodCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodCategoryDto {
  private Long id;
  private String name;
  public static FoodCategoryDto fromEntity(FoodCategory foodCategory){
    return FoodCategoryDto.builder()
        .id(foodCategory.getId())
        .name(foodCategory.getName())
        .build();
  }
}
