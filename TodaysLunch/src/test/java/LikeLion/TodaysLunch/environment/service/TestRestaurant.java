package LikeLion.TodaysLunch.environment.service;

import LikeLion.TodaysLunch.category.domain.FoodCategory;
import LikeLion.TodaysLunch.category.domain.LocationCategory;
import LikeLion.TodaysLunch.category.domain.LocationTag;
import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.menu.domain.Menu;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import LikeLion.TodaysLunch.restaurant.domain.RestaurantContributor;

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

  public TestRestaurant 심사맛집_등록하기(FoodCategory foodCategory,
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
    this.restaurant = environ.restaurantRepository().save(restaurant);
    return this;
  }

  public Menu 메뉴_등록하기(String name, Long price, Long salePrice, String saleExplain, Member member) {
    Menu menu = Menu.builder()
            .name(name)
            .price(price)
            .salePrice(salePrice)
            .saleExplain(saleExplain)
            .build();
    최저메뉴가격_등록하기(price, salePrice);
    맛집기여자_등록하기(member);
    menu.setRestaurant(this.restaurant);
    return environ.menuRepository().save(menu);
  }

  private void 최저메뉴가격_등록하기(Long price, Long salePrice) {
    Long lowestPrice = salePrice;
    if(lowestPrice == null) {
      lowestPrice = price;
    }
    this.restaurant.updateLowestPrice(lowestPrice);
    environ.restaurantRepository().save(this.restaurant);
  }

  private void 맛집기여자_등록하기(Member member){
    if(environ.restaurantContributorRepository().findByRestaurantAndMember(this.restaurant, member).isEmpty()){
      RestaurantContributor contributor = RestaurantContributor.builder()
              .member(member)
              .restaurant(this.restaurant)
              .build();
      environ.restaurantContributorRepository().save(contributor);
    }
  }

  public Restaurant getRestaurant() {
    return this.restaurant;
  }
}
