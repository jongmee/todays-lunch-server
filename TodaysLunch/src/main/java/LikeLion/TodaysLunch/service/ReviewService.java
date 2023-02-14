package LikeLion.TodaysLunch.service;

import LikeLion.TodaysLunch.controller.ReviewController;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.domain.Review;
import LikeLion.TodaysLunch.dto.ReviewDto;
import LikeLion.TodaysLunch.repository.DataJpaRestaurantRepository;
import LikeLion.TodaysLunch.repository.MenuRepository;
import LikeLion.TodaysLunch.repository.ReviewRepository;
import java.util.List;
import javax.transaction.Transactional;

@Transactional
public class ReviewService {
  private final ReviewRepository reviewRepository;
  private final DataJpaRestaurantRepository restaurantRepository;

  public ReviewService(ReviewRepository reviewRepository, DataJpaRestaurantRepository restaurantRepository) {
    this.reviewRepository = reviewRepository;
    this.restaurantRepository = restaurantRepository;
  }
  public Review create(Long restaurantId, ReviewDto reviewDto){
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new IllegalArgumentException("리뷰 생성 실패! 리뷰를 생성하기 위한 대상 맛집이 없습니다."));
    Review review = reviewDto.toEntity();
    review.setRestaurant(restaurant);
    return reviewRepository.save(review);
  }

  public List<Review> reviewsList(Long restaurantId){
    Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
    return reviewRepository.findAllByRestaurant(restaurant);
  }

  public Review update(Long id, ReviewDto reviewDto){
    Review review = reviewRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("리뷰 수정 실패! 대상 리뷰가 없습니다."));
    review.update(reviewDto);
    return reviewRepository.save(review);
  }

  public Review delete(Long id){
    Review review = reviewRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("리뷰 삭제 실패! 대상 리뷰가 없습니다."));
    reviewRepository.delete(review);
    return review;
  }
}
