package LikeLion.TodaysLunch.domain;

import com.sun.istack.NotNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Menu {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String name;
  private Long price;
  private Long imageCount;
  @ManyToOne
  @JoinColumn
  private Restaurant restaurant;
  @OneToOne
  @JoinColumn
  private Sale sale;
  @Builder
  public Menu(String name, Long price){
    this.name = name;
    this.price = price;
    this.imageCount = 0L;
  }
}
