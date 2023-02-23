package LikeLion.TodaysLunch.dto;

import LikeLion.TodaysLunch.domain.Review;
import LikeLion.TodaysLunch.domain.Sale;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor
public class SaleDto {
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
  private LocalDate startDate;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
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
