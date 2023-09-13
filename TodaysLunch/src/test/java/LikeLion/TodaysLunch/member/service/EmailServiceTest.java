package LikeLion.TodaysLunch.member.service;

import static org.junit.jupiter.api.Assertions.*;

import LikeLion.TodaysLunch.skeleton.service.ServiceTest;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class EmailServiceTest extends ServiceTest {

  @Autowired
  private EmailService emailService;

  @Test
  void 서로다른_임시비밀번호_발급하기() throws Exception {
    // given & when
    String 임시비밀번호1 = emailService.sendTemporaryPassword("qwer@naver.com");
    String 임시비밀번호2 = emailService.sendTemporaryPassword("qwer@naver.com");

    // then
    assertNotEquals(임시비밀번호1, 임시비밀번호2);
  }

  @Test
  void 서로다른_이메일인증번호_발급하기() throws Exception {
    // given & when
    String 인증번호1 = emailService.sendEmailVerifyMessage("qwer@naver.com");
    String 인증번호2 = emailService.sendEmailVerifyMessage("qwer@naver.com");

    // then
    assertNotEquals(인증번호1, 인증번호2);
  }
}