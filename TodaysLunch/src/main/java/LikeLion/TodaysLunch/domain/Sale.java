package LikeLion.TodaysLunch.domain;

import com.sun.istack.NotNull;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Sale {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotNull
  private LocalDate startDate;
  @NotNull
  private LocalDate endDate;
  @NotNull
  private Long salePrice;

  /**
   * Todo: 유저 모델과의 mapping 필요
   */
  @Builder
  public Sale(LocalDate startDate, LocalDate endDate, Long salePrice) {
    this.startDate = startDate;
    this.endDate = endDate;
    this.salePrice = salePrice;
  }
}
