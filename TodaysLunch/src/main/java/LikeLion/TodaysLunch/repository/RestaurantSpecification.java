package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.LocationCategory;
import LikeLion.TodaysLunch.domain.LocationTag;
import LikeLion.TodaysLunch.domain.Restaurant;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class RestaurantSpecification {

  public static Specification<Restaurant> equalJudgement(Boolean judgement){
    return new Specification<Restaurant>() {
      @Override
      public Predicate toPredicate(Root<Restaurant> root, CriteriaQuery<?> query,
          CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get("judgement"), judgement);
      }
    };
  }
  public static Specification<Restaurant> equalFoodCategory(FoodCategory foodCategory){
    return new Specification<Restaurant>() {
      @Override
      public Predicate toPredicate(Root<Restaurant> root, CriteriaQuery<?> query,
          CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get("foodCategory"), foodCategory);
      }
    };
  }

  public static Specification<Restaurant> equalLocationCategory(LocationCategory locationCategory){
    return new Specification<Restaurant>() {
      @Override
      public Predicate toPredicate(Root<Restaurant> root, CriteriaQuery<?> query,
          CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get("locationCategory"), locationCategory);
      }
    };
  }

  public static Specification<Restaurant> equalLocationTag(LocationTag locationTag){
    return new Specification<Restaurant>() {
      @Override
      public Predicate toPredicate(Root<Restaurant> root, CriteriaQuery<?> query,
          CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get("locationTag"), locationTag);
      }
    };
  }

  public static Specification<Restaurant> likeRestaurantName(String restaurantName){
    return new Specification<Restaurant>() {
      @Override
      public Predicate toPredicate(Root<Restaurant> root, CriteriaQuery<?> query,
          CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.like(root.get("restaurantName"), "%"+restaurantName+"%");
      }
    };
  }
}
