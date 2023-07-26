package LikeLion.TodaysLunch.menu.controller;

import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.menu.domain.Menu;
import LikeLion.TodaysLunch.menu.dto.MenuDto;
import LikeLion.TodaysLunch.menu.dto.MenuImageDto;
import LikeLion.TodaysLunch.menu.service.MenuService;
import LikeLion.TodaysLunch.restaurant.service.RestaurantService;
import LikeLion.TodaysLunch.member.service.MemberService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
  private final RestaurantService restaurantService;
  private final MemberService memberService;

  @Autowired
  public MenuController(MenuService menuService, RestaurantService restaurantService, MemberService memberService) {
    this.menuService = menuService;
    this.restaurantService = restaurantService;
    this.memberService = memberService;
  }

  @GetMapping("/search/menu-name")
  public ResponseEntity<List<Menu>> searchMenuName(@RequestParam String keyword, Pageable pageable){
    List<Menu> menus = menuService.searchMenuName(keyword, pageable).getContent();
    return ResponseEntity.status(HttpStatus.OK).body(menus);
  }

  @GetMapping("restaurants/{restaurantId}/menus")
  public ResponseEntity<HashMap<String, Object>> menuList(@PathVariable Long restaurantId, Pageable pageable) {
    Page<MenuDto> menus = menuService.findMenuByRestaurant(restaurantId, pageable);
    HashMap<String, Object> responseMap = new HashMap<>();
    responseMap.put("data", menus.getContent());
    responseMap.put("totalPages", menus.getTotalPages());
    return ResponseEntity.status(HttpStatus.OK).body(responseMap);
  }

  @PostMapping("restaurants/{restaurantId}/menus")
  public ResponseEntity<Void> createMenu(
      @RequestBody MenuDto menuDto,
      @PathVariable Long restaurantId,
      @AuthenticationPrincipal Member member){
    menuService.create(menuDto, restaurantId, member);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PatchMapping("restaurants/{restaurantId}/menus/{menuId}")
  public ResponseEntity<Void> updateMenu(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) Long price,
      @PathVariable Long restaurantId,
      @PathVariable Long menuId,
      @AuthenticationPrincipal Member member) {
    menuService.update(name, price, restaurantId, menuId, member);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping("restaurants/{restaurantId}/menus/{menuId}")
  public ResponseEntity<Menu> deleteMenu(@PathVariable Long restaurantId, @PathVariable Long menuId){
    Menu menu = menuService.delete(restaurantId, menuId);
    return ResponseEntity.status(HttpStatus.OK).body(menu);
  }

  @PostMapping("menus/{menuId}/images")
  public ResponseEntity<Void> createMenuImage(
      @RequestParam MultipartFile menuImage,
      @PathVariable Long menuId,
      @AuthenticationPrincipal Member member) throws IOException {
    memberService.getAuthenticatedMember(member);
    menuService.createImage(menuImage, menuId, member);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("menus/{menuId}/images")
  public ResponseEntity<List<MenuImageDto>> menuImageList(@PathVariable Long menuId){
    List<MenuImageDto> menuImages = menuService.menuImageList(menuId);
    return ResponseEntity.status(HttpStatus.OK).body(menuImages);
  }

  @DeleteMapping("menus/{menuId}/images/{imageId}")
  public ResponseEntity<Void> deleteMenuImage(
      @PathVariable Long menuId,
      @PathVariable Long imageId
  ){
    menuService.deleteImage(menuId, imageId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
