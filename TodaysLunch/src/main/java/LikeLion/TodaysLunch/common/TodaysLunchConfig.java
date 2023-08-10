package LikeLion.TodaysLunch.common;

import LikeLion.TodaysLunch.category.repository.FoodCategoryRepository;
import LikeLion.TodaysLunch.category.repository.LocationCategoryRepository;
import LikeLion.TodaysLunch.category.repository.LocationTagRepository;
import LikeLion.TodaysLunch.category.repository.RecommendCategoryRepository;
import LikeLion.TodaysLunch.category.service.CategoryService;
import LikeLion.TodaysLunch.customized.repository.MemberFoodCategoryRepository;
import LikeLion.TodaysLunch.customized.repository.MemberLocationCategoryRepository;
import LikeLion.TodaysLunch.customized.repository.MyStoreRepository;
import LikeLion.TodaysLunch.image.repository.ImageUrlRepository;
import LikeLion.TodaysLunch.image.repository.MenuImageRepository;
import LikeLion.TodaysLunch.member.repository.MemberRepository;
import LikeLion.TodaysLunch.menu.repository.MenuRepository;
import LikeLion.TodaysLunch.menu.service.MenuService;
import LikeLion.TodaysLunch.restaurant.repository.AgreementRepository;
import LikeLion.TodaysLunch.restaurant.repository.DataJpaRestaurantRepository;
import LikeLion.TodaysLunch.restaurant.repository.RestRecmdRelRepository;
import LikeLion.TodaysLunch.restaurant.repository.RestaurantContributorRepository;
import LikeLion.TodaysLunch.restaurant.service.RestaurantService;
import LikeLion.TodaysLunch.review.repository.ReviewLikeRepository;
import LikeLion.TodaysLunch.review.repository.ReviewRepository;
import LikeLion.TodaysLunch.review.service.ReviewService;
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
    private final AgreementRepository agreementRepository;
    private final RecommendCategoryRepository recommendCategoryRepository;
    private final RestRecmdRelRepository restRecmdRelRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final RestaurantContributorRepository restaurantContributorRepository;
    private final MyStoreRepository myStoreRepository;
    private final MemberFoodCategoryRepository memberFoodCategoryRepository;
    private final MemberLocationCategoryRepository memberLocationCategoryRepository;
    private final MenuImageRepository menuImageRepository;

    @Autowired
    public TodaysLunchConfig(DataJpaRestaurantRepository restaurantRepository,
                             FoodCategoryRepository foodCategoryRepository,
                             LocationCategoryRepository locationCategoryRepository,
                             LocationTagRepository locationTagRepository,
                             MenuRepository menuRepository,
                             ReviewRepository reviewRepository, MemberRepository memberRepository,
                             ImageUrlRepository imageUrlRepository,
                            AgreementRepository agreementRepository,
                            RecommendCategoryRepository recommendCategoryRepository,
                            RestRecmdRelRepository restRecmdRelRepository,
                            ReviewLikeRepository reviewLikeRepository,
                            RestaurantContributorRepository restaurantContributorRepository,
                            MyStoreRepository myStoreRepository,
                            MemberFoodCategoryRepository memberFoodCategoryRepository,
                            MemberLocationCategoryRepository memberLocationCategoryRepository,
                            MenuImageRepository menuImageRepository) {
        this.restaurantRepository = restaurantRepository;
        this.foodCategoryRepository = foodCategoryRepository;
        this.locationCategoryRepository = locationCategoryRepository;
        this.locationTagRepository = locationTagRepository;
        this.menuRepository = menuRepository;
        this.reviewRepository = reviewRepository;
        this.memberRepository = memberRepository;
        this.imageUrlRepository = imageUrlRepository;
        this.agreementRepository = agreementRepository;
        this.recommendCategoryRepository = recommendCategoryRepository;
        this.restRecmdRelRepository = restRecmdRelRepository;
        this.reviewLikeRepository = reviewLikeRepository;
        this.restaurantContributorRepository = restaurantContributorRepository;
        this.myStoreRepository = myStoreRepository;
        this.memberFoodCategoryRepository = memberFoodCategoryRepository;
        this.memberLocationCategoryRepository = memberLocationCategoryRepository;
        this.menuImageRepository = menuImageRepository;
    }

    @Bean
    public RestaurantService restaurantService() {
        return new RestaurantService(restaurantRepository,
            foodCategoryRepository, locationTagRepository,
            locationCategoryRepository, imageUrlRepository,
            memberRepository, agreementRepository,
            recommendCategoryRepository, restRecmdRelRepository,
            restaurantContributorRepository, myStoreRepository,
            memberLocationCategoryRepository);
    }

    @Bean
    public MenuService menuService() {
        return new MenuService(menuRepository,
            imageUrlRepository, restaurantRepository,
            restaurantContributorRepository, menuImageRepository);
    }

    @Bean
    public ReviewService reviewService() {
        return new ReviewService(memberRepository, reviewRepository, restaurantRepository, reviewLikeRepository);
    }

    @Bean
    CategoryService categoryService() {
        return new CategoryService(foodCategoryRepository,
            locationCategoryRepository, locationTagRepository,
            recommendCategoryRepository, restRecmdRelRepository,
            restaurantRepository);
    }

}
