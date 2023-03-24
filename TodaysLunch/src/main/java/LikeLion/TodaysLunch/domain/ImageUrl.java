package LikeLion.TodaysLunch.domain;

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
public class ImageUrl {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String originalName;
  @Column(nullable = false)
  private String imageUrl;

  @ManyToOne
  @JoinColumn
  private Menu menu;

  // 이미지 등록자
  @OneToOne
  @JoinColumn
  private Member member;
  @Builder
  public ImageUrl(String originalName, String imageUrl, Member member, Menu menu){
    this.originalName = originalName;
    this.imageUrl = imageUrl;
    this.member = member;
    this.menu = menu;
  }
}
