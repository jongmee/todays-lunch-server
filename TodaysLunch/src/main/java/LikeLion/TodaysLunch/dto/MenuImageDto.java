package LikeLion.TodaysLunch.dto;

import LikeLion.TodaysLunch.domain.ImageUrl;
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
  private String nickname;
  public static MenuImageDto fromEntity(ImageUrl image){
    return MenuImageDto.builder()
        .id(image.getId())
        .imageUrl(image.getImageUrl())
        .nickname(image.getMember().getNickname())
        .build();
  }
}
