package LikeLion.TodaysLunch.domain;

import LikeLion.TodaysLunch.domain.relation.RestaurantRecommendCategoryRelation;
import com.sun.istack.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.*;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@NoArgsConstructor
@Entity
public class Restaurant {
  @PrePersist
  public void prePersist() {
    this.rating = this.rating == null? 0.0:this.rating;
    this.judgement = this.judgement == null? false:this.judgement;
    this.reviewCount = this.reviewCount == null? 0L:this.reviewCount;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
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
  @OneToMany(mappedBy="restaurant", cascade = CascadeType.ALL)
  private Set<RestaurantRecommendCategoryRelation> recommendCategoryRelations = new HashSet<>();
  @OneToOne
  @JoinColumn
  private ImageUrl imageUrl;
  private Double latitude;
  private Double longitude;
  private String address;
  private String introduction;
  private Double rating;
  private Boolean judgement;
  private LocalDate startDate;
  private LocalDate endDate;
  private AtomicLong agreementCount;
  private Long reviewCount;
  private Long lowestPrice;
  private String bestReview;
  @OneToOne
  @JoinColumn
  private Member member;

  // 맛집 심사를 위한 등록에서 쓰임
  @Builder
  public Restaurant(String restaurantName, FoodCategory foodCategory,
      LocationCategory locationCategory, LocationTag locationTag,
      String address, String introduction, Double longitude, Double latitude,
      Member member) {
    this.restaurantName = restaurantName;
    this.foodCategory = foodCategory;
    this.locationCategory = locationCategory;
    this.locationTag = locationTag;
    this.address = address;
    this.introduction = introduction;
    this.judgement = true;
    this.startDate = LocalDate.now();
    this.endDate = LocalDate.now().plusDays(7);
    this.longitude = longitude;
    this.latitude = latitude;
    this.member = member;
    this.agreementCount = new AtomicLong(0);
  }
  public void setId(Long id) {
    this.id = id;
  }
  public void setImageUrl(ImageUrl imageUrl) {
    this.imageUrl = imageUrl;
  }
  public void setRating(Double rating) {
    this.rating = rating;
  }
  public void setReviewCount(Long reviewCount) {
    this.reviewCount = reviewCount;
  }
  public void setMember(Member member) { this.member = member; }
  public void setLowestPrice(Long lowestPrice) { this.lowestPrice = lowestPrice; }
  public void setAgreementCount(AtomicLong agreementCount) { this.agreementCount = agreementCount; }
  public void setJudgement(Boolean judgement) { this.judgement = judgement; }
  public void setBestReview(String bestReview) { this.bestReview = bestReview; }
  public void addRecommendCategoryRelation(RestaurantRecommendCategoryRelation recommendCategoryRelation){
    recommendCategoryRelations.add(recommendCategoryRelation);
  }
}
