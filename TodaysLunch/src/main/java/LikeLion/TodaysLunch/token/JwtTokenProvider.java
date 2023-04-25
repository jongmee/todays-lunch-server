package LikeLion.TodaysLunch.token;

import LikeLion.TodaysLunch.dto.TokenDto;
import LikeLion.TodaysLunch.service.login.CustomUserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Component
public class JwtTokenProvider {
    private static String secretKey = "todayslunch";
    private static String SECRET_KEY = Base64.getEncoder().encodeToString(secretKey.getBytes());
    private long tokenValidTime = 24 * 60 * 60 * 1000L;
    private final CustomUserDetailService customUserDetailService;

    @Autowired
    public JwtTokenProvider(CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }


    public TokenDto createToken(String userPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();
        long expiration = now.getTime() + tokenValidTime;
        return new TokenDto(Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(expiration))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact(),expiration);
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public static Date getExpirationTime(String jwtToken) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken)
                .getBody().getExpiration();

    }

    public boolean validateToken(String jwtToken) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken).getBody();
            return true;
        } catch (MalformedJwtException e) {
            throw new MalformedJwtException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new JwtException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("JWT 토큰이 잘못되었습니다.");
        }
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
}
