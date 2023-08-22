package LikeLion.TodaysLunch.category.domain;

import LikeLion.TodaysLunch.category.dto.LocationCategoryDto;
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
public class LocationCategory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length=30)
  private String name;

  @Column(nullable = false)
  private Double latitude;

  @Column(nullable = false)
  private Double longitude;

  public LocationCategory update(LocationCategoryDto locationCategoryDto){
    this.name = locationCategoryDto.getName();
    this.latitude = locationCategoryDto.getLatitude();
    this.longitude = locationCategoryDto.getLongitude();
    return this;
  }

}
