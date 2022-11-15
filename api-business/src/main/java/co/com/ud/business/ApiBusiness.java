package co.com.ud.business;

import co.com.ud.business.broker.MensajesBroker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * @author sierraj
 */
@SpringBootApplication
@ComponentScan("co.com.ud")
public class ApiBusiness implements CommandLineRunner{

    @Autowired
    private MensajesBroker mensajesBroker;
    
    public static void main(String[] args) {
        SpringApplication.run(ApiBusiness.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        this.mensajesBroker.iniciarHilo();
    }
}