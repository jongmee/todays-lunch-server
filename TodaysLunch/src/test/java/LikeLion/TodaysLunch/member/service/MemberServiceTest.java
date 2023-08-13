package LikeLion.TodaysLunch.member.service;

import static org.junit.jupiter.api.Assertions.*;

import LikeLion.TodaysLunch.customized.domain.MemberFoodCategory;
import LikeLion.TodaysLunch.customized.domain.MemberLocationCategory;
import LikeLion.TodaysLunch.customized.repository.MemberFoodCategoryRepository;
import LikeLion.TodaysLunch.customized.repository.MemberLocationCategoryRepository;
import LikeLion.TodaysLunch.exception.NotFoundException;
import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.member.dto.AdminJoinDto;
import LikeLion.TodaysLunch.member.dto.MemberJoinDto;
import LikeLion.TodaysLunch.skeleton.ServiceTest;
import LikeLion.TodaysLunch.skeleton.TestUser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberServiceTest extends ServiceTest {

  @Autowired
  private MemberService memberService;
  @Autowired
  private EmailService emailService;
  @Autowired
  private MemberLocationCategoryRepository memberLocationCategoryRepository;
  @Autowired
  private MemberFoodCategoryRepository memberFoodCategoryRepository;

  @Test
  void 임시비밀번호로_비밀번호_변경하기() throws Exception {
    // given
    String 이메일 = "jp3869@naver.com";
    TestUser 유저 = makeTestUser(이메일, "1234", "유저1", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));

    // when
    Member member = memberService.checkExistingEmail(이메일);
    String password = emailService.sendTemporaryPassword(이메일);
    memberService.changePassword(member, password, true);

    // then
    Member 비밀번호가_변경된_유저 = testUserEnviron.memberRepository().findByEmail(이메일).get();
    assertEquals(true, testUserEnviron.passwordEncoder().matches(password, 비밀번호가_변경된_유저.getPassword()));
  }

  @Test
  void 비밀번호_일치_확인하기() {
    // given
    String 비밀번호 ="1234";
    TestUser 유저 = makeTestUser("dummy@test.com", 비밀번호, "유저1", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));

    // when
    Boolean 응답 = memberService.checkPassword(유저.getMember(), 비밀번호);

    // then
    assertEquals(true,응답);
  }

  @Test
  void 일반유저_회원가입하기() {
    // given
    MemberJoinDto 회원가입_요청 = MemberJoinDto.builder()
        .email("qwer1234@naver.com")
        .password("1234")
        .nickname("일반유저")
        .foodCategoryList(new ArrayList<>(Arrays.asList("한식")))
        .locationCategoryList(new ArrayList<>(Arrays.asList("서강대")))
        .build();

    // when
    memberService.join(회원가입_요청);

    // then
    Member 등록된_유저 = testUserEnviron.memberRepository().findByEmail("qwer1234@naver.com")
        .orElseThrow(() -> new NotFoundException("해당 이메일로 가입된 유저"));
    assertEquals("일반유저", 등록된_유저.getNickname());
    assertEquals(Arrays.asList("ROLE_USER"), 등록된_유저.getRoles());
  }

  @Test
  void 관리자_회원가입하기() {
    // given
    AdminJoinDto 회원가입_요청 = AdminJoinDto.builder()
        .email("qwer1234@naver.com")
        .password("1234")
        .nickname("관리자")
        .build();

    // when
    memberService.adminJoin(회원가입_요청);

    // then
    Member 등록된_관리자 = testUserEnviron.memberRepository().findByEmail("qwer1234@naver.com")
        .orElseThrow(() -> new NotFoundException("해당 이메일로 가입된 유저"));
    assertEquals("관리자", 등록된_관리자.getNickname());
    assertEquals(Arrays.asList("ROLE_ADMIN"), 등록된_관리자.getRoles());
  }

  @Test
  void 유저의_위치카테고리_수정하기() {
    // given
    MemberJoinDto 회원가입_요청 = MemberJoinDto.builder()
        .email("qwer1234@naver.com")
        .password("1234")
        .nickname("일반유저")
        .foodCategoryList(new ArrayList<>(Arrays.asList("한식")))
        .locationCategoryList(new ArrayList<>(Arrays.asList("서강대")))
        .build();
    memberService.join(회원가입_요청);

    Member 등록된_유저 = testUserEnviron.memberRepository().findByEmail("qwer1234@naver.com")
        .orElseThrow(() -> new NotFoundException("해당 이메일로 가입된 유저"));

    // when
    memberService.myLocationCategoryEdit(등록된_유저, new ArrayList<>(Arrays.asList("서강대","서울대")));

    // then
    List<MemberLocationCategory> 등록된_유저의_위치카테고리 = memberLocationCategoryRepository.findAllByMember(등록된_유저);
    assertEquals(2, 등록된_유저의_위치카테고리.size());
    assertEquals("서강대", 등록된_유저의_위치카테고리.get(0).getLocationCategory().getName());
    assertEquals("서울대", 등록된_유저의_위치카테고리.get(1).getLocationCategory().getName());
  }

  @Test
  void 유저의_음식카테고리_수정하기() {
    // given
    MemberJoinDto 회원가입_요청 = MemberJoinDto.builder()
        .email("qwer1234@naver.com")
        .password("1234")
        .nickname("일반유저")
        .foodCategoryList(new ArrayList<>(Arrays.asList("한식")))
        .locationCategoryList(new ArrayList<>(Arrays.asList("서강대")))
        .build();
    memberService.join(회원가입_요청);

    Member 등록된_유저 = testUserEnviron.memberRepository().findByEmail("qwer1234@naver.com")
        .orElseThrow(() -> new NotFoundException("해당 이메일로 가입된 유저"));

    // when
    memberService.myFoodCategoryEdit(등록된_유저, new ArrayList<>(Arrays.asList("한식", "중식")));

    // then
    List<MemberFoodCategory> 등록된_유저의_음식카테고리 = memberFoodCategoryRepository.findAllByMember(등록된_유저);
    assertEquals(2, 등록된_유저의_음식카테고리.size());
    assertEquals("한식", 등록된_유저의_음식카테고리.get(0).getFoodCategory().getName());
    assertEquals("중식", 등록된_유저의_음식카테고리.get(1).getFoodCategory().getName());
  }
}