package LikeLion.TodaysLunch.environment.service;

import LikeLion.TodaysLunch.customized.repository.MemberFoodCategoryRepository;
import LikeLion.TodaysLunch.customized.repository.MemberLocationCategoryRepository;
import LikeLion.TodaysLunch.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TestUserEnviron {

  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private MemberFoodCategoryRepository memberFoodCategoryRepository;
  @Autowired
  private MemberLocationCategoryRepository memberLocationCategoryRepository;

  public MemberRepository memberRepository() {
    return memberRepository;
  }

  public PasswordEncoder passwordEncoder() {
    return passwordEncoder;
  }

  public MemberFoodCategoryRepository memberFoodCategoryRepository() {
    return memberFoodCategoryRepository;
  }

  public MemberLocationCategoryRepository memberLocationCategoryRepository() {
    return memberLocationCategoryRepository;
  }
}
