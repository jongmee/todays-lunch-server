package LikeLion.TodaysLunch.member.dto;

import LikeLion.TodaysLunch.category.domain.FoodCategory;
import LikeLion.TodaysLunch.image.domain.ImageUrl;
import LikeLion.TodaysLunch.category.domain.LocationCategory;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String nickname;

    private LocationCategory locationCategory;

    private FoodCategory foodCategory;

    private ImageUrl imageUrl;

}
