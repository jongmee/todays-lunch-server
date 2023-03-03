package LikeLion.TodaysLunch.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberJoinDto {
  @NotBlank(message = "닉네임은 Null과 공백일 수 없습니다!")
  private String nickname;
  @NotBlank(message = "비밀번호는 Null과 공백일 수 없습니다!")
  private String password;
  @NotBlank(message = "음식 카테고리는 Null과 공백일 수 없습니다!")
  private String foodCategory;
  @NotBlank(message = "위치 카테고리는 Null과 공백일 수 없습니다!")
  private String locationCategory;
}
