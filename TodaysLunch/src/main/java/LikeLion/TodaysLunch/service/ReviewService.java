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

    Long count = restaurant.getReviewCount() + 1;
    restaurant.setReviewCount(count);

    Double rating = restaurant.getRating();
    if(rating.equals(0.0)){
      restaurant.setRating((double)reviewDto.getRating());
    } else {
      List<Review> reviews= reviewRepository.findAllByRestaurant(restaurant);
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
    return reviewRepository.save(review);
  }

  public List<Review> reviewsList(Long restaurantId){
    Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
    return reviewRepository.findAllByRestaurant(restaurant);
  }

  public Review update(Long reviewId, Long restaurantId, ReviewDto reviewDto){
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new IllegalArgumentException("리뷰 수정 실패! 대상 리뷰가 없습니다."));

    Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
    Long count = restaurant.getReviewCount();
    List<Review> reviews= reviewRepository.findAllByRestaurant(restaurant);
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

    List<Review> reviews= reviewRepository.findAllByRestaurant(restaurant);
    Double sum = 0.0;
    for(int i = 0; i < reviews.size()-1; i++){
      sum += reviews.get(i).getRating();
    }
    restaurant.setRating((double)sum/count);

    restaurantRepository.save(restaurant);

    reviewRepository.delete(review);
    return review;
  }
}
