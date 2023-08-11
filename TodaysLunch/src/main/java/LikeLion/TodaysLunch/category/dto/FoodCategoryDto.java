package LikeLion.TodaysLunch.category.dto;

import LikeLion.TodaysLunch.category.domain.FoodCategory;
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

  public FoodCategory toEntity(){
    return FoodCategory.builder()
        .id(id)
        .name(name)
        .build();
  }
}
