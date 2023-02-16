package LikeLion.TodaysLunch.domain;

import com.sun.istack.NotNull;
import javax.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
@Getter
@Entity
public class Restaurant {
  @PrePersist
  public void prePersist() {
    this.restaurantRecmd = this.restaurantRecmd == null? 0:this.restaurantRecmd;
    this.restaurantDecmd = this.restaurantDecmd == null? 0:this.restaurantDecmd;
    this.rating = this.rating == null? 0.0:this.rating;
    this.judgement = this.judgement == null? false:this.judgement;
    this.reviewCount = this.reviewCount == null? 0L:this.reviewCount;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
//  @NotNull
  private String restaurantName;
  @ManyToOne
  @JoinColumn
  private FoodCategory foodCategory;
  @ManyToOne
  @JoinColumn
  private LocationCategory locationCategory;
  @ManyToOne
  @JoinColumn
  private LocationTag locationTag;
  private Long restaurantRecmd;
  private Long restaurantDecmd;
//  /**
//   * Todo: 이미지 필드에 대해 찾아보기
//   */
//  private String restaurantImage;
  private Double latitude;
  private Double longitude;

  private String address;
  private String introduction;
  private Double rating;
  private Boolean judgement;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private Long agreement;

  private Long reviewCount;
  public void setId(Long id) {
    this.id = id;
  }

  public void setRestaurantName(String restaurantName) {
    this.restaurantName = restaurantName;
  }

  public void setRating(Double rating) {
    this.rating = rating;
  }

  public void setReviewCount(Long reviewCount) {
    this.reviewCount = reviewCount;
  }
}
