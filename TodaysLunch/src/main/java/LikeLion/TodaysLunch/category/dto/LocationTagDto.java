package LikeLion.TodaysLunch.category.dto;

import LikeLion.TodaysLunch.category.domain.LocationTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationTagDto {

  private Long id;
  private String name;
  private Double latitude;
  private Double longitude;
  private Long locationCategoryId;

  public static LocationTagDto fromEntity(LocationTag locationTag, Long locationCategoryId){
    return LocationTagDto.builder()
        .id(locationTag.getId())
        .name(locationTag.getName())
        .latitude(locationTag.getLatitude())
        .longitude(locationTag.getLongitude())
        .locationCategoryId(locationCategoryId)
        .build();
  }

  public LocationTag toEntity(){
    return LocationTag.builder()
        .name(name)
        .latitude(latitude)
        .longitude(longitude)
        .build();
  }
}
