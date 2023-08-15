package LikeLion.TodaysLunch.skeleton.service;

import LikeLion.TodaysLunch.category.domain.FoodCategory;
import LikeLion.TodaysLunch.category.domain.LocationCategory;
import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.customized.domain.MemberFoodCategory;
import LikeLion.TodaysLunch.customized.domain.MemberLocationCategory;
import java.util.List;

public class TestUser {
  private final TestUserEnviron environ;
  private Member member;
  public TestUser(TestUserEnviron environ) {
    this.environ = environ;
  }
  public TestUser 유저_등록하기(String email, String password, String nickname) {
    Member member = Member.builder().email(email).password(environ.passwordEncoder().encode(password)).nickname(nickname).build();
    member.setMyStoreCount(0L);
    member.setTemporaryPw(false);
    this.member = environ.memberRepository().save(member);
    return this;
  }
  public TestUser 유저의_음식카테고리_등록하기(List<FoodCategory> foodCategoryList) {
    for(FoodCategory category: foodCategoryList) {
      MemberFoodCategory memberFoodCategory = MemberFoodCategory.builder()
          .foodCategory(category)
          .member(this.member)
          .build();
      environ.memberFoodCategoryRepository().save(memberFoodCategory);
    }
    return this;
  }
  public TestUser 유저의_위치카테고리_등록하기(List<LocationCategory> locationCategoryList) {
    for (LocationCategory category : locationCategoryList) {
      MemberLocationCategory memberLocationCategory = MemberLocationCategory.builder()
          .locationCategory(category)
          .member(this.member)
          .build();
      environ.memberLocationCategoryRepository().save(memberLocationCategory);
    }
    return this;
  }
  public Member getMember() { return this.member; }
}
