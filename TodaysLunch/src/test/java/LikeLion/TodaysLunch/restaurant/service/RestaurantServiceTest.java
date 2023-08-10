package LikeLion.TodaysLunch.restaurant.service;

import LikeLion.TodaysLunch.menu.domain.Menu;
import LikeLion.TodaysLunch.menu.dto.MenuDto;
import LikeLion.TodaysLunch.menu.service.MenuService;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import LikeLion.TodaysLunch.restaurant.dto.JudgeRestaurantCreateDto;
import LikeLion.TodaysLunch.exception.NotFoundException;
import LikeLion.TodaysLunch.restaurant.dto.JudgeRestaurantListDto;
import LikeLion.TodaysLunch.restaurant.dto.RestaurantListDto;
import LikeLion.TodaysLunch.restaurant.dto.RestaurantRecommendDto;
import LikeLion.TodaysLunch.skeleton.ServiceTest;
import LikeLion.TodaysLunch.skeleton.TestRestaurant;
import LikeLion.TodaysLunch.skeleton.TestUser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RestaurantServiceTest extends ServiceTest {
  @Autowired
  private RestaurantService restaurantService;
  @Autowired
  private MenuService menuService;
  @Test
  void 맛집_심사_등록하기() throws IOException {
    // given
    Long recommendCategoryId = recommendCategoryRepository.findByName("혼밥하기 좋으니 가게🍚")
        .orElseThrow(() -> new NotFoundException("추천 카테고리")).getId();
    JudgeRestaurantCreateDto createDto = JudgeRestaurantCreateDto
        .builder()
        .foodCategoryName("한식")
        .restaurantName("가츠벤또")
        .latitude(37.546924)
        .longitude(126.940155)
        .address("서울시 마포구")
        .introduction("참 맛있어요!")
        .recommendCategoryIds(new ArrayList<>(Arrays.asList(recommendCategoryId)))
        .build();
    TestUser 유저 = makeTestUser("qwer1234@naver.com", "1234", "lee", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));


    // when
    restaurantService.createJudgeRestaurant(createDto, null, 유저.getMember());

    // then
    Restaurant restaurantForTest = testRestaurantEnviron.restaurantRepository().findByRestaurantName("가츠벤또")
        .orElseThrow(() -> new NotFoundException("맛집"));
    Assertions.assertEquals("참 맛있어요!", restaurantForTest.getIntroduction());
  }
  @Test
  void 심사맛집_동의수_업데이트() {
    // given
    TestUser 유저 = makeTestUser("qwer1234@naver.com", "1234", "lee", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 맛집 = makeTestJudgeRestaurant("한식", "서강대", "정문", "서울시 마포구", "가츠벤또","정말 맛있다", 126.940155, 37.546924, 유저.getMember());

    // when
    Restaurant restaurantForTest = testRestaurantEnviron.restaurantRepository().findByRestaurantName("가츠벤또")
        .orElseThrow(() -> new NotFoundException("맛집"));
    restaurantService.addOrCancelAgreement(유저.getMember(), restaurantForTest.getId());

    // then
    Assertions.assertEquals(1L, restaurantForTest.getAgreementCount().get());
  }
  @Test
  void 심사맛집을_정식맛집으로_전환하기(){
    // given
    TestUser 유저1 = makeTestUser("qwer@naver.com", "1234", "유저1", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestUser 유저2 = makeTestUser("qwer1@naver.com", "1234", "유저2", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestUser 유저3 = makeTestUser("qwer12@naver.com", "1234", "유저3", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestUser 유저4 = makeTestUser("qwer123@naver.com", "1234", "유저4", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestUser 유저5 = makeTestUser("qwer1234@naver.com", "1234", "유저5", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestUser 유저6 = makeTestUser("qwer12345@naver.com", "1234", "유저6", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));

    TestRestaurant 맛집 = makeTestJudgeRestaurant("한식", "서강대", "정문", "서울시 마포구", "가츠벤또","정말 맛있다", 126.940155, 37.546924, 유저1.getMember());
    Boolean previousJudge = 맛집.getRestaurant().getJudgement();

    // when
    Restaurant restaurantForTest = testRestaurantEnviron.restaurantRepository().findByRestaurantName("가츠벤또")
        .orElseThrow(() -> new NotFoundException("맛집"));
    restaurantService.addOrCancelAgreement(유저1.getMember(), restaurantForTest.getId());
    restaurantService.addOrCancelAgreement(유저2.getMember(), restaurantForTest.getId());
    restaurantService.addOrCancelAgreement(유저3.getMember(), restaurantForTest.getId());
    restaurantService.addOrCancelAgreement(유저4.getMember(), restaurantForTest.getId());
    restaurantService.addOrCancelAgreement(유저5.getMember(), restaurantForTest.getId());

    // then
    Assertions.assertEquals(true, previousJudge);
    Assertions.assertEquals(false, restaurantForTest.getJudgement());
  }
  @Test
  void 유저가_참여한_맛집_목록보기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    MenuDto 메뉴_생성_요청 = MenuDto.builder().name("사케동").price(15000L).build();
    menuService.create(메뉴_생성_요청, 정식맛집.getRestaurant().getId(), 유저.getMember());

    // when
    HashMap 응답값 = restaurantService.participateRestaurantList(유저.getMember());

    // then
    Assertions.assertEquals(1, 응답값.get("participationCount"));
    Assertions.assertEquals(1, 응답값.get("contributionCount"));
  }
  @Test
  void 맛집_조회시_찜한여부_포함시키기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    TestRestaurant 정식맛집2 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    restaurantService.addMyStore(정식맛집.getRestaurant().getId(), 유저.getMember());

    // when
    HashMap 응답 = restaurantService.restaurantList(null, null, null, null, null, 0, 2, "rating", "descending", 유저.getMember());

    // then
    List<RestaurantListDto> 맛집목록 = (List<RestaurantListDto>) 응답.get("data");
    Boolean 찜한여부1 = 맛집목록.get(0).getLiked();
    Boolean 찜한여부2 = 맛집목록.get(1).getLiked();
    Assertions.assertEquals(true, 찜한여부1);
    Assertions.assertEquals(false, 찜한여부2);
  }
  @Test
  void 로그아웃시_찜한여부_false로_포함하기(){
    // given
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, null);

    // when
    HashMap 응답 = restaurantService.restaurantList(null, null, null, null, null, 0, 2, "rating", "descending", null);

    // then
    List<RestaurantListDto> 맛집목록 = (List<RestaurantListDto>) 응답.get("data");
    Boolean 찜한여부 = 맛집목록.get(0).getLiked();
    Assertions.assertEquals(false, 찜한여부);
  }
  @Test
  void 심사맛집_조회시_동의여부_포함시키기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 심사맛집 = makeTestJudgeRestaurant("한식", "서강대", "정문", "서울시 마포구", "가츠벤또","정말 맛있다", 126.940155, 37.546924, 유저.getMember());
    restaurantService.addOrCancelAgreement(유저.getMember(), 심사맛집.getRestaurant().getId());

    // when
    HashMap 응답 = restaurantService.judgeRestaurantList(null, null, null, null, 0, 3, "rating", "descending", null, 유저.getMember());

    // then
    List<JudgeRestaurantListDto> 맛집목록 = (List<JudgeRestaurantListDto>) 응답.get("data");
    Boolean 동의여부 = 맛집목록.get(0).getAgreed();
    Assertions.assertEquals(true, 동의여부);
  }
  @Test
  void 로그아웃시_동의여부_false로_포함하기(){
    // given
    TestRestaurant 심사맛집 = makeTestJudgeRestaurant("한식", "서강대", "정문", "서울시 마포구", "가츠벤또","정말 맛있다", 126.940155, 37.546924, null);

    // when
    HashMap 응답 = restaurantService.judgeRestaurantList(null, null, null, null, 0, 3, "rating", "descending", null, null);

    // then
    List<JudgeRestaurantListDto> 맛집목록 = (List<JudgeRestaurantListDto>) 응답.get("data");
    Boolean 동의여부 = 맛집목록.get(0).getAgreed();
    Assertions.assertEquals(false, 동의여부);
  }
  @Test
  void 로그인_상태에서_맛집_추천하기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    TestRestaurant 정식맛집2 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    TestRestaurant 정식맛집3 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    TestRestaurant 정식맛집4 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    TestRestaurant 정식맛집5 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    TestRestaurant 정식맛집6 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    TestRestaurant 정식맛집7 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());

    // when
    List<RestaurantRecommendDto> 추천된_맛집들 = restaurantService.recommendation(유저.getMember());

    // then
    Assertions.assertEquals(5, 추천된_맛집들.size());
  }
  @Test
  void 여러위치카테고리_가진_유저에게_맛집_추천하기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대", "연세대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    TestRestaurant 정식맛집2 = makeTestRestaurant("한식", "연세대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    TestRestaurant 정식맛집3 = makeTestRestaurant("한식", "서울대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    TestRestaurant 정식맛집4 = makeTestRestaurant("한식", "서울대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());

    // when
    List<RestaurantRecommendDto> 추천된_맛집들 = restaurantService.recommendation(유저.getMember());

    // then
    Assertions.assertEquals(2, 추천된_맛집들.size());
  }
  @Test
  void 로그아웃_상태에서_맛집_추천하기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    TestRestaurant 정식맛집2 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    TestRestaurant 정식맛집3 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    TestRestaurant 정식맛집4 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());

    // when
    List<RestaurantRecommendDto> 추천된_맛집들 = restaurantService.recommendation(null);

    // then
    Assertions.assertEquals(4, 추천된_맛집들.size());
  }
  @Test
  void 심사맛집은_추천하지_않기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    TestRestaurant 심사맛집 = makeTestJudgeRestaurant("한식", "서강대", "정문", "서울시 마포구", "가츠벤또","정말 맛있다", 126.940155, 37.546924, 유저.getMember());

    // when
    List<RestaurantRecommendDto> 추천된_맛집들 = restaurantService.recommendation(유저.getMember());

    // then
    Assertions.assertEquals(1, 추천된_맛집들.size());
  }
}