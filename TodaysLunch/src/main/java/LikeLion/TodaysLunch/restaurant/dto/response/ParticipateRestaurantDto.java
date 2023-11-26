package LikeLion.TodaysLunch.restaurant.dto.response;

import LikeLion.TodaysLunch.restaurant.dto.response.common.EngagedRestaurantDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipateRestaurantDto {

  private List<EngagedRestaurantDto> participation;
  private long participationCount;
  private int totalPages;

  public static ParticipateRestaurantDto create(
          List<EngagedRestaurantDto> participation, long participationCount, int totalPages){
    return ParticipateRestaurantDto.builder()
            .participation(participation)
            .participationCount(participationCount)
            .totalPages(totalPages)
            .build();
  }
}
