package LikeLion.TodaysLunch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"LikeLion.TodaysLunch.token"})
public class TodaysLunchApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodaysLunchApplication.class, args);
	}

}
