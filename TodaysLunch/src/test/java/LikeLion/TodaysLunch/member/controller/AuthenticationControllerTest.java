package LikeLion.TodaysLunch.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import LikeLion.TodaysLunch.member.dto.MemberLoginDto;
import LikeLion.TodaysLunch.member.dto.TokenDto;
import LikeLion.TodaysLunch.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private MemberService memberService;

  @Test
  void 로그인시_인가_정보_반환하기() throws Exception {
    // given
    MemberLoginDto 로그인_요청 = MemberLoginDto.builder()
        .email("dummy@test.com")
        .password("1234!!!!asdf")
        .build();
    TokenDto.LoginToken 로그인_응답 = TokenDto.LoginToken.builder()
        .accessToken("dummy.access.token")
        .refreshToken("dummy.refresh.token")
        .refreshTokenExpiresTime(123456)
        .build();
    로그인_응답.setId(1L);
    로그인_응답.setTemporaryPw(false);
    BDDMockito.given(memberService.login(any())).willReturn(로그인_응답);

    // when & then
    mockMvc.perform(post("/login")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(로그인_요청)))
        .andDo(print())
        .andExpect(status().isOk());
  }
}
