package LikeLion.TodaysLunch.controller;

import LikeLion.TodaysLunch.domain.Menu;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.service.MenuService;
import LikeLion.TodaysLunch.service.RestaurantService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
  private final String defaultPageValue = "0";
  private final String defaultPageSize = "100";
  private final String defaultSortCriteria = "rating";
  private final String defaultSortOrder = "descending";

  private final RestaurantService restaurantService ;
  private final MenuService menuService;
  @Autowired
  public RestaurantController(RestaurantService restaurantService,
      MenuService menuService){
    this.restaurantService = restaurantService;
    this.menuService = menuService;
  }

  /**
   * paging parameter 예시
   * http://localhost:8080/restaurant?page=1&size=5
   */
  @GetMapping("")
  public ResponseEntity<List<Restaurant>> allRestaurantList(@RequestParam(defaultValue = defaultPageValue) int page,
      @RequestParam(defaultValue = defaultPageSize) int size,
      @RequestParam(defaultValue = defaultSortCriteria) String sort,
      @RequestParam(defaultValue = defaultSortOrder) String order) {
    List<Restaurant> restaurants = restaurantService.restaurantList(page, size, sort, order).getContent();
    return ResponseEntity.status(HttpStatus.OK).body(restaurants);
  }

  @GetMapping("/{restaurantId}")
  public ResponseEntity<Restaurant> detail(@PathVariable Long restaurantId){
    Restaurant restaurant = restaurantService.restaurantDetail(restaurantId);
    return ResponseEntity.status(HttpStatus.OK).body(restaurant);
  }

  @GetMapping("/{restaurantId}/menus")
  public ResponseEntity<List<Menu>> menuList(@PathVariable Long restaurantId){
    Restaurant restaurant = restaurantService.restaurantDetail(restaurantId);
    List<Menu> menus = menuService.findMenuByRestaurant(restaurant);
    return ResponseEntity.status(HttpStatus.OK).body(menus);
  }

  @GetMapping("/food-category")
  public ResponseEntity<List<Restaurant>> filterByFoodCategory(@RequestParam String categoryName,
      @RequestParam(defaultValue = defaultPageValue) int page,
      @RequestParam(defaultValue = defaultPageSize) int size,
      @RequestParam(defaultValue = defaultSortCriteria) String sort,
      @RequestParam(defaultValue = defaultSortOrder) String order){
    List<Restaurant> restaurants = restaurantService.filterByFoodCategory(categoryName, page, size, sort, order).getContent();
    return ResponseEntity.status(HttpStatus.OK).body(restaurants);
  }

  @GetMapping("/location-category")
  public ResponseEntity<List<Restaurant>> filterByLocationCategory(@RequestParam String categoryName,
      @RequestParam(defaultValue = defaultPageValue) int page,
      @RequestParam(defaultValue = defaultPageSize) int size,
      @RequestParam(defaultValue = defaultSortCriteria) String sort,
      @RequestParam(defaultValue = defaultSortOrder) String order){
    List<Restaurant> restaurants = restaurantService.filterByLocationCategory(categoryName, page, size, sort, order).getContent();
    return ResponseEntity.status(HttpStatus.OK).body(restaurants);
  }

  @GetMapping("/location-tag")
  public ResponseEntity<List<Restaurant>> filterByLocationTag(@RequestParam String tagName,
      @RequestParam(defaultValue = defaultPageValue) int page,
      @RequestParam(defaultValue = defaultPageSize) int size,
      @RequestParam(defaultValue = defaultSortCriteria) String sort,
      @RequestParam(defaultValue = defaultSortOrder) String order){
    List<Restaurant> restaurants = restaurantService.filterByLocationTag(tagName, page, size, sort, order).getContent();
    return ResponseEntity.status(HttpStatus.OK).body(restaurants);
  }

  @GetMapping("/locationtag-foodcategory")
  public ResponseEntity<List<Restaurant>> filterByLocationTagAndFoodCategory(@RequestParam String tagName, String categoryName,
      @RequestParam(defaultValue = defaultPageValue) int page,
      @RequestParam(defaultValue = defaultPageSize) int size,
      @RequestParam(defaultValue = defaultSortCriteria) String sort,
      @RequestParam(defaultValue = defaultSortOrder) String order){
    List<Restaurant> restaurants = restaurantService.filterByLocationTagAndFoodCategory(tagName, categoryName, page, size, sort, order).getContent();
    return ResponseEntity.status(HttpStatus.OK).body(restaurants);
  }

  @GetMapping("/search/restaurant-name")
  public ResponseEntity<List<Restaurant>> searchRestaurantName(@RequestParam String keyword, Pageable pageable){
    List<Restaurant> restaurants= restaurantService.searchRestaurantName(keyword, pageable).getContent();
    return ResponseEntity.status(HttpStatus.OK).body(restaurants);
  }

}
