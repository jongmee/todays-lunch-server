package LikeLion.TodaysLunch.review.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import LikeLion.TodaysLunch.review.domain.Review;
import LikeLion.TodaysLunch.review.dto.ReviewDto;
import LikeLion.TodaysLunch.review.repository.ReviewRepository;
import LikeLion.TodaysLunch.skeleton.service.ServiceTest;
import LikeLion.TodaysLunch.skeleton.service.TestRestaurant;
import LikeLion.TodaysLunch.skeleton.service.TestUser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    Review 리뷰1 = 리뷰_생성하기(유저1.getMember(), 맛집.getRestaurant(), "아주맛잇군", 2);
    Review 리뷰2 = 리뷰_생성하기(유저2.getMember(), 맛집.getRestaurant(), "아주맛잇군2", 2);

    // when
    Long 맛집ID = 맛집.getRestaurant().getId();
    reviewService.addOrCancelLike(맛집ID, 리뷰2.getId(), 유저1.getMember());
    reviewService.addOrCancelLike(맛집ID, 리뷰1.getId(), 유저2.getMember());
    reviewService.addOrCancelLike(맛집ID, 리뷰2.getId(), 유저2.getMember());

    //then
    assertEquals("아주맛잇군2", 맛집.getRestaurant().getBestReview());
  }

  @Test
  void 리뷰리스트_반환하기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저1", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));

    TestRestaurant 맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "가츠벤또","정말 맛있다", 126.940155, 37.546924, 유저.getMember());
    ReviewDto 작성한_리뷰1 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(1).build();
    ReviewDto 작성한_리뷰2 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(2).build();
    ReviewDto 작성한_리뷰3 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(5).build();
    ReviewDto 작성한_리뷰4 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(3).build();
    ReviewDto 작성한_리뷰5 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(3).build();

    Long 맛집ID = 맛집.getRestaurant().getId();
    reviewService.create(맛집ID, 작성한_리뷰1, 유저.getMember());
    reviewService.create(맛집ID, 작성한_리뷰2, 유저.getMember());
    reviewService.create(맛집ID, 작성한_리뷰3, 유저.getMember());
    reviewService.create(맛집ID, 작성한_리뷰4, 유저.getMember());
    reviewService.create(맛집ID, 작성한_리뷰5, 유저.getMember());

    // when
    HashMap 응답값 = reviewService.reviewsList(맛집ID, 0, 2, "rating", "ascending", 유저.getMember());

    // then
    assertEquals(3, 응답값.get("totalPages"));
    assertEquals(5L, 응답값.get("totalReviewCount"));
  }

  @Test
  void 리뷰_등록하기(){
    // given
    TestUser 유저1 = makeTestUser("qwer@naver.com", "1234", "유저1", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));

    TestRestaurant 맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "가츠벤또","정말 맛있다", 126.940155, 37.546924, 유저1.getMember());
    ReviewDto 작성한_리뷰1 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(1).build();
    ReviewDto 작성한_리뷰2 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(2).build();

    // when
    Long 맛집ID = 맛집.getRestaurant().getId();
    reviewService.create(맛집ID, 작성한_리뷰1, 유저1.getMember());
    reviewService.create(맛집ID, 작성한_리뷰2, 유저1.getMember());

    //then
    Long 맛집의_리뷰_수 = 맛집.getRestaurant().getReviewCount();
    assertEquals(2L, 맛집의_리뷰_수);
  }

  @Test
  void 리뷰_등록하고_맛집_평점에_반영하기(){
    // given
    TestUser 유저1 = makeTestUser("qwer@naver.com", "1234", "유저1", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));

    TestRestaurant 맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "가츠벤또","정말 맛있다", 126.940155, 37.546924, 유저1.getMember());
    ReviewDto 작성한_리뷰1 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(1).build();
    ReviewDto 작성한_리뷰2 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(2).build();
    ReviewDto 작성한_리뷰3 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(5).build();
    ReviewDto 작성한_리뷰4 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(3).build();
    ReviewDto 작성한_리뷰5 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(3).build();

    // when
    Long 맛집ID = 맛집.getRestaurant().getId();
    reviewService.create(맛집ID, 작성한_리뷰1, 유저1.getMember());
    Double 평점1 = 맛집.getRestaurant().getRating();
    reviewService.create(맛집ID, 작성한_리뷰2, 유저1.getMember());
    reviewService.create(맛집ID, 작성한_리뷰3, 유저1.getMember());
    reviewService.create(맛집ID, 작성한_리뷰4, 유저1.getMember());
    reviewService.create(맛집ID, 작성한_리뷰5, 유저1.getMember());
    Double 평점2 = 맛집.getRestaurant().getRating();

    //then
    assertEquals(1.0, 평점1);
    assertEquals((1.0+2.0+5.0+3.0+3.0)/5, 평점2);
  }

  @Test
  void 리뷰를_수정하고_맛집_평점에_반영하기(){
    // given
    TestUser 유저1 = makeTestUser("qwer@naver.com", "1234", "유저1", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));

    TestRestaurant 맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "가츠벤또","정말 맛있다", 126.940155, 37.546924, 유저1.getMember());
    ReviewDto 작성한_리뷰 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(5).build();
    ReviewDto 작성한_리뷰2 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(2).build();
    ReviewDto 작성한_리뷰3 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(5).build();

    Long 맛집ID = 맛집.getRestaurant().getId();
    Review 생성된_리뷰1 = reviewService.create(맛집ID, 작성한_리뷰, 유저1.getMember());
    Review 생성된_리뷰2 = reviewService.create(맛집ID, 작성한_리뷰2, 유저1.getMember());
    Review 생성된_리뷰3 = reviewService.create(맛집ID, 작성한_리뷰3, 유저1.getMember());
    Double 기존평점 = 맛집.getRestaurant().getRating();

    // when
    ReviewDto 수정한_리뷰 = ReviewDto.builder().reviewContent("생각해보니까 별로인듯").rating(1).build();
    reviewService.update(생성된_리뷰1.getId(), 맛집ID, 수정한_리뷰);
    Double 수정된평점 = 맛집.getRestaurant().getRating();
    assertEquals((5.0+2.0+5.0)/3, 기존평점);
    assertEquals((1.0+2.0+5.0)/3, 수정된평점);
  }

  @Test
  void 리뷰를_삭제하고_맛집_평점에_반영하기(){
    // given
    TestUser 유저1 = makeTestUser("qwer@naver.com", "1234", "유저1", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));

    TestRestaurant 맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "가츠벤또","정말 맛있다", 126.940155, 37.546924, 유저1.getMember());
    ReviewDto 작성한_리뷰 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(5).build();
    ReviewDto 작성한_리뷰2 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(2).build();
    ReviewDto 작성한_리뷰3 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(5).build();

    Long 맛집ID = 맛집.getRestaurant().getId();
    Review 생성된_리뷰1 = reviewService.create(맛집ID, 작성한_리뷰, 유저1.getMember());
    Review 생성된_리뷰2 = reviewService.create(맛집ID, 작성한_리뷰2, 유저1.getMember());
    Review 생성된_리뷰3 = reviewService.create(맛집ID, 작성한_리뷰3, 유저1.getMember());
    Double 기존평점 = 맛집.getRestaurant().getRating();

    // when
    reviewService.delete(생성된_리뷰1.getId(), 맛집ID);
    Double 수정된평점 = 맛집.getRestaurant().getRating();
    assertEquals((5.0+2.0+5.0)/3, 기존평점);
    assertEquals((2.0+5.0)/2, 수정된평점);
  }

  @Test
  void 리뷰_좋아요_누르고_취소하기(){
    // given
    TestUser 유저1 = makeTestUser("qwer@naver.com", "1234", "유저1", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "가츠벤또","정말 맛있다", 126.940155, 37.546924, 유저1.getMember());
    Review 리뷰 = 리뷰_생성하기(유저1.getMember(), 맛집.getRestaurant(), "아주맛잇군", 2);

    // when
    Long 맛집ID = 맛집.getRestaurant().getId();
    Long 리뷰ID = 리뷰.getId();
    reviewService.addOrCancelLike(맛집ID, 리뷰ID, 유저1.getMember());
    reviewService.addOrCancelLike(맛집ID, 리뷰ID, 유저1.getMember());

    // then
    Long 리뷰의_좋아요_수 = 리뷰.getLikeCount();
    assertEquals(0L, 리뷰의_좋아요_수);
  }

  @Test
  void 유저가_좋아요한_리뷰_구분하기(){
    // given
    TestUser 유저1 = makeTestUser("qwer@naver.com", "1234", "유저1", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestUser 유저2 = makeTestUser("qwer1@naver.com", "1234", "유저2", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "가츠벤또","정말 맛있다", 126.940155, 37.546924, 유저1.getMember());
    Review 리뷰1 = 리뷰_생성하기(유저1.getMember(), 맛집.getRestaurant(), "아주맛잇군!", 5);
    Review 리뷰2 = 리뷰_생성하기(유저1.getMember(), 맛집.getRestaurant(), "맛잇군!", 2);

    // when
    Long 맛집ID = 맛집.getRestaurant().getId();
    Long 리뷰ID = 리뷰1.getId();
    reviewService.addOrCancelLike(맛집ID, 리뷰ID, 유저2.getMember());
    Map 응답값 = reviewService.reviewsList(맛집ID, 0, 5, "rating", "ascending", 유저2.getMember());
    List<ReviewDto> 전체_리뷰들 = (List<ReviewDto>) 응답값.get("data");

    // then
    ReviewDto 조회한_리뷰1 = 전체_리뷰들.get(0);
    ReviewDto 조회한_리뷰2 = 전체_리뷰들.get(1);
    assertEquals(리뷰1.getId(), 조회한_리뷰2.getId());
    assertEquals(리뷰2.getId(), 조회한_리뷰1.getId());
    assertEquals("false", 조회한_리뷰1.getLiked());
    assertEquals("true", 조회한_리뷰2.getLiked());
  }

  @Test
  void 비로그인시_리뷰에_좋아요_확인못하기(){
    // given
    TestUser 유저1 = makeTestUser("qwer@naver.com", "1234", "유저1", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestUser 유저2 = makeTestUser("qwer1@naver.com", "1234", "유저2", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "가츠벤또","정말 맛있다", 126.940155, 37.546924, 유저1.getMember());
    Review 리뷰1 = 리뷰_생성하기(유저1.getMember(), 맛집.getRestaurant(), "아주맛잇군!", 5);
    Review 리뷰2 = 리뷰_생성하기(유저2.getMember(), 맛집.getRestaurant(), "맛잇군!", 2);

    // when
    Long 맛집ID = 맛집.getRestaurant().getId();
    Long 리뷰ID = 리뷰1.getId();
    reviewService.addOrCancelLike(맛집ID, 리뷰ID, 유저2.getMember());
    Map 응답값 = reviewService.reviewsList(맛집ID, 0, 5, "rating", "ascending", null);
    List<ReviewDto> 전체_리뷰들 = (List<ReviewDto>) 응답값.get("data");

    // then
    ReviewDto 조회한_리뷰1 = 전체_리뷰들.get(0);
    ReviewDto 조회한_리뷰2 = 전체_리뷰들.get(1);
    assertEquals("false", 조회한_리뷰1.getLiked());
    assertEquals("false", 조회한_리뷰2.getLiked());
  }

  @Test
  void 유저가_쓴_리뷰_모아보기(){
    // given
    TestUser 유저1 = makeTestUser("qwer@naver.com", "1234", "유저1", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestUser 유저2 = makeTestUser("qwer1@naver.com", "1234", "유저2", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "가츠벤또","정말 맛있다", 126.940155, 37.546924, 유저1.getMember());
    ReviewDto 작성한_리뷰1 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(1).build();
    ReviewDto 작성한_리뷰2 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(2).build();
    ReviewDto 작성한_리뷰3 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(2).build();

    // when
    Long 맛집ID = 맛집.getRestaurant().getId();
    reviewService.create(맛집ID, 작성한_리뷰1, 유저1.getMember());
    reviewService.create(맛집ID, 작성한_리뷰2, 유저2.getMember());
    reviewService.create(맛집ID, 작성한_리뷰3, 유저2.getMember());
    Map 응답값 = reviewService.myReviewList(유저2.getMember().getId(), 0, 3, "rating", "ascending");

    // then
    List<ReviewDto> 유저2의_리뷰들 = (List<ReviewDto>) 응답값.get("data");
    assertEquals(2, 유저2의_리뷰들.size());
  }

  @Test
  void 리뷰등록하고_맛집에_리뷰수_반영하기(){
    // given
    TestUser 유저1 = makeTestUser("qwer@naver.com", "1234", "유저1", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "가츠벤또","정말 맛있다", 126.940155, 37.546924, 유저1.getMember());
    ReviewDto 작성한_리뷰1 = ReviewDto.builder().reviewContent("정말 맛있는 집이예요!").rating(1).build();

    // when
    Long 맛집ID = 맛집.getRestaurant().getId();
    reviewService.create(맛집ID, 작성한_리뷰1, 유저1.getMember());

    // then
    assertEquals(1L, 맛집.getRestaurant().getReviewCount());
  }

  Review 리뷰_생성하기(Member member, Restaurant restaurant, String reviewContent, Integer rating){
    Review review = new Review(reviewContent, rating);
    review.setMember(member);
    review.setRestaurant(restaurant);
    return reviewRepository.save(review);
  }
}