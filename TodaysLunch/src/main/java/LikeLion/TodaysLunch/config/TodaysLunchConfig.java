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
    private final AgreementRepository agreementRepository;
    private final RecommendCategoryRepository recommendCategoryRepository;
    private final RestRecmdRelRepository restRecmdRelRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final RestaurantContributorRepository restaurantContributorRepository;

    @Autowired
    public TodaysLunchConfig(DataJpaRestaurantRepository restaurantRepository,
                             FoodCategoryRepository foodCategoryRepository,
                             LocationCategoryRepository locationCategoryRepository,
                             LocationTagRepository locationTagRepository,
                             MenuRepository menuRepository,
                             ReviewRepository reviewRepository, MemberRepository memberRepository,
                             ImageUrlRepository imageUrlRepository, SaleRepository saleRepository,
                            AgreementRepository agreementRepository,
                            RecommendCategoryRepository recommendCategoryRepository,
                            RestRecmdRelRepository restRecmdRelRepository,
                            ReviewLikeRepository reviewLikeRepository,
                            RestaurantContributorRepository restaurantContributorRepository) {
        this.restaurantRepository = restaurantRepository;
        this.foodCategoryRepository = foodCategoryRepository;
        this.locationCategoryRepository = locationCategoryRepository;
        this.locationTagRepository = locationTagRepository;
        this.menuRepository = menuRepository;
        this.reviewRepository = reviewRepository;
        this.memberRepository = memberRepository;
        this.imageUrlRepository = imageUrlRepository;
        this.saleRepository = saleRepository;
        this.agreementRepository = agreementRepository;
        this.recommendCategoryRepository = recommendCategoryRepository;
        this.restRecmdRelRepository = restRecmdRelRepository;
        this.reviewLikeRepository = reviewLikeRepository;
        this.restaurantContributorRepository = restaurantContributorRepository;
    }

    @Bean
    public RestaurantService restaurantService() {
        return new RestaurantService(restaurantRepository,
            foodCategoryRepository, locationTagRepository,
            locationCategoryRepository, imageUrlRepository,
            memberRepository, agreementRepository,
            recommendCategoryRepository, restRecmdRelRepository);
    }

    @Bean
    public MenuService menuService() {
        return new MenuService(menuRepository, imageUrlRepository, restaurantRepository, restaurantContributorRepository);
    }

    @Bean
    public ReviewService reviewService() {
        return new ReviewService(reviewRepository, restaurantRepository, reviewLikeRepository);
    }

    @Bean SaleService saleService(){
        return new SaleService(menuRepository, saleRepository);
    }

    @Bean CategoryService categoryService() { return new CategoryService(foodCategoryRepository, locationCategoryRepository, locationTagRepository, recommendCategoryRepository); }

}
