package LikeLion.TodaysLunch.environment.service;

import LikeLion.TodaysLunch.restaurant.repository.DataJpaRestaurantRepository;
import LikeLion.TodaysLunch.menu.repository.MenuRepository;
import LikeLion.TodaysLunch.restaurant.repository.RestaurantContributorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestRestaurantEnviron {

  @Autowired
  private DataJpaRestaurantRepository restaurantRepository;
  @Autowired
  private MenuRepository menuRepository;
  @Autowired
  private RestaurantContributorRepository restaurantContributorRepository;

  public DataJpaRestaurantRepository restaurantRepository() {
    return restaurantRepository;
  }

  public MenuRepository menuRepository() {
    return menuRepository;
  }

  public RestaurantContributorRepository restaurantContributorRepository() {
    return restaurantContributorRepository;
  }
}
