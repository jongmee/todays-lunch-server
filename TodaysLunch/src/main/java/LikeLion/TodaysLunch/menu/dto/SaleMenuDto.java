package LikeLion.TodaysLunch.menu.dto;

import LikeLion.TodaysLunch.image.domain.MenuImage;
import LikeLion.TodaysLunch.menu.domain.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleMenuDto {

  private String name;
  private Long price;
  private Long salePrice;
  private String imageUrl;
  private Long restaurantId;
  private String restaurantName;

  public static SaleMenuDto fromEntity(Menu menu, String image){
    return SaleMenuDto.builder()
        .name(menu.getName())
        .price(menu.getPrice())
        .salePrice(menu.getSalePrice())
        .imageUrl(image)
        .restaurantId(menu.getRestaurant().getId())
        .restaurantName(menu.getRestaurant().getRestaurantName())
        .build();
  }

}
