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
  public static MenuDto fromEntity(Menu menu){
    return MenuDto.builder()
        .id(menu.getId())
        .name(menu.getName())
        .price(menu.getPrice())
        .imageCount(menu.getImageCount())
        .build();
  }
  public Menu toEntity(){
    return Menu.builder().name(name).price(price).build();
  }
}
