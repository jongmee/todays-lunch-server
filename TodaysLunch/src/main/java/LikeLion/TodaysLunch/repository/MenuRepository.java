package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.Menu;
import LikeLion.TodaysLunch.domain.Restaurant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
  List<Menu> findAllByRestaurant(Restaurant restaurant);

}
