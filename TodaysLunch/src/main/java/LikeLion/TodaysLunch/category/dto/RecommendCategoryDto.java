package LikeLion.TodaysLunch.category.dto;

import LikeLion.TodaysLunch.category.domain.RecommendCategory;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RecommendCategoryDto {
  @Getter
  @NoArgsConstructor
  public static class Edit {
    private List<Long> recommendCategoryIds;
    @Builder
    public Edit(List<Long> recommendCategoryIds) {
      this.recommendCategoryIds = recommendCategoryIds;
    }
  }
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class CategoryList {
    private Long id;
    private String name;
    private String color;
    public static CategoryList fromEntity(RecommendCategory category) {
      return CategoryList.builder()
          .id(category.getId())
          .name(category.getName())
          .color(category.getColor())
          .build();
    }
  }
}
