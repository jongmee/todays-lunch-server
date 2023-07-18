package LikeLion.TodaysLunch.skeleton;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.LocationCategory;
import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.relation.MemberFoodCategory;
import LikeLion.TodaysLunch.domain.relation.MemberLocationCategory;
import java.util.List;

public class TestUser {
  private final TestUserEnviron environ;
  private Member member;
  public TestUser(TestUserEnviron environ) {
    this.environ = environ;
  }
  public TestUser 유저_등록하기(String email, String password, String nickname) {
    Member member = Member.builder().email(email).password(password).nickname(nickname).build();
    member.setMyStoreCount(0L);
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
