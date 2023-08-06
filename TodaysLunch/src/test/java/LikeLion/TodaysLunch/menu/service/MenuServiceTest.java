package LikeLion.TodaysLunch.menu.service;

import static org.junit.jupiter.api.Assertions.*;

import LikeLion.TodaysLunch.exception.NotFoundException;
import LikeLion.TodaysLunch.menu.domain.Menu;
import LikeLion.TodaysLunch.menu.dto.MenuDto;
import LikeLion.TodaysLunch.menu.repository.MenuRepository;
import LikeLion.TodaysLunch.skeleton.ServiceTest;
import LikeLion.TodaysLunch.skeleton.TestRestaurant;
import LikeLion.TodaysLunch.skeleton.TestUser;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest {
  @Autowired
  private MenuService menuService;
  @Autowired
  private MenuRepository menuRepository;
  @Test
  void 메뉴_등록하기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    MenuDto 새로운_메뉴1 = MenuDto.builder().name("사케동").price(15000L).salePrice(10000L).saleExplain("서강대학생전용입니다").build();
    MenuDto 새로운_메뉴2 = MenuDto.builder().name("명란크림우동").price(10000L).salePrice(9000L).build();
    MenuDto 새로운_메뉴3 = MenuDto.builder().name("카레").price(10000L).build();

    // when
    menuService.create(새로운_메뉴1, 정식맛집.getRestaurant().getId(), 유저.getMember());
    menuService.create(새로운_메뉴2, 정식맛집.getRestaurant().getId(), 유저.getMember());
    menuService.create(새로운_메뉴3, 정식맛집.getRestaurant().getId(), 유저.getMember());

    // then
    Menu 등록된_메뉴1 = menuRepository.findByName("사케동")
        .orElseThrow(() -> new NotFoundException("메뉴"));
    Menu 등록된_메뉴2 = menuRepository.findByName("명란크림우동")
        .orElseThrow(() -> new NotFoundException("메뉴"));
    Menu 등록된_메뉴3 = menuRepository.findByName("카레")
        .orElseThrow(() -> new NotFoundException("메뉴"));

    Assertions.assertEquals(10000L, 등록된_메뉴1.getSalePrice());
    Assertions.assertEquals(null, 등록된_메뉴2.getSaleExplain());
    Assertions.assertEquals(null, 등록된_메뉴3.getSalePrice());
  }
  @Test
  void 메뉴_수정하기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());

    MenuDto 새로운_메뉴1 = MenuDto.builder().name("사케동").price(15000L).salePrice(10000L).saleExplain("서강대학생전용입니다").build();
    menuService.create(새로운_메뉴1, 정식맛집.getRestaurant().getId(), 유저.getMember());
    Menu 등록된_메뉴1 = menuRepository.findByName("사케동")
        .orElseThrow(() -> new NotFoundException("메뉴"));

    MenuDto 새로운_메뉴2 = MenuDto.builder().name("명란크림우동").price(10000L).salePrice(9000L).build();
    menuService.create(새로운_메뉴2, 정식맛집.getRestaurant().getId(), 유저.getMember());
    Menu 등록된_메뉴2 = menuRepository.findByName("명란크림우동")
        .orElseThrow(() -> new NotFoundException("메뉴"));

    // when
    MenuDto 수정_요청1 = MenuDto.builder().name("사케동").price(15000L).build();
    menuService.update(수정_요청1, 정식맛집.getRestaurant().getId(), 등록된_메뉴1.getId(), 유저.getMember());

    MenuDto 수정_요청2 = MenuDto.builder().name("명란크림우동").price(10000L).salePrice(9000L).saleExplain("8월첫째주만합니다").build();
    menuService.update(수정_요청2, 정식맛집.getRestaurant().getId(), 등록된_메뉴2.getId(), 유저.getMember());

    // then
    Assertions.assertEquals(null, 등록된_메뉴1.getSaleExplain());
    Assertions.assertEquals("8월첫째주만합니다", 등록된_메뉴2.getSaleExplain());
  }
}