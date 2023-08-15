package LikeLion.TodaysLunch.category.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import LikeLion.TodaysLunch.category.dto.RecommendCategoryDto;
import LikeLion.TodaysLunch.skeleton.controller.ControllerTest;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

class CategoryControllerTest extends ControllerTest {
  @Test
  @WithMockUser(username="test", roles={"SUPER"})
  void 추천카테고리_수정하기() throws Exception {
    // given
    willDoNothing()
        .given(categoryService).recommendCategoryEdit(any(),any());
    RecommendCategoryDto.Edit editDto = RecommendCategoryDto.Edit.builder().recommendCategoryIds(new ArrayList<>(Arrays.asList(1L, 2L))).build();

    // when & then
    mockMvc.perform(patch("/restaurants/{restaurantId}/recommend-category", 1L)
            .with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(editDto)))
        .andDo(print())
        .andExpect(status().isOk());
  }
}