package LikeLion.TodaysLunch.review.repository;

import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import LikeLion.TodaysLunch.review.domain.Review;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  Page<Review> findAllByRestaurant(Restaurant restaurant, Pageable pageable);
  Page<Review> findAllByMember(Member member, Pageable pageable);
  List<Review> findAllByMember(Member member);
}
