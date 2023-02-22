package LikeLion.TodaysLunch.domain;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private String password;

    private LocationCategory locationCategory;
    private FoodCategory foodCategory;
    @OneToOne
    @JoinColumn
    private ImageUrl imageUrl;

    public Member(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

//    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "member")
//    private List<Review> review = new ArrayList<>();


    public void updateLocationCategory(String locationCategory) {
        this.locationCategory.setName(locationCategory);
    }


}
