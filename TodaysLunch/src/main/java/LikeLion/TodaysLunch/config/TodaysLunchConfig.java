package LikeLion.TodaysLunch.config;

import LikeLion.TodaysLunch.repository.*;
import LikeLion.TodaysLunch.service.*;
import LikeLion.TodaysLunch.service.login.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TodaysLunchConfig {
    private final DataJpaRestaurantRepository restaurantRepository;
    private final FoodCategoryRepository foodCategoryRepository;
    private final LocationCategoryRepository locationCategoryRepository;
    private final LocationTagRepository locationTagRepository;
    private final MenuRepository menuRepository;
    private final ReviewRepository reviewRepository;

    private final MemberRepository memberRepository;

    private final ImageUrlRepository imageUrlRepository;
    private final SaleRepository saleRepository;

    @Autowired
    public TodaysLunchConfig(DataJpaRestaurantRepository restaurantRepository,
                             FoodCategoryRepository foodCategoryRepository,
                             LocationCategoryRepository locationCategoryRepository,
                             LocationTagRepository locationTagRepository,
                             MenuRepository menuRepository,
                             ReviewRepository reviewRepository, MemberRepository memberRepository,
                             ImageUrlRepository imageUrlRepository, SaleRepository saleRepository) {
        this.restaurantRepository = restaurantRepository;
        this.foodCategoryRepository = foodCategoryRepository;
        this.locationCategoryRepository = locationCategoryRepository;
        this.locationTagRepository = locationTagRepository;
        this.menuRepository = menuRepository;
        this.reviewRepository = reviewRepository;
        this.memberRepository = memberRepository;
        this.imageUrlRepository = imageUrlRepository;
        this.saleRepository = saleRepository;
    }

    @Bean
    public RestaurantService restaurantService() {
        return new RestaurantService(restaurantRepository,
                foodCategoryRepository, locationTagRepository,
                locationCategoryRepository, imageUrlRepository);
    }


    @Bean
    public MenuService menuService() {
        return new MenuService(menuRepository, saleRepository);
    }

    @Bean
    public ReviewService reviewService() {
        return new ReviewService(reviewRepository, restaurantRepository);
    }

}
