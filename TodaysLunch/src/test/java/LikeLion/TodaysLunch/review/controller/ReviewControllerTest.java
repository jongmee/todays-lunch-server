package LikeLion.TodaysLunch.review.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import LikeLion.TodaysLunch.review.dto.MyReviewDto;
import LikeLion.TodaysLunch.skeleton.controller.ControllerTest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

public class ReviewControllerTest extends ControllerTest {
  @Test
  @WithMockUser(username="test", roles={"SUPER"})
  void 리뷰_등록하기() throws Exception {

  }
  @Test
  @WithMockUser(username="test", roles={"SUPER"})
  void 유저가_작성한_리뷰목록_반환하기() throws Exception {
    // given
    HashMap<String, Object> 응답 = new HashMap<>();
    MyReviewDto 리뷰 = MyReviewDto.builder()
        .reviewId(1L)
        .restaurantId(1L)
        .restaurantName("test name")
        .imageUrl("https://dummy-image-url")
        .reviewContent("dummy review content")
        .rating(1)
        .createdDate(LocalDate.of(2023, 2, 3))
        .likeCount(1L)
        .liked(false).build();
    응답.put("data", new ArrayList<>(Arrays.asList(리뷰)));
    응답.put("totalPages", 1);
    BDDMockito.given(reviewService.myReviewList(anyLong(), anyInt(), anyInt(), any(), any())).willReturn(응답);
    // when & then
    mockMvc.perform(get("/myreviews")
            .param("reviewer-id", "1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
  }

}
