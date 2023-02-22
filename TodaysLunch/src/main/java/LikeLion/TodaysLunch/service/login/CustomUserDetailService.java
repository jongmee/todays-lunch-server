package LikeLion.TodaysLunch.service.login;

import LikeLion.TodaysLunch.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @Bean
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        return memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 멤버입니다"));
    }

}
