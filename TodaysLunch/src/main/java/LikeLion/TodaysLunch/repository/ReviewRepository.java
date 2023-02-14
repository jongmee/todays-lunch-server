package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.domain.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  List<Review> findAllByRestaurant(Restaurant restaurant);
}
