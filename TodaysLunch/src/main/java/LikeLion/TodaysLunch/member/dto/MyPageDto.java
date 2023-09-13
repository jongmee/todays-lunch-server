package LikeLion.TodaysLunch.member.dto;

import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.category.dto.FoodCategoryDto;
import LikeLion.TodaysLunch.category.dto.LocationCategoryDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageDto {

  private Long id;
  private String email;
  private String nickname;
  private String icon;
  private List<FoodCategoryDto> foodCategoryList;
  private List<LocationCategoryDto> locationCategoryList;
  private Integer myJudgeCount;
  private Integer  participationCount;
  private Integer contributionCount;
  private Integer myStoreCount;
  private Integer reviewCount;

  public static MyPageDto fromEntity(
      Member member,
      List<FoodCategoryDto> foodCategoryList,
      List<LocationCategoryDto> locationCategoryList,
      Integer myJudgeCount,
      Integer participationCount,
      Integer myStoreCount,
      Integer reviewCount,
      Integer contributionCount){
    String image = null;
    if (member.getIcon() != null)
      image = member.getIcon().getImageUrl();

    return MyPageDto.builder()
        .id(member.getId())
        .email(member.getEmail())
        .nickname(member.getNickname())
        .icon(image)
        .foodCategoryList(foodCategoryList)
        .locationCategoryList(locationCategoryList)
        .myJudgeCount(myJudgeCount)
        .participationCount(participationCount)
        .myStoreCount(myStoreCount)
        .reviewCount(reviewCount)
        .contributionCount(contributionCount)
        .build();
  }

}
