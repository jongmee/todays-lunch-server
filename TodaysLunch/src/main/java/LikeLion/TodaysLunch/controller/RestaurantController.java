package LikeLion.TodaysLunch.controller;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.LocationCategory;
import LikeLion.TodaysLunch.domain.LocationTag;
import LikeLion.TodaysLunch.domain.Menu;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.service.FoodCategoryService;
import LikeLion.TodaysLunch.service.LocationCategoryService;
import LikeLion.TodaysLunch.service.LocationTagService;
import LikeLion.TodaysLunch.service.MenuService;
import LikeLion.TodaysLunch.service.RestaurantService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
  private final RestaurantService restaurantService ;
  private final FoodCategoryService foodCategoryService;
  private final LocationCategoryService locationCategoryService;
  private final LocationTagService locationTagService;
  private final MenuService menuService;
  @Autowired
  public RestaurantController(RestaurantService restaurantService,
      FoodCategoryService foodCategoryService,
      LocationCategoryService locationCategoryService,
      LocationTagService locationTagService,
      MenuService menuService){
    this.restaurantService = restaurantService;
    this.foodCategoryService = foodCategoryService;
    this.locationCategoryService = locationCategoryService;
    this.locationTagService = locationTagService;
    this.menuService = menuService;
  }

  /**
   * paging parameter 예시
   * http://localhost:8080/restaurant?page=1&size=5
   */
  @GetMapping("")
  public List<Restaurant> allRestaurantList(Pageable pageable){
    return restaurantService.restaurantList(pageable).getContent();
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

  @GetMapping("/food-category")
  public List<Restaurant> filterByFoodCategory(@RequestParam String categoryName, Pageable pageable){
    FoodCategory foodCategory = foodCategoryService.findFoodCategoryByName(categoryName);
    return restaurantService.filterByFoodCategory(foodCategory, pageable).getContent();
  }

  @GetMapping("/location-category")
  public List<Restaurant> filterByLocationCategory(@RequestParam String categoryName, Pageable pageable){
    LocationCategory locationCategory = locationCategoryService.findLocationCategoryByName(categoryName);
    return restaurantService.filterByLocationCategory(locationCategory, pageable).getContent();
  }

  @GetMapping("/location-tag")
  public List<Restaurant> filterByLocationTag(@RequestParam String tagName, Pageable pageable){
    LocationTag locationTag = locationTagService.findLocationTagByName(tagName);
    return restaurantService.filterByLocationTag(locationTag, pageable).getContent();
  }

  @GetMapping("/locationtag-foodcategory")
  public List<Restaurant> filterByLocationTagAndFoodCategory(@RequestParam String tagName, String categoryName, Pageable pageable){
    LocationTag locationTag = locationTagService.findLocationTagByName(tagName);
    FoodCategory foodCategory = foodCategoryService.findFoodCategoryByName(categoryName);
    return restaurantService.filterByLocationTagAndFoodCategory(locationTag, foodCategory, pageable).getContent();
  }

}
