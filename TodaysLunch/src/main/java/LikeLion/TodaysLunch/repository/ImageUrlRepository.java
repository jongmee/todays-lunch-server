package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.ImageUrl;
import LikeLion.TodaysLunch.domain.Menu;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageUrlRepository extends JpaRepository<ImageUrl, Long> {
  List<ImageUrl> findAllByMenu(Menu menu);

}
