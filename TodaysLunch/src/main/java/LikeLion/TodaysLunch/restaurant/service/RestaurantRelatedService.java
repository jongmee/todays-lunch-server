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
import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.member.repository.MemberRepository;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantRelatedService {

    private static final double MAX_DIFFERENCE = 10000d;

    private final FoodCategoryRepository foodCategoryRepository;
    private final LocationCategoryRepository locationCategoryRepository;
    private final LocationTagRepository locationTagRepository;
    private final RecommendCategoryRepository recommendCategoryRepository;
    private final MemberRepository memberRepository;

    public FoodCategory findFoodCategory(String name) {
        return foodCategoryRepository.findByName(name).orElseThrow(() -> new NotFoundException("음식 카테고리"));
    }

    public LocationCategory findLocationCategory(String name) {
        return locationCategoryRepository.findByName(name).orElseThrow(() -> new NotFoundException("위치 카테고리"));
    }

    public LocationTag findLocationTag(String name) {
        return locationTagRepository.findByName(name).orElseThrow(() -> new NotFoundException("위치 태그"));
    }

    public RecommendCategory findRecommendCategory(Long id) {
        return recommendCategoryRepository.findById(id).orElseThrow(() -> new NotFoundException("추천 카테고리"));
    }

    public Specification<Restaurant> determineFilterCondition(
            String foodCategoryName, String locationCategoryName, String locationTagName,
            Long recommendCategoryId, Long registrantId, String keyword, boolean isJudgement) {
        FoodCategory foodCategory = null;
        if(foodCategoryName != null) {
            foodCategory = findFoodCategory(foodCategoryName);
        }
        LocationCategory locationCategory = null;
        if(locationCategoryName != null) {
            locationCategory = findLocationCategory(locationCategoryName);
        }
        LocationTag locationTag = null;
        if(locationTagName != null) {
            locationTag = findLocationTag(locationTagName);
        }
        RecommendCategory recommendCategory = null;
        if(recommendCategoryId != null) {
            recommendCategory = findRecommendCategory(recommendCategoryId);
        }
        Member registrant = null;
        if(registrantId != null) {
            registrant = memberRepository.findById(registrantId).orElseThrow(() -> new NotFoundException("유저"));
        }
        return buildSpecification(foodCategory, locationCategory, locationTag, recommendCategory, keyword, isJudgement, registrant);
    }

    /* Todo: 위치 태그와 위치 카테고리의 중복되는 필드들 어떻게 처리할지 고민하기 */
    public LocationTag findNearestTag(double latitude, double longitude) {
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

    public LocationCategory findNearestCategory(double latitude, double longitude) {
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
}
