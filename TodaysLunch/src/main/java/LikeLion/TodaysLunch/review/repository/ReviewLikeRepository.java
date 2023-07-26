package LikeLion.TodaysLunch.review.repository;

import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.review.domain.Review;
import LikeLion.TodaysLunch.review.domain.ReviewLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
  Optional<ReviewLike> findByReviewAndMember(Review review, Member member);
}
