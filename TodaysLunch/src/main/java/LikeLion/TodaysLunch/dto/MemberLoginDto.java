package LikeLion.TodaysLunch.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberLoginDto {
  @NotBlank(message = "로그인을 위해 닉네임을 입력해주세요")
  private String nickname;
  @NotBlank(message = "로그인을 위해 비밀번호를 입력해주세요")
  private String password;
}
