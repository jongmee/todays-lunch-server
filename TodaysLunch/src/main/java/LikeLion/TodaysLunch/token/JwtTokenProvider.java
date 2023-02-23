package LikeLion.TodaysLunch.token;

import LikeLion.TodaysLunch.dto.TokenDto;
import LikeLion.TodaysLunch.service.login.CustomUserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
        return new TokenDto(Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact());
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
            return getExpirationTime(jwtToken).before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }
}
