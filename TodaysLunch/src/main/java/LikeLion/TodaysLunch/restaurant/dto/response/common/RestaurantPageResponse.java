package LikeLion.TodaysLunch.restaurant.dto.response.common;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantPageResponse {
    private int totalPages;
    private List<?> data;

    public static RestaurantPageResponse create(final int totalPages, final List<?> data) {
        return RestaurantPageResponse.builder()
                .totalPages(totalPages)
                .data(data)
                .build();
    }
}
