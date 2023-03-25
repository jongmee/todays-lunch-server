package LikeLion.TodaysLunch.domain.relation;

import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.Restaurant;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class MyStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn
    private Member member;
    @ManyToOne
    @JoinColumn
    private Restaurant restaurant;

    public MyStore(Member member, Restaurant restaurant){
        this.member = member;
        this.restaurant = restaurant;
    }
}
