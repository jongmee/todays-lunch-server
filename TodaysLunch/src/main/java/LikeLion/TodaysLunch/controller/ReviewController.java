package LikeLion.TodaysLunch.controller;

import LikeLion.TodaysLunch.domain.Review;
import LikeLion.TodaysLunch.dto.ReviewDto;
import LikeLion.TodaysLunch.service.ReviewService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
  public ResponseEntity<List<Review>> allReviewList(@PathVariable Long restaurantId){
    List<Review> reviews = reviewService.reviewsList(restaurantId);
    return ResponseEntity.status(HttpStatus.OK).body(reviews);
  }

  @PatchMapping("/reviews/{id}")
  public  ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody ReviewDto dto){
    Review updatedReview = reviewService.update(id, dto);
    return ResponseEntity.status(HttpStatus.OK).body(updatedReview);
  }

  @DeleteMapping("/reviews/{id}")
  public ResponseEntity<Review> delete(@PathVariable Long id){
    Review deletedReview = reviewService.delete(id);
    return ResponseEntity.status(HttpStatus.OK).body(deletedReview);
  }


}
