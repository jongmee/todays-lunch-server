package LikeLion.TodaysLunch.menu.domain;

import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Menu {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Long price;

  private Long salePrice;

  private String saleExplain;

  @Column(nullable = false)
  private Long imageCount;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Restaurant restaurant;

  @Builder
  public Menu(String name, Long price, Long salePrice, String saleExplain){
    this.name = name;
    this.price = price;
    this.imageCount = 0L;
    this.salePrice = salePrice;
    this.saleExplain = saleExplain;
  }

  public Menu updateMenu(String name, Long price, Long salePrice, String saleExplain){
    this.name = name;
    this.price = price;
    this.salePrice = salePrice;
    this.saleExplain = saleExplain;
    return this;
  }

  public void setImageCount(Long imageCount) {
    this.imageCount = imageCount;
  }
  public void setRestaurant(Restaurant restaurant) {
    this.restaurant = restaurant;
  }
}
