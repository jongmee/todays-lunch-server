package LikeLion.TodaysLunch.service;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.repository.FoodCategoryRepository;
import javax.transaction.Transactional;

@Transactional
public class FoodCategoryService {
  private final FoodCategoryRepository foodCategoryRepository;
  public FoodCategoryService(FoodCategoryRepository foodCategoryRepository) {
    this.foodCategoryRepository = foodCategoryRepository;
  }

  public FoodCategory findFoodCategoryByName(String name){
    return foodCategoryRepository.findByName(name).get();
  }

}
