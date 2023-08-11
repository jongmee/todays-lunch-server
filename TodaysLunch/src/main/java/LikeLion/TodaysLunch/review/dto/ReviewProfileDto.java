package LikeLion.TodaysLunch.review.dto;

import LikeLion.TodaysLunch.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewProfileDto {

  private Long id;
  private String email;
  private String nickname;
  private String icon;

  public static ReviewProfileDto fromEntity(Member member){
    String image = null;
    if(member.getIcon() != null)
      image = member.getIcon().getImageUrl();

    return ReviewProfileDto.builder()
        .id(member.getId())
        .email(member.getEmail())
        .nickname(member.getNickname())
        .icon(image)
        .build();
  }
}
