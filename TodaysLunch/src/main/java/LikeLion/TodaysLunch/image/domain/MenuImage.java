package LikeLion.TodaysLunch.image.domain;

import LikeLion.TodaysLunch.menu.domain.Menu;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class MenuImage {

  @Id
  private Long imagePk;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Menu menu;

  @Column(nullable = false)
  private Boolean isBest;

  @Builder
  public MenuImage(ImageUrl imagePk, Menu menu){
    this.imagePk = imagePk.getId();
    this.menu = menu;
    this.isBest = false;
  }

  public void setBest(Boolean best) {   isBest = best;  }
}
