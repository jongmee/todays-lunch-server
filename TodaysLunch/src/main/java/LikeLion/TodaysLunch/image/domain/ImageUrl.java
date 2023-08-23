package LikeLion.TodaysLunch.image.domain;

import LikeLion.TodaysLunch.member.domain.Member;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

  @Column(nullable = false, length = 300)
  private String originalName;

  @Column(nullable = false)
  private String imageUrl;

  // 이미지 등록자
  @JoinColumn
  @OneToOne(fetch = FetchType.LAZY)
  private Member member;

  @Builder
  public ImageUrl(String originalName, String imageUrl, Member member){
    this.originalName = originalName;
    this.imageUrl = imageUrl;
    this.member = member;
  }
}
