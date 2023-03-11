package LikeLion.TodaysLunch.controller;


import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.dto.JudgeRestaurantCreateDto;
import LikeLion.TodaysLunch.dto.JudgeRestaurantDto;
import LikeLion.TodaysLunch.dto.JudgeRestaurantListDto;
import LikeLion.TodaysLunch.dto.MemberDto;
import LikeLion.TodaysLunch.exception.ErrorResponse;
import LikeLion.TodaysLunch.service.RestaurantService;
import LikeLion.TodaysLunch.service.login.MemberService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
      @RequestParam(value = "keyword", required = false) String keyword,
      @RequestParam(defaultValue = PAGE_VALUE) int page,
      @RequestParam(defaultValue = PAGE_SIZE) int size,
      @RequestParam(defaultValue = SORT) String sort,
      @RequestParam(defaultValue = ORDER) String order) {
    Page<Restaurant> restaurants = restaurantService.restaurantList(foodCategory, locationCategory,
        locationTag, keyword, page, size, sort, order);
    HashMap<String, Object> responseMap = new HashMap<>();
    responseMap.put("data", restaurants.getContent());
    responseMap.put("totalPages", restaurants.getTotalPages());
    return ResponseEntity.status(HttpStatus.OK).body(responseMap);
  }

  @GetMapping("/{restaurantId}")
  public ResponseEntity<Restaurant> detail(@PathVariable Long restaurantId) {
    Restaurant restaurant = restaurantService.restaurantDetail(restaurantId);
    return ResponseEntity.status(HttpStatus.OK).body(restaurant);
  }

  @PostMapping("/judges")
  public ResponseEntity<Object> createJudge(
      @RequestPart(required = false) MultipartFile restaurantImage,
      @RequestPart JudgeRestaurantCreateDto createDto,
      @AuthenticationPrincipal Member member
  ) throws IOException {
    memberService.getAuthenticatedMember(member);
    Restaurant restaurant = restaurantService.createJudgeRestaurant(createDto, restaurantImage, member);
    return ResponseEntity.status(HttpStatus.OK).body(restaurant);
  }

  @GetMapping("/judges")
  public ResponseEntity<HashMap<String, Object>> AllJudgeRestaurantList(
      @RequestParam(value = "food-category", required = false) String foodCategory,
      @RequestParam(value = "location-category", required = false) String locationCategory,
      @RequestParam(value = "location-tag", required = false) String locationTag,
      @RequestParam(defaultValue = PAGE_VALUE) int page,
      @RequestParam(defaultValue = PAGE_SIZE) int size,
      @RequestParam(defaultValue = SORT) String sort,
      @RequestParam(defaultValue = ORDER) String order){
    Page<JudgeRestaurantListDto> restaurants = restaurantService.judgeRestaurantList(foodCategory, locationCategory, locationTag, page, size, sort, order);
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
  public ResponseEntity<String> addAgreement(@PathVariable Long restaurantId, @AuthenticationPrincipal Member member){
    memberService.getAuthenticatedMember(member);
    return ResponseEntity.status(HttpStatus.OK).body(restaurantService.addOrCancelAgreement(member, restaurantId));
  }

  @GetMapping("/judges/{restaurantId}/agree")
  public ResponseEntity<String> isAlreadyAgree(@PathVariable Long restaurantId, @AuthenticationPrincipal Member member){
    memberService.getAuthenticatedMember(member);
    return ResponseEntity.status(HttpStatus.OK).body(restaurantService.isAlreadyAgree(member, restaurantId));

  }

  // 임시로 유저의 ID 값을 경로 변수로 받기
  @GetMapping("/recommendation/{userId}")
  public ResponseEntity<List<Restaurant>> recommendation(@PathVariable Long userId){
    List<Restaurant> restaurants = restaurantService.recommendation(userId);
    return ResponseEntity.status(HttpStatus.OK).body(restaurants);
  }
}
