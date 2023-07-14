package LikeLion.TodaysLunch.dto;


import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TokenDto {

    @NotNull
    private String accessToken;
    @NotNull
    private String refreshToken;
    @NotNull
    private long refreshTokenExpiresTime;

    public TokenDto(String accessToken, String refreshToken, long tokenExpiresTime) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresTime = tokenExpiresTime;
    }
}
