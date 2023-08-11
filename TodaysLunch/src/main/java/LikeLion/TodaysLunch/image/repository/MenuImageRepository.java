package LikeLion.TodaysLunch.image.repository;

import LikeLion.TodaysLunch.image.domain.MenuImage;
import LikeLion.TodaysLunch.menu.domain.Menu;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuImageRepository extends JpaRepository<MenuImage, Long> {
  List<MenuImage> findAllByMenu(Menu menu);
  Page<MenuImage> findAllByMenu(Menu menu, Pageable pageable);
}
