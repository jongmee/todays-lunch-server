package LikeLion.TodaysLunch.restaurant.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import LikeLion.TodaysLunch.exception.NotFoundException;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import LikeLion.TodaysLunch.restaurant.dto.request.JudgeRestaurantCreateDto;
import LikeLion.TodaysLunch.restaurant.dto.response.JudgeRestaurantListDto;
import LikeLion.TodaysLunch.restaurant.dto.response.common.RestaurantPageResponse;
import LikeLion.TodaysLunch.environment.service.ServiceTest;
import LikeLion.TodaysLunch.environment.service.TestRestaurant;
import LikeLion.TodaysLunch.environment.service.TestUser;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

public class JudgeRestaurantServiceTest extends ServiceTest {
    @Autowired
    private JudgeRestaurantService judgeRestaurantService;

    @Test
    @Transactional
    void 맛집_심사_등록하기() throws IOException {
        // given
        JudgeRestaurantCreateDto 등록_요청 = JudgeRestaurantCreateDto
                .builder()
                .foodCategoryName("한식")
                .restaurantName("가츠벤또")
                .latitude(37.546924)
                .longitude(126.940155)
                .address("서울시 마포구")
                .introduction("참 맛있어요!")
                .recommendCategoryIds(List.of(추천카테고리_반환하기(추천카테고리이름1)))
                .build();
        TestUser 유저 = makeTestUser("qwer1234@naver.com", "1234", "lee", List.of("한식"), List.of("서강대"));


        // when
        judgeRestaurantService.createJudgeRestaurant(등록_요청, null, 유저.getMember());

        // then
        Restaurant restaurantForTest = testRestaurantEnviron.restaurantRepository().findByRestaurantName("가츠벤또")
                .orElseThrow(() -> new NotFoundException("맛집"));
        assertEquals("참 맛있어요!", restaurantForTest.getIntroduction());
        assertEquals(1, restaurantForTest.getRecommendCategoryRelations().size());
    }

    @Test
    void 심사맛집_동의수_업데이트하기() {
        // given
        TestUser 유저 = makeTestUser("qwer1234@naver.com", "1234", "lee", List.of("한식"), List.of("서강대"));
        TestRestaurant 맛집 = makeTestJudgeRestaurant("한식", "서강대", "정문", "서울시 마포구", "가츠벤또","정말 맛있다", 126.940155, 37.546924, 유저.getMember());

        // when
        judgeRestaurantService.addOrCancelAgreement(유저.getMember(), 맛집.getRestaurant().getId());

        // then
        Restaurant 수정된_맛집 = testRestaurantEnviron.restaurantRepository().findByRestaurantName("가츠벤또")
                .orElseThrow(() -> new NotFoundException("맛집"));
        assertEquals(1L, 수정된_맛집.getAgreementCount());
    }

    @Test
    void 심사맛집을_정식맛집으로_전환하기(){
        // given
        TestUser 유저1 = makeTestUser("qwer@naver.com", "1234", "유저1", List.of("한식"), List.of("서강대"));
        TestUser 유저2 = makeTestUser("qwer1@naver.com", "1234", "유저2", List.of("한식"), List.of("서강대"));
        TestUser 유저3 = makeTestUser("qwer12@naver.com", "1234", "유저3", List.of("한식"), List.of("서강대"));
        TestUser 유저4 = makeTestUser("qwer123@naver.com", "1234", "유저4", List.of("한식"), List.of("서강대"));
        TestUser 유저5 = makeTestUser("qwer1234@naver.com", "1234", "유저5", List.of("한식"), List.of("서강대"));
        TestUser 유저6 = makeTestUser("qwer12345@naver.com", "1234", "유저6", List.of("한식"), List.of("서강대"));

        TestRestaurant 맛집 = makeTestJudgeRestaurant("한식", "서강대", "정문", "서울시 마포구", "가츠벤또","정말 맛있다", 126.940155, 37.546924, 유저1.getMember());
        Boolean 이전_심사_상태 = 맛집.getRestaurant().getJudgement();

        // when
        Long 맛집ID = 맛집.getRestaurant().getId();
        judgeRestaurantService.addOrCancelAgreement(유저1.getMember(), 맛집ID);
        judgeRestaurantService.addOrCancelAgreement(유저2.getMember(), 맛집ID);
        judgeRestaurantService.addOrCancelAgreement(유저3.getMember(), 맛집ID);
        judgeRestaurantService.addOrCancelAgreement(유저4.getMember(), 맛집ID);
        judgeRestaurantService.addOrCancelAgreement(유저5.getMember(), 맛집ID);

        // then
        Restaurant 수정된_맛집 = testRestaurantEnviron.restaurantRepository().findByRestaurantName("가츠벤또")
                .orElseThrow(() -> new NotFoundException("맛집"));
        assertEquals(true, 이전_심사_상태);
        assertEquals(false, 수정된_맛집.getJudgement());
    }
    @Test
    void 심사맛집_조회시_동의여부_포함시키기(){
        // given
        TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", List.of("한식"), List.of("서강대"));
        TestRestaurant 심사맛집 = makeTestJudgeRestaurant("한식", "서강대", "정문", "서울시 마포구", "가츠벤또","정말 맛있다", 126.940155, 37.546924, 유저.getMember());
        judgeRestaurantService.addOrCancelAgreement(유저.getMember(), 심사맛집.getRestaurant().getId());

        // when
        RestaurantPageResponse 응답 = judgeRestaurantService.judgeRestaurantList(null, null, null, null, PageRequest.of(0, 3, Sort.by("rating").descending()), null, 유저.getMember());

        // then
        List<JudgeRestaurantListDto> 맛집목록 = (List<JudgeRestaurantListDto>) 응답.getData();
        Boolean 동의여부 = 맛집목록.get(0).getAgreed();
        assertEquals(true, 동의여부);
    }

    @Test
    void 로그아웃시_동의여부_false로_포함하기(){
        // given
        TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", List.of("한식"), List.of("서강대"));
        makeTestJudgeRestaurant("한식", "서강대", "정문", "서울시 마포구", "가츠벤또","정말 맛있다", 126.940155, 37.546924, 유저.getMember());

        // when
        RestaurantPageResponse 응답 = judgeRestaurantService.judgeRestaurantList(null, null, null, null, PageRequest.of(0, 3, Sort.by("rating").descending()), null, null);

        // then
        List<JudgeRestaurantListDto> 맛집목록 = (List<JudgeRestaurantListDto>) 응답.getData();
        Boolean 동의여부 = 맛집목록.get(0).getAgreed();
        assertEquals(false, 동의여부);
    }
}
