package LikeLion.TodaysLunch.menu.dto;

import LikeLion.TodaysLunch.menu.domain.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDto {
  private Long id;
  private String name;
  private Long price;
  private Long imageCount;
  private Long salePrice;
  private String saleExplain;
  public static MenuDto fromEntity(Menu menu){
    return MenuDto.builder()
        .id(menu.getId())
        .name(menu.getName())
        .price(menu.getPrice())
        .salePrice(menu.getSalePrice())
        .saleExplain(menu.getSaleExplain())
        .imageCount(menu.getImageCount())
        .build();
  }
  public Menu toEntity(){
    return Menu.builder().name(name).price(price).salePrice(salePrice).saleExplain(saleExplain).build();
  }
  public Menu updateMenu(Menu menu){
    return menu.updateMenu(name, price, salePrice, saleExplain);
  }
}
