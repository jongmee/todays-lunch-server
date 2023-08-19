package LikeLion.TodaysLunch.menu.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import LikeLion.TodaysLunch.exception.NotFoundException;
import LikeLion.TodaysLunch.image.domain.ImageUrl;
import LikeLion.TodaysLunch.image.domain.MenuImage;
import LikeLion.TodaysLunch.image.repository.ImageUrlRepository;
import LikeLion.TodaysLunch.image.repository.MenuImageRepository;
import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.menu.domain.Menu;
import LikeLion.TodaysLunch.menu.dto.MenuDto;
import LikeLion.TodaysLunch.menu.dto.MenuImageDto;
import LikeLion.TodaysLunch.menu.dto.SaleMenuDto;
import LikeLion.TodaysLunch.menu.repository.MenuRepository;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import LikeLion.TodaysLunch.skeleton.service.ServiceTest;
import LikeLion.TodaysLunch.skeleton.service.TestRestaurant;
import LikeLion.TodaysLunch.skeleton.service.TestUser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


class MenuServiceTest extends ServiceTest {

  @Autowired
  private MenuService menuService;
  @Autowired
  private MenuRepository menuRepository;
  @Autowired
  private MenuImageRepository menuImageRepository;
  @Autowired
  private ImageUrlRepository imageUrlRepository;

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

