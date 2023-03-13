package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.Review;
import LikeLion.TodaysLunch.domain.ReviewLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
  Optional<ReviewLike> findByReviewAndMember(Review review, Member member);
}
