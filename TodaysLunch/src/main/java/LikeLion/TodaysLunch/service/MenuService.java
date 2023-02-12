package LikeLion.TodaysLunch.service;

import LikeLion.TodaysLunch.domain.Menu;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.repository.MenuRepository;
import java.util.List;
import javax.transaction.Transactional;

@Transactional
public class MenuService {
  private final MenuRepository menuRepository;
  public MenuService(MenuRepository menuRepository) {
    this.menuRepository = menuRepository;
  }

  public List<Menu> findMenuByRestaurant(Restaurant restaurant){
    return menuRepository.findAllByRestaurant(restaurant);
  }
}
