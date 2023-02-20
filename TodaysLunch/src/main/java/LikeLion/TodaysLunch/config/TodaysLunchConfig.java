package LikeLion.TodaysLunch.config;

import LikeLion.TodaysLunch.repository.*;
import LikeLion.TodaysLunch.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TodaysLunchConfig {
  private final DataJpaRestaurantRepository restaurantRepository;
  private final FoodCategoryRepository foodCategoryRepository;
  private final LocationCategoryRepository locationCategoryRepository;
  private final LocationTagRepository locationTagRepository;
  private final MenuRepository menuRepository;
  private final ReviewRepository reviewRepository;
  @Autowired
  public TodaysLunchConfig(DataJpaRestaurantRepository restaurantRepository,
      FoodCategoryRepository foodCategoryRepository,
      LocationCategoryRepository locationCategoryRepository,
      LocationTagRepository locationTagRepository,
      MenuRepository menuRepository,
      ReviewRepository reviewRepository)
  {
    this.restaurantRepository = restaurantRepository;
    this.foodCategoryRepository = foodCategoryRepository;
    this.locationCategoryRepository = locationCategoryRepository;
    this.locationTagRepository = locationTagRepository;
    this.menuRepository = menuRepository;
    this.reviewRepository = reviewRepository;
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
  public LocationCategoryService locationCategoryService(){
    return new LocationCategoryService(locationCategoryRepository);
  }

  @Bean
  public LocationTagService locationTagService(){
    return new LocationTagService(locationTagRepository);
  }
  @Bean
  public MenuService menuService(){
    return new MenuService(menuRepository);
  }

  @Bean
  public ReviewService reviewService(){
    return new ReviewService(reviewRepository, restaurantRepository);
  }
}
