package LikeLion.TodaysLunch.category.domain;

import LikeLion.TodaysLunch.category.dto.RecommendCategoryDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class RecommendCategory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length=30)
  private String name;

  @Column(nullable = false, length=10)
  private String color;

  @Builder
  public RecommendCategory(String name, String color) {
    this.name = name;
    this.color = color;
  }

  public RecommendCategory update(RecommendCategoryDto.CategoryList categoryDto){
    this.name = categoryDto.getName();
    this.color = categoryDto.getColor();
    return this;
  }
}
