package LikeLion.TodaysLunch.domain.relation;

import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.Menu;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MemberMenuRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Member member;
    @ManyToOne
    @JoinColumn
    private Menu menu;

}
