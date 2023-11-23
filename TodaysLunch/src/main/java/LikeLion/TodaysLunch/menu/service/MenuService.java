package LikeLion.TodaysLunch.menu.service;

import LikeLion.TodaysLunch.image.domain.ImageUrl;
import LikeLion.TodaysLunch.image.domain.MenuImage;
import LikeLion.TodaysLunch.image.repository.MenuImageRepository;
import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.menu.domain.Menu;
import LikeLion.TodaysLunch.menu.dto.SaleMenuDto;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {

  @Autowired
  private S3UploadService s3UploadService;

  private final MenuRepository menuRepository;
  private final ImageUrlRepository imageUrlRepository;
  private final DataJpaRestaurantRepository restaurantRepository;
  private final RestaurantContributorRepository restaurantContributorRepository;
  private final MenuImageRepository menuImageRepository;

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
    if (originalLowestPrice == null || originalLowestPrice > price)
      restaurant.setLowestPrice(price);

    createRestaurantContributor(restaurant, member);

    restaurant.setUpdatedDate(LocalDateTime.now());

    Menu menu = menuDto.toEntity();
    menu.setRestaurant(restaurant);
    menuRepository.save(menu);
  }

  public void update(MenuDto menuDto, Long restaurantId, Long menuId, Member member){
    Menu menu = menuRepository.findById(menuId)
        .orElseThrow(() -> new NotFoundException("메뉴"));
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new NotFoundException("맛집"));

    menuDto.updateMenu(menu);

    // 최저 메뉴 가격 설정
    Long lower;
    Long lowestPrice = 1000000L;
    List<Menu> allMenus = menuRepository.findAllByRestaurant(restaurant);
    for (Menu m : allMenus) {
      lower = m.getSalePrice() == null ? m.getPrice() : m.getSalePrice();
      if (lower < lowestPrice)
        lowestPrice = lower;
    }
    restaurant.setLowestPrice(lowestPrice);

    createRestaurantContributor(restaurant, member);

    restaurant.setUpdatedDate(LocalDateTime.now());
  }

  public Menu delete(Long restaurantId, Long menuId){
    Menu menu = menuRepository.findById(menuId)
        .orElseThrow(() -> new NotFoundException("메뉴"));

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

    // 최저 메뉴 가격 재설정
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new NotFoundException("맛집"));

    if(restaurant.getLowestPrice().equals(menu.getPrice()) || restaurant.getLowestPrice().equals(menu.getSalePrice())) {
      List<Menu> allMenus = menuRepository.findAllByRestaurant(restaurant);

      Long lowestPrice = 1000000L;
      for (Menu m : allMenus) {
        if (!m.getId().equals(menuId)) {
          Long p = m.getPrice();
          if (m.getSalePrice() != null)
            p = m.getSalePrice();

          if (p < lowestPrice)
            lowestPrice = p;
        }
      }
      if(lowestPrice == 1000000L) lowestPrice = null;
      restaurant.setLowestPrice(lowestPrice);
    }

    menuRepository.delete(menu);
    return menu;
  }

  public ImageUrl createImage(MultipartFile image, Long menuId, Member member) throws IOException {
    Menu menu = menuRepository.findById(menuId)
        .orElseThrow(() -> new NotFoundException("메뉴"));

    ImageUrl re = null;
    if(image != null && !image.isEmpty()){
      Long count = menu.getImageCount();
      menu.setImageCount(++count);
      menu.getRestaurant().setUpdatedDate(LocalDateTime.now());
      restaurantRepository.save(menu.getRestaurant());

      // s3에 이미지 저장
      String savedUrl = s3UploadService.upload(image, "menu");
      // image url을 db에 저장
      String originalName = image.getOriginalFilename();
      ImageUrl imageUrl = ImageUrl.builder()
          .originalName(originalName)
          .imageUrl(savedUrl)
          .member(member)
          .build();

      re = imageUrlRepository.save(imageUrl);

      MenuImage relation = MenuImage.builder()
          .imagePk(imageUrl)
          .menu(menu)
          .build();

      menuImageRepository.save(relation);

      createRestaurantContributor(menu.getRestaurant(), member);
    }

    return re;
  }

  public HashMap<String, Object> menuImageList(Long menuId, int page, int size){
    Pageable pageable = PageRequest.of(page, size);
    Menu menu = menuRepository.findById(menuId)
        .orElseThrow(() -> new NotFoundException("메뉴"));

    Page<MenuImage> menuImages = menuImageRepository.findAllByMenu(menu, pageable);
    List<MenuImageDto> images = menuImages.stream()
        .map(MenuImage::getImagePk)
        .map(pk->imageUrlRepository.findById(pk).orElseThrow(() -> new NotFoundException("이미지")))
        .map(MenuImageDto::fromEntity)
        .collect(Collectors.toList());

    HashMap<String, Object> responseMap = new HashMap<>();
    responseMap.put("data", images);
    responseMap.put("totalPages", menuImages.getTotalPages());

    return responseMap;
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

    s3UploadService.delete(imageUrl.getImageUrl());
    imageUrlRepository.delete(imageUrl);
    menuImageRepository.delete(relation);
  }

  public HashMap<String, Object> saleMenuList(Pageable pageable){
    Page<Menu> saleMenuList = menuRepository.findAllBySalePriceIsNotNull(pageable);

    List<SaleMenuDto> saleMenuDtos = new ArrayList<>();

    String image;
    for(Menu menu: saleMenuList){
        image = null;
        MenuImage imageUrl = menuImageRepository.findByMenuAndIsBest(menu, true)
            .orElseGet(()->null);
        if (imageUrl == null) {
          List<MenuImage> images = menuImageRepository.findAllByMenu(menu);
          if(images.size() != 0)
            imageUrl = images.get(0);
        }
        if (imageUrl != null) {
          image = imageUrlRepository.findById(imageUrl.getImagePk())
              .orElseThrow(() -> new NotFoundException("이미지")).getImageUrl();
        }
      saleMenuDtos.add(SaleMenuDto.fromEntity(menu, image));
    }

    HashMap<String, Object> responseMap = new HashMap<>();
    responseMap.put("data", saleMenuDtos);
    responseMap.put("totalPages", saleMenuList.getTotalPages());

    return responseMap;
  }

  public void setBestMenuImage(Long imageId){
    MenuImage menuImage = menuImageRepository.findById(imageId)
        .orElseThrow(() -> new NotFoundException("메뉴와 이미지의 관계"));

    menuImage.setBest(true);

    List<MenuImage> menuImages = menuImageRepository.findAllByMenu(menuImage.getMenu());
    for(MenuImage m: menuImages){
      if(!(m.getImagePk().equals(imageId)) && m.getIsBest())
        m.setBest(false);
    }
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