package LikeLion.TodaysLunch.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RecommendCategoryDto {
  @Getter
  @NoArgsConstructor
  @Builder
  public static class Edit {
    private List<Long> recommendCategoryIds;
    @Builder
    public Edit(List<Long> recommendCategoryIds) {
      this.recommendCategoryIds = recommendCategoryIds;
    }
  }
}
