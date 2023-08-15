package LikeLion.TodaysLunch.menu.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import LikeLion.TodaysLunch.menu.dto.MenuDto;
import LikeLion.TodaysLunch.skeleton.controller.ControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

class MenuControllerTest extends ControllerTest {
  @Test
  @WithMockUser(username="test", roles={"SUPER"})
  void 메뉴_올바르게_등록하기() throws Exception {
    MenuDto 메뉴_생성_요청 = MenuDto.builder().name("메뉴 이름").price(15000L).build();

    // when & then
    mockMvc.perform(post("/restaurants/{restaurantId}/menus", 1L)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(메뉴_생성_요청)))
        .andDo(print())
        .andExpect(status().isOk());
  }
  @Test
  @WithMockUser(username="test", roles={"SUPER"})
  void 메뉴_잘못된_형식으로_등록하기() throws Exception {
    // given
    MenuDto 메뉴_생성_요청 = MenuDto.builder().price(100L).build();

    // when & then
    mockMvc.perform(post("/restaurants/{restaurantId}/menus", 1L)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(메뉴_생성_요청)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }
}