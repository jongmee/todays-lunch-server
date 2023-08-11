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

  public static LocationTagDto fromEntity(LocationTag locationTag){
    return LocationTagDto.builder()
        .id(locationTag.getId())
        .name(locationTag.getName())
        .build();
  }
}
