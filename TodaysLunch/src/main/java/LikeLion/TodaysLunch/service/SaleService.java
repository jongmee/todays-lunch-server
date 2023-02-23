package LikeLion.TodaysLunch.service;

import LikeLion.TodaysLunch.domain.Menu;
import LikeLion.TodaysLunch.domain.Sale;
import LikeLion.TodaysLunch.dto.SaleDto;
import LikeLion.TodaysLunch.repository.MenuRepository;
import LikeLion.TodaysLunch.repository.SaleRepository;
import java.time.LocalDate;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Transactional
public class SaleService {
  private final MenuRepository menuRepository;
  private final SaleRepository saleRepository;
  @Autowired
  public SaleService(MenuRepository menuRepository, SaleRepository saleRepository) {
    this.menuRepository = menuRepository;
    this.saleRepository = saleRepository;
  }

  public Page<Menu> saleMenuList(Pageable pageable){
    LocalDate todayDate = LocalDate.now();
    List<Sale> sales = saleRepository.findByEndDateBefore(todayDate);
    for(int i = 0; i < sales.size(); i++){
      Sale sale = sales.get(i);
      Menu menu = menuRepository.findBySale(sale);
      System.out.print(menu.getSale().getEndDate());
      menu.setSale(null);
      menuRepository.save(menu);
      saleRepository.delete(sale);
    }
    return menuRepository.findBySaleIsNotNull(pageable);
  }

  public Sale create(SaleDto saleDto, Long menuId){
    Menu menu = menuRepository.findById(menuId).get();
    Sale sale = saleDto.toEntity();
    menu.setSale(sale);
    Sale saved = saleRepository.save(sale);
    menuRepository.save(menu);
    return saved;
  }

}
