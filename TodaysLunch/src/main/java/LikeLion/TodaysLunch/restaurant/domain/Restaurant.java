package LikeLion.TodaysLunch.restaurant.domain;

import LikeLion.TodaysLunch.category.domain.FoodCategory;
import LikeLion.TodaysLunch.image.domain.ImageUrl;
import LikeLion.TodaysLunch.category.domain.LocationCategory;
import LikeLion.TodaysLunch.category.domain.LocationTag;
import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.review.domain.Review;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor
@Entity
public class Restaurant {
  private static final Long EVALUATION_CRITERIA = 4L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 20)
  private String restaurantName;

  @JoinColumn(nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private FoodCategory foodCategory;

  @JoinColumn(nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private LocationCategory locationCategory;

  @JoinColumn(nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private LocationTag locationTag;

  @OneToMany(mappedBy="restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<RestaurantRecommendCategoryRelation> recommendCategoryRelations = new HashSet<>();

  @JoinColumn
  @OneToOne(fetch = FetchType.LAZY)
  private ImageUrl imageUrl;

  @Column(nullable = false)
  private Double latitude;

  @Column(nullable = false)
  private Double longitude;

  @Column(nullable = false, length = 150)
  private String address;

  @Column(length = 300)
  private String introduction;

  private Double rating;

  @Column(nullable = false)
  private Boolean judgement;

  @Column(nullable = false)
  private LocalDate startDate;

  @Column(nullable = false)
  private LocalDate endDate;

  @Column(nullable = false)
  private Long agreementCount;

  @Column(nullable = false)
  private Long reviewCount;

  @Column(nullable = false)
  private Long likeCount;

  private Long lowestPrice;

  @JoinColumn
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  private Review bestReview;

  @Column(nullable = false)
  private LocalDateTime updatedDate;

  @JoinColumn
  @OneToOne(fetch = FetchType.LAZY)
  private Member registrant;

  @Builder
  public Restaurant(String restaurantName, FoodCategory foodCategory,
      LocationCategory locationCategory, LocationTag locationTag,
      String address, String introduction, Double longitude, Double latitude,
      Member registrant) {
    this.restaurantName = restaurantName;
    this.foodCategory = foodCategory;
    this.locationCategory = locationCategory;
    this.locationTag = locationTag;
    this.address = address;
    this.introduction = introduction;
    this.longitude = longitude;
    this.latitude = latitude;
    this.registrant = registrant;
    this.judgement = true;
    this.startDate = LocalDate.now();
    this.endDate = LocalDate.now().plusDays(7);
    this.agreementCount = 0L;
    this.reviewCount = 0L;
    this.updatedDate = LocalDateTime.now();
    this.likeCount = 0L;
    this.rating = 0.0;
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
  public void setLowestPrice(Long lowestPrice) {
    this.lowestPrice = lowestPrice;
  }
  public void setAgreementCount(Long agreementCount) {
    this.agreementCount = agreementCount;
  }
  public void setJudgement(Boolean judgement) {
    this.judgement = judgement;
  }
  public void setBestReview(Review review) {
    this.bestReview = review;
  }
  public void setUpdatedDate(LocalDateTime date) {
    this.updatedDate = date;
  }
  public void setNullRegistrant() {
    this.registrant = null;
  }
  public void setLikeCount(Long likeCount) {
    this.likeCount = likeCount;
  }
  public void addRecommendCategoryRelation(RestaurantRecommendCategoryRelation recommendCategoryRelation){
    recommendCategoryRelations.add(recommendCategoryRelation);
  }
  public void deleteRecommendCategoryRelation(RestaurantRecommendCategoryRelation recommendCategoryRelation){
    recommendCategoryRelations.remove(recommendCategoryRelation);
  }

  public void increaseAgreementCount() {
    this.agreementCount += 1;
  }

  public void decreaseAgreementCount() {
    this.agreementCount -= 1;
  }

  public void evaluateRestaurant() {
    if(this.agreementCount.compareTo(EVALUATION_CRITERIA) > 0) {
      this.judgement = false;
    }
  }

  public void increaseLikeCount() {
    this.likeCount += 1;
  }

  public void decreaseLikeCount() {
    this.likeCount -= 1;
  }
}
