package LikeLion.TodaysLunch.category.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import LikeLion.TodaysLunch.category.domain.RecommendCategory;
import LikeLion.TodaysLunch.category.dto.RecommendCategoryDto;
import LikeLion.TodaysLunch.category.service.CategoryService;
import LikeLion.TodaysLunch.restaurant.repository.RestRecmdRelRepository;
import LikeLion.TodaysLunch.skeleton.ServiceTest;
import LikeLion.TodaysLunch.skeleton.TestRestaurant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class CategoryServiceTest extends ServiceTest {
  @Autowired
  private CategoryService categoryService;
  @Autowired
  private RestRecmdRelRepository restRecmdRelRepository;
  @Test
  void 추천카테고리_수정() {
    // given
    TestRestaurant 맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, null);

    List<RecommendCategory> existingCategories = restRecmdRelRepository.findAllByRestaurant(맛집.getRestaurant())
        .stream().map(r->r.getRecommendCategory()).collect(Collectors.toList());

    // when
    RecommendCategoryDto.Edit editDto1 = RecommendCategoryDto.Edit.builder().recommendCategoryIds(new ArrayList<>(Arrays.asList(1L, 2L))).build();
    categoryService.recommendCategoryEdit(맛집.getRestaurant().getId(),editDto1);
    List<RecommendCategory> newCategories = restRecmdRelRepository.findAllByRestaurant(맛집.getRestaurant())
        .stream().map(r->r.getRecommendCategory()).collect(Collectors.toList());

    RecommendCategoryDto.Edit editDto2 = RecommendCategoryDto.Edit.builder().recommendCategoryIds(new ArrayList<>(Arrays.asList(2L))).build();
    categoryService.recommendCategoryEdit(맛집.getRestaurant().getId(),editDto2);
    List<RecommendCategory> newCategories2 = restRecmdRelRepository.findAllByRestaurant(맛집.getRestaurant())
        .stream().map(r->r.getRecommendCategory()).collect(Collectors.toList());

    // then
    Assertions.assertEquals(0, existingCategories.size());
    Assertions.assertEquals(2, newCategories.size());
    Assertions.assertEquals(1, newCategories2.size());
  }
  @Test
  void 추천카테고리_수정을_맛집속성에_반영하기(){
    // given
    TestRestaurant 맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, null);

    // when
    RecommendCategoryDto.Edit editDto1 = RecommendCategoryDto.Edit.builder().recommendCategoryIds(new ArrayList<>(Arrays.asList(1L, 2L))).build();
    categoryService.recommendCategoryEdit(맛집.getRestaurant().getId(),editDto1);

    // then
    Assertions.assertEquals(2, 맛집.getRestaurant().getRecommendCategoryRelations().size());
  }
  @Test
  void 추천카테고리수정을_맛집_수정시간에_반영하기(){
    // given
    TestRestaurant 맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구",
        "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, null);
    LocalDateTime 생성시간 = LocalDateTime.now();

    // when
    RecommendCategoryDto.Edit editDto1 = RecommendCategoryDto.Edit.builder().recommendCategoryIds(new ArrayList<>(Arrays.asList(1L, 2L))).build();
    categoryService.recommendCategoryEdit(맛집.getRestaurant().getId(),editDto1);

    // then
    LocalDateTime 수정시간 = 맛집.getRestaurant().getUpdatedDate();
    assertThat(수정시간).isAfter(생성시간);
  }
}