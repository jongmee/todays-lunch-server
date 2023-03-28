package LikeLion.TodaysLunch.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class JudgeRestaurantCreateDto {
  @NotBlank(message = "맛집 이름은 Null과 공백일 수 없습니다!")
  private String restaurantName;
  @NotBlank(message = "맛집의 도로명 주소는 Null과 공백일 수 없습니다!")
  private String address;
  private String introduction;
  @NotBlank(message = "음식 카테고리 이름은 Null과 공백일 수 없습니다!")
  private String foodCategoryName;
  private List<Long> recommendCategoryIds;
  @NotBlank(message = "위도는 Null과 공백일 수 없습니다!")
  private Double latitude;
  @NotBlank(message = "경도는 Null과 공백일 수 없습니다!")
  private Double longitude;

}
