package LikeLion.TodaysLunch.skeleton;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.LocationCategory;
import LikeLion.TodaysLunch.domain.LocationTag;
import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.Menu;
import LikeLion.TodaysLunch.domain.Restaurant;

public class TestRestaurant {
  private final TestRestaurantEnviron environ;
  private Restaurant restaurant;
  public TestRestaurant(TestRestaurantEnviron testRestaurantEnviron) {
    this.environ = testRestaurantEnviron;
  }
  public TestRestaurant 정식맛집_등록하기(FoodCategory foodCategory,
      LocationCategory locationCategory, LocationTag locationTag,
      String address, String restaurantName, String introduction,
      Double longitude, Double latitude, Member registrant) {
    Restaurant restaurant = Restaurant.builder()
        .foodCategory(foodCategory)
        .locationCategory(locationCategory)
        .locationTag(locationTag)
        .address(address)
        .restaurantName(restaurantName)
        .introduction(introduction)
        .longitude(longitude)
        .latitude(latitude)
        .registrant(registrant)
        .build();
    restaurant.setJudgement(false);
    this.restaurant = environ.restaurantRepository().save(restaurant);
    return this;
  }
  public TestRestaurant 메뉴_등록하기(String name, Long price) {
    Menu menu = Menu.builder().name(name).price(price).build();
    Long originalPrice = this.restaurant.getLowestPrice();
    if (originalPrice == null || originalPrice > price) this.restaurant.setLowestPrice(price);
    environ.restaurantRepository().save(this.restaurant);
    menu.setRestaurant(this.restaurant);
    environ.menuRepository().save(menu);
    return this;
  }
  public Restaurant getRestaurant() { return this.restaurant; }
}
