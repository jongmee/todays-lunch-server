package LikeLion.TodaysLunch.menu.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import LikeLion.TodaysLunch.exception.NotFoundException;
import LikeLion.TodaysLunch.image.domain.ImageUrl;
import LikeLion.TodaysLunch.image.domain.MenuImage;
import LikeLion.TodaysLunch.image.repository.ImageUrlRepository;
import LikeLion.TodaysLunch.image.repository.MenuImageRepository;
import LikeLion.TodaysLunch.menu.domain.Menu;
import LikeLion.TodaysLunch.menu.dto.MenuDto;
import LikeLion.TodaysLunch.menu.dto.MenuImageDto;
import LikeLion.TodaysLunch.menu.repository.MenuRepository;
import LikeLion.TodaysLunch.skeleton.ServiceTest;
import LikeLion.TodaysLunch.skeleton.TestRestaurant;
import LikeLion.TodaysLunch.skeleton.TestUser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
    assertEquals(null, 등록된_메뉴1.getSaleExplain());
    assertEquals("8월첫째주만합니다", 등록된_메뉴2.getSaleExplain());
  }

  @Test
  void 메뉴_삭제하기() throws Exception{
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());

    MenuDto 새로운_메뉴 = MenuDto.builder().name("사케동").price(15000L).salePrice(10000L).saleExplain("서강대학생전용입니다").build();
    menuService.create(새로운_메뉴, 정식맛집.getRestaurant().getId(), 유저.getMember());
    Menu 등록된_메뉴 = menuRepository.findByName("사케동")
        .orElseThrow(() -> new NotFoundException("메뉴"));

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

    MenuDto 새로운_메뉴 = MenuDto.builder().name("사케동").price(15000L).salePrice(10000L).saleExplain("서강대학생전용입니다").build();
    menuService.create(새로운_메뉴, 정식맛집.getRestaurant().getId(), 유저.getMember());
    Menu 등록된_메뉴 = menuRepository.findByName("사케동")
        .orElseThrow(() -> new NotFoundException("메뉴"));

    String 이미지_이름 = "test_logo.jpg";
    MultipartFile 이미지 = 이미지_가져오기(이미지_이름);

    // when
    menuService.createImage(이미지, 등록된_메뉴.getId(), 유저.getMember());

    // then
    List<MenuImage> 등록된_관계들 = menuImageRepository.findAllByMenu(등록된_메뉴);
    ImageUrl 등록된_이미지 = imageUrlRepository.findById(등록된_관계들.get(0).getImagePk())
        .orElseThrow(() -> new NotFoundException("이미지"));
    assertEquals(이미지_이름, 등록된_이미지.getOriginalName());
  }

  @Test
  void 메뉴이미지_삭제하기() throws Exception{
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());

    MenuDto 새로운_메뉴 = MenuDto.builder().name("사케동").price(15000L).salePrice(10000L).saleExplain("서강대학생전용입니다").build();
    menuService.create(새로운_메뉴, 정식맛집.getRestaurant().getId(), 유저.getMember());
    Menu 등록된_메뉴 = menuRepository.findByName("사케동")
        .orElseThrow(() -> new NotFoundException("메뉴"));

    String 이미지_이름 = "test_logo.jpg";
    MultipartFile 이미지 = 이미지_가져오기(이미지_이름);

    menuService.createImage(이미지, 등록된_메뉴.getId(), 유저.getMember());

    List<MenuImage> 등록된_관계들 = menuImageRepository.findAllByMenu(등록된_메뉴);
    ImageUrl 등록된_이미지 = imageUrlRepository.findById(등록된_관계들.get(0).getImagePk())
        .orElseThrow(() -> new NotFoundException("이미지"));

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

    MenuDto 새로운_메뉴 = MenuDto.builder().name("사케동").price(15000L).salePrice(10000L).saleExplain("서강대학생전용입니다").build();
    menuService.create(새로운_메뉴, 정식맛집.getRestaurant().getId(), 유저.getMember());
    Menu 등록된_메뉴 = menuRepository.findByName("사케동")
        .orElseThrow(() -> new NotFoundException("메뉴"));

    String 이미지_이름 = "test_logo.jpg";
    MultipartFile 이미지 = 이미지_가져오기(이미지_이름);

    menuService.createImage(이미지, 등록된_메뉴.getId(), 유저.getMember());
    menuService.createImage(이미지, 등록된_메뉴.getId(), 유저.getMember());

    // when
    List<MenuImageDto> 이미지_목록 = menuService.menuImageList(등록된_메뉴.getId());

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

    MenuDto 새로운_메뉴 = MenuDto.builder().name("사케동").price(15000L).salePrice(10000L).saleExplain("서강대학생전용입니다").build();
    menuService.create(새로운_메뉴, 정식맛집.getRestaurant().getId(), 유저.getMember());
    Menu 등록된_메뉴 = menuRepository.findByName("사케동")
        .orElseThrow(() -> new NotFoundException("메뉴"));

    MenuDto 수정_요청 = MenuDto.builder().name("사케동").price(15000L).build();
    menuService.update(수정_요청, 정식맛집.getRestaurant().getId(), 등록된_메뉴.getId(), 유저.getMember());

    LocalDateTime 비교시간 = LocalDateTime.now();

    // when
    menuService.create(새로운_메뉴, 정식맛집.getRestaurant().getId(), 유저.getMember());
    LocalDateTime 수정시간 = 정식맛집.getRestaurant().getUpdatedDate();

    //then
    assertThat(수정시간).isAfter(비교시간);
  }

  @Test
  void 메뉴이미지_등록하면_맛집_업데이트날짜에_반영하기() throws IOException {
    // given
    TestUser 유저 = makeTestUser("qwer@naver.com", "1234", "유저", new ArrayList<>(Arrays.asList("한식")), new ArrayList<>(Arrays.asList("서강대")));
    TestRestaurant 정식맛집 = makeTestRestaurant("한식", "서강대", "정문", "서울시 마포구", "정든그릇", "정말 맛있는 집!", 37.546924, 126.940155, 유저.getMember());

    MenuDto 새로운_메뉴 = MenuDto.builder().name("사케동").price(15000L).salePrice(10000L).saleExplain("서강대학생전용입니다").build();
    menuService.create(새로운_메뉴, 정식맛집.getRestaurant().getId(), 유저.getMember());
    Menu 등록된_메뉴 = menuRepository.findByName("사케동")
        .orElseThrow(() -> new NotFoundException("메뉴"));

    String 이미지_이름 = "test_logo.jpg";
    MultipartFile 이미지 = 이미지_가져오기(이미지_이름);

    LocalDateTime 비교시간 = LocalDateTime.now();

    // when
    menuService.createImage(이미지, 등록된_메뉴.getId(), 유저.getMember());
    LocalDateTime 수정시간 = 정식맛집.getRestaurant().getUpdatedDate();

    // then
    assertThat(수정시간).isAfter(비교시간);
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
}