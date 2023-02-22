package LikeLion.TodaysLunch.service;

import static org.junit.jupiter.api.Assertions.*;

import LikeLion.TodaysLunch.domain.Menu;
import LikeLion.TodaysLunch.domain.Sale;
import LikeLion.TodaysLunch.repository.MenuRepository;
import LikeLion.TodaysLunch.repository.SaleRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

@SpringBootTest
class MenuServiceTest {
  @Autowired
  private MenuRepository menuRepository;
  @Autowired
  private SaleRepository saleRepository;
  @Autowired
  private MenuService menuService;

  @Test
  public void 세일_메뉴_불러오기() {
    //given
    Sale sale = new Sale();
    LocalDate startDate = LocalDate.of(2023, 2, 1);
    LocalDate endDate = LocalDate.of(2023, 2, 7);
    sale.setStartDate(startDate);
    sale.setEndDate(endDate);
    saleRepository.save(sale);
    Menu menu = new Menu();
    menu.setName("menu name");
    menu.setSale(sale);
    menu.setPrice(2000L);
    menuRepository.save(menu);
    Sale sale2 = new Sale();
    sale2.setStartDate(LocalDate.of(2023, 2, 1));
    sale2.setEndDate(LocalDate.of(2023, 2, 22));
    saleRepository.save(sale2);
    Menu menu2 = new Menu();
    menu2.setName("menu name");
    menu2.setSale(sale2);
    menu2.setPrice(2000L);
    menuRepository.save(menu2);
    //when
    Pageable pageable = PageRequest.of(0, 2);
    List<Menu> menus = menuService.saleMenuList(pageable).getContent();
    //then
    assertEquals(1, menus.size());
  }

}