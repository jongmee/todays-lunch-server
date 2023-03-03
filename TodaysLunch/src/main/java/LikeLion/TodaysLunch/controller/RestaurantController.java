package LikeLion.TodaysLunch.controller;


import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.dto.MemberDto;
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
  public ResponseEntity<Restaurant> createJudge(
      @RequestParam(required = false) MultipartFile restaurantImage,
      @RequestParam String address,
      @RequestParam Double latitude,
      @RequestParam Double longitude,
      @RequestParam String restaurantName,
      @RequestParam String foodCategoryName,
      @RequestParam String locationCategoryName,
      @RequestParam String locationTagName,
      @RequestParam(required = false) String introduction,
      @AuthenticationPrincipal Member member
  ) throws IOException {
    try {
      memberService.getAuthenticatedMember(member);
      Restaurant restaurant = restaurantService.createJudgeRestaurant(latitude, longitude, address, restaurantName,
          foodCategoryName, locationCategoryName, locationTagName, introduction, restaurantImage, member);
      return ResponseEntity.status(HttpStatus.OK).body(restaurant);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Restaurant());
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Restaurant());
    }
  }

  @GetMapping("/test")
  public String test(){
    return "success";
  }

  @GetMapping("/judges")
  public ResponseEntity<HashMap<String, Object>> AllJudgeRestaurantList(Pageable pageable){
  Page<Restaurant> restaurants = restaurantService.judgeRestaurantList(pageable);
  HashMap<String, Object> responseMap = new HashMap<>();
  responseMap.put("data", restaurants.getContent());
  responseMap.put("totalPages", restaurants.getTotalPages());
  return ResponseEntity.status(HttpStatus.OK).body(responseMap);
  }

  @PostMapping("/judges/{restaurantId}/agree")
  public ResponseEntity<String> addAgreement(@PathVariable Long restaurantId, @AuthenticationPrincipal Member member){
    try{
      memberService.getAuthenticatedMember(member);
      return ResponseEntity.status(HttpStatus.OK).body(restaurantService.addOrCancelAgreement(member, restaurantId));
    } catch (IllegalArgumentException e){
      if (e.getMessage().equals("인가 되지 않은 사용자입니다."))
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
      else if (e.getMessage().equals("맛집 심사 동의를 위한 대상 맛집 찾기 실패!"))
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("심사 맛집 동의 실패!");
  }

  @GetMapping("/judges/{restaurantId}/agree")
  public ResponseEntity<String> isAlreadyAgree(@PathVariable Long restaurantId, @AuthenticationPrincipal Member member){
    try {
      memberService.getAuthenticatedMember(member);
      return ResponseEntity.status(HttpStatus.OK).body(restaurantService.isAlreadyAgree(member, restaurantId));
    } catch (IllegalArgumentException e){
      if (e.getMessage().equals("인가 되지 않은 사용자입니다."))
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
      else if (e.getMessage().equals("맛집 심사 동의를 위한 대상 맛집 찾기 실패!"))
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("심사 맛집 동의 조회 실패!");
  }

  // 임시로 유저의 ID 값을 경로 변수로 받기
  @GetMapping("/recommendation/{userId}")
  public ResponseEntity<List<Restaurant>> recommendation(@PathVariable Long userId){
    List<Restaurant> restaurants = restaurantService.recommendation(userId);
    return ResponseEntity.status(HttpStatus.OK).body(restaurants);
  }
}
