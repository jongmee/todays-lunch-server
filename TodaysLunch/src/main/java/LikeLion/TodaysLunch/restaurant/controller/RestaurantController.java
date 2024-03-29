package LikeLion.TodaysLunch.restaurant.controller;


import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.restaurant.dto.request.JudgeRestaurantCreateDto;
import LikeLion.TodaysLunch.restaurant.dto.response.ContributeRestaurantDto;
import LikeLion.TodaysLunch.restaurant.dto.response.JudgeRestaurantDto;
import LikeLion.TodaysLunch.restaurant.dto.response.ParticipateRestaurantDto;
import LikeLion.TodaysLunch.restaurant.dto.response.RestaurantDto;
import LikeLion.TodaysLunch.restaurant.dto.response.common.RestaurantPageResponse;
import LikeLion.TodaysLunch.restaurant.dto.response.RestaurantRecommendDto;
import LikeLion.TodaysLunch.restaurant.service.JudgeRestaurantService;
import LikeLion.TodaysLunch.restaurant.service.RestaurantService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

  private static final String PAGE_VALUE = "0";
  private static final String PAGE_SIZE = "100";
  private static final String SORT = "rating";
  private static final String ASCENDING = "ascending";
  private static final String DESCENDING = "descending";

  private final RestaurantService restaurantService;
  private final JudgeRestaurantService judgeRestaurantService;

  @GetMapping("")
  public ResponseEntity<RestaurantPageResponse> allRestaurantList(
      @RequestParam(value = "food-category", required = false) String foodCategory,
      @RequestParam(value = "location-category", required = false) String locationCategory,
      @RequestParam(value = "location-tag", required = false) String locationTag,
      @RequestParam(value = "recommend-category-id", required = false) Long recommendCategoryId,
      @RequestParam(value = "keyword", required = false) String keyword,
      @RequestParam(defaultValue = PAGE_VALUE) int page,
      @RequestParam(defaultValue = PAGE_SIZE) int size,
      @RequestParam(defaultValue = SORT) String sort,
      @RequestParam(defaultValue = DESCENDING) String order,
      @AuthenticationPrincipal Member member) {
    Pageable pageable = determinePageAndSort(page, size, sort, order);
    return ResponseEntity.status(HttpStatus.OK).body(restaurantService.restaurantList(foodCategory, locationCategory,
        locationTag, recommendCategoryId, keyword, pageable, member));
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
    judgeRestaurantService.createJudgeRestaurant(createDto, restaurantImage, member);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/judges")
  public ResponseEntity<RestaurantPageResponse> AllJudgeRestaurantList(
      @RequestParam(value = "food-category", required = false) String foodCategory,
      @RequestParam(value = "location-category", required = false) String locationCategory,
      @RequestParam(value = "location-tag", required = false) String locationTag,
      @RequestParam(value = "recommend-category-id", required = false) Long recommendCategoryId,
      @RequestParam(defaultValue = PAGE_VALUE) int page,
      @RequestParam(defaultValue = PAGE_SIZE) int size,
      @RequestParam(defaultValue = SORT) String sort,
      @RequestParam(defaultValue = DESCENDING) String order,
      @RequestParam(value = "registrant-id", required = false) Long registrantId,
      @AuthenticationPrincipal Member member){
    Pageable pageable = determinePageAndSort(page, size, sort, order);
    return ResponseEntity.status(HttpStatus.OK).body(judgeRestaurantService.judgeRestaurantList(foodCategory, locationCategory, locationTag, recommendCategoryId, pageable, registrantId, member));
  }

  @GetMapping("/judges/{restaurantId}")
  public ResponseEntity<JudgeRestaurantDto> judgeRestaurantDetail(
      @PathVariable Long restaurantId,
      @AuthenticationPrincipal Member member){
    return ResponseEntity.status(HttpStatus.OK).body(judgeRestaurantService.judgeRestaurantDetail(restaurantId, member));
  }

  @PostMapping("/judges/{restaurantId}/agree")
  public ResponseEntity<Void> addAgreement(
      @PathVariable Long restaurantId,
      @AuthenticationPrincipal Member member){
    judgeRestaurantService.addOrCancelAgreement(member, restaurantId);
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
  public ResponseEntity<RestaurantPageResponse> myStore(
      @RequestParam(defaultValue = PAGE_VALUE) int page,
      @RequestParam(defaultValue = PAGE_SIZE) int size,
      @AuthenticationPrincipal Member member){
    return ResponseEntity.status(HttpStatus.OK).body(restaurantService.myStoreList(PageRequest.of(page, size), member));
  }

  @GetMapping("/participate")
  public ResponseEntity<ParticipateRestaurantDto> participateRestaurantList (
      @AuthenticationPrincipal Member member,
      @RequestParam(defaultValue = PAGE_VALUE) int page,
      @RequestParam(defaultValue = PAGE_SIZE) int size){
    return ResponseEntity.status(HttpStatus.OK).body(restaurantService.participateRestaurantList(PageRequest.of(page, size), member));
  }

  @GetMapping("/contribute")
  public ResponseEntity<ContributeRestaurantDto> contributeRestaurantList (
      @AuthenticationPrincipal Member member,
      @RequestParam(defaultValue = PAGE_VALUE) int page,
      @RequestParam(defaultValue = PAGE_SIZE) int size){
    return ResponseEntity.status(HttpStatus.OK).body(restaurantService.contributeRestaurantList(PageRequest.of(page, size), member));
  }

  @DeleteMapping("/{restaurantId}")
  public ResponseEntity<Void> deleteRestaurant(@PathVariable Long restaurantId){
    restaurantService.deleteRestaurant(restaurantId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  private Pageable determinePageAndSort(int page, int size, String sort, String order){
    if(order.equals(ASCENDING)){
      return PageRequest.of(page, size, Sort.by(sort).ascending());
    }
    return PageRequest.of(page, size, Sort.by(sort).descending());
  }
}
