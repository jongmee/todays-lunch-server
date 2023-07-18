package LikeLion.TodaysLunch.skeleton;

import LikeLion.TodaysLunch.repository.MemberFoodCategoryRepository;
import LikeLion.TodaysLunch.repository.MemberLocationCategoryRepository;
import LikeLion.TodaysLunch.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestUserEnviron {
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private MemberFoodCategoryRepository memberFoodCategoryRepository;
  @Autowired
  private MemberLocationCategoryRepository memberLocationCategoryRepository;
  public MemberRepository memberRepository() {
    return memberRepository;
  }
  public MemberFoodCategoryRepository memberFoodCategoryRepository() {
    return memberFoodCategoryRepository;
  }
  public MemberLocationCategoryRepository memberLocationCategoryRepository() {
    return memberLocationCategoryRepository;
  }
}
