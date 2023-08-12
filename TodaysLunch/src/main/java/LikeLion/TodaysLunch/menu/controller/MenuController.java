package LikeLion.TodaysLunch.menu.controller;

import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.menu.domain.Menu;
import LikeLion.TodaysLunch.menu.dto.MenuDto;
import LikeLion.TodaysLunch.menu.dto.MenuImageDto;
import LikeLion.TodaysLunch.menu.service.MenuService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MenuController {

  private final MenuService menuService;

  @Autowired
  public MenuController(MenuService menuService) {
    this.menuService = menuService;
  }

  @GetMapping("/search/menu-name")
  public ResponseEntity<List<Menu>> searchMenuName(
      Pageable pageable,
      @RequestParam String keyword){
    return ResponseEntity.status(HttpStatus.OK).body(menuService.searchMenuName(keyword, pageable).getContent());
  }

  @GetMapping("/restaurants/{restaurantId}/menus")
  public ResponseEntity<HashMap<String, Object>> menuList(@PathVariable Long restaurantId, Pageable pageable) {
    Page<MenuDto> menus = menuService.findMenuByRestaurant(restaurantId, pageable);
    HashMap<String, Object> responseMap = new HashMap<>();
    responseMap.put("data", menus.getContent());
    responseMap.put("totalPages", menus.getTotalPages());
    return ResponseEntity.status(HttpStatus.OK).body(responseMap);
  }

  @PostMapping("/restaurants/{restaurantId}/menus")
  public ResponseEntity<Void> createMenu(
      @Valid @RequestBody MenuDto menuDto,
      @PathVariable Long restaurantId,
      @AuthenticationPrincipal Member member){
    menuService.create(menuDto, restaurantId, member);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PatchMapping("/restaurants/{restaurantId}/menus/{menuId}")
  public ResponseEntity<Void> updateMenu(
      @Valid @RequestBody MenuDto menuDto,
      @PathVariable Long restaurantId,
      @PathVariable Long menuId,
      @AuthenticationPrincipal Member member) {
    menuService.update(menuDto, restaurantId, menuId, member);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping("/restaurants/{restaurantId}/menus/{menuId}")
  public ResponseEntity<Void> deleteMenu(
      @PathVariable Long restaurantId,
      @PathVariable Long menuId){
    menuService.delete(restaurantId, menuId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping("/menus/{menuId}/images")
  public ResponseEntity<Void> createMenuImage(
      @RequestParam MultipartFile menuImage,
      @PathVariable Long menuId,
      @AuthenticationPrincipal Member member) throws IOException {
    menuService.createImage(menuImage, menuId, member);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/menus/{menuId}/images")
  public ResponseEntity<HashMap<String, Object>> menuImageList(
      @PathVariable Long menuId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "8") int size){
    return ResponseEntity.status(HttpStatus.OK).body(menuService.menuImageList(menuId, page, size));
  }

  @DeleteMapping("/menus/{menuId}/images/{imageId}")
  public ResponseEntity<Void> deleteMenuImage(
      @PathVariable Long menuId,
      @PathVariable Long imageId){
    menuService.deleteImage(menuId, imageId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/menus/sale")
  public ResponseEntity<HashMap<String, Object>> saleMenuList(Pageable pageable){
    return ResponseEntity.status(HttpStatus.OK).body(menuService.saleMenuList(pageable));
  }
}
