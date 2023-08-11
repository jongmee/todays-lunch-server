package LikeLion.TodaysLunch.member.dto;

import LikeLion.TodaysLunch.member.domain.Member;
import java.util.Collections;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberJoinDto {

  @NotBlank(message = "닉네임은 Null, 공백일 수 없습니다.")
  private String nickname;
  @NotBlank(message = "이메일 Null, 공백일 수 없습니다.")
  @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
  private String email;
  @NotBlank(message = "비밀번호는 Null, 공백일 수 없습니다.")
  private String password;
  private List<String> foodCategoryList;
  private List<String> locationCategoryList;

  public Member toEntity(String encodedPassword){
    return Member.builder()
        .nickname(nickname)
        .email(email)
        .password(encodedPassword)
        .roles(Collections.singletonList("ROLE_USER"))
        .myStoreCount(0L)
        .temporaryPw(false)
        .build();
  }
}
