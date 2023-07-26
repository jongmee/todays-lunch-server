package LikeLion.TodaysLunch.customized.dto;

import LikeLion.TodaysLunch.category.dto.LocationCategoryDto;
import java.util.List;
import lombok.Getter;

@Getter
public class MyLocationCategoryEditDto {
  private List<LocationCategoryDto> categoryList;
}
