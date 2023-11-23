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
  private static final double EARTH_RADIUS = 6370d;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length=30)
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

  public double getDistance(double latitude, double longitude) {
    double latitudeDifference = Math.toRadians(latitude - this.latitude);
    double longitudeDifference = Math.toRadians(longitude - this.longitude);
    double raw = Math.cos(Math.toRadians(latitude))
            * Math.cos(Math.toRadians(this.latitude))
            * Math.pow(Math.sin(longitudeDifference),2)
            + Math.pow(Math.sin(latitudeDifference/2), 2);
    return EARTH_RADIUS * 2 * Math.atan2(Math.sqrt(raw), Math.sqrt(1-raw));
  }
}
