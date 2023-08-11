package LikeLion.TodaysLunch.restaurant.domain;

import LikeLion.TodaysLunch.category.domain.RecommendCategory;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RestaurantRecommendCategoryRelation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Restaurant restaurant;

  @ManyToOne
  @JoinColumn(nullable = false)
  private RecommendCategory recommendCategory;

  @Builder
  public RestaurantRecommendCategoryRelation(Restaurant restaurant, RecommendCategory recommendCategory){
    this.restaurant = restaurant;
    this.recommendCategory = recommendCategory;
  }

}
