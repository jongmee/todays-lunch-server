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
public class ContributeRestaurantDto {

    private List<EngagedRestaurantDto> contribution;
    private long contributionCount;
    private int totalPages;

    public static ContributeRestaurantDto create(
            List<EngagedRestaurantDto> contribution, long contributionCount, int totalPages){
        return ContributeRestaurantDto.builder()
                .contribution(contribution)
                .contributionCount(contributionCount)
                .totalPages(totalPages)
                .build();
    }
}
