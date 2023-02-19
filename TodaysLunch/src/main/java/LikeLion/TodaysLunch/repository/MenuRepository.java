package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.Menu;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.domain.Sale;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
  List<Menu> findAllByRestaurant(Restaurant restaurant);
  Page<Menu> findByNameContaining(String keyword, Pageable pageable);
  Page<Menu> findBySaleIsNotNull(Pageable pageable);
  Menu findBySale(Sale sale);
}
