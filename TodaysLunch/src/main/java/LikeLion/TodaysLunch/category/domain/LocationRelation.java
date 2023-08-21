package LikeLion.TodaysLunch.category.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class LocationRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private LocationCategory locationCategory;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private LocationTag locationTag;

}
