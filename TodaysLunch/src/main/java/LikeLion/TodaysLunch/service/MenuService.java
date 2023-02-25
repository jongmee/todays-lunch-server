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
  private final ImageUrlRepository imageUrlRepository;

  private final DataJpaRestaurantRepository restaurantRepository;
  @Autowired
  public MenuService(MenuRepository menuRepository,
      ImageUrlRepository imageUrlRepository,
      DataJpaRestaurantRepository restaurantRepository) {
    this.menuRepository = menuRepository;
    this.imageUrlRepository = imageUrlRepository;
    this.restaurantRepository = restaurantRepository;
  }

  public Page<Menu> findMenuByRestaurant(Restaurant restaurant, Pageable pageable){
    return menuRepository.findAllByRestaurant(restaurant, pageable);
  }

  public Page<Menu> searchMenuName(String keyword, Pageable pageable){
    return menuRepository.findByNameContaining(keyword, pageable);
  }


  public Menu create(MultipartFile menuImage, String name, Long price, Long restaurantId) throws IOException {
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new IllegalArgumentException("메뉴 생성 실패! 메뉴를 생성하기 위한 대상 맛집이 없습니다."));
    // 최저 메뉴 가격 설정
    Long originalLowestPrice = restaurant.getLowestPrice();
    if (originalLowestPrice == null || originalLowestPrice > price){
      restaurant.setLowestPrice(price);
      restaurantRepository.save(restaurant);
    }
    Menu menu = new Menu();
    menu.setName(name);
    menu.setPrice(price);
    menu.setRestaurant(restaurant);
    if(menuImage != null && !menuImage.isEmpty()){
      // s3에 이미지 저장
      String savedUrl = s3UploadService.upload(menuImage, "menu");
      // image url을 db에 저장
      ImageUrl imageUrl = new ImageUrl();
      String originalName = menuImage.getOriginalFilename();

      imageUrl.setOriginalName(originalName);
      imageUrl.setImageUrl(savedUrl);

      Menu savedMenu = menuRepository.save(menu);
      imageUrl.setMenu(savedMenu);

      imageUrlRepository.save(imageUrl);
    }
    return menuRepository.save(menu);
  }

  public Menu update(String name, Long price, Long restaurantId, Long menuId){
    Menu menu = menuRepository.findById(menuId).get();
//    if(menuImage != null && !menuImage.isEmpty()){
//      // 기존 image를 s3에서 삭제
//      ImageUrl deleteImage = new ImageUrl();
//      if(menu.getImageUrl() != null) {
//        deleteImage = menu.getImageUrl();
//        s3UploadService.delete(deleteImage.getImageUrl()); // (전체 url string을 넘김)
//      }
//
//      // s3에 update된 image 저장
//      String savedUrl = s3UploadService.upload(menuImage, "menu");
//
//      // update된 image url을 db에 저장하고 menu에 등록
//      ImageUrl imageUrl = new ImageUrl();
//      String originalName = menuImage.getOriginalFilename();
//      imageUrl.setOriginalName(originalName);
//      imageUrl.setImageUrl(savedUrl);
//      imageUrlRepository.save(imageUrl);
//      menu.setImageUrl(imageUrl);
//      // 기존 image url을 db에서 삭제
//      if(deleteImage.getImageUrl() != null) {
//        imageUrlRepository.delete(deleteImage);
//      }
//    }
    if(name != null) menu.setName(name);
    if(price != null) menu.setPrice(price);

    return menuRepository.save(menu);
  }

  public Menu delete(Long restaurantId, Long menuId){
    Menu menu = menuRepository.findById(menuId).get();

    // 메뉴의 image가 있다면 s3와 db에서 삭제
    List<ImageUrl> imageUrls = imageUrlRepository.findAllByMenu(menu);
    for(int i = 0 ; i < imageUrls.size() ; i++){
      ImageUrl deleteImage = imageUrls.get(i);
      s3UploadService.delete(deleteImage.getImageUrl());
      imageUrlRepository.delete(deleteImage);
    }

    menuRepository.delete(menu);
    return menu;
  }
}
