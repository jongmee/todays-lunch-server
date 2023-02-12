package LikeLion.TodaysLunch.controller;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.LocationTag;
import LikeLion.TodaysLunch.domain.Menu;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.service.FoodCategoryService;
import LikeLion.TodaysLunch.service.LocationTagService;
import LikeLion.TodaysLunch.service.MenuService;
import LikeLion.TodaysLunch.service.RestaurantService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
  private final RestaurantService restaurantService ;
  private final FoodCategoryService foodCategoryService;
  private final LocationTagService locationTagService;
  private final MenuService menuService;
  @Autowired
  public RestaurantController(RestaurantService restaurantService, FoodCategoryService foodCategoryService, LocationTagService locationTagService, MenuService menuService){
    this.restaurantService = restaurantService;
    this.foodCategoryService = foodCategoryService;
    this.locationTagService = locationTagService;
    this.menuService = menuService;
  }

  @GetMapping("/{restaurantId}")
  public Restaurant detail(@PathVariable Long restaurantId){
    return restaurantService.restaurantDetail(restaurantId);
  }

  @GetMapping("/{restaurantId}/menus")
  public List<Menu> menuList(@PathVariable Long restaurantId){
    Restaurant restaurant = restaurantService.restaurantDetail(restaurantId);
    return menuService.findMenuByRestaurant(restaurant);
  }

  @GetMapping("/food-category/{foodCategoryName}")
  public List<Restaurant> filterByFoodCategory(@PathVariable String foodCategoryName){
    FoodCategory foodCategory = foodCategoryService.findFoodCategoryByName(foodCategoryName);
    return restaurantService.filterByFoodCategory(foodCategory);
  }

  @GetMapping("/location-tag/{locationTagName}")
  public List<Restaurant> filterByLocationTag(@PathVariable String locationTagName){
    LocationTag locationTag = locationTagService.findLocationTagByName(locationTagName);
    return restaurantService.filterByLocationTag(locationTag);
  }

}
