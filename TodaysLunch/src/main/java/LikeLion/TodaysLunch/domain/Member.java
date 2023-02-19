package LikeLion.TodaysLunch.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

    @ManyToOne
    private LocationCategory locationCategory;
    @ManyToOne
    private FoodCategory foodCategory;

    public Member(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

//    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "member")
//    private List<Review> review = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

//    public List<Review> getReview() {
//        return review;
//    }

    public void updateLocationCategory(String locationCategory) {
        this.locationCategory = new LocationCategory(locationCategory);
    }


}
