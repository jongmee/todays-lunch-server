package LikeLion.TodaysLunch.service;

import static org.junit.jupiter.api.Assertions.*;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.LocationCategory;
import LikeLion.TodaysLunch.domain.LocationTag;
import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.repository.AgreementRepository;
import LikeLion.TodaysLunch.repository.DataJpaRestaurantRepository;
import LikeLion.TodaysLunch.repository.FoodCategoryRepository;
import LikeLion.TodaysLunch.repository.LocationCategoryRepository;
import LikeLion.TodaysLunch.repository.LocationTagRepository;
import LikeLion.TodaysLunch.repository.MemberRepository;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class RestaurantServiceTest {
  @Autowired
  private DataJpaRestaurantRepository restaurantRepository;
  @Autowired
  private AgreementRepository agreementRepository;
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private FoodCategoryRepository foodCategoryRepository;
  @Autowired
  private LocationCategoryRepository locationCategoryRepository;
  @Autowired
  private LocationTagRepository locationTagRepository;
  @Autowired
  private RestaurantService restaurantService;
  @Test
  void 심사맛집_동의수_업데이트() {
    // given
    FoodCategory foodCategory = 음식카테고리_생성하기();
    LocationCategory locationCategory = 위치카테고리_생성하기();
    LocationTag locationTag = 위치태그_생성하기();
    Member member = 유저_생성하기("nickname1", "password", foodCategory, locationCategory);

    맛집_생성하기(foodCategory, locationCategory, locationTag, member);

    // when
    restaurantService.addOrCancelAgreement(member, 1L);

    // then
    Restaurant restaurantForTest = restaurantRepository.findById(1L).get();
    Assertions.assertEquals(1L, restaurantForTest.getAgreementCount().get());
  }
  @Test
  void 심사맛집을_정식맛집으로_전환하기(){
    // given
    FoodCategory foodCategory = 음식카테고리_생성하기();
    LocationCategory locationCategory = 위치카테고리_생성하기();
    LocationTag locationTag = 위치태그_생성하기();
    Member member = 유저_생성하기("nickname1", "password", foodCategory, locationCategory);
    Member member2 = 유저_생성하기("nickname2", "password", foodCategory, locationCategory);
    Member member3 = 유저_생성하기("nickname3", "password", foodCategory, locationCategory);
    Member member4 = 유저_생성하기("nickname4", "password", foodCategory, locationCategory);
    Member member5 = 유저_생성하기("nickname5", "password", foodCategory, locationCategory);
    Restaurant restaurant = 맛집_생성하기(foodCategory, locationCategory, locationTag, member);
    Boolean previousJudge = restaurant.getJudgement();

    // when
    restaurantService.addOrCancelAgreement(member, 1L);
    restaurantService.addOrCancelAgreement(member2, 1L);
    restaurantService.addOrCancelAgreement(member3, 1L);
    restaurantService.addOrCancelAgreement(member4, 1L);
    restaurantService.addOrCancelAgreement(member5, 1L);

    // then
    Restaurant restaurantForTest = restaurantRepository.findById(1L).get();
    Assertions.assertEquals(true, previousJudge);
    Assertions.assertEquals(false, restaurantForTest.getJudgement());
  }
   FoodCategory 음식카테고리_생성하기(){
     FoodCategory foodCategory = new FoodCategory();
     foodCategory.setName("food_category1");
     return foodCategoryRepository.save(foodCategory);
  }
  LocationCategory 위치카테고리_생성하기(){
    LocationCategory locationCategory = new LocationCategory();
    locationCategory.setName("location_category1");
    locationCategory.setLatitude(0.0);
    locationCategory.setLongitude(0.0);
    return locationCategoryRepository.save(locationCategory);
  }
  LocationTag 위치태그_생성하기(){
    LocationTag locationTag = new LocationTag();
    locationTag.setName("location_tag1");
    return locationTagRepository.save(locationTag);
  }
  Member 유저_생성하기(String nickname, String password, FoodCategory foodCategory, LocationCategory locationCategory){
    return memberRepository.save(new Member(nickname, password, foodCategory, locationCategory));
  }
  Restaurant 맛집_생성하기(FoodCategory foodCategory, LocationCategory locationCategory, LocationTag locationTag, Member member){
    Restaurant restaurant = Restaurant.builder()
        .restaurantName("restaurant_name1")
        .address("서울")
        .locationTag(locationTag)
        .locationCategory(locationCategory)
        .foodCategory(foodCategory)
        .latitude(0.0)
        .longitude(0.0)
        .introduction("설명")
        .member(member)
        .build();
    return restaurantRepository.save(restaurant);
  }
}