package LikeLion.TodaysLunch.skeleton;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomMember {
  String email() default "qwer1234@naver.com";
  String password() default "1234";
  String role() default "ROLE_ADMIN";
}
