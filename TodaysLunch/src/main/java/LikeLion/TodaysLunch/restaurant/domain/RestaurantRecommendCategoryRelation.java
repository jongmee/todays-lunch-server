package LikeLion.TodaysLunch.restaurant.domain;

import LikeLion.TodaysLunch.category.domain.RecommendCategory;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

  @JoinColumn(nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Restaurant restaurant;

  @JoinColumn(nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private RecommendCategory recommendCategory;

  @Builder
  public RestaurantRecommendCategoryRelation(Restaurant restaurant, RecommendCategory recommendCategory){
    this.restaurant = restaurant;
    this.recommendCategory = recommendCategory;
  }

}
