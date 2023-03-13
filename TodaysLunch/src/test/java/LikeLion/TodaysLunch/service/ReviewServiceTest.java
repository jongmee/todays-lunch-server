package LikeLion.TodaysLunch.service;

import static org.junit.jupiter.api.Assertions.*;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.LocationCategory;
import LikeLion.TodaysLunch.domain.LocationTag;
import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.domain.Review;
import LikeLion.TodaysLunch.repository.DataJpaRestaurantRepository;
import LikeLion.TodaysLunch.repository.FoodCategoryRepository;
import LikeLion.TodaysLunch.repository.LocationCategoryRepository;
import LikeLion.TodaysLunch.repository.LocationTagRepository;
import LikeLion.TodaysLunch.repository.MemberRepository;
import LikeLion.TodaysLunch.repository.ReviewRepository;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class ReviewServiceTest {
  @Autowired
  private DataJpaRestaurantRepository restaurantRepository;
  @Autowired
  private ReviewRepository reviewRepository;
  @Autowired
  private FoodCategoryRepository foodCategoryRepository;
  @Autowired
  private LocationCategoryRepository locationCategoryRepository;
  @Autowired
  private LocationTagRepository locationTagRepository;
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private ReviewService reviewService;
  @Test
  void 베스트리뷰_맛집엔티티에_반영하기(){
    // given
    FoodCategory foodCategory = 음식카테고리_생성하기();
    LocationCategory locationCategory = 위치카테고리_생성하기();
    LocationTag locationTag = 위치태그_생성하기();
    Member member = 유저_생성하기("nickname1", "password", foodCategory, locationCategory);
    Member member2 = 유저_생성하기("nickname2", "password", foodCategory, locationCategory);

    Restaurant restaurant = 맛집_생성하기(foodCategory, locationCategory, locationTag, member);

    리뷰_생성하기(member, restaurant, "아주맛잇군", 2);
    리뷰_생성하기(member, restaurant, "아주맛잇군2", 2);

    // when
    reviewService.addOrCancelLike(1L, 2L, member);
    reviewService.addOrCancelLike(1L, 1L, member2);
    reviewService.addOrCancelLike(1L, 2L, member2);

    //then
    Restaurant restaurantForTest = restaurantRepository.findById(1L).get();
    Assertions.assertEquals("아주맛잇군2", restaurantForTest.getBestReview());
  }
  FoodCategory 음식카테고리_생성하기(){
    FoodCategory foodCategory = new FoodCategory();
    foodCategory.setName("food_category1");
    return foodCategoryRepository.save(foodCategory);
  }
  LocationCategory 위치카테고리_생성하기(){
    LocationCategory locationCategory = new LocationCategory();
    locationCategory.setName("location_category1");
    locationCategory.setLatitude(0.0);
    locationCategory.setLongitude(0.0);
    return locationCategoryRepository.save(locationCategory);
  }
  LocationTag 위치태그_생성하기(){
    LocationTag locationTag = new LocationTag();
    locationTag.setName("location_tag1");
    return locationTagRepository.save(locationTag);
  }
  Member 유저_생성하기(String nickname, String password, FoodCategory foodCategory, LocationCategory locationCategory){
    return memberRepository.save(new Member(nickname, password, foodCategory, locationCategory));
  }
  Restaurant 맛집_생성하기(FoodCategory foodCategory, LocationCategory locationCategory, LocationTag locationTag, Member member){
    Restaurant restaurant = Restaurant.builder()
        .restaurantName("restaurant_name1")
        .address("서울")
        .locationTag(locationTag)
        .locationCategory(locationCategory)
        .foodCategory(foodCategory)
        .latitude(0.0)
        .longitude(0.0)
        .introduction("설명")
        .member(member)
        .build();
    return restaurantRepository.save(restaurant);
  }

  Review 리뷰_생성하기(Member member, Restaurant restaurant, String reviewContent, Integer rating){
    Review review = new Review(reviewContent, rating);
    review.setMember(member);
    review.setRestaurant(restaurant);
    return reviewRepository.save(review);
  }

}