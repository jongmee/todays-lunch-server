package LikeLion.TodaysLunch.restaurant.service;

import LikeLion.TodaysLunch.category.domain.FoodCategory;
import LikeLion.TodaysLunch.category.domain.LocationCategory;
import LikeLion.TodaysLunch.category.domain.LocationTag;
import LikeLion.TodaysLunch.category.domain.RecommendCategory;
import LikeLion.TodaysLunch.exception.NotFoundException;
import LikeLion.TodaysLunch.external.S3UploadService;
import LikeLion.TodaysLunch.image.domain.ImageUrl;
import LikeLion.TodaysLunch.image.repository.ImageUrlRepository;
import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.restaurant.domain.Agreement;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import LikeLion.TodaysLunch.restaurant.domain.RestaurantRecommendCategoryRelation;
import LikeLion.TodaysLunch.restaurant.dto.request.JudgeRestaurantCreateDto;
import LikeLion.TodaysLunch.restaurant.dto.response.JudgeRestaurantDto;
import LikeLion.TodaysLunch.restaurant.dto.response.JudgeRestaurantListDto;
import LikeLion.TodaysLunch.restaurant.dto.response.common.RestaurantPageResponse;
import LikeLion.TodaysLunch.restaurant.repository.AgreementRepository;
import LikeLion.TodaysLunch.restaurant.repository.DataJpaRestaurantRepository;
import LikeLion.TodaysLunch.restaurant.repository.RestRecmdRelRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JudgeRestaurantService {

    private final DataJpaRestaurantRepository restaurantRepository;
    private final ImageUrlRepository imageUrlRepository;
    private final RestRecmdRelRepository restRecmdRelRepository;
    private final AgreementRepository agreementRepository;
    private final S3UploadService s3UploadService;
    private final RestaurantRelatedService restaurantRelatedService;

    @Transactional
    public void createJudgeRestaurant(JudgeRestaurantCreateDto createDto, MultipartFile restaurantImage, Member registrant) throws IOException {
        double latitude = createDto.getLatitude();
        double longitude = createDto.getLongitude();

        FoodCategory foodCategory = restaurantRelatedService.findFoodCategory(createDto.getFoodCategoryName());
        LocationTag locationTag = restaurantRelatedService.findNearestTag(latitude, longitude);
        LocationCategory locationCategory = restaurantRelatedService.findNearestCategory(latitude, longitude);
        Restaurant restaurant = createDto.toEntity(foodCategory, locationTag, locationCategory, registrant);

        if(restaurantImage != null && !restaurantImage.isEmpty()) {
            ImageUrl imageUrl = createRestaurantImage(restaurantImage, registrant);
            imageUrlRepository.save(imageUrl);
            restaurant.setImageUrl(imageUrl);
        }
        if("ROLE_ADMIN".equals(registrant.getRoles().get(0))) {
            restaurant.setJudgement(false);
        }
        restaurantRepository.save(restaurant);

        /* Todo: 한 번에 추천 카테고리 조회하기 & 메서드 분리하기 */
        List<Long> recommendCategoryIds= createDto.getRecommendCategoryIds();
        for(Long id: recommendCategoryIds){
            RecommendCategory recommendCategory = restaurantRelatedService.findRecommendCategory(id);
            RestaurantRecommendCategoryRelation relation = RestaurantRecommendCategoryRelation.builder()
                    .restaurant(restaurant)
                    .recommendCategory(recommendCategory)
                    .build();
            restRecmdRelRepository.save(relation);
            restaurant.addRecommendCategoryRelation(relation);
        }
    }

    /* Todo: s3에서 트랜잭션 처리하기 */
    private ImageUrl createRestaurantImage(MultipartFile restaurantImage, Member registrant) throws IOException {
        String originalName = restaurantImage.getOriginalFilename();
        String savedUrl = s3UploadService.upload(restaurantImage, "judge_restaurant");
        return ImageUrl.builder()
                .originalName(originalName)
                .imageUrl(savedUrl)
                .member(registrant)
                .build();
    }

    public RestaurantPageResponse judgeRestaurantList(
            String foodCategoryName, String locationCategoryName, String locationTagName, Long recommendCategoryId, Pageable pageable, Long registrantId, Member member) {
        Specification<Restaurant> spec = restaurantRelatedService.determineFilterCondition(
                foodCategoryName, locationCategoryName, locationTagName, recommendCategoryId, registrantId, null, true);

        Page<Restaurant> restaurantList = restaurantRepository.findAll(spec, pageable);
        List<JudgeRestaurantListDto> restaurantDtos = new ArrayList<>(restaurantList.getSize());
        for(Restaurant restaurant: restaurantList) {
            Boolean agreed = alreadyAgree(member, restaurant);
            restaurantDtos.add(JudgeRestaurantListDto.fromEntity(restaurant, agreed));
        }

        return RestaurantPageResponse.create(restaurantList.getTotalPages(), restaurantDtos);
    }

    public JudgeRestaurantDto judgeRestaurantDetail(Long id, Member member) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new NotFoundException("맛집"));
        boolean agreed = alreadyAgree(member, restaurant);
        return JudgeRestaurantDto.fromEntity(restaurant, agreed);
    }

    @Transactional
    public void addOrCancelAgreement(Member member, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new NotFoundException("맛집"));
        if(alreadyAgree(member, restaurant)) {
            Agreement agreement = agreementRepository.findByMemberAndRestaurant(member, restaurant).get();
            agreementRepository.delete(agreement);
            restaurant.decreaseAgreementCount();
        } else {
            Agreement agreement = Agreement.builder().member(member).restaurant(restaurant).build();
            agreementRepository.save(agreement);
            restaurant.increaseAgreementCount();
            restaurant.evaluateRestaurant();
        }
    }

    private boolean alreadyAgree(Member member, Restaurant restaurant) {
        return !agreementRepository.findByMemberAndRestaurant(member, restaurant).isEmpty();
    }
}
