package LikeLion.TodaysLunch.member.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MemberLoginDto {
  @NotBlank(message = "로그인을 위해 이메일을 입력해주세요")
  @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
  private String email;
  @NotBlank(message = "로그인을 위해 비밀번호를 입력해주세요")
  private String password;
}
