package LikeLion.TodaysLunch.member.config;

import LikeLion.TodaysLunch.external.JwtAuthenticationFilter;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@AllArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().headers().frameOptions().disable()
                .and()

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/logout-member", "/login", "/join", "/email-verification/send-code", "/find-pw", "/admin-join").permitAll()
                .antMatchers(HttpMethod.GET, "/food-category", "/location-category", "/location-tag", "/recommend-category").permitAll()
                .antMatchers("/mypage", "/check-pw", "/mystore", "/participate-restaurant", "/myreviews").authenticated()
                .antMatchers("/food-category/**", "/location-category/**", "/location-tag/**", "/recommend-category/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET).permitAll()
                .anyRequest().authenticated()
                .and()

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors().configurationSource(corsConfigurationSource());
    }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {

    CorsConfiguration configuration = new CorsConfiguration();

    // 쿠키 사용
    configuration.setAllowCredentials(true);
    configuration.setAllowedOriginPatterns(Arrays.asList("https://todays-lunch.me/"));

    // 메서드
    configuration.setAllowedMethods(
        Arrays.asList(HttpMethod.POST.name(), HttpMethod.GET.name(),
            HttpMethod.PUT.name(), HttpMethod.DELETE.name(), HttpMethod.PATCH.name(),
            HttpMethod.OPTIONS.name())
    );

    // 요청헤더
    configuration.setAllowedHeaders(
        Arrays.asList("Authorization", "Content-Type")
        );

    // 응답 헤더
    configuration.setExposedHeaders(
        Arrays.asList("Content-Type", "Authorization")
        );

    UrlBasedCorsConfigurationSource source
        = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    FilterRegistrationBean bean
        = new FilterRegistrationBean(new CorsFilter(source));
    bean.setOrder(0);
    return source;
  }
}
