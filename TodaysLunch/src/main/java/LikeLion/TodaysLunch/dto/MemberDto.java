package LikeLion.TodaysLunch.dto;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.ImageUrl;
import LikeLion.TodaysLunch.domain.LocationCategory;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MemberDto {
    @NotNull
    private String nickname;

    @NotNull
    private String password;

    private LocationCategory locationCategory;

    private FoodCategory foodCategory;

    private ImageUrl imageUrl;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLocationCategory(LocationCategory locationCategory) {
        this.locationCategory = locationCategory;
    }

    public void setFoodCategory(FoodCategory foodCategory) {
        this.foodCategory = foodCategory;
    }

    public void setImageUrl(ImageUrl imageUrl) {
        this.imageUrl = imageUrl;
    }
}
