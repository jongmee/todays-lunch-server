package LikeLion.TodaysLunch.restaurant.repository;

import LikeLion.TodaysLunch.category.domain.FoodCategory;
import LikeLion.TodaysLunch.category.domain.LocationCategory;
import LikeLion.TodaysLunch.category.domain.LocationTag;
import LikeLion.TodaysLunch.category.domain.RecommendCategory;
import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationBuilder {
    public static Specification<Restaurant> buildSpecification(FoodCategory foodCategory, LocationCategory locationCategory,
        LocationTag locationTag, RecommendCategory recommendCategory, String keyword, Boolean judgement, Member member){
        Specification<Restaurant> spec =(root, query, criteriaBuilder) -> null;
        if (foodCategory != null) {
            spec = spec.and(RestaurantSpecification.equalFoodCategory(foodCategory));
        }
        if (locationCategory != null) {
            spec = spec.and(RestaurantSpecification.equalLocationCategory(locationCategory));
        }
        if (locationTag != null) {
            spec = spec.and(RestaurantSpecification.equalLocationTag(locationTag));
        }
        if (recommendCategory != null) {
            spec = spec.and(RestaurantSpecification.equalRecommendCategory(recommendCategory));
        }
        if (keyword != null) {
            spec = spec.and(RestaurantSpecification.likeRestaurantName(keyword));
        }
        if (member != null) {
            spec = spec.and(RestaurantSpecification.equalRegistrant(member));
        }
        return spec.and(RestaurantSpecification.equalJudgement(judgement));
    }
}
