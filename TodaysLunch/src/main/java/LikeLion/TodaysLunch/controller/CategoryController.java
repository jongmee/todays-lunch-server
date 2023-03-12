package LikeLion.TodaysLunch.controller;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.LocationCategory;
import LikeLion.TodaysLunch.domain.LocationTag;
import LikeLion.TodaysLunch.domain.RecommendCategory;
import LikeLion.TodaysLunch.service.CategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {
  private final CategoryService categoryService;
  @Autowired
  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }
  @GetMapping("/food-category")
  ResponseEntity<List<FoodCategory>> foodCategoryList(){
    return ResponseEntity.status(HttpStatus.OK).body(categoryService.foodCategoryList());
  }
  @GetMapping("/location-category")
  ResponseEntity<List<LocationCategory>> locationCategoryList(){
    return ResponseEntity.status(HttpStatus.OK).body(categoryService.locationCategoryList());
  }
  @GetMapping("/location-tag")
  ResponseEntity<List<LocationTag>> locationTagList(){
    return ResponseEntity.status(HttpStatus.OK).body(categoryService.locationTagList());
  }
  @GetMapping("/recommend-category")
  ResponseEntity<List<RecommendCategory>> recommendCategoryList(){
    return ResponseEntity.status(HttpStatus.OK).body(categoryService.recommendCategoryList());
  }
}
