package LikeLion.TodaysLunch.review.controller;

import LikeLion.TodaysLunch.skeleton.ControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

public class ReviewControllerTest extends ControllerTest {
  @Test
  @WithMockUser(username="test", roles={"SUPER"})
  void 리뷰_등록하기() throws Exception {

  }
}
