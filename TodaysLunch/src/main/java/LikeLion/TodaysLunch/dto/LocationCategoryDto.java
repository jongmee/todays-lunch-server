package LikeLion.TodaysLunch.dto;

import LikeLion.TodaysLunch.domain.LocationCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationCategoryDto {
  private Long id;
  private String name;
  public static LocationCategoryDto fromEntity(LocationCategory locationCategory){
    return LocationCategoryDto.builder()
        .id(locationCategory.getId())
        .name(locationCategory.getName())
        .build();
  }
  public LocationCategory toEntity(){
    return LocationCategory.builder()
        .id(id)
        .name(name)
        .build();
  }
}
