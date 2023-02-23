package LikeLion.TodaysLunch.repository;

import static org.junit.jupiter.api.Assertions.*;

import LikeLion.TodaysLunch.domain.Menu;
import LikeLion.TodaysLunch.domain.Sale;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
class MenuRepositoryTest {
  @Autowired
  private MenuRepository menuRepository;
  @Autowired
  private SaleRepository saleRepository;
//  @Test
//  void salemenu(){
//    Sale sale = new Sale();
//    LocalDate startDate = LocalDate.of(2023, 2, 1);
//    LocalDate endDate = LocalDate.of(2023, 2, 25);
//    LocalDate today = LocalDate.now();
//    sale.setStartDate(startDate);
//    sale.setEndDate(endDate);
//    saleRepository.save(sale);
//    Menu menu = new Menu();
//    menu.setName("menu name");
//    menu.setSale(sale);
//    menu.setPrice(2000L);
//    Menu menu2 = new Menu();
//    menu2.setName("menu name2");
//    menu2.setPrice(2000L);
//    menuRepository.save(menu);
//    menuRepository.save(menu2);
//    Pageable pageable = PageRequest.of(0, 5);
//    int re = (menuRepository.findBySaleIsNotNull(pageable).getContent()).size();
//    System.out.print(re);
//  }

}