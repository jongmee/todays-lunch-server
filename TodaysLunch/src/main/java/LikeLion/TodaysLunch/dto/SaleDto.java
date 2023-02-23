package LikeLion.TodaysLunch.dto;

import LikeLion.TodaysLunch.domain.Review;
import LikeLion.TodaysLunch.domain.Sale;
import com.sun.istack.NotNull;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaleDto {
  private LocalDate startDate;
  private LocalDate endDate;
  private Long salePrice;

  @Builder
  public SaleDto(LocalDate startDate, LocalDate endDate, Long salePrice){
    this.startDate = startDate;
    this.endDate = endDate;
    this.salePrice = salePrice;
  }

  public Sale toEntity(){
    return Sale.builder().startDate(startDate).endDate(endDate).salePrice(salePrice).build();
  }
}
