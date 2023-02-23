package LikeLion.TodaysLunch.dto;


import LikeLion.TodaysLunch.token.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.util.Date;

public class TokenDto {

    private final String token;
    private final Date expirationTime;

    public TokenDto(String token) {
        this.token = token;
        this.expirationTime = JwtTokenProvider.getExpirationTime(token);
    }

    public String getToken() {
        return this.token;
    }

    public Date getTokenExpiresTime() {
        return this.expirationTime;
    }
}
