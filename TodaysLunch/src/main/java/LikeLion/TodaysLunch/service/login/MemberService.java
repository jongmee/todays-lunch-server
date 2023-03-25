package LikeLion.TodaysLunch.service.login;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.LocationCategory;
import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.dto.MemberDto;
import LikeLion.TodaysLunch.dto.MemberDtoMapper;
import LikeLion.TodaysLunch.dto.MemberJoinDto;
import LikeLion.TodaysLunch.dto.MemberLoginDto;
import LikeLion.TodaysLunch.dto.TokenDto;
import LikeLion.TodaysLunch.exception.DuplicationException;
import LikeLion.TodaysLunch.exception.NotFoundException;
import LikeLion.TodaysLunch.exception.UnauthorizedException;
import LikeLion.TodaysLunch.repository.FoodCategoryRepository;
import LikeLion.TodaysLunch.repository.LocationCategoryRepository;
import LikeLion.TodaysLunch.repository.MemberRepository;
import LikeLion.TodaysLunch.token.JwtTokenProvider;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final FoodCategoryRepository foodCategoryRepository;
    private final LocationCategoryRepository locationCategoryRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public Long join(MemberJoinDto memberDto) {
        validateDuplication(memberDto);

        FoodCategory foodCategory = foodCategoryRepository.findByName(memberDto.getFoodCategory())
            .orElseThrow(() -> new NotFoundException("음식 카테고리"));
        LocationCategory locationCategory = locationCategoryRepository.findByName(memberDto.getLocationCategory())
            .orElseThrow(() -> new NotFoundException("위치 카테고리"));

        Member member = memberDto.toEntity(foodCategory, locationCategory, passwordEncoder.encode(memberDto.getPassword()));

        return memberRepository.save(member).getId();
    }

    private void validateDuplication(MemberJoinDto memberDto) {
        if (memberRepository.findByEmail(memberDto.getEmail()).isPresent()) {
            throw new DuplicationException("이메일");
        }
    }

    @Transactional
    public TokenDto login(MemberLoginDto memberDto) {
        Member member = memberRepository.findByEmail(memberDto.getEmail())
                .orElseThrow(() -> new NotFoundException("이메일"));

        if (!passwordEncoder.matches(memberDto.getPassword(), member.getPassword())) {
            throw new UnauthorizedException("회원정보의 비밀번호와 일치하지 않습니다.");
        }

        TokenDto tokenDto = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        long expiration = JwtTokenProvider.getExpirationTime(tokenDto.getToken()).getTime();
        redisTemplate.opsForValue()
                .set(member.getEmail(), tokenDto.getToken(), expiration, TimeUnit.MILLISECONDS);

        return tokenDto;
    }

    @Transactional
    public void logout(String token) {
        jwtTokenProvider.validateToken(token);

        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        if (redisTemplate.opsForValue().get(authentication.getName()) != null) {
            redisTemplate.delete(authentication.getName());
        }
    }

    public MemberDto getAuthenticatedMember(@AuthenticationPrincipal Member member) {
        if (member != null) {
            return MemberDtoMapper.toDto(member);
        } else {
            throw new UnauthorizedException("인가 되지 않은 사용자입니다.");
        }
    }
}
