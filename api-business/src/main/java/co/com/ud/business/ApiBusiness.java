package co.com.ud.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * @author sierraj
 */
@SpringBootApplication
@ComponentScan("co.com.ud")
public class ApiBusiness {

    public static void main(String[] args) {
        SpringApplication.run(ApiBusiness.class, args);
    }
}
