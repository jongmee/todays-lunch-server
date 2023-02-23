package LikeLion.TodaysLunch.controller;

import LikeLion.TodaysLunch.domain.Menu;
import LikeLion.TodaysLunch.service.MenuService;
import LikeLion.TodaysLunch.service.SaleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SaleController {
  private final SaleService saleService;
  @Autowired
  public SaleController(SaleService saleService) {
    this.saleService = saleService;
  }

  @GetMapping("/sale-menus")
  public ResponseEntity<List<Menu>> saleMenuList(Pageable pageable){
    List<Menu> menus = saleService.saleMenuList(pageable).getContent();
    return ResponseEntity.status(HttpStatus.OK).body(menus);
  }

}
