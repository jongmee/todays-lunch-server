package LikeLion.TodaysLunch.image.domain;

import LikeLion.TodaysLunch.menu.domain.Menu;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
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
  @JoinColumn
  @NotNull
  private Menu menu;

  @Builder
  public MenuImage(ImageUrl imagePk, Menu menu){
    this.imagePk = imagePk.getId();
    this.menu = menu;
  }
}
