package LikeLion.TodaysLunch.service;

import LikeLion.TodaysLunch.domain.ImageUrl;
import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.Menu;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.domain.Sale;
import LikeLion.TodaysLunch.dto.MenuDto;
import LikeLion.TodaysLunch.dto.MenuImageDto;
import LikeLion.TodaysLunch.exception.NotFoundException;
import LikeLion.TodaysLunch.repository.DataJpaRestaurantRepository;
import LikeLion.TodaysLunch.repository.ImageUrlRepository;
import LikeLion.TodaysLunch.repository.MenuRepository;
import LikeLion.TodaysLunch.repository.SaleRepository;
import LikeLion.TodaysLunch.s3.S3UploadService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
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

  public Page<MenuDto> findMenuByRestaurant(Long restaurantId, Pageable pageable){
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new NotFoundException("맛집"));
    return menuRepository.findAllByRestaurant(restaurant, pageable).map(MenuDto::fromEntity);
  }

  public Page<Menu> searchMenuName(String keyword, Pageable pageable){
    return menuRepository.findByNameContaining(keyword, pageable);
  }


  public void create(MenuDto menuDto, Long restaurantId){
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new NotFoundException("맛집"));
    Long price = menuDto.getPrice();
    // 최저 메뉴 가격 설정
    Long originalLowestPrice = restaurant.getLowestPrice();
    if (originalLowestPrice == null || originalLowestPrice > price){
      restaurant.setLowestPrice(price);
      restaurantRepository.save(restaurant);
    }

    Menu menu = menuDto.toEntity();
    menu.setRestaurant(restaurant);
    menuRepository.save(menu);
  }

  public Menu update(String name, Long price, Long restaurantId, Long menuId){
    Menu menu = menuRepository.findById(menuId)
        .orElseThrow(() -> new NotFoundException("메뉴"));
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new NotFoundException("맛집"));

    // 최저 메뉴 가격 설정
    Long originalLowestPrice = restaurant.getLowestPrice();
    if (originalLowestPrice == null || originalLowestPrice > price){
      restaurant.setLowestPrice(price);
      restaurantRepository.save(restaurant);
    }

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

  public void createImage(MultipartFile image, Long menuId, Member member) throws IOException {
    Menu menu = menuRepository.findById(menuId)
        .orElseThrow(() -> new NotFoundException("메뉴"));

    if(image != null && !image.isEmpty()){
      Long count = menu.getImageCount();
      menu.setImageCount(++count);
      Menu savedMenu = menuRepository.save(menu);

      // s3에 이미지 저장
      String savedUrl = s3UploadService.upload(image, "menu");
      // image url을 db에 저장
      String originalName = image.getOriginalFilename();
      ImageUrl imageUrl = ImageUrl.builder()
              .originalName(originalName)
                  .imageUrl(savedUrl)
                      .member(member)
                        .menu(savedMenu)
                          .build();

      imageUrlRepository.save(imageUrl);
    }
  }

  public List<MenuImageDto> menuImageList(Long menuId){
    Menu menu = menuRepository.findById(menuId)
        .orElseThrow(() -> new NotFoundException("메뉴"));
    return imageUrlRepository.findAllByMenu(menu).stream()
        .map(MenuImageDto::fromEntity).collect(Collectors.toList());
  }

  public void deleteImage(Long menuId, Long imageId){
    Menu menu = menuRepository.findById(menuId)
        .orElseThrow(() -> new NotFoundException("메뉴"));
    ImageUrl imageUrl = imageUrlRepository.findById(imageId)
        .orElseThrow(() -> new NotFoundException("메뉴의 이미지"));

    Long count = menu.getImageCount();
    menu.setImageCount(--count);
    menuRepository.save(menu);

    s3UploadService.delete(imageUrl.getImageUrl());
    imageUrlRepository.delete(imageUrl);
  }
}
