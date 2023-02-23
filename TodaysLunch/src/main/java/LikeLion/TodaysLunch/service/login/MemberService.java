package LikeLion.TodaysLunch.service.login;

import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.dto.MemberDto;
import LikeLion.TodaysLunch.dto.TokenDto;
import LikeLion.TodaysLunch.repository.MemberRepository;
import LikeLion.TodaysLunch.token.JwtTokenProvider;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public Long join(MemberDto memberDto) {
        validateDuplication(memberDto);
        Member member = Member.builder()
                .nickname(memberDto.getNickname())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        return memberRepository.save(member).getId();
    }

    private void validateDuplication(MemberDto memberDto) {
        if (memberRepository.findByNickname(memberDto.getNickname()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
    }

    @Transactional
    public TokenDto login(MemberDto memberDto) {
        Member member = memberRepository.findByNickname(memberDto.getNickname())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        if (!passwordEncoder.matches(memberDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        TokenDto tokenDto = jwtTokenProvider.createToken(member.getNickname(), member.getRoles());
        long expiration = JwtTokenProvider.getExpirationTime(tokenDto.getToken()).getTime();
        redisTemplate.opsForValue()
                .set(member.getNickname(), tokenDto.getToken(), expiration, TimeUnit.MILLISECONDS);

        return tokenDto;
    }

    @Transactional
    public void logout(TokenDto tokenDto) {
        String token = tokenDto.getToken();
        if (jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        if (redisTemplate.opsForValue().get(authentication.getName()) != null) {
            redisTemplate.delete(authentication.getName());
        }
        long expiration = JwtTokenProvider.getExpirationTime(token).getTime();
        redisTemplate.opsForValue().set(token, "logout", expiration, TimeUnit.MILLISECONDS);
    }
}
