package LikeLion.TodaysLunch.review.controller;

import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.review.dto.ReviewDto;
import LikeLion.TodaysLunch.review.service.ReviewService;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReviewController {
  static final String PAGE_VALUE = "0";
  static final String PAGE_SIZE = "100";
  static final String SORT = "createdDate";
  static final String ORDER = "descending";
  private final ReviewService reviewService;
  @Autowired
  public ReviewController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }
  @PostMapping("/restaurants/{restaurantId}/reviews")
  public ResponseEntity<Void> createReview(@RequestBody ReviewDto reviewDto,
      @PathVariable Long restaurantId, @AuthenticationPrincipal Member member){
    reviewService.create(restaurantId, reviewDto, member);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/restaurants/{restaurantId}/reviews")
  public ResponseEntity<HashMap<String, Object>> allReviewList(
      @PathVariable Long restaurantId,
      @RequestParam(defaultValue = PAGE_VALUE) int page,
      @RequestParam(defaultValue = PAGE_SIZE) int size,
      @RequestParam(defaultValue = SORT) String sort,
      @RequestParam(defaultValue = ORDER) String order,
      @AuthenticationPrincipal Member member){
    HashMap<String, Object> responseMap = reviewService.reviewsList(restaurantId, page, size, sort, order, member);
    return ResponseEntity.status(HttpStatus.OK).body(responseMap);
  }

  @PatchMapping("/restaurants/{restaurantId}/reviews/{reviewId}")
  public  ResponseEntity<Void> updateReview(@PathVariable Long reviewId, @PathVariable Long restaurantId,
      @RequestBody ReviewDto dto){
    reviewService.update(reviewId, restaurantId, dto);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping("/restaurants/{restaurantId}/reviews/{reviewId}")
  public ResponseEntity<Void> delete(@PathVariable Long reviewId, @PathVariable Long restaurantId){
    reviewService.delete(reviewId, restaurantId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping("/restaurants/{restaurantId}/reviews/{reviewId}/like")
  public ResponseEntity<Void> likeReview(
      @PathVariable Long restaurantId,
      @PathVariable Long reviewId, @AuthenticationPrincipal Member member){
    reviewService.addOrCancelLike(restaurantId, reviewId, member);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/myreviews")
  public ResponseEntity<HashMap<String, Object>> myReviewList(
      @RequestParam(value = "reviewer-id") Long reviewerID,
      @RequestParam(defaultValue = PAGE_VALUE) int page,
      @RequestParam(defaultValue = PAGE_SIZE) int size,
      @RequestParam(defaultValue = SORT) String sort,
      @RequestParam(defaultValue = ORDER) String order){
    HashMap<String, Object> responseMap = reviewService.myReviewList(reviewerID, page, size, sort, order);
    return ResponseEntity.status(HttpStatus.OK).body(responseMap);
  }

}