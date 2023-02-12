package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
