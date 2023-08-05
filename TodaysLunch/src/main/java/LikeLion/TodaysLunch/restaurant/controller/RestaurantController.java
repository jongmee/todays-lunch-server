package LikeLion.TodaysLunch.restaurant.controller;


import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.restaurant.dto.JudgeRestaurantCreateDto;
import LikeLion.TodaysLunch.restaurant.dto.JudgeRestaurantDto;
import LikeLion.TodaysLunch.restaurant.dto.JudgeRestaurantListDto;
import LikeLion.TodaysLunch.restaurant.dto.RestaurantDto;
import LikeLion.TodaysLunch.restaurant.dto.RestaurantListDto;
import LikeLion.TodaysLunch.restaurant.service.RestaurantService;
import LikeLion.TodaysLunch.member.service.MemberService;
import java.io.IOException;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
  private final MemberService memberService;

  @Autowired
  public RestaurantController(RestaurantService restaurantService, MemberService memberService) {
    this.restaurantService = restaurantService;
    this.memberService = memberService;
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
    Page<RestaurantListDto> restaurants = restaurantService.restaurantList(foodCategory, locationCategory,
        locationTag, recommendCategoryId, keyword, page, size, sort, order, member);
    HashMap<String, Object> responseMap = new HashMap<>();
    responseMap.put("data", restaurants.getContent());
    responseMap.put("totalPages", restaurants.getTotalPages());
    return ResponseEntity.status(HttpStatus.OK).body(responseMap);
  }

  @GetMapping("/{restaurantId}")
  public ResponseEntity<RestaurantDto> detail(@PathVariable Long restaurantId) {
    RestaurantDto restaurantDto = restaurantService.restaurantDetail(restaurantId);
    return ResponseEntity.status(HttpStatus.OK).body(restaurantDto);
  }

  @PostMapping("/judges")
  public ResponseEntity<Void> createJudge(
      @RequestPart(required = false) MultipartFile restaurantImage,
      @RequestPart JudgeRestaurantCreateDto createDto,
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
    Page<JudgeRestaurantListDto> restaurants = restaurantService.judgeRestaurantList(foodCategory, locationCategory, locationTag, recommendCategoryId, page, size, sort, order, registrantId, member);
    HashMap<String, Object> responseMap = new HashMap<>();
    responseMap.put("data", restaurants.getContent());
    responseMap.put("totalPages", restaurants.getTotalPages());
    return ResponseEntity.status(HttpStatus.OK).body(responseMap);
  }

  @GetMapping("/judges/{restaurantId}")
  public ResponseEntity<JudgeRestaurantDto> judgeRestaurantDetail(@PathVariable Long restaurantId){
    return ResponseEntity.status(HttpStatus.OK).body(restaurantService.judgeRestaurantDetail(restaurantId));
  }

  @PostMapping("/judges/{restaurantId}/agree")
  public ResponseEntity<Void> addAgreement(@PathVariable Long restaurantId, @AuthenticationPrincipal Member member){
    restaurantService.addOrCancelAgreement(member, restaurantId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/judges/{restaurantId}/agree")
  public ResponseEntity<String> isAlreadyAgree(@PathVariable Long restaurantId, @AuthenticationPrincipal Member member){
    return ResponseEntity.status(HttpStatus.OK).body(restaurantService.isAlreadyAgree(member, restaurantId));
  }

////   임시로 유저의 ID 값을 경로 변수로 받기
//  @GetMapping("/recommendation/{userId}")
//  public ResponseEntity<List<Restaurant>> recommendation(@PathVariable Long userId){
//    List<Restaurant> restaurants = restaurantService.recommendation(userId);
//    return ResponseEntity.status(HttpStatus.OK).body(restaurants);
//  }

  @PostMapping("/{restaurantId}/mystore")
  public ResponseEntity<Void> addMyStore(@PathVariable Long restaurantId, @AuthenticationPrincipal Member member){
    restaurantService.addMyStore(restaurantId, member);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/{restaurantId}/mystore")
  public ResponseEntity<String> isAlreadyMyStore(@PathVariable Long restaurantId, @AuthenticationPrincipal Member member){
    return ResponseEntity.status(HttpStatus.OK).body(restaurantService.isAlreadyMyStore(member, restaurantId));
  }

  @GetMapping("/mystore")
  public ResponseEntity<HashMap<String, Object>> myStore(
      @RequestParam(defaultValue = PAGE_VALUE) int page,
      @RequestParam(defaultValue = PAGE_SIZE) int size,
      @AuthenticationPrincipal Member member){
    return ResponseEntity.status(HttpStatus.OK).body(restaurantService.myStoreList(page, size, member));
  }

  @GetMapping("/participate-restaurant")
  public ResponseEntity<HashMap<String, Object>> participateRestaurantList (@AuthenticationPrincipal Member member){
    return ResponseEntity.status(HttpStatus.OK).body(restaurantService.participateRestaurantList(member));
  }
}
