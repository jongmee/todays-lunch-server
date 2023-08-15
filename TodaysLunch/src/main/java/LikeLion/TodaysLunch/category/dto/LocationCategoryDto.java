package LikeLion.TodaysLunch.category.dto;

import LikeLion.TodaysLunch.category.domain.LocationCategory;
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
  private Double latitude;
  private Double longitude;

  public static LocationCategoryDto fromEntity(LocationCategory locationCategory){
    return LocationCategoryDto.builder()
        .id(locationCategory.getId())
        .name(locationCategory.getName())
        .latitude(locationCategory.getLatitude())
        .longitude(locationCategory.getLongitude())
        .build();
  }

  public LocationCategory toEntity(){
    return LocationCategory.builder()
        .name(name)
        .latitude(latitude)
        .longitude(longitude)
        .build();
  }
}
