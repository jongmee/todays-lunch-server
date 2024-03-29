package LikeLion.TodaysLunch.restaurant.dto.response;

import LikeLion.TodaysLunch.category.dto.RecommendCategoryDto;
import LikeLion.TodaysLunch.category.dto.RecommendCategoryDto.CategoryList;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDto {

  private Long id;
  private String restaurantName;
  private String foodCategory;
  private String locationCategory;
  private String locationTag;
  private Set<RecommendCategoryDto.CategoryList> recommendCategoryList;
  private String imageUrl;
  private Double latitude;
  private Double longitude;
  private String address;
  private Double rating;
  private Long reviewCount;
  private ContributorDto registrant;
  private List<ContributorDto> contributors;
  private String bestReview;
  private Boolean liked;
  private LocalDate updatedDate;
  private Long likeCount;

  public static RestaurantDto fromEntity(Restaurant restaurant, List<ContributorDto> contributors, Boolean liked, String bestReview){
    String image = null;
    if (restaurant.getImageUrl() != null)
      image = restaurant.getImageUrl().getImageUrl();

    ContributorDto registrant = null;
    if(restaurant.getRegistrant() != null)
      registrant = ContributorDto.fromEntity(restaurant.getRegistrant());

    return RestaurantDto.builder()
        .id(restaurant.getId())
        .restaurantName(restaurant.getRestaurantName())
        .foodCategory(restaurant.getFoodCategory().getName())
        .locationCategory(restaurant.getLocationCategory().getName())
        .locationTag(restaurant.getLocationTag().getName())
        .recommendCategoryList(
            restaurant.getRecommendCategoryRelations().stream()
            .map(rel -> CategoryList.fromEntity(rel.getRecommendCategory()))
            .collect(Collectors.toSet()))
        .imageUrl(image)
        .latitude(restaurant.getLatitude())
        .longitude(restaurant.getLongitude())
        .address(restaurant.getAddress())
        .rating(restaurant.getRating())
        .reviewCount(restaurant.getReviewCount())
        .registrant(registrant)
        .contributors(contributors)
        .bestReview(bestReview)
        .liked(liked)
        .updatedDate(restaurant.getUpdatedDate().toLocalDate())
        .likeCount(restaurant.getLikeCount())
        .build();
  }
}
