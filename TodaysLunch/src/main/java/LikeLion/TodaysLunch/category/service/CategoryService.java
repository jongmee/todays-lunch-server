package LikeLion.TodaysLunch.category.service;

import LikeLion.TodaysLunch.category.domain.FoodCategory;
import LikeLion.TodaysLunch.category.domain.LocationCategory;
import LikeLion.TodaysLunch.category.domain.LocationTag;
import LikeLion.TodaysLunch.category.domain.RecommendCategory;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import LikeLion.TodaysLunch.restaurant.domain.RestaurantRecommendCategoryRelation;
import LikeLion.TodaysLunch.category.dto.RecommendCategoryDto;
import LikeLion.TodaysLunch.exception.NotFoundException;
import LikeLion.TodaysLunch.restaurant.repository.DataJpaRestaurantRepository;
import LikeLion.TodaysLunch.category.repository.FoodCategoryRepository;
import LikeLion.TodaysLunch.category.repository.LocationCategoryRepository;
import LikeLion.TodaysLunch.category.repository.LocationTagRepository;
import LikeLion.TodaysLunch.category.repository.RecommendCategoryRepository;
import LikeLion.TodaysLunch.restaurant.repository.RestRecmdRelRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
public class CategoryService {
  private final FoodCategoryRepository foodCategoryRepository;
  private final LocationCategoryRepository locationCategoryRepository;
  private final LocationTagRepository locationTagRepository;
  private final RecommendCategoryRepository recommendCategoryRepository;
  private final RestRecmdRelRepository restRecmdRelRepository;
  private final DataJpaRestaurantRepository restaurantRepository;

  public List<FoodCategory> foodCategoryList(){
    return foodCategoryRepository.findAll();
  }
  public List<LocationCategory> locationCategoryList(){
    return locationCategoryRepository.findAll();
  }
  public List<LocationTag> locationTagList(){
    return locationTagRepository.findAll();
  }
  public List<RecommendCategory> recommendCategoryList(){
    return recommendCategoryRepository.findAll();
  }
  public void recommendCategoryEdit(Long restaurantId, RecommendCategoryDto.Edit editDto){
    Map<RecommendCategory, RestaurantRecommendCategoryRelation> pair = new HashMap<>();
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new NotFoundException("맛집"));
    List<RestaurantRecommendCategoryRelation> existingRelations = restRecmdRelRepository.findAllByRestaurant(restaurant);
    List<RecommendCategory> existingCategories = new ArrayList<>();
    for(RestaurantRecommendCategoryRelation relation : existingRelations){
      RecommendCategory category = relation.getRecommendCategory();
      pair.put(category, relation);
      existingCategories.add(category);
    }
    List<RecommendCategory> newCategories = new ArrayList<>();
    for(Long id: editDto.getRecommendCategoryIds()){
      newCategories.add(recommendCategoryRepository.findById(id)
          .orElseThrow(() -> new NotFoundException("추천 카테고리")));
    }
    List<RecommendCategory> objCategories = new ArrayList<>(newCategories);
    objCategories.removeAll(existingCategories); // 새로 추가할 category들
    existingCategories.removeAll(newCategories); // 삭제할 category들
    for(RecommendCategory category: existingCategories){
      restRecmdRelRepository.delete(pair.get(category));
    }
    for(RecommendCategory category: objCategories){
      RestaurantRecommendCategoryRelation relation = new RestaurantRecommendCategoryRelation(restaurant, category);
      restRecmdRelRepository.save(relation);
    }
  }
}
