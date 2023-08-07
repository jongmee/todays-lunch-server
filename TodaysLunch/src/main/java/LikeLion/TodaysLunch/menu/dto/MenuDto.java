package LikeLion.TodaysLunch.menu.dto;

import LikeLion.TodaysLunch.menu.domain.Menu;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
  @NotBlank(message="메뉴의 이름은 공백, 빈칸이 될 수 없습니다.")
  private String name;
  @NotNull(message="메뉴의 가격은 공백, 빈칸이 될 수 없습니다.")
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
