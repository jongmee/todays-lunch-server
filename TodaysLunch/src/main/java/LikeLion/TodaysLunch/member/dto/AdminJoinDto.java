package LikeLion.TodaysLunch.member.dto;

import LikeLion.TodaysLunch.member.domain.Member;
import java.util.Collections;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminJoinDto extends JoinDto{

  @Builder
  public AdminJoinDto(String email, String password, String nickname){
    this.email = email;
    this.password = password;
    this.nickname = nickname;
  }

  public Member toEntity(String encodedPassword){
    return Member.builder()
        .nickname(nickname)
        .email(email)
        .password(encodedPassword)
        .roles(Collections.singletonList("ROLE_ADMIN"))
        .myStoreCount(0L)
        .temporaryPw(false)
        .build();
  }

}
