package LikeLion.TodaysLunch.customized.dto;

import LikeLion.TodaysLunch.category.dto.FoodCategoryDto;
import java.util.List;
import lombok.Getter;

@Getter
public class MyFoodCategoryEditDto {
  private List<FoodCategoryDto> categoryList;
}
