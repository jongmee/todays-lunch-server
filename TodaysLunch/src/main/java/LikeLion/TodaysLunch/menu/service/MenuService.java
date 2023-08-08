package LikeLion.TodaysLunch.menu.service;

import LikeLion.TodaysLunch.image.domain.ImageUrl;
import LikeLion.TodaysLunch.image.domain.MenuImage;
import LikeLion.TodaysLunch.image.repository.MenuImageRepository;
import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.menu.domain.Menu;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import LikeLion.TodaysLunch.restaurant.domain.RestaurantContributor;
import LikeLion.TodaysLunch.menu.dto.MenuDto;
import LikeLion.TodaysLunch.menu.dto.MenuImageDto;
import LikeLion.TodaysLunch.exception.NotFoundException;
import LikeLion.TodaysLunch.restaurant.repository.DataJpaRestaurantRepository;
import LikeLion.TodaysLunch.image.repository.ImageUrlRepository;
import LikeLion.TodaysLunch.menu.repository.MenuRepository;
import LikeLion.TodaysLunch.restaurant.repository.RestaurantContributorRepository;
import LikeLion.TodaysLunch.external.S3UploadService;
import java.io.IOException;
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
  private final RestaurantContributorRepository restaurantContributorRepository;
  private final MenuImageRepository menuImageRepository;
  @Autowired
  public MenuService(MenuRepository menuRepository,
      ImageUrlRepository imageUrlRepository,
      DataJpaRestaurantRepository restaurantRepository,
      RestaurantContributorRepository restaurantContributorRepository,
      MenuImageRepository menuImageRepository) {
    this.menuRepository = menuRepository;
    this.imageUrlRepository = imageUrlRepository;
    this.restaurantRepository = restaurantRepository;
    this.restaurantContributorRepository = restaurantContributorRepository;
    this.menuImageRepository = menuImageRepository;
  }

  public Page<MenuDto> findMenuByRestaurant(Long restaurantId, Pageable pageable){
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new NotFoundException("맛집"));
    return menuRepository.findAllByRestaurant(restaurant, pageable).map(MenuDto::fromEntity);
  }

  public Page<Menu> searchMenuName(String keyword, Pageable pageable){
    return menuRepository.findByNameContaining(keyword, pageable);
  }


  public void create(MenuDto menuDto, Long restaurantId, Member member){
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new NotFoundException("맛집"));
    Long price = menuDto.getPrice();
    Long salePrice = menuDto.getSalePrice();
    if(salePrice != null && salePrice < price)
      price = salePrice;

    // 최저 메뉴 가격 설정
    Long originalLowestPrice = restaurant.getLowestPrice();
    if (originalLowestPrice == null || originalLowestPrice > price){
      restaurant.setLowestPrice(price);
      restaurantRepository.save(restaurant);
    }

    Menu menu = menuDto.toEntity();
    menu.setRestaurant(restaurant);
    menuRepository.save(menu);

    createRestaurantContributor(restaurant, member);
  }

  public void update(MenuDto menuDto, Long restaurantId, Long menuId, Member member){
    Menu menu = menuRepository.findById(menuId)
        .orElseThrow(() -> new NotFoundException("메뉴"));
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new NotFoundException("맛집"));

    Long price = menuDto.getPrice();
    Long salePrice = menuDto.getSalePrice();
    if(salePrice != null && salePrice < price)
      price = salePrice;

    // 최저 메뉴 가격 설정
    Long originalLowestPrice = restaurant.getLowestPrice();
    if (originalLowestPrice == null || originalLowestPrice > price){
      restaurant.setLowestPrice(price);
      restaurantRepository.save(restaurant);
    }

    menu = menuDto.updateMenu(menu);

    menuRepository.save(menu);

    createRestaurantContributor(restaurant, member);
  }

  public Menu delete(Long restaurantId, Long menuId){
    Menu menu = menuRepository.findById(menuId).get();

    // 메뉴의 image가 있다면 s3와 db에서 삭제
    List<MenuImage> relations = menuImageRepository.findAllByMenu(menu);
    for(MenuImage relation: relations){
      menuImageRepository.delete(relation);
    }
    List<ImageUrl> imageUrls = relations.stream()
        .map(MenuImage::getImagePk)
        .map(pk->imageUrlRepository.findById(pk).orElseThrow(() -> new NotFoundException("이미지")))
        .collect(Collectors.toList());
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
          .build();

      imageUrlRepository.save(imageUrl);

      MenuImage relation = MenuImage.builder()
          .imagePk(imageUrl)
          .menu(menu)
          .build();

      menuImageRepository.save(relation);

      createRestaurantContributor(menu.getRestaurant(), member);
    }
  }

  public List<MenuImageDto> menuImageList(Long menuId){
    Menu menu = menuRepository.findById(menuId)
        .orElseThrow(() -> new NotFoundException("메뉴"));
    return menuImageRepository.findAllByMenu(menu).stream()
        .map(MenuImage::getImagePk)
        .map(pk->imageUrlRepository.findById(pk).orElseThrow(() -> new NotFoundException("이미지")))
        .map(MenuImageDto::fromEntity)
        .collect(Collectors.toList());
  }

  public void deleteImage(Long menuId, Long imageId){
    Menu menu = menuRepository.findById(menuId)
        .orElseThrow(() -> new NotFoundException("메뉴"));
    ImageUrl imageUrl = imageUrlRepository.findById(imageId)
        .orElseThrow(() -> new NotFoundException("메뉴의 이미지"));
    MenuImage relation = menuImageRepository.findById(imageUrl.getId())
        .orElseThrow(() -> new NotFoundException("메뉴와 이미지의 관계"));

    Long count = menu.getImageCount();
    menu.setImageCount(--count);
    menuRepository.save(menu);

    s3UploadService.delete(imageUrl.getImageUrl());
    imageUrlRepository.delete(imageUrl);
    menuImageRepository.delete(relation);
  }

  private void createRestaurantContributor(Restaurant restaurant, Member member){
    if(restaurantContributorRepository.findByRestaurantAndMember(restaurant, member).isEmpty()){
      RestaurantContributor contributor = RestaurantContributor.builder()
          .member(member)
          .restaurant(restaurant)
          .build();
      restaurantContributorRepository.save(contributor);
    }
  }
}