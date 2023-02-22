package LikeLion.TodaysLunch.controller;

import LikeLion.TodaysLunch.domain.Menu;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.service.MenuService;
import LikeLion.TodaysLunch.service.RestaurantService;
import java.io.IOException;
import java.util.List;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MenuController {
  private final MenuService menuService;
  private final RestaurantService restaurantService;

  @Autowired
  public MenuController(MenuService menuService, RestaurantService restaurantService) {
    this.menuService = menuService;
    this.restaurantService = restaurantService;
  }

  @GetMapping("/search/menu-name")
  public ResponseEntity<List<Menu>> searchMenuName(@RequestParam String keyword, Pageable pageable){
    List<Menu> menus = menuService.searchMenuName(keyword, pageable).getContent();
    return ResponseEntity.status(HttpStatus.OK).body(menus);
  }

  @GetMapping("/sale-menus")
  public ResponseEntity<List<Menu>> saleMenuList(Pageable pageable){
    List<Menu> menus = menuService.saleMenuList(pageable).getContent();
    return ResponseEntity.status(HttpStatus.OK).body(menus);
  }

  @GetMapping("restaurants/{restaurantId}/menus")
  public ResponseEntity<List<Menu>> menuList(@PathVariable Long restaurantId) {
    Restaurant restaurant = restaurantService.restaurantDetail(restaurantId);
    List<Menu> menus = menuService.findMenuByRestaurant(restaurant);
    return ResponseEntity.status(HttpStatus.OK).body(menus);
  }

  @PostMapping("restaurants/{restaurantId}/menus")
  public ResponseEntity<Menu> createMenu(@RequestParam(required = false) MultipartFile menuImage,
      @RequestParam String name, @RequestParam Long price, @PathVariable Long restaurantId) throws IOException {
    Menu menu = menuService.createMenu(menuImage, name, price, restaurantId);
    return ResponseEntity.status(HttpStatus.OK).body(menu);
  }
}
