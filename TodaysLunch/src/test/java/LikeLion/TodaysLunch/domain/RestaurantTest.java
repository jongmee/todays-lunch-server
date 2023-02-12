package LikeLion.TodaysLunch.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class RestaurantTest {
  @Autowired private  RestaurantRepository restaurantRepository;
  @Test
  public void insertNotNull(){
    Restaurant restaurant = new Restaurant();
    restaurant.setRestaurantName("first");
    Restaurant result = restaurantRepository.save(restaurant);
    assertThat(result.getJudgement()).isEqualTo(false);
    assertThat(result.getRating()).isEqualTo(0);
    assertThat(result.getRestaurantRecmd()).isEqualTo(0);
  }

}