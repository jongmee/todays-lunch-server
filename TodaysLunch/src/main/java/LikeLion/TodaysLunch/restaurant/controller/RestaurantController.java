package LikeLion.TodaysLunch.restaurant.controller;


import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.restaurant.dto.JudgeRestaurantCreateDto;
import LikeLion.TodaysLunch.restaurant.dto.JudgeRestaurantDto;
import LikeLion.TodaysLunch.restaurant.dto.RestaurantDto;
import LikeLion.TodaysLunch.restaurant.dto.RestaurantRecommendDto;
import LikeLion.TodaysLunch.restaurant.service.RestaurantService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

  static final String PAGE_VALUE = "0";
  static final String PAGE_SIZE = "100";
  static final String SORT = "rating";
  static final String ORDER = "descending";

  private final RestaurantService restaurantService;

  @Autowired
  public RestaurantController(RestaurantService restaurantService) {
    this.restaurantService = restaurantService;
  }

  @GetMapping("")
  public ResponseEntity<HashMap<String, Object>> allRestaurantList(
      @RequestParam(value = "food-category", required = false) String foodCategory,
      @RequestParam(value = "location-category", required = false) String locationCategory,
      @RequestParam(value = "location-tag", required = false) String locationTag,
      @RequestParam(value = "recommend-category-id", required = false) Long recommendCategoryId,
      @RequestParam(value = "keyword", required = false) String keyword,
      @RequestParam(defaultValue = PAGE_VALUE) int page,
      @RequestParam(defaultValue = PAGE_SIZE) int size,
      @RequestParam(defaultValue = SORT) String sort,
      @RequestParam(defaultValue = ORDER) String order,
      @AuthenticationPrincipal Member member) {
      return ResponseEntity.status(HttpStatus.OK).body(restaurantService.restaurantList(foodCategory, locationCategory,
        locationTag, recommendCategoryId, keyword, page, size, sort, order, member));
  }

  @GetMapping("/{restaurantId}")
  public ResponseEntity<RestaurantDto> detail(
      @PathVariable Long restaurantId,
      @AuthenticationPrincipal Member member) {
    return ResponseEntity.status(HttpStatus.OK).body(restaurantService.restaurantDetail(restaurantId, member));
  }

  @PostMapping("/judges")
  public ResponseEntity<Void> createJudge(
      @RequestPart(required = false) MultipartFile restaurantImage,
      @Valid @RequestPart JudgeRestaurantCreateDto createDto,
      @AuthenticationPrincipal Member member
  ) throws IOException {
    restaurantService.createJudgeRestaurant(createDto, restaurantImage, member);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/judges")
  public ResponseEntity<HashMap<String, Object>> AllJudgeRestaurantList(
      @RequestParam(value = "food-category", required = false) String foodCategory,
      @RequestParam(value = "location-category", required = false) String locationCategory,
      @RequestParam(value = "location-tag", required = false) String locationTag,
      @RequestParam(value = "recommend-category-id", required = false) Long recommendCategoryId,
      @RequestParam(defaultValue = PAGE_VALUE) int page,
      @RequestParam(defaultValue = PAGE_SIZE) int size,
      @RequestParam(defaultValue = SORT) String sort,
      @RequestParam(defaultValue = ORDER) String order,
      @RequestParam(value = "registrant-id", required = false) Long registrantId,
      @AuthenticationPrincipal Member member){
    return ResponseEntity.status(HttpStatus.OK).body(restaurantService.judgeRestaurantList(foodCategory, locationCategory, locationTag, recommendCategoryId, page, size, sort, order, registrantId, member));
  }

  @GetMapping("/judges/{restaurantId}")
  public ResponseEntity<JudgeRestaurantDto> judgeRestaurantDetail(
      @PathVariable Long restaurantId,
      @AuthenticationPrincipal Member member){
    return ResponseEntity.status(HttpStatus.OK).body(restaurantService.judgeRestaurantDetail(restaurantId, member));
  }

  @PostMapping("/judges/{restaurantId}/agree")
  public ResponseEntity<Void> addAgreement(
      @PathVariable Long restaurantId,
      @AuthenticationPrincipal Member member){
    restaurantService.addOrCancelAgreement(member, restaurantId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/recommendation")
  public ResponseEntity<List<RestaurantRecommendDto>> recommendation(@AuthenticationPrincipal Member member){
    return ResponseEntity.status(HttpStatus.OK).body(restaurantService.recommendation(member));
  }

  @PostMapping("/{restaurantId}/mystore")
  public ResponseEntity<Void> addMyStore(
      @PathVariable Long restaurantId,
      @AuthenticationPrincipal Member member){
    restaurantService.addMyStore(restaurantId, member);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/mystore")
  public ResponseEntity<HashMap<String, Object>> myStore(
      @RequestParam(defaultValue = PAGE_VALUE) int page,
      @RequestParam(defaultValue = PAGE_SIZE) int size,
      @AuthenticationPrincipal Member member){
    return ResponseEntity.status(HttpStatus.OK).body(restaurantService.myStoreList(page, size, member));
  }

  @GetMapping("/participate")
  public ResponseEntity<HashMap<String, Object>> participateRestaurantList (
      @AuthenticationPrincipal Member member,
      @RequestParam(defaultValue = PAGE_VALUE) int page,
      @RequestParam(defaultValue = PAGE_SIZE) int size){
    return ResponseEntity.status(HttpStatus.OK).body(restaurantService.participateRestaurantList(member, page, size));
  }

  @GetMapping("/contribute")
  public ResponseEntity<HashMap<String, Object>> contributeRestaurantList (
      @AuthenticationPrincipal Member member,
      @RequestParam(defaultValue = PAGE_VALUE) int page,
      @RequestParam(defaultValue = PAGE_SIZE) int size){
    return ResponseEntity.status(HttpStatus.OK).body(restaurantService.contributeRestaurantList(member, page, size));
  }

  @DeleteMapping("/{restaurantId}")
  public ResponseEntity<Void> deleteRestaurant(@PathVariable Long restaurantId){
    restaurantService.deleteRestaurant(restaurantId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
