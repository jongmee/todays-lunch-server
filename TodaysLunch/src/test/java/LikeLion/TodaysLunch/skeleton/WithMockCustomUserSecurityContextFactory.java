package LikeLion.TodaysLunch.skeleton;

import LikeLion.TodaysLunch.member.domain.Member;
import java.util.Collections;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomMember> {
  @Override
  public SecurityContext createSecurityContext(WithMockCustomMember customMember){
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    Member principal = Member.builder()
        .nickname("test member")
        .email(customMember.email())
        .password(customMember.password())
        .roles(Collections.singletonList(customMember.role()))
        .myStoreCount(0L)
        .temporaryPw(false)
        .build();
    Authentication auth = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
    context.setAuthentication(auth);
    return context;
  }
}
