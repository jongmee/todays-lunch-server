package LikeLion.TodaysLunch.domain;

import com.sun.istack.NotNull;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Menu {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotNull
  private String name;
  private Long price;
  @ManyToOne
  @JoinColumn
  private Restaurant restaurant;
  @OneToOne
  @JoinColumn
  private Sale sale;
  /**
   * Todo: 이미지 필드 추가
   */

}
