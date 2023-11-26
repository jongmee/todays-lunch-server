package LikeLion.TodaysLunch.restaurant.dto.response;

import LikeLion.TodaysLunch.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContributorDto {

  private Long id;
  private String nickname;
  private String icon;

  public static ContributorDto fromEntity(Member member){
    String image = null;
    if(member.getIcon() != null)
      image = member.getIcon().getImageUrl();

    return ContributorDto.builder()
        .id(member.getId())
        .nickname(member.getNickname())
        .icon(image)
        .build();
  }
}
