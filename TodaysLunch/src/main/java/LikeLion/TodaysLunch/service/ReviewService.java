package LikeLion.TodaysLunch.service;

import LikeLion.TodaysLunch.controller.ReviewController;
import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.domain.Review;
import LikeLion.TodaysLunch.domain.ReviewLike;
import LikeLion.TodaysLunch.dto.ReviewDto;
import LikeLion.TodaysLunch.exception.NotFoundException;
import LikeLion.TodaysLunch.repository.DataJpaRestaurantRepository;
import LikeLion.TodaysLunch.repository.MenuRepository;
import LikeLion.TodaysLunch.repository.ReviewLikeRepository;
import LikeLion.TodaysLunch.repository.ReviewRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;


@Transactional
@RequiredArgsConstructor
public class ReviewService {
  private final ReviewRepository reviewRepository;
  private final DataJpaRestaurantRepository restaurantRepository;
  private final ReviewLikeRepository reviewLikeRepository;

  public Review create(Long restaurantId, ReviewDto reviewDto, Member member){
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new NotFoundException("맛집"));

    Long count = restaurant.getReviewCount() + 1;
    restaurant.setReviewCount(count);

    Double rating = restaurant.getRating();
    if(rating.equals(0.0)){
      restaurant.setRating((double)reviewDto.getRating());
    } else {
      Pageable pageable = PageRequest.of(0, (int)reviewRepository.count());
      List<Review> reviews= reviewRepository.findAllByRestaurant(restaurant, pageable).getContent();
      Double sum = 0.0;
      for(int i = 0; i < reviews.size(); i++){
        sum += reviews.get(i).getRating();
      }
      sum += reviewDto.getRating();
      restaurant.setRating((double)sum/count);
    }

    restaurantRepository.save(restaurant);

    Review review = reviewDto.toEntity();
    review.setRestaurant(restaurant);
    review.setMember(member);
    return reviewRepository.save(review);
  }

  public Page<ReviewDto> reviewsList(Long restaurantId, Pageable pageable){
    Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
    return reviewRepository.findAllByRestaurant(restaurant, pageable).map(ReviewDto::fromEntity);
  }

  public Review update(Long reviewId, Long restaurantId, ReviewDto reviewDto){
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new IllegalArgumentException("리뷰 수정 실패! 대상 리뷰가 없습니다."));

    Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
    Long count = restaurant.getReviewCount();
    Pageable pageable = PageRequest.of(0, (int)reviewRepository.count());
    List<Review> reviews= reviewRepository.findAllByRestaurant(restaurant, pageable).getContent();
    Double sum = 0.0;
    for(int i = 0; i < reviews.size()-1; i++){
      sum += reviews.get(i).getRating();
    }
    sum += reviewDto.getRating();
    restaurant.setRating((double)sum/count);

    restaurantRepository.save(restaurant);
    review.update(reviewDto);

    return reviewRepository.save(review);
  }

  public Review delete(Long reviewId, Long restaurantId){
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new IllegalArgumentException("리뷰 삭제 실패! 대상 리뷰가 없습니다."));

    Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
    Long count = restaurant.getReviewCount() - 1;
    restaurant.setReviewCount(count);

    Pageable pageable = PageRequest.of(0, (int)reviewRepository.count());
    List<Review> reviews= reviewRepository.findAllByRestaurant(restaurant, pageable).getContent();
    Double sum = 0.0;
    for(int i = 0; i < reviews.size()-1; i++){
      sum += reviews.get(i).getRating();
    }
    restaurant.setRating((double)sum/count);

    restaurantRepository.save(restaurant);

    reviewRepository.delete(review);
    return review;
  }

  public void addOrCancelLike(Long restaurantId, Long reviewId, Member member){
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new NotFoundException("맛집"));
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new NotFoundException("리뷰"));

    if (isNotAlreadyLike(member, review)){
      reviewLikeRepository.save(new ReviewLike(member, review));

      AtomicLong likeCount = review.getLikeCount();
      likeCount.incrementAndGet();
      review.setLikeCount(likeCount);

      reviewRepository.save(review);
    } else {
      ReviewLike like = reviewLikeRepository.findByReviewAndMember(review, member).get();
      AtomicLong likeCount = review.getLikeCount();
      likeCount.decrementAndGet();
      review.setLikeCount(likeCount);
      reviewRepository.save(review);
      reviewLikeRepository.delete(like);
    }

    Pageable pageable = PageRequest.of(0, (int)reviewRepository.count(), Sort.by("likeCount").descending());
    Review bestReview = reviewRepository.findAllByRestaurant(restaurant, pageable).getContent().get(0);
    if(bestReview.getLikeCount().get() <= review.getLikeCount().get()){
      restaurant.setBestReview(review.getReviewContent());
      restaurantRepository.save(restaurant);
    }
  }

  public String isAlreadyLike(Member member, Long reviewId){
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new NotFoundException("리뷰"));

    if(isNotAlreadyLike(member, review))
      return "false";
    else
      return "true";
  }

  // 유저가 이미 추천한 리뷰인지 체크
  private boolean isNotAlreadyLike(Member member, Review review){
    return reviewLikeRepository.findByReviewAndMember(review, member).isEmpty();
  }

}
