package LikeLion.TodaysLunch.category.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import LikeLion.TodaysLunch.category.domain.FoodCategory;
import LikeLion.TodaysLunch.category.domain.RecommendCategory;
import LikeLion.TodaysLunch.category.dto.RecommendCategoryDto;
import LikeLion.TodaysLunch.exception.NotFoundException;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import LikeLion.TodaysLunch.restaurant.repository.RestRecmdRelRepository;
import LikeLion.TodaysLunch.environment.service.ServiceTest;
import LikeLion.TodaysLunch.environment.service.TestRestaurant;
import LikeLion.TodaysLunch.environment.service.TestUser;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


class CategoryServiceTest extends ServiceTest {

  @Autowired
  private CategoryService categoryService;
  @Autowired
  private RestRecmdRelRepository restRecmdRelRepository;

  @Test
  void 추천카테고리_수정() {
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    List<RecommendCategory> 이미_존재하는_카테고리들 = restRecmdRelRepository.findAllByRestaurant(맛집.getRestaurant())
        .stream().map(r->r.getRecommendCategory()).collect(Collectors.toList());

    // when
    RecommendCategoryDto.Edit 수정_요청1 = RecommendCategoryDto.Edit.builder().recommendCategoryIds(new ArrayList<>(Arrays.asList(추천카테고리_반환하기(추천카테고리이름1), 추천카테고리_반환하기(추천카테고리이름2)))).build();
    categoryService.recommendCategoryEdit(맛집.getRestaurant().getId(), 수정_요청1);
    List<RecommendCategory> 수정된_카테고리들1 = restRecmdRelRepository.findAllByRestaurant(맛집.getRestaurant())
        .stream().map(r->r.getRecommendCategory()).collect(Collectors.toList());

    RecommendCategoryDto.Edit 수정_요청2 = RecommendCategoryDto.Edit.builder().recommendCategoryIds(new ArrayList<>(Arrays.asList(추천카테고리_반환하기(추천카테고리이름2)))).build();
    categoryService.recommendCategoryEdit(맛집.getRestaurant().getId(),수정_요청2);
    List<RecommendCategory> 수정된_카테고리들2 = restRecmdRelRepository.findAllByRestaurant(맛집.getRestaurant())
        .stream().map(r->r.getRecommendCategory()).collect(Collectors.toList());

    // then
    assertEquals(0, 이미_존재하는_카테고리들.size());
    assertEquals(2, 수정된_카테고리들1.size());
    assertEquals(1, 수정된_카테고리들2.size());
  }

  @Test
  @Transactional
  void 추천카테고리_수정을_맛집속성에_반영하기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());

    // when
    RecommendCategoryDto.Edit 수정_요청 = RecommendCategoryDto.Edit.builder().recommendCategoryIds(new ArrayList<>(Arrays.asList(추천카테고리_반환하기(추천카테고리이름1), 추천카테고리_반환하기(추천카테고리이름2)))).build();
    categoryService.recommendCategoryEdit(맛집.getRestaurant().getId(),수정_요청);

    // then
    Restaurant 수정된_맛집 = testRestaurantEnviron.restaurantRepository().findByRestaurantName("정든그릇")
        .orElseThrow(() -> new NotFoundException("맛집"));
    assertEquals(2, 수정된_맛집.getRecommendCategoryRelations().size());
  }

  @Test
  void 추천카테고리수정을_맛집_수정시간에_반영하기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    LocalDateTime 생성시간 = LocalDateTime.now();

    // when
    RecommendCategoryDto.Edit 수정_요청 = RecommendCategoryDto.Edit.builder().recommendCategoryIds(new ArrayList<>(Arrays.asList(추천카테고리_반환하기(추천카테고리이름1), 추천카테고리_반환하기(추천카테고리이름2)))).build();
    categoryService.recommendCategoryEdit(맛집.getRestaurant().getId(),수정_요청);

    // then
    Restaurant 수정된_맛집 = testRestaurantEnviron.restaurantRepository().findByRestaurantName("정든그릇")
        .orElseThrow(() -> new NotFoundException("맛집"));
    LocalDateTime 수정시간 = 수정된_맛집.getUpdatedDate();
    assertThat(수정시간).isAfter(생성시간);
  }

  @Test
  void 음식카테고리_수정하기(){
    // given
    FoodCategory 음식카테고리 = foodCategoryRepository.findByName("한식")
        .orElseThrow(() -> new NotFoundException("음식 카테고리"));

    // when
    categoryService.updateFoodCategory(음식카테고리.getId(), "변경된 이름");

    // then
    assertEquals("변경된 이름", foodCategoryRepository.findByName("변경된 이름").get().getName());
  }
}