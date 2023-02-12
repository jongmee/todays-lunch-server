package LikeLion.TodaysLunch.domain;

import com.sun.istack.NotNull;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Sale {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotNull
  private LocalDateTime startDate;
  @NotNull
  private LocalDateTime endDate;
  /**
   * Todo: 유저 모델과의 mapping 필요
   */

}
