package LikeLion.TodaysLunch.menu.dto;

import LikeLion.TodaysLunch.image.domain.ImageUrl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MenuImageDto {
  private Long id;
  private String imageUrl;
  private Long userId;
  private String nickname;
  public static MenuImageDto fromEntity(ImageUrl image){
    return MenuImageDto.builder()
        .id(image.getId())
        .imageUrl(image.getImageUrl())
        .userId(image.getMember().getId())
        .nickname(image.getMember().getNickname())
        .build();
  }
}
