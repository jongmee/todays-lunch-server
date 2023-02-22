package LikeLion.TodaysLunch.service;

import LikeLion.TodaysLunch.domain.ImageUrl;
import LikeLion.TodaysLunch.domain.Menu;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.domain.Sale;
import LikeLion.TodaysLunch.repository.DataJpaRestaurantRepository;
import LikeLion.TodaysLunch.repository.ImageUrlRepository;
import LikeLion.TodaysLunch.repository.MenuRepository;
import LikeLion.TodaysLunch.repository.SaleRepository;
import LikeLion.TodaysLunch.s3.S3UploadService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

@Transactional
public class MenuService {
  @Autowired
  private S3UploadService s3UploadService;
  private final MenuRepository menuRepository;
  private final SaleRepository saleRepository;
  private final ImageUrlRepository imageUrlRepository;

  private final DataJpaRestaurantRepository restaurantRepository;
  @Autowired
  public MenuService(MenuRepository menuRepository,
      SaleRepository saleRepository,
      ImageUrlRepository imageUrlRepository,
      DataJpaRestaurantRepository restaurantRepository) {
    this.menuRepository = menuRepository;
    this.saleRepository = saleRepository;
    this.imageUrlRepository = imageUrlRepository;
    this.restaurantRepository = restaurantRepository;
  }

  public List<Menu> findMenuByRestaurant(Restaurant restaurant){
    return menuRepository.findAllByRestaurant(restaurant);
  }

  public Page<Menu> searchMenuName(String keyword, Pageable pageable){
    return menuRepository.findByNameContaining(keyword, pageable);
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

  public Menu createMenu(MultipartFile menuImage, String name, Long price, Long restaurantId) throws IOException {
    Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
    Menu menu = new Menu();
    menu.setName(name);
    menu.setPrice(price);
    menu.setRestaurant(restaurant);
    if(!menuImage.isEmpty()){
      ImageUrl imageUrl = new ImageUrl();
      String originalName = menuImage.getOriginalFilename();
      String savedUrl = s3UploadService.upload(menuImage, "menu");
      imageUrl.setImageUrl(savedUrl);
      imageUrl.setOriginalName(originalName);
      imageUrlRepository.save(imageUrl);
      menu.setImageUrl(imageUrl);
    }
    return menuRepository.save(menu);
  }
}
