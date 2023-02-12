package LikeLion.TodaysLunch;

import LikeLion.TodaysLunch.repository.FoodCategoryRepository;
import LikeLion.TodaysLunch.repository.LocationTagRepository;
import LikeLion.TodaysLunch.repository.MenuRepository;
import LikeLion.TodaysLunch.repository.RestaurantRepository;
import LikeLion.TodaysLunch.service.FoodCategoryService;
import LikeLion.TodaysLunch.service.LocationTagService;
import LikeLion.TodaysLunch.service.MenuService;
import LikeLion.TodaysLunch.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TodaysLunchConfig {
  private final RestaurantRepository restaurantRepository;
  private final FoodCategoryRepository foodCategoryRepository;
  private final LocationTagRepository locationTagRepository;
  private final MenuRepository menuRepository;
  @Autowired
  public TodaysLunchConfig(RestaurantRepository restaurantRepository, FoodCategoryRepository foodCategoryRepository, LocationTagRepository locationTagRepository, MenuRepository menuRepository){
    this.restaurantRepository = restaurantRepository;
    this.foodCategoryRepository = foodCategoryRepository;
    this.locationTagRepository = locationTagRepository;
    this.menuRepository = menuRepository;
  }
  @Bean
  public RestaurantService restaurantService(){
    return new RestaurantService(restaurantRepository);
  }
  @Bean
  public FoodCategoryService foodCategoryService(){
    return new FoodCategoryService(foodCategoryRepository);
  }

  @Bean
  public LocationTagService locationTagService(){
    return new LocationTagService(locationTagRepository);
  }
  @Bean
  public MenuService menuService(){
    return new MenuService(menuRepository);
  }
}
