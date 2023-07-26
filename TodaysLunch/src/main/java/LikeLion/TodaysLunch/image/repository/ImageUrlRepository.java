package LikeLion.TodaysLunch.image.repository;

import LikeLion.TodaysLunch.image.domain.ImageUrl;
import LikeLion.TodaysLunch.menu.domain.Menu;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageUrlRepository extends JpaRepository<ImageUrl, Long> {
  List<ImageUrl> findAllByMenu(Menu menu);

}
