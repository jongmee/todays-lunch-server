package LikeLion.TodaysLunch.menu.repository;

import LikeLion.TodaysLunch.menu.domain.Menu;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
  Page<Menu> findAllByRestaurant(Restaurant restaurant, Pageable pageable);
  List<Menu> findAllByRestaurant(Restaurant restaurant);
  Page<Menu> findByNameContaining(String keyword, Pageable pageable);
  Optional<Menu> findByName(String name);
  Page<Menu> findAllBySalePriceIsNotNull(Pageable pageable);
}
