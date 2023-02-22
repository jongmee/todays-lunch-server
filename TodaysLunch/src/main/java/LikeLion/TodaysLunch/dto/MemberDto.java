package LikeLion.TodaysLunch.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MemberDto {
    @NotNull
    private String nickname;

    @NotNull
    private String password;
}
