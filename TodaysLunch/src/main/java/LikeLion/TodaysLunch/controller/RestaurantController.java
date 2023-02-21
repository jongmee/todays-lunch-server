package LikeLion.TodaysLunch.controller;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.Menu;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.repository.RestaurantSpecification;
import LikeLion.TodaysLunch.service.MenuService;
import LikeLion.TodaysLunch.service.RestaurantService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

  static final String PAGE_VALUE = "0";
  static final String PAGE_SIZE = "100";
  static final String SORT = "rating";
  static final String ORDER = "descending";

  private final RestaurantService restaurantService;
  private final MenuService menuService;

  @Autowired
  public RestaurantController(RestaurantService restaurantService,
      MenuService menuService) {
    this.restaurantService = restaurantService;
    this.menuService = menuService;
  }

  /**
   * paging parameter 예시
   * http://localhost:8080/restaurant?page=1&size=5
   */
  /**
   * 설계
   * restaurant?food-category=korean&location-category=sogang&location-tag&keyword=검색어&page=1&size=5&sorting=rating&order=ascending
   */
  @GetMapping("")
  public ResponseEntity<List<Restaurant>> allRestaurantList(
      @RequestParam(value = "food-category", required = false) String foodCategory,
      @RequestParam(value = "location-category", required = false) String locationCategory,
      @RequestParam(value = "location-tag", required = false) String locationTag,
      @RequestParam(value = "keyword", required = false) String keyword,
      @RequestParam(defaultValue = PAGE_VALUE) int page,
      @RequestParam(defaultValue = PAGE_SIZE) int size,
      @RequestParam(defaultValue = SORT) String sort,
      @RequestParam(defaultValue = ORDER) String order) {
    List<Restaurant> restaurants = restaurantService.restaurantList(foodCategory, locationCategory,
        locationTag, keyword, page, size, sort, order).getContent();
    return ResponseEntity.status(HttpStatus.OK).body(restaurants);
  }

  @GetMapping("/{restaurantId}")
  public ResponseEntity<Restaurant> detail(@PathVariable Long restaurantId) {
    Restaurant restaurant = restaurantService.restaurantDetail(restaurantId);
    return ResponseEntity.status(HttpStatus.OK).body(restaurant);
  }

  @GetMapping("/{restaurantId}/menus")
  public ResponseEntity<List<Menu>> menuList(@PathVariable Long restaurantId) {
    Restaurant restaurant = restaurantService.restaurantDetail(restaurantId);
    List<Menu> menus = menuService.findMenuByRestaurant(restaurant);
    return ResponseEntity.status(HttpStatus.OK).body(menus);
  }
}
