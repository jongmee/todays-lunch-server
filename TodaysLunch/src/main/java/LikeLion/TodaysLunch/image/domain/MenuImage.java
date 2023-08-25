package LikeLion.TodaysLunch.image.domain;

import LikeLion.TodaysLunch.menu.domain.Menu;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@NoArgsConstructor
@Getter
public class MenuImage {

  @Id
  private Long imagePk;

  @JoinColumn(nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.CASCADE)
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
