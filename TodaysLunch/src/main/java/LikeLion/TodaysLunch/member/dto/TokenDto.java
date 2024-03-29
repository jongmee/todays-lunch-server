package LikeLion.TodaysLunch.member.dto;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

public class TokenDto {
    @Getter
    public static class LoginToken {

        private Long id;
        private Boolean temporaryPw;
        @NotNull
        private String accessToken;
        @NotNull
        private String refreshToken;
        @NotNull
        private long refreshTokenExpiresTime;

        @Builder
        public LoginToken(String accessToken, String refreshToken, long refreshTokenExpiresTime) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.refreshTokenExpiresTime = refreshTokenExpiresTime;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setTemporaryPw(Boolean temporaryPw){
            this.temporaryPw = temporaryPw;
        }

    }

    @Getter
    public static class Refresh {
        @NotEmpty(message="accessToken을 입력해주세요.")
        private String accessToken;
        @NotEmpty(message="refreshToken을 입력해주세요")
        private String refreshToken;
    }
}
