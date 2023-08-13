package LikeLion.TodaysLunch.category.controller;

import LikeLion.TodaysLunch.category.domain.LocationTag;
import LikeLion.TodaysLunch.category.dto.FoodCategoryDto;
import LikeLion.TodaysLunch.category.dto.LocationCategoryDto;
import LikeLion.TodaysLunch.category.dto.LocationTagDto;
import LikeLion.TodaysLunch.category.dto.RecommendCategoryDto;
import LikeLion.TodaysLunch.category.service.CategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {

  private final CategoryService categoryService;

  @Autowired
  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping("/food-category")
  ResponseEntity<List<FoodCategoryDto>> foodCategoryList(){
    return ResponseEntity.status(HttpStatus.OK).body(categoryService.foodCategoryList());
  }

  @GetMapping("/location-category")
  ResponseEntity<List<LocationCategoryDto>> locationCategoryList(){
    return ResponseEntity.status(HttpStatus.OK).body(categoryService.locationCategoryList());
  }

  @GetMapping("/location-tag")
  ResponseEntity<List<LocationTagDto>> locationTagList(){
    return ResponseEntity.status(HttpStatus.OK).body(categoryService.locationTagList());
  }

  @GetMapping("/recommend-category")
  ResponseEntity<List<RecommendCategoryDto.CategoryList>> recommendCategoryList(){
    return ResponseEntity.status(HttpStatus.OK).body(categoryService.recommendCategoryList());
  }

  @PatchMapping("/restaurants/{restaurantId}/recommend-category")
  ResponseEntity<Void> recommendCategoryEdit(
      @PathVariable Long restaurantId,
      @RequestBody RecommendCategoryDto.Edit editDto){
    categoryService.recommendCategoryEdit(restaurantId, editDto);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping("/food-category")
  ResponseEntity<Void> createFoodCategory(@RequestParam String foodCategoryName){
    categoryService.createFoodCategory(foodCategoryName);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping("/location-category")
  ResponseEntity<Void> createLocationCategory(@RequestBody LocationCategoryDto locationCategoryDto){
    categoryService.createLocationCategory(locationCategoryDto);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping("/location-tag")
  ResponseEntity<Void> createLocationTag(@RequestBody LocationTagDto locationTagDto){
    categoryService.createLocationTag(locationTagDto);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping("/recommend-category")
  ResponseEntity<Void> createRecommendCategory(@RequestBody RecommendCategoryDto.CategoryList categoryDto){
    categoryService.createRecommendCategory(categoryDto);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PatchMapping("/food-category/{foodCategoryId}")
  ResponseEntity<Void> updateFoodCategory(
      @PathVariable Long foodCategoryId,
      @RequestParam String foodCategoryName){
    categoryService.updateFoodCategory(foodCategoryId, foodCategoryName);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PatchMapping("/location-category/{locationCategoryId}")
  ResponseEntity<Void> updateLocationCategory(
      @PathVariable Long locationCategoryId,
      @RequestParam LocationCategoryDto locationCategoryDto){
    categoryService.updateLocationCategory(locationCategoryId, locationCategoryDto);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PatchMapping("/location-tag/{locationTagId}")
  ResponseEntity<Void> updateLocationTag(
      @PathVariable Long locationTagId,
      @RequestParam LocationTagDto locationTagDto){
    categoryService.updateLocationTag(locationTagId, locationTagDto);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PatchMapping("/recommend-category/{recommendCategoryId}")
  ResponseEntity<Void> updateRecommendCategory(
      @PathVariable Long recommendCategoryId,
      @RequestParam RecommendCategoryDto.CategoryList categoryDto){
    categoryService.updateRecommendCategory(recommendCategoryId, categoryDto);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping("/food-category/{foodCategoryId}")
  ResponseEntity<Void> deleteFoodCategory(@PathVariable Long foodCategoryId){
    categoryService.deleteFoodCategory(foodCategoryId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping("/location-category/{locationCategoryId}")
  ResponseEntity<Void> deleteLocationCategory(@PathVariable Long locationCategoryId){
    categoryService.deleteLocationCategory(locationCategoryId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping("/location-tag/{locationTagId}")
  ResponseEntity<Void> deleteLocationTag(@PathVariable Long locationTagId){
    categoryService.deleteLocationTag(locationTagId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping("/recommend-category/{recommendCategoryId}")
  ResponseEntity<Void> deleteRecommendCategory(@PathVariable Long recommendCategoryId){
    categoryService.deleteRecommendCategory(recommendCategoryId);;
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}