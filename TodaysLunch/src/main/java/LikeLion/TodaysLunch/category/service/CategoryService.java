package LikeLion.TodaysLunch.category.service;

import LikeLion.TodaysLunch.category.domain.FoodCategory;
import LikeLion.TodaysLunch.category.domain.LocationCategory;
import LikeLion.TodaysLunch.category.domain.LocationRelation;
import LikeLion.TodaysLunch.category.domain.LocationTag;
import LikeLion.TodaysLunch.category.domain.RecommendCategory;
import LikeLion.TodaysLunch.category.dto.FoodCategoryDto;
import LikeLion.TodaysLunch.category.dto.LocationCategoryDto;
import LikeLion.TodaysLunch.category.dto.LocationTagDto;
import LikeLion.TodaysLunch.category.repository.LocationRelationRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

  private final FoodCategoryRepository foodCategoryRepository;
  private final LocationCategoryRepository locationCategoryRepository;
  private final LocationTagRepository locationTagRepository;
  private final RecommendCategoryRepository recommendCategoryRepository;
  private final RestRecmdRelRepository restRecmdRelRepository;
  private final DataJpaRestaurantRepository restaurantRepository;
  private final LocationRelationRepository locationRelationRepository;

  public List<FoodCategoryDto> foodCategoryList(){
    return foodCategoryRepository.findAll()
        .stream().map(c-> FoodCategoryDto.fromEntity(c))
        .collect(Collectors.toList());
  }

  public List<LocationCategoryDto> locationCategoryList(){
    return locationCategoryRepository.findAll()
        .stream().map(c->LocationCategoryDto.fromEntity(c))
        .collect(Collectors.toList());
  }

  public List<LocationTagDto> locationTagList(){
    List<LocationTag> tagList = locationTagRepository.findAll();
    List<LocationTagDto> dtoList = new ArrayList<>(tagList.size());

    Long categoryId;
    for(LocationTag tag: tagList){
      categoryId = locationRelationRepository.findByLocationTag(tag)
          .orElseThrow(() -> new NotFoundException("음식 카테고리")).getLocationCategory().getId();
      dtoList.add(LocationTagDto.fromEntity(tag, categoryId));
    }

    return dtoList;
  }

  public List<RecommendCategoryDto.CategoryList> recommendCategoryList(){
    return recommendCategoryRepository.findAll()
        .stream().map(c->RecommendCategoryDto.CategoryList.fromEntity(c))
        .collect(Collectors.toList());
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
      restaurant.deleteRecommendCategoryRelation(pair.get(category));
    }

    for(RecommendCategory category: objCategories){
      RestaurantRecommendCategoryRelation relation = new RestaurantRecommendCategoryRelation(restaurant, category);
      restRecmdRelRepository.save(relation);
      restaurant.addRecommendCategoryRelation(relation);
    }

    if(objCategories.size()>0 || existingCategories.size()>0)
      restaurant.setUpdatedDate(LocalDateTime.now());

  }

  public void createFoodCategory(String foodCategoryName){
    foodCategoryRepository.save(FoodCategory.builder().name(foodCategoryName).build());
  }

  public void createLocationCategory(LocationCategoryDto locationCategoryDto){
    locationCategoryRepository.save(locationCategoryDto.toEntity());
  }

  public void createLocationTag(LocationTagDto locationTagDto){
    locationTagRepository.save(locationTagDto.toEntity());
  }

  public void createRecommendCategory(RecommendCategoryDto.CategoryList categoryDto){
    recommendCategoryRepository.save(categoryDto.toEntity());
  }

  public void updateFoodCategory(Long id, String foodCategoryName){
    FoodCategory foodCategory = foodCategoryRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("음식 카테고리"));
    foodCategory.update(foodCategoryName);
  }

  public void updateLocationCategory(Long id, LocationCategoryDto locationCategoryDto){
    LocationCategory locationCategory = locationCategoryRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("위치 카테고리"));
    locationCategory.update(locationCategoryDto);
  }

  public void updateLocationTag(Long id, LocationTagDto locationTagDto){
    LocationTag locationTag = locationTagRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("위치 태그"));
    locationTag.update(locationTagDto);
  }

  public  void updateRecommendCategory(Long id, RecommendCategoryDto.CategoryList categoryDto){
    RecommendCategory recommendCategory = recommendCategoryRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("추천 카테고리"));
    recommendCategory.update(categoryDto);
  }

  public void deleteFoodCategory(Long id){
    FoodCategory foodCategory = foodCategoryRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("음식 카테고리"));
    foodCategoryRepository.delete(foodCategory);
  }

  public void deleteLocationCategory(Long id){
    LocationCategory locationCategory = locationCategoryRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("위치 카테고리"));
    locationCategoryRepository.delete(locationCategory);
  }

  public void deleteLocationTag(Long id){
    LocationTag locationTag = locationTagRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("위치 태그"));
    locationTagRepository.delete(locationTag);
  }

  public void deleteRecommendCategory(Long id){
    RecommendCategory recommendCategory = recommendCategoryRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("추천 카테고리"));
    recommendCategoryRepository.delete(recommendCategory);
  }
}
