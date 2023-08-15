package LikeLion.TodaysLunch.category.domain;

import LikeLion.TodaysLunch.category.dto.LocationTagDto;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationTag {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Double latitude;

  @Column(nullable = false)
  private Double longitude;

  public LocationTag update(LocationTagDto locationTagDto){
    this.name = locationTagDto.getName();
    this.latitude = locationTagDto.getLatitude();
    this.longitude = locationTagDto.getLongitude();
    return this;
  }

}
