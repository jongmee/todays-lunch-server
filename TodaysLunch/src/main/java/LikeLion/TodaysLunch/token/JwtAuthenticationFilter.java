package LikeLion.TodaysLunch.token;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, RedisTemplate redisTemplate) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            if(redisTemplate.opsForValue().get(authentication.getName())==null){
                throw new IllegalArgumentException("이미 로그아웃된 토큰입니다.");
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
            chain.doFilter(request, response);
        } catch (SignatureException e) {
            setErrorResponse(response, e.getMessage());
        } catch (Exception e) {
            setErrorResponse(response, e.getMessage());
        }

    }

    private void setErrorResponse(ServletResponse response, String errorMessage){
        ObjectMapper objectMapper = new ObjectMapper();
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpResponse.setCharacterEncoding("UTF-8");
        ErrorResponse errorResponse = new ErrorResponse(errorMessage, HttpStatus.UNAUTHORIZED.value());
        try{
            httpResponse.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    @Data
    public static class ErrorResponse{
        private final String message;
        private final Integer code;
    }
}
