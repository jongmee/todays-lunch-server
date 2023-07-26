package LikeLion.TodaysLunch.skeleton;

import LikeLion.TodaysLunch.restaurant.repository.DataJpaRestaurantRepository;
import LikeLion.TodaysLunch.menu.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestRestaurantEnviron {
  @Autowired
  private DataJpaRestaurantRepository restaurantRepository;
  @Autowired
  private MenuRepository menuRepository;
  public DataJpaRestaurantRepository restaurantRepository() { return restaurantRepository; }
  public MenuRepository menuRepository() { return menuRepository; }
}
