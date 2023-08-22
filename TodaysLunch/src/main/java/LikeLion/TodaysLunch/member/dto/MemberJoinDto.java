package LikeLion.TodaysLunch.member.dto;

import LikeLion.TodaysLunch.member.domain.Member;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberJoinDto extends JoinDto{

  private List<String> foodCategoryList;
  private List<String> locationCategoryList;

  @Builder
  public MemberJoinDto(String email, String password, String nickname, List<String> foodCategoryList, List<String> locationCategoryList){
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.foodCategoryList = foodCategoryList;
    this.locationCategoryList = locationCategoryList;
  }

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
