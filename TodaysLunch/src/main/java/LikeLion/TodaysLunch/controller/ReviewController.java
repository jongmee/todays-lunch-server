package LikeLion.TodaysLunch.controller;

import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.Review;
import LikeLion.TodaysLunch.dto.ReviewDto;
import LikeLion.TodaysLunch.service.ReviewService;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  public ResponseEntity<HashMap<String, Object>> allReviewList(@PathVariable Long restaurantId, Pageable pageable){
    Page<ReviewDto> reviews = reviewService.reviewsList(restaurantId, pageable);
    HashMap<String, Object> responseMap = new HashMap<>();
    responseMap.put("data", reviews.getContent());
    responseMap.put("totalPages", reviews.getTotalPages());
    return ResponseEntity.status(HttpStatus.OK).body(responseMap);
  }

  @PatchMapping("/restaurants/{restaurantId}/reviews/{reviewId}")
  public  ResponseEntity<Void> updateReview(@PathVariable Long reviewId, @PathVariable Long restaurantId,
      @RequestBody ReviewDto dto){
    Review updatedReview = reviewService.update(reviewId, restaurantId, dto);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping("/restaurants/{restaurantId}/reviews/{reviewId}")
  public ResponseEntity<Void> delete(@PathVariable Long reviewId, @PathVariable Long restaurantId){
    Review deletedReview = reviewService.delete(reviewId, restaurantId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping("/restaurants/{restaurantId}/reviews/{reviewId}/like")
  public ResponseEntity<Void> likeReview(
      @PathVariable Long restaurantId,
      @PathVariable Long reviewId, @AuthenticationPrincipal Member member){
    reviewService.addOrCancelLike(restaurantId, reviewId, member);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/reviews/{reviewId}/like")
  public  ResponseEntity<String>isAlreadyLike(
      @PathVariable Long reviewId, @AuthenticationPrincipal Member member){
    return ResponseEntity.status(HttpStatus.OK).body(reviewService.isAlreadyLike(member, reviewId));
  }

}
