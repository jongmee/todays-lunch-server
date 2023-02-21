package LikeLion.TodaysLunch.domain;

import com.sun.istack.NotNull;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
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

}
