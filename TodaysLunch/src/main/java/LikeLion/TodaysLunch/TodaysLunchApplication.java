package LikeLion.TodaysLunch;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"LikeLion.TodaysLunch.token","LikeLion.TodaysLunch.config"})
public class TodaysLunchApplication/* implements CommandLineRunner*/ {

    /*@Override
    public void run(String... args) throws Exception{
        String [] beans = applicationContext.getBeanDefinitionNames();
        Arrays.sort(beans);
        for(String bean: beans){
            System.out.println(bean);
        }
    }*/
    public static void main(String[] args) {
        SpringApplication.run(TodaysLunchApplication.class, args);
    }

    @Autowired
    private ApplicationContext applicationContext;

}
