package LikeLion.TodaysLunch.domain.relation;

import LikeLion.TodaysLunch.domain.RecommendCategory;
import LikeLion.TodaysLunch.domain.Restaurant;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class RestaurantRecommendCategoryRelation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  @JoinColumn
  private Restaurant restaurant;
  @ManyToOne
  @JoinColumn
  private RecommendCategory recommendCategory;

  @Builder
  public RestaurantRecommendCategoryRelation(Restaurant restaurant, RecommendCategory recommendCategory){
    this.restaurant = restaurant;
    this.recommendCategory = recommendCategory;
  }

}
