package LikeLion.TodaysLunch.restaurant.dto.response.common;

import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EngagedRestaurantDto {

    private Long id;
    private String restaurantName;
    private String imageUrl;
    private String foodCategory;
    private String locationCategory;
    private Double rating;
    private Long reviewCount;
    private Boolean liked;

    public static EngagedRestaurantDto fromEntity(Restaurant restaurant, Boolean liked){
        String image = null;
        if (restaurant.getImageUrl() != null)
            image = restaurant.getImageUrl().getImageUrl();

        return EngagedRestaurantDto.builder()
                .id(restaurant.getId())
                .restaurantName(restaurant.getRestaurantName())
                .imageUrl(image)
                .foodCategory(restaurant.getFoodCategory().getName())
                .locationCategory(restaurant.getLocationCategory().getName())
                .rating(restaurant.getRating())
                .reviewCount(restaurant.getReviewCount())
                .liked(liked)
                .build();
    }
}
