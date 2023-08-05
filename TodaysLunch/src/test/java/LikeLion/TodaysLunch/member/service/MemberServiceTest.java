package LikeLion.TodaysLunch.member.service;

import static org.junit.jupiter.api.Assertions.*;

import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.member.dto.MemberLoginDto;
import LikeLion.TodaysLunch.member.dto.TokenDto;
import LikeLion.TodaysLunch.skeleton.ServiceTest;
import LikeLion.TodaysLunch.skeleton.TestUser;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

class MemberServiceTest extends ServiceTest {
  @Autowired
  private MemberService memberService;
  @Autowired
  private EmailService emailService;
  @Test
  void 임시비밀번호로_비밀번호_변경하기() throws Exception {
    // given
    String email = "jp3869@naver.com";
    TestUser 유저 = makeTestUser(email, "1234", "유저1", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));

    // when
    Member member = memberService.checkExistingEmail(email);
    String password = emailService.sendTemporaryPassword(email);
    memberService.changePassword(member, password, true);

    // then
    Member 비밀번호가_변경된_유저 = testUserEnviron.memberRepository().findByEmail(email).get();
    Assertions.assertEquals(true, testUserEnviron.passwordEncoder().matches(password, 비밀번호가_변경된_유저.getPassword()));
  }

  @Test
  void 비밀번호_일치_확인하기() {
    // given
    String 비밀번호 ="1234";
    TestUser 유저 = makeTestUser("dummy@test.com", 비밀번호, "유저1", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));

    // when
    Boolean 응답 = memberService.checkPassword(유저.getMember(), 비밀번호);

    // then
    Assertions.assertEquals(true,응답);
  }
}