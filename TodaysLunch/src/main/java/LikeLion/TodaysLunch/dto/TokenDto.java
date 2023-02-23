package LikeLion.TodaysLunch.dto;


import javax.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TokenDto {

    @NotNull
    private String token;
    @NotNull
    private long tokenExpiresTime;

    public TokenDto(String token, long tokenExpiresTime) {
        this.token = token;
        this.tokenExpiresTime = tokenExpiresTime;
    }

    public String getToken() {
        return this.token;
    }

    public long getTokenExpiresTime() {
        return this.tokenExpiresTime;
    }
}
