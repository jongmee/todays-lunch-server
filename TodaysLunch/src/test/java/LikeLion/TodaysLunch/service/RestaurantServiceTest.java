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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
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
    FoodCategory foodCategory = new FoodCategory();
    foodCategory.setName("food_category1");
    foodCategoryRepository.save(foodCategory);

    LocationCategory locationCategory = new LocationCategory();
    locationCategory.setName("location_category1");
    locationCategory.setLatitude(0.0);
    locationCategory.setLongitude(0.0);
    locationCategoryRepository.save(locationCategory);

    LocationTag locationTag = new LocationTag();
    locationTag.setName("location_tag1");
    locationTagRepository.save(locationTag);

    Member member = new Member("nickname1", "password", foodCategory, locationCategory);
    memberRepository.save(member);

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
    restaurantRepository.save(restaurant);

    // when
    restaurantService.addOrCancelAgreement(member, 1L);

    // then
    Restaurant restaurantForTest = restaurantRepository.findById(1L).get();
    Assertions.assertEquals(1L, restaurantForTest.getAgreementCount().get());
  }
}