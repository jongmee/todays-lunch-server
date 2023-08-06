package LikeLion.TodaysLunch.skeleton;


import LikeLion.TodaysLunch.external.JwtTokenProvider;
import LikeLion.TodaysLunch.member.config.WebSecurityConfig;
import LikeLion.TodaysLunch.category.controller.CategoryController;
import LikeLion.TodaysLunch.category.service.CategoryService;
import LikeLion.TodaysLunch.external.JwtAuthenticationFilter;
import LikeLion.TodaysLunch.member.controller.MemberController;
import LikeLion.TodaysLunch.member.service.MemberService;
import LikeLion.TodaysLunch.member.service.EmailService;
import LikeLion.TodaysLunch.review.controller.ReviewController;
import LikeLion.TodaysLunch.review.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = {CategoryController.class, ReviewController.class, MemberController.class},
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebSecurityConfig.class, JwtAuthenticationFilter.class}))
@MockBean(JpaMetamodelMappingContext.class)
public abstract class ControllerTest {
  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected ObjectMapper objectMapper;
  @MockBean
  protected CategoryService categoryService;
  @MockBean
  protected ReviewService reviewService;
  @MockBean
  protected MemberService memberService;
  @MockBean
  protected EmailService emailService;
}