    assertEquals(10000L, 등록된_메뉴1.getSalePrice());
    assertEquals(null, 등록된_메뉴2.getSaleExplain());
    assertEquals(null, 등록된_메뉴3.getSalePrice());
  }

  @Test
  void 메뉴_수정하기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());

    Menu 등록된_메뉴1 = 메뉴_생성하기("사케동", 15000L, 10000L, "서강대학생전용입니다", 정식맛집.getRestaurant().getId(), 유저.getMember());
    Menu 등록된_메뉴2 = 메뉴_생성하기("명란크림우동", 10000L, 9000L, null, 정식맛집.getRestaurant().getId(), 유저.getMember());

    // when
    MenuDto 수정_요청1 = MenuDto.builder().name("사케동").price(15000L).build();
    menuService.update(수정_요청1, 정식맛집.getRestaurant().getId(), 등록된_메뉴1.getId(), 유저.getMember());

    MenuDto 수정_요청2 = MenuDto.builder().name("명란크림우동").price(10000L).salePrice(9000L).saleExplain("8월첫째주만합니다").build();
    menuService.update(수정_요청2, 정식맛집.getRestaurant().getId(), 등록된_메뉴2.getId(), 유저.getMember());

    // then
    assertEquals(null, 등록된_메뉴1.getSaleExplain());
    assertEquals("8월첫째주만합니다", 등록된_메뉴2.getSaleExplain());
  }

  @Test
  void 메뉴_삭제하기() throws Exception{
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());

    Menu 등록된_메뉴 = 메뉴_생성하기("사케동", 15000L, 10000L, "서강대학생전용입니다", 정식맛집.getRestaurant().getId(), 유저.getMember());

    String 이미지_이름 = "test_logo.jpg";
    MultipartFile 이미지 = 이미지_가져오기(이미지_이름);

    menuService.createImage(이미지, 등록된_메뉴.getId(), 유저.getMember());
    menuService.createImage(이미지, 등록된_메뉴.getId(), 유저.getMember());

    // when
    menuService.delete(정식맛집.getRestaurant().getId(), 등록된_메뉴.getId());

    // then
    List<MenuImage> 등록된_관계들 = menuImageRepository.findAllByMenu(등록된_메뉴);
    assertEquals(0, 등록된_관계들.size());
    assertEquals(0, menuRepository.findAll().size());
    assertEquals(0, imageUrlRepository.findAll().size());
  }

  @Test
  void 메뉴이미지_등록하기() throws Exception{
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());

    Menu 등록된_메뉴 = 메뉴_생성하기("사케동", 15000L, 10000L, "서강대학생전용입니다", 정식맛집.getRestaurant().getId(), 유저.getMember());

    String 이미지_이름 = "test_logo.jpg";
    MultipartFile 이미지 = 이미지_가져오기(이미지_이름);

    // when
    ImageUrl 등록된_이미지 = menuService.createImage(이미지, 등록된_메뉴.getId(), 유저.getMember());

    // then
    assertEquals(이미지_이름, 등록된_이미지.getOriginalName());
  }

  @Test
  void 메뉴이미지_삭제하기() throws Exception{
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());

    Menu 등록된_메뉴 = 메뉴_생성하기("사케동", 15000L, 10000L, "서강대학생전용입니다", 정식맛집.getRestaurant().getId(), 유저.getMember());

    String 이미지_이름 = "test_logo.jpg";
    MultipartFile 이미지 = 이미지_가져오기(이미지_이름);

    ImageUrl 등록된_이미지 = menuService.createImage(이미지, 등록된_메뉴.getId(), 유저.getMember());

    List<MenuImage> 등록된_관계들 = menuImageRepository.findAllByMenu(등록된_메뉴);

    // when
    menuService.deleteImage(등록된_메뉴.getId(), 등록된_이미지.getId());

    // then
    List<MenuImage> 삭제후_관계들 = menuImageRepository.findAllByMenu(등록된_메뉴);
    assertEquals(0, 삭제후_관계들.size());
  }

  @Test
  void 메뉴이미지_목록_조회하기() throws Exception{
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());

    Menu 등록된_메뉴 = 메뉴_생성하기("사케동", 15000L, 10000L, "서강대학생전용입니다", 정식맛집.getRestaurant().getId(), 유저.getMember());

    String 이미지_이름 = "test_logo.jpg";
    MultipartFile 이미지 = 이미지_가져오기(이미지_이름);

    menuService.createImage(이미지, 등록된_메뉴.getId(), 유저.getMember());
    menuService.createImage(이미지, 등록된_메뉴.getId(), 유저.getMember());

    // when
    HashMap<String, Object> 응답 = menuService.menuImageList(등록된_메뉴.getId(), 0, 5);
    List<MenuImageDto> 이미지_목록 = (List<MenuImageDto>) 응답.get("data");

    // then
    assertEquals(2, 이미지_목록.size());
  }

  @Test
  void 메뉴_등록하면_맛집_업데이트날짜에_반영하기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());
    MenuDto 새로운_메뉴 = MenuDto.builder().name("사케동").price(15000L).salePrice(10000L).saleExplain("서강대학생전용입니다").build();
    LocalDateTime 비교시간 = LocalDateTime.now();

    // when
    menuService.create(새로운_메뉴, 정식맛집.getRestaurant().getId(), 유저.getMember());
    LocalDateTime 수정시간 = 정식맛집.getRestaurant().getUpdatedDate();

    //then
    assertThat(수정시간).isAfter(비교시간);
  }

  @Test
  void 메뉴_수정하면_맛집_업데이트날짜에_반영하기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());

    Menu 등록된_메뉴 = 메뉴_생성하기("사케동", 15000L, 10000L, "서강대학생전용입니다", 정식맛집.getRestaurant().getId(), 유저.getMember());

    MenuDto 수정_요청 = MenuDto.builder().name("사케동").price(15000L).build();
    menuService.update(수정_요청, 정식맛집.getRestaurant().getId(), 등록된_메뉴.getId(), 유저.getMember());

    LocalDateTime 비교시간 = LocalDateTime.now();

    // when
    메뉴_생성하기("가츠동", 15000L, 10000L, "서강대학생전용입니다", 정식맛집.getRestaurant().getId(), 유저.getMember());
    LocalDateTime 수정시간 = 정식맛집.getRestaurant().getUpdatedDate();

    //then
    assertThat(수정시간).isAfter(비교시간);
  }

  @Test
  void 메뉴이미지_등록하면_맛집_업데이트날짜에_반영하기() throws IOException {
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());

    Menu 등록된_메뉴 = 메뉴_생성하기("사케동", 15000L, 10000L, "서강대학생전용입니다", 정식맛집.getRestaurant().getId(), 유저.getMember());

    String 이미지_이름 = "test_logo.jpg";
    MultipartFile 이미지 = 이미지_가져오기(이미지_이름);

    LocalDateTime 비교시간 = LocalDateTime.now();

    // when
    menuService.createImage(이미지, 등록된_메뉴.getId(), 유저.getMember());
    LocalDateTime 수정시간 = 정식맛집.getRestaurant().getUpdatedDate();

    // then
    assertThat(수정시간).isAfter(비교시간);
  }

  @Test
  void 메뉴의_세일정보_삭제하기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());

    Menu 등록된_메뉴 = 메뉴_생성하기("사케동", 15000L, 10000L, "서강대학생전용입니다", 정식맛집.getRestaurant().getId(), 유저.getMember());

    // when
    MenuDto 수정_요청 = MenuDto.builder().name("사케동").price(15000L).build();
    menuService.update(수정_요청, 정식맛집.getRestaurant().getId(), 등록된_메뉴.getId(), 유저.getMember());

    // then
    Menu 수정된_메뉴 = menuRepository.findByName("사케동")
        .orElseThrow(() -> new NotFoundException("메뉴"));
    assertEquals(null, 수정된_메뉴.getSalePrice());
  }

  @Test
  void 세일메뉴_목록_조회하기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());

    메뉴_생성하기("사케동", 15000L, 10000L, "서강대학생전용입니다", 정식맛집.getRestaurant().getId(), 유저.getMember());
    메뉴_생성하기("가츠동", 15000L, 10000L, null, 정식맛집.getRestaurant().getId(), 유저.getMember());
    메뉴_생성하기("에비동", 15000L, null, null, 정식맛집.getRestaurant().getId(), 유저.getMember());

    // when
    Pageable pageable = PageRequest.of(0, 5);
    HashMap 응답 = menuService.saleMenuList(pageable);

    // then
    List<SaleMenuDto> 세일메뉴목록 = (List<SaleMenuDto>) 응답.get("data");
    assertEquals(2, 세일메뉴목록.size());
  }

  @Test
  void 세일메뉴_목록에서_대표이미지_반환하기() throws IOException {
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());

    Menu 등록된_메뉴1 = 메뉴_생성하기("사케동", 15000L, 10000L, "서강대학생전용입니다", 정식맛집.getRestaurant().getId(), 유저.getMember());
    Menu 등록된_메뉴2 = 메뉴_생성하기("가츠동", 15000L, 10000L, null, 정식맛집.getRestaurant().getId(), 유저.getMember());

    MultipartFile 이미지 = 이미지_가져오기("test_logo.jpg");
    ImageUrl 등록된_이미지1 = menuService.createImage(이미지, 등록된_메뉴1.getId(), 유저.getMember());
    ImageUrl 등록된_이미지2 = menuService.createImage(이미지, 등록된_메뉴1.getId(), 유저.getMember());

    // when
    menuService.setBestMenuImage(등록된_이미지1.getId());
    menuService.setBestMenuImage(등록된_이미지2.getId());

    Pageable pageable = PageRequest.of(0, 5);
    HashMap 응답 = menuService.saleMenuList(pageable);

    // then
    List<SaleMenuDto> 세일메뉴목록 = (List<SaleMenuDto>) 응답.get("data");
    ImageUrl 첫번째메뉴의_대표이미지 = imageUrlRepository.findByImageUrl(세일메뉴목록.get(0).getImageUrl())
        .orElseThrow(() -> new NotFoundException("이미지"));
    MenuImage 첫번째메뉴의_다른이미지 = menuImageRepository.findById(등록된_이미지1.getId())
        .orElseThrow(() -> new NotFoundException("메뉴와 이미지의 관계"));

    assertEquals(2, 세일메뉴목록.size());
    assertEquals(등록된_이미지2.getImageUrl(), 첫번째메뉴의_대표이미지.getImageUrl());
    assertEquals(null, 세일메뉴목록.get(1).getImageUrl());
    assertEquals(false, 첫번째메뉴의_다른이미지.getIsBest());
  }

  @Test
  void 메뉴_등록시_맛집의_최저메뉴에_반영하기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());

    // when
    Menu 등록된_메뉴1 = 메뉴_생성하기("사케동", 15000L, 10000L, "서강대학생전용입니다", 정식맛집.getRestaurant().getId(), 유저.getMember());
    Menu 등록된_메뉴2 = 메뉴_생성하기("가츠동", 9000L, 7000L, null, 정식맛집.getRestaurant().getId(), 유저.getMember());
    Menu 등록된_메뉴3 = 메뉴_생성하기("에비동", 6000L, null, null, 정식맛집.getRestaurant().getId(), 유저.getMember());

    // then
    assertEquals(6000L, 정식맛집.getRestaurant().getLowestPrice());
  }

  @Test
  void 메뉴_삭제시_맛집의_최저메뉴에_반영하기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());

    // when
    Menu 등록된_메뉴1 = 메뉴_생성하기("사케동", 15000L, 10000L, "서강대학생전용입니다", 정식맛집.getRestaurant().getId(), 유저.getMember());
    Menu 등록된_메뉴2 = 메뉴_생성하기("가츠동", 9000L, 7000L, null, 정식맛집.getRestaurant().getId(), 유저.getMember());
    menuService.delete(정식맛집.getRestaurant().getId(), 등록된_메뉴2.getId());

    // then
    assertEquals(10000L, 정식맛집.getRestaurant().getLowestPrice());
  }

  @Test
  void 메뉴_수정시_맛집의_최저메뉴에_반영하기(){
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());

    // when
    Menu 등록된_메뉴1 = 메뉴_생성하기("사케동", 15000L, 10000L, "서강대학생전용입니다", 정식맛집.getRestaurant().getId(), 유저.getMember());
    MenuDto 수정_요청 = MenuDto.builder().name("사케동").price(15000L).build();
    menuService.update(수정_요청, 정식맛집.getRestaurant().getId(), 등록된_메뉴1.getId(), 유저.getMember());

    // then
    assertEquals(15000L, 정식맛집.getRestaurant().getLowestPrice());
  }


  private MultipartFile 이미지_가져오기(String imageName) throws IOException {
    File file = new File(new File("").getAbsolutePath() + "/src/test/resources/testImage/"+imageName);
    FileItem fileItem = new DiskFileItem("originFile", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());

    InputStream input = new FileInputStream(file);
    OutputStream os = fileItem.getOutputStream();
    IOUtils.copy(input, os);

    MultipartFile mFile = new CommonsMultipartFile(fileItem);
    return mFile;
  }

  private Menu 메뉴_생성하기(String 메뉴이름, Long 가격, Long 세일가격, String 세일정보, Long 맛집ID, Member 등록자){
    MenuDto 새로운_메뉴 = MenuDto.builder().name(메뉴이름).price(가격).salePrice(세일가격).saleExplain(세일정보).build();
    menuService.create(새로운_메뉴, 맛집ID, 등록자);
    return menuRepository.findByName(메뉴이름)
        .orElseThrow(() -> new NotFoundException("메뉴"));
  }
}