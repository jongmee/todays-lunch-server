package LikeLion.TodaysLunch.restaurant.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import LikeLion.TodaysLunch.restaurant.dto.response.ContributeRestaurantDto;
import LikeLion.TodaysLunch.restaurant.dto.response.ParticipateRestaurantDto;
import LikeLion.TodaysLunch.restaurant.dto.response.RestaurantDto;
import LikeLion.TodaysLunch.restaurant.dto.response.RestaurantListDto;
import LikeLion.TodaysLunch.restaurant.dto.response.RestaurantRecommendDto;
import LikeLion.TodaysLunch.restaurant.dto.response.common.RestaurantPageResponse;
import LikeLion.TodaysLunch.environment.service.ServiceTest;
import LikeLion.TodaysLunch.environment.service.TestRestaurant;
import LikeLion.TodaysLunch.environment.service.TestUser;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

class RestaurantServiceTest extends ServiceTest {

  @Autowired
  private RestaurantService restaurantService;

  @Test
  void 유저가_참여한_맛집_목록보기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", List.of("한식"), List.of("서강대"));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    정식맛집.메뉴_등록하기("사케동", 15000L, null, null, 유저.getMember());

    // when
    ParticipateRestaurantDto 응답값 = restaurantService.participateRestaurantList(PageRequest.of(0, 5), 유저.getMember());

    // then
    assertEquals(1L, 응답값.getParticipationCount());
  }

  @Test
  void 맛집_조회시_찜한여부_포함시키기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", List.of("한식"), List.of("서강대"));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    TestRestaurant 정식맛집2 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    restaurantService.addMyStore(정식맛집.getRestaurant().getId(), 유저.getMember());

    // when
    RestaurantPageResponse 응답 = restaurantService.restaurantList(null, null, null, null, null, PageRequest.of(0, 2, Sort.by("rating").descending()), 유저.getMember());

    // then
    List<RestaurantListDto> 맛집목록 = (List<RestaurantListDto>) 응답.getData();
    Boolean 찜한여부1 = 맛집목록.get(0).getLiked();
    Boolean 찜한여부2 = 맛집목록.get(1).getLiked();
    assertEquals(true, 찜한여부1);
    assertEquals(false, 찜한여부2);
  }

  @Test
  void 로그아웃시_찜한여부_false로_포함하기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", List.of("한식"), List.of("서강대"));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());

    // when
    RestaurantPageResponse 응답 = restaurantService.restaurantList(null, null, null, null, null, PageRequest.of(0, 2, Sort.by("rating").descending()), null);

    // then
    List<RestaurantListDto> 맛집목록 = (List<RestaurantListDto>) 응답.getData();
    Boolean 찜한여부 = 맛집목록.get(0).getLiked();
    assertEquals(false, 찜한여부);
  }

  @Test
  void 로그인_상태에서_맛집_추천하기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", List.of("한식"), List.of("서강대"));
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
    assertEquals(5, 추천된_맛집들.size());
  }

  @Test
  void 여러위치카테고리_가진_유저에게_맛집_추천하기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", List.of("한식"), List.of("서강대", "연세대"));
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
    assertEquals(2, 추천된_맛집들.size());
  }

  @Test
  void 로그아웃_상태에서_맛집_추천하기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", List.of("한식"), List.of("서강대"));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    TestRestaurant 정식맛집2 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "가츠벤또", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());

    // when
    List<RestaurantRecommendDto> 추천된_맛집들 = restaurantService.recommendation(null);

    // then
    assertEquals(2, 추천된_맛집들.size());
    assertEquals("가츠벤또", 추천된_맛집들.get(0).getRestaurantName());
    assertEquals("정든그릇", 추천된_맛집들.get(1).getRestaurantName());
  }

  @Test
  void 심사맛집은_추천하지_않기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", List.of("한식"), List.of("서강대"));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    TestRestaurant 심사맛집 = makeTestJudgeRestaurant("한식", "서강대", "정문", "서울시 마포구", "가츠벤또","정말 맛있다", 126.940155, 37.546924, 유저.getMember());

    // when
    List<RestaurantRecommendDto> 추천된_맛집들 = restaurantService.recommendation(유저.getMember());

    // then
    assertEquals(1, 추천된_맛집들.size());
  }

  @Test
  void 맛집기여자를_상세정보에서_반환하기(){
    // given
    TestUser 유저1 = makeTestUser("qwer@naver.com", "1234", "유저1", List.of("한식"), List.of("서강대"));
    TestUser 유저2 = makeTestUser("qwer1@naver.com", "1234", "유저2", List.of("한식"), List.of("서강대"));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저1.getMember());

    // when
    정식맛집.메뉴_등록하기("사케동", 15000L, null, null, 유저1.getMember());
    정식맛집.메뉴_등록하기("명란우동", 15000L, null, null, 유저2.getMember());
    RestaurantDto 상세정보 = restaurantService.restaurantDetail(정식맛집.getRestaurant().getId(), null);

    // then
    assertEquals(2, 상세정보.getContributors().size());
    assertEquals("유저1", 상세정보.getContributors().get(0).getNickname());
    assertEquals("유저2", 상세정보.getContributors().get(1).getNickname());
  }

  @Test
  void 유저가_기여한_맛집_목록보기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저1", List.of("한식"), List.of("서강대"));
    TestRestaurant 정식맛집1 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    TestRestaurant 정식맛집2 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "가츠벤또", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());

    // when
    정식맛집1.메뉴_등록하기("사케동", 15000L, null, null, 유저.getMember());
    정식맛집2.메뉴_등록하기("명란우동", 15000L, null, null, 유저.getMember());
    ContributeRestaurantDto 응답값 = restaurantService.contributeRestaurantList(PageRequest.of(0, 5), 유저.getMember());

    // then
    assertEquals(2L, 응답값.getContributionCount());
  }
}