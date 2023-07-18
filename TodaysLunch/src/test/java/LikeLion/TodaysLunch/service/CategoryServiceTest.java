package LikeLion.TodaysLunch.service;

import static org.junit.jupiter.api.Assertions.*;

import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.RecommendCategory;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.domain.relation.RestaurantRecommendCategoryRelation;
import LikeLion.TodaysLunch.dto.RecommendCategoryDto;
import LikeLion.TodaysLunch.exception.NotFoundException;
import LikeLion.TodaysLunch.repository.RestRecmdRelRepository;
import LikeLion.TodaysLunch.skeleton.ServiceTest;
import LikeLion.TodaysLunch.skeleton.TestRestaurant;
import LikeLion.TodaysLunch.skeleton.TestUser;
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
}