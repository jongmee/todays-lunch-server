package LikeLion.TodaysLunch.controller;

import LikeLion.TodaysLunch.domain.Menu;
import LikeLion.TodaysLunch.service.MenuService;
import java.util.List;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menus")
public class MenuController {
  private final MenuService menuService;

  @Autowired
  public MenuController(MenuService menuService) {
    this.menuService = menuService;
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
}
