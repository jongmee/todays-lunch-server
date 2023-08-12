package LikeLion.TodaysLunch.member.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public abstract class JoinDto {

  @NotBlank(message = "닉네임은 Null, 공백일 수 없습니다.")
  protected String nickname;
  @NotBlank(message = "이메일 Null, 공백일 수 없습니다.")
  @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
  protected String email;
  @NotBlank(message = "비밀번호는 Null, 공백일 수 없습니다.")
  protected String password;

}
