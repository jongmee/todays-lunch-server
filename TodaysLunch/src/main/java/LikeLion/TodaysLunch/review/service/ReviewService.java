package LikeLion.TodaysLunch.review.service;

import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import LikeLion.TodaysLunch.review.domain.Review;
import LikeLion.TodaysLunch.review.domain.ReviewLike;
import LikeLion.TodaysLunch.review.dto.MyReviewDto;
import LikeLion.TodaysLunch.review.dto.ReviewDto;
import LikeLion.TodaysLunch.exception.NotFoundException;
import LikeLion.TodaysLunch.restaurant.repository.DataJpaRestaurantRepository;
import LikeLion.TodaysLunch.member.repository.MemberRepository;
import LikeLion.TodaysLunch.review.repository.ReviewLikeRepository;
import LikeLion.TodaysLunch.review.repository.ReviewRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Transactional
@RequiredArgsConstructor
public class ReviewService {

  private final MemberRepository memberRepository;
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
      for(Review r: reviews){
        sum += r.getRating();
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

  public HashMap<String, Object> reviewsList(Long restaurantId, int page, int size, String sort, String order, Member member){
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new NotFoundException("맛집"));

    Pageable pageable = determineSort(page, size, sort, order);
    Page<Review> reviews = reviewRepository.findAllByRestaurant(restaurant, pageable);

    List<ReviewDto> reviewDtos = new ArrayList<>((int)reviews.getTotalElements());
    Boolean liked;
    for(Review review: reviews){
      if(isNotAlreadyLike(member, review))
        liked = false;
      else
        liked = true;
      reviewDtos.add(ReviewDto.fromEntity(review, liked));
    }

    HashMap<String, Object> responseMap = new HashMap<>();
    responseMap.put("data", reviewDtos);
    responseMap.put("totalPages", reviews.getTotalPages());
    responseMap.put("totalReviewCount", totalReviewCount(restaurantId));

    return responseMap;
  }

  public HashMap<String, Object> myReviewList(Long reviewerId, int page, int size, String sort, String order){
    Member reviewer = memberRepository.findById(reviewerId)
        .orElseThrow(() -> new NotFoundException("유저"));

    Pageable pageable = determineSort(page, size, sort, order);
    Page<Review> reviews = reviewRepository.findAllByMember(reviewer, pageable);

    List<MyReviewDto> reviewDtos = new ArrayList<>((int)reviews.getTotalElements());
    Boolean liked;
    for(Review review: reviews){
      if(isNotAlreadyLike(reviewer, review))
        liked = false;
      else
        liked = true;
      reviewDtos.add(MyReviewDto.fromEntity(review, liked));
    }

    HashMap<String, Object> responseMap = new HashMap<>();
    responseMap.put("data", reviewDtos);
    responseMap.put("totalPages", reviews.getTotalPages());

    return responseMap;
  }

  public void update(Long reviewId, Long restaurantId, ReviewDto reviewDto){
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new NotFoundException("리뷰"));

    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new NotFoundException("맛집"));
    Long count = restaurant.getReviewCount();

    List<Review> reviews= reviewRepository.findAllByRestaurant(restaurant);
    Double sum = 0.0;
    for(Review r: reviews){
      if(!r.getId().equals(reviewId))
        sum += r.getRating();
    }
    sum += reviewDto.getRating();
    restaurant.setRating((double)sum/count);

    restaurantRepository.save(restaurant);
    review.update(reviewDto);

    reviewRepository.save(review);
  }

  public void delete(Long reviewId, Long restaurantId){
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new NotFoundException("리뷰"));

    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new NotFoundException("맛집"));

    Long count = restaurant.getReviewCount() - 1;
    restaurant.setReviewCount(count);

    List<Review> reviews= reviewRepository.findAllByRestaurant(restaurant);
    Double sum = 0.0;
    for(Review r: reviews){
      if(!r.getId().equals(reviewId))
        sum += r.getRating();
    }
    restaurant.setRating((double)sum/count);

    restaurantRepository.save(restaurant);

    reviewRepository.delete(review);
  }

  public void addOrCancelLike(Long restaurantId, Long reviewId, Member member){
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new NotFoundException("맛집"));
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new NotFoundException("리뷰"));

    if (isNotAlreadyLike(member, review)){
      reviewLikeRepository.save(new ReviewLike(member, review));

      Long likeCount = review.getLikeCount();
      likeCount += 1L;
      review.setLikeCount(likeCount);

      reviewRepository.save(review);
    } else {
      ReviewLike like = reviewLikeRepository.findByReviewAndMember(review, member).get();
      Long likeCount = review.getLikeCount();
      likeCount -= 1L;
      review.setLikeCount(likeCount);
      reviewRepository.save(review);
      reviewLikeRepository.delete(like);
    }

    Pageable pageable = PageRequest.of(0, (int)reviewRepository.count(), Sort.by("likeCount").descending());
    Review bestReview = reviewRepository.findAllByRestaurant(restaurant, pageable).getContent().get(0);
    if(bestReview.getLikeCount() <= review.getLikeCount()){
      restaurant.setBestReview(review);
      restaurantRepository.save(restaurant);
    }
  }

  // 유저가 이미 추천한 리뷰인지 체크
  private boolean isNotAlreadyLike(Member member, Review review){
    return reviewLikeRepository.findByReviewAndMember(review, member).isEmpty();
  }

  private Pageable determineSort(int page, int size, String sort, String order){
    Pageable pageable = PageRequest.of(page, size);
    if(order.equals("ascending")){
      pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
    } else if(order.equals("descending")){
      pageable = PageRequest.of(page, size, Sort.by(sort).descending());
    }
    return pageable;
  }

  private Long totalReviewCount(Long restaurantId){
    return restaurantRepository.findById(restaurantId).orElseThrow(() -> new NotFoundException("맛집")).getReviewCount();
  }
}
