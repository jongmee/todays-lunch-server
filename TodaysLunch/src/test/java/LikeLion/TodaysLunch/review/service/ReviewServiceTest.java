package LikeLion.TodaysLunch.review.service;

import static org.junit.jupiter.api.Assertions.*;

import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import LikeLion.TodaysLunch.review.domain.Review;
import LikeLion.TodaysLunch.review.dto.ReviewDto;
import LikeLion.TodaysLunch.review.repository.ReviewRepository;
import LikeLion.TodaysLunch.skeleton.ServiceTest;
import LikeLion.TodaysLunch.skeleton.TestRestaurant;
import LikeLion.TodaysLunch.skeleton.TestUser;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ReviewServiceTest extends ServiceTest {
  @Autowired
  private ReviewRepository reviewRepository;
  @Autowired
  private ReviewService reviewService;
  @Test
  void 베스트리뷰_맛집엔티티에_반영하기(){
    // given
    TestUser 유저1 = makeTestUser("qwer@naver.com", "1234", "유저1", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestUser 유저2 = makeTestUser("qwer1@naver.com", "1234", "유저2", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));

    TestRestaurant 맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "가츠벤또","정말 맛있다", 126.940155, 37.546924, 유저1.getMember());

    리뷰_생성하기(유저1.getMember(), 맛집.getRestaurant(), "아주맛잇군", 2);
    리뷰_생성하기(유저2.getMember(), 맛집.getRestaurant(), "아주맛잇군2", 2);

    // when
    reviewService.addOrCancelLike(1L, 2L, 유저1.getMember());
    reviewService.addOrCancelLike(1L, 1L, 유저2.getMember());
    reviewService.addOrCancelLike(1L, 2L, 유저2.getMember());

    //then
    Restaurant restaurantForTest = testRestaurantEnviron.restaurantRepository().findById(1L).get();
    Assertions.assertEquals("아주맛잇군2", restaurantForTest.getBestReview());
  }
  @Test
  void 리뷰_등록하기(){
    // given
    TestUser 유저1 = makeTestUser("qwer@naver.com", "1234", "유저1", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));

    TestRestaurant 맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "가츠벤또","정말 맛있다", 126.940155, 37.546924, 유저1.getMember());
    ReviewDto reviewDto1 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(1).build();
    ReviewDto reviewDto2 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(2).build();

    // when
    reviewService.create(1L, reviewDto1, 유저1.getMember());
    reviewService.create(1L, reviewDto2, 유저1.getMember());

    //then
    Assertions.assertEquals(2L, 맛집.getRestaurant().getReviewCount());
  }
  @Test
  void 리뷰_등록하고_맛집_평점에_반영하기(){
    // given
    TestUser 유저1 = makeTestUser("qwer@naver.com", "1234", "유저1", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));

    TestRestaurant 맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "가츠벤또","정말 맛있다", 126.940155, 37.546924, 유저1.getMember());
    ReviewDto reviewDto1 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(1).build();
    ReviewDto reviewDto2 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(2).build();
    ReviewDto reviewDto3 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(5).build();
    ReviewDto reviewDto4 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(3).build();
    ReviewDto reviewDto5 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(3).build();

    // when
    reviewService.create(1L, reviewDto1, 유저1.getMember());
    Double rating1 = 맛집.getRestaurant().getRating();
    reviewService.create(1L, reviewDto2, 유저1.getMember());
    reviewService.create(1L, reviewDto3, 유저1.getMember());
    reviewService.create(1L, reviewDto4, 유저1.getMember());
    reviewService.create(1L, reviewDto5, 유저1.getMember());
    Double rating2 = 맛집.getRestaurant().getRating();

    //then
    Assertions.assertEquals(1.0, rating1);
    Assertions.assertEquals((1.0+2.0+5.0+3.0+3.0)/5, rating2);
  }

  Review 리뷰_생성하기(Member member, Restaurant restaurant, String reviewContent, Integer rating){
    Review review = new Review(reviewContent, rating);
    review.setMember(member);
    review.setRestaurant(restaurant);
    return reviewRepository.save(review);
  }

}