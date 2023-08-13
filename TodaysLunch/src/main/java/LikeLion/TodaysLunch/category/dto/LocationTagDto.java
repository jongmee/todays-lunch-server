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

  public static LocationTagDto fromEntity(LocationTag locationTag){
    return LocationTagDto.builder()
        .id(locationTag.getId())
        .name(locationTag.getName())
        .latitude(locationTag.getLatitude())
        .longitude(locationTag.getLongitude())
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
