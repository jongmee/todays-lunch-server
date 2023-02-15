package LikeLion.TodaysLunch.controller;

import LikeLion.TodaysLunch.domain.Review;
import LikeLion.TodaysLunch.dto.ReviewDto;
import LikeLion.TodaysLunch.service.ReviewService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReviewController {
  private final ReviewService reviewService;
  @Autowired
  public ReviewController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }
  @PostMapping("/restaurants/{restaurantId}/reviews")
  public ResponseEntity<Review> createReview(@RequestBody ReviewDto reviewDto, @PathVariable Long restaurantId){
    Review review = reviewService.create(restaurantId, reviewDto);
    return ResponseEntity.status(HttpStatus.OK).body(review);
  }

  @GetMapping("/restaurants/{restaurantId}/reviews")
  public ResponseEntity<List<Review>> allReviewList(@PathVariable Long restaurantId, Pageable pageable){
    List<Review> reviews = reviewService.reviewsList(restaurantId, pageable).getContent();
    return ResponseEntity.status(HttpStatus.OK).body(reviews);
  }

  @PatchMapping("/restaurants/{restaurantId}/reviews/{reviewId}")
  public  ResponseEntity<Review> updateReview(@PathVariable Long reviewId, @PathVariable Long restaurantId,
      @RequestBody ReviewDto dto){
    Review updatedReview = reviewService.update(reviewId, restaurantId, dto);
    return ResponseEntity.status(HttpStatus.OK).body(updatedReview);
  }

  @DeleteMapping("/restaurants/{restaurantId}/reviews/{reviewId}")
  public ResponseEntity<Review> delete(@PathVariable Long reviewId, @PathVariable Long restaurantId){
    Review deletedReview = reviewService.delete(reviewId, restaurantId);
    return ResponseEntity.status(HttpStatus.OK).body(deletedReview);
  }


}
