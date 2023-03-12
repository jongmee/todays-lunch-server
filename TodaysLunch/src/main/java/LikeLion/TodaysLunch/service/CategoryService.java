package LikeLion.TodaysLunch.service;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.LocationCategory;
import LikeLion.TodaysLunch.domain.LocationTag;
import LikeLion.TodaysLunch.domain.RecommendCategory;
import LikeLion.TodaysLunch.repository.FoodCategoryRepository;
import LikeLion.TodaysLunch.repository.LocationCategoryRepository;
import LikeLion.TodaysLunch.repository.LocationTagRepository;
import LikeLion.TodaysLunch.repository.RecommendCategoryRepository;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
public class CategoryService {
  private final FoodCategoryRepository foodCategoryRepository;
  private final LocationCategoryRepository locationCategoryRepository;
  private final LocationTagRepository locationTagRepository;
  private final RecommendCategoryRepository recommendCategoryRepository;

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
}
