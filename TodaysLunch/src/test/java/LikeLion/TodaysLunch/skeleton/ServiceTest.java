package LikeLion.TodaysLunch.skeleton;

import LikeLion.TodaysLunch.category.domain.FoodCategory;
import LikeLion.TodaysLunch.category.domain.LocationCategory;
import LikeLion.TodaysLunch.category.domain.LocationTag;
import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.category.domain.RecommendCategory;
import LikeLion.TodaysLunch.exception.NotFoundException;
import LikeLion.TodaysLunch.category.repository.FoodCategoryRepository;
import LikeLion.TodaysLunch.category.repository.LocationCategoryRepository;
import LikeLion.TodaysLunch.category.repository.LocationTagRepository;
import LikeLion.TodaysLunch.category.repository.RecommendCategoryRepository;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public abstract class ServiceTest {

  protected final String 추천카테고리이름1 = "혼밥하기 좋으니 가게 🍚";
  protected final String 추천카테고리이름2 = "술자리 저격이니 가게 🍺";

  @Autowired
  protected FoodCategoryRepository foodCategoryRepository;
  @Autowired
  protected LocationCategoryRepository locationCategoryRepository;
  @Autowired
  protected LocationTagRepository locationTagRepository;
  @Autowired
  protected RecommendCategoryRepository recommendCategoryRepository;
  @Autowired
  protected TestUserEnviron testUserEnviron;
  @Autowired
  protected TestRestaurantEnviron testRestaurantEnviron;

  @BeforeEach
  void beforeEach() {
    카테고리_등록하기();
  }

  public TestUser makeTestUser(String email, String password, String nickname,
      List<String> foodCategories, List<String> locationCategories) {
    TestUser tu = new TestUser(testUserEnviron);

    tu.유저_등록하기(email, password, nickname);

    List<FoodCategory> foodCategoryList = new ArrayList<>();
    List<LocationCategory> locationCategoryList = new ArrayList<>();

    for(String categoryName: foodCategories) {
      foodCategoryList.add(foodCategoryRepository.findByName(categoryName)
          .orElseThrow(() -> new NotFoundException("음식 카테고리")));
    }
    for(String categoryName: locationCategories) {
      locationCategoryList.add(locationCategoryRepository.findByName(categoryName)
          .orElseThrow(() -> new NotFoundException("위치 카테고리")));
    }

    tu.유저의_음식카테고리_등록하기(foodCategoryList);
    tu.유저의_위치카테고리_등록하기(locationCategoryList);

    return tu;
  }

  public TestRestaurant makeTestRestaurant(String foodCategoryName, String locationCategoryName,
      String locationTagName, String address, String restaurantName, String introduction,
      Double longitude, Double latitude, Member registrant) {
    TestRestaurant tr = new TestRestaurant(testRestaurantEnviron);

    FoodCategory foodCategory = foodCategoryRepository.findByName(foodCategoryName)
        .orElseThrow(() -> new NotFoundException("음식 카테고리"));
    LocationCategory locationCategory = locationCategoryRepository.findByName(locationCategoryName)
        .orElseThrow(() -> new NotFoundException("위치 카테고리"));
    LocationTag locationTag = locationTagRepository.findByName(locationTagName)
        .orElseThrow(() -> new NotFoundException("위치 태그"));

    tr.정식맛집_등록하기(foodCategory, locationCategory, locationTag, address, restaurantName, introduction, longitude, latitude, registrant);

    return tr;
  }

  public TestRestaurant makeTestJudgeRestaurant(String foodCategoryName, String locationCategoryName,
      String locationTagName, String address, String restaurantName, String introduction,
      Double longitude, Double latitude, Member registrant) {
    TestRestaurant tr = new TestRestaurant(testRestaurantEnviron);

    FoodCategory foodCategory = foodCategoryRepository.findByName(foodCategoryName)
        .orElseThrow(() -> new NotFoundException("음식 카테고리"));
    LocationCategory locationCategory = locationCategoryRepository.findByName(locationCategoryName)
        .orElseThrow(() -> new NotFoundException("위치 카테고리"));
    LocationTag locationTag = locationTagRepository.findByName(locationTagName)
        .orElseThrow(() -> new NotFoundException("위치 태그"));

    tr.심사맛집_등록하기(foodCategory, locationCategory, locationTag, address, restaurantName, introduction, longitude, latitude, registrant);

    return tr;
  }

  private void 카테고리_등록하기() {
    LocationCategory locationCategory1 = 위치카테고리_생성하기("서강대",37.550940, 126.941136);
    LocationCategory locationCategory2 = 위치카테고리_생성하기("연세대",37.565750, 126.938744);
    LocationCategory locationCategory3 = 위치카테고리_생성하기("서울대",37.459992, 126.951466);
    LocationTag locationTag1 = 위치태그_생성하기("정문", 37.551691, 126.937659);
    LocationTag locationTag2 = 위치태그_생성하기("남문", 37.549549, 126.938950);
    LocationTag locationTag3 = 위치태그_생성하기("후문", 37.550793, 126.944010);
    LocationTag locationTag4 = 위치태그_생성하기("대흥", 37.547896, 126.941879);
    LocationTag locationTag5 = 위치태그_생성하기("신촌", 37.555482, 126.936850);
    LocationTag locationTag6 = 위치태그_생성하기("이대", 37.556776, 126.945947);
    FoodCategory foodCategory1 = 음식카테고리_생성하기("한식");
    FoodCategory foodCategory2 = 음식카테고리_생성하기("중식");
    RecommendCategory recommendCategory1 = 추천카테고리_생성하기(추천카테고리이름1);
    RecommendCategory recommendCategory2 = 추천카테고리_생성하기(추천카테고리이름2);
  }

  private FoodCategory 음식카테고리_생성하기(String name) {
    FoodCategory foodCategory =
        FoodCategory
            .builder()
            .name(name)
            .build();
    return foodCategoryRepository.save(foodCategory);
  }

  private LocationCategory 위치카테고리_생성하기(String name, Double latitude, Double longitude) {
    LocationCategory locationCategory =
        LocationCategory
            .builder()
            .name(name)
            .longitude(longitude)
            .latitude(latitude)
            .build();
    return locationCategoryRepository.save(locationCategory);
  }

  private LocationTag 위치태그_생성하기(String name, Double latitude, Double longitude) {
    LocationTag locationTag = new LocationTag();
    locationTag.setName(name);
    locationTag.setLatitude(latitude);
    locationTag.setLongitude(longitude);
    return locationTagRepository.save(locationTag);
  }

  private RecommendCategory 추천카테고리_생성하기(String name) {
    return recommendCategoryRepository.save(RecommendCategory
        .builder()
        .color("#0100FF")
        .name(name)
        .build());
  }

  protected Long 추천카테고리_반환하기(String name){
    return recommendCategoryRepository.findByName(name)
        .orElseThrow(() -> new NotFoundException("추천 카테고리")).getId();
  }
}
