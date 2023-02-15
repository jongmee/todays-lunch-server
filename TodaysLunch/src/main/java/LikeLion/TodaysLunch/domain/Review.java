package LikeLion.TodaysLunch.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

@Entity
public class Review {
  @PrePersist
  public void prePersist() {
    this.rating = this.rating == null? 0:this.rating;
    this.reviewDecmd = this.reviewDecmd == null? 0:reviewDecmd;
    this.reviewRecmd = this.reviewRecmd == null? 0:reviewRecmd;
  }
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(length = 200, nullable = false)
  private String reviewContent;
  private Integer rating;
  private Long reviewRecmd;
  private Long reviewDecmd;

  @ManyToOne
  @JoinColumn(name="group")
  private Member member;

}
