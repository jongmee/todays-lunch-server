package LikeLion.TodaysLunch.dto;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.LocationCategory;
import LikeLion.TodaysLunch.domain.Member;
import java.util.Collections;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberJoinDto {
  @NotBlank(message = "닉네임은 Null과 공백일 수 없습니다!")
  private String nickname;
  @NotBlank(message = "닉네임은 Null과 공백일 수 없습니다!")
  private String email;
  @NotBlank(message = "비밀번호는 Null과 공백일 수 없습니다!")
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
        .build();
  }
}
