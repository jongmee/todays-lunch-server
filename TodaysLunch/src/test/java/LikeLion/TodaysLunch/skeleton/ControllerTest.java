package LikeLion.TodaysLunch.skeleton;


import LikeLion.TodaysLunch.config.WebSecurityConfig;
import LikeLion.TodaysLunch.controller.CategoryController;
import LikeLion.TodaysLunch.service.CategoryService;
import LikeLion.TodaysLunch.token.JwtAuthenticationFilter;
import LikeLion.TodaysLunch.token.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = CategoryController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebSecurityConfig.class, JwtAuthenticationFilter.class}))
@MockBean(JpaMetamodelMappingContext.class)
public abstract class ControllerTest {
  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected ObjectMapper objectMapper;
  @MockBean
  protected CategoryService categoryService;

}
