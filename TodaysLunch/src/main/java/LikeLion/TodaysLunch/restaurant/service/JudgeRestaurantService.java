package LikeLion.TodaysLunch.restaurant.service;

import static LikeLion.TodaysLunch.restaurant.repository.SpecificationBuilder.buildSpecification;

import LikeLion.TodaysLunch.category.domain.FoodCategory;
import LikeLion.TodaysLunch.category.domain.LocationCategory;
import LikeLion.TodaysLunch.category.domain.LocationTag;
import LikeLion.TodaysLunch.category.domain.RecommendCategory;
import LikeLion.TodaysLunch.category.repository.FoodCategoryRepository;
import LikeLion.TodaysLunch.category.repository.LocationCategoryRepository;
import LikeLion.TodaysLunch.category.repository.LocationTagRepository;
import LikeLion.TodaysLunch.category.repository.RecommendCategoryRepository;
import LikeLion.TodaysLunch.exception.NotFoundException;
import LikeLion.TodaysLunch.external.S3UploadService;
import LikeLion.TodaysLunch.image.domain.ImageUrl;
import LikeLion.TodaysLunch.image.repository.ImageUrlRepository;
import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.member.repository.MemberRepository;
import LikeLion.TodaysLunch.restaurant.domain.Agreement;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import LikeLion.TodaysLunch.restaurant.domain.RestaurantRecommendCategoryRelation;
import LikeLion.TodaysLunch.restaurant.dto.JudgeRestaurantCreateDto;
import LikeLion.TodaysLunch.restaurant.dto.JudgeRestaurantDto;
import LikeLion.TodaysLunch.restaurant.dto.JudgeRestaurantListDto;
import LikeLion.TodaysLunch.restaurant.dto.RestaurantPageResponse;
import LikeLion.TodaysLunch.restaurant.repository.AgreementRepository;
import LikeLion.TodaysLunch.restaurant.repository.DataJpaRestaurantRepository;
import LikeLion.TodaysLunch.restaurant.repository.RestRecmdRelRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JudgeRestaurantService {

    private static final double MAX_DIFFERENCE = 10000d;

    private final DataJpaRestaurantRepository restaurantRepository;
    private final FoodCategoryRepository foodCategoryRepository;
    private final LocationCategoryRepository locationCategoryRepository;
    private final LocationTagRepository locationTagRepository;
    private final ImageUrlRepository imageUrlRepository;
    private final MemberRepository memberRepository;
    private final RecommendCategoryRepository recommendCategoryRepository;
    private final RestRecmdRelRepository restRecmdRelRepository;
    private final AgreementRepository agreementRepository;
    private S3UploadService s3UploadService;

    @Transactional
    public void createJudgeRestaurant(JudgeRestaurantCreateDto createDto, MultipartFile restaurantImage, Member registrant) throws IOException {
        double latitude = createDto.getLatitude();
        double longitude = createDto.getLongitude();

        FoodCategory foodCategory = foodCategoryRepository.findByName(createDto.getFoodCategoryName()).orElseThrow(() -> new NotFoundException("음식 카테고리"));
        LocationTag locationTag = findNearestTag(latitude, longitude);
        LocationCategory locationCategory = findNearestCategory(latitude, longitude);
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
            RecommendCategory recommendCategory = recommendCategoryRepository.findById(id).orElseThrow(() -> new NotFoundException("추천 카테고리"));
            RestaurantRecommendCategoryRelation relation = RestaurantRecommendCategoryRelation.builder()
                    .restaurant(restaurant)
                    .recommendCategory(recommendCategory)
                    .build();
            restRecmdRelRepository.save(relation);
            restaurant.addRecommendCategoryRelation(relation);
        }
    }

    /* Todo: 위치 태그와 위치 카테고리의 중복되는 필드들 어떻게 처리할지 고민하기 */
    private LocationTag findNearestTag(double latitude, double longitude) {
        List<LocationTag> locationTagList = locationTagRepository.findAll();
        double minDistance = MAX_DIFFERENCE;
        LocationTag locationTag = null;
        for(LocationTag tag: locationTagList){
            double distance = tag.getDistance(latitude, longitude);
            if(minDistance > distance) {
                minDistance = distance;
                locationTag = tag;
            }
        }
        return locationTag;
    }

    private LocationCategory findNearestCategory(double latitude, double longitude) {
        List<LocationCategory> locationCategoryList = locationCategoryRepository.findAll();
        double minDistance = MAX_DIFFERENCE;
        LocationCategory locationCategory = null;
        for(LocationCategory category: locationCategoryList){
            double distance = category.getDistance(latitude, longitude);
            if(minDistance > distance) {
                minDistance = distance;
                locationCategory = category;
            }
        }
        return locationCategory;
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
            String foodCategoryName, String locationCategoryName, String locationTagName, Long recommendCategoryId,
            int page, int size, String sort, String order, Long registrantId, Member member) {
        Pageable pageable = determineSort(page, size, sort, order);
        Specification<Restaurant> spec = determineFilterCondition(foodCategoryName, locationCategoryName, locationTagName, recommendCategoryId, registrantId);

        Page<Restaurant> restaurantList = restaurantRepository.findAll(spec, pageable);
        List<JudgeRestaurantListDto> restaurantDtos = new ArrayList<>(restaurantList.getSize());
        for(Restaurant restaurant: restaurantList) {
            Boolean agreed = alreadyAgree(member, restaurant);
            restaurantDtos.add(JudgeRestaurantListDto.fromEntity(restaurant, agreed));
        }

        return RestaurantPageResponse.create(restaurantList.getTotalPages(), restaurantDtos);
    }

    private Specification<Restaurant> determineFilterCondition(
            String foodCategoryName, String locationCategoryName, String locationTagName, Long recommendCategoryId, Long registrantId) {
        FoodCategory foodCategory = foodCategoryRepository.findByName(foodCategoryName).orElseGet(null);
        LocationCategory locationCategory = locationCategoryRepository.findByName(locationCategoryName).orElseGet(null);
        LocationTag locationTag = locationTagRepository.findByName(locationTagName).orElseGet(null);
        RecommendCategory recommendCategory = recommendCategoryRepository.findById(recommendCategoryId).orElseGet(null);
        Member registrant = null;
        if (registrantId != null){
            registrant = memberRepository.findById(registrantId).orElseThrow(() -> new NotFoundException("유저"));
        }
        return buildSpecification(foodCategory, locationCategory, locationTag, recommendCategory, null, true, registrant);
    }

    private Pageable determineSort(int page, int size, String sort, String order) {
        Pageable pageable = PageRequest.of(page, size);
        if(order.equals("ascending")) {
            return PageRequest.of(page, size, Sort.by(sort).ascending());
        } else if(order.equals("descending")) {
            return PageRequest.of(page, size, Sort.by(sort).descending());
        }
        return pageable;
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
