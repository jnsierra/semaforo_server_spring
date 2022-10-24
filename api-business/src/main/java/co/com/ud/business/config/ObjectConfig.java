package co.com.ud.business.config;

import co.com.ud.business.bean.ManageTrafficLights;
import co.com.ud.business.bean.impl.EnvioMensajesLogica;
import co.com.ud.business.bean.impl.ManageTrafficLightsImpl;
import co.com.ud.business.bean.impl.ServerSemaforo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author sierraj
 */
@Configuration
public class ObjectConfig {
    
    @Bean
    @Scope("singleton")
    public ManageTrafficLights getManageTrafficLight(@Qualifier("serverSemaforo") ServerSemaforo serverSemaforo,
            @Qualifier("envioMsn") EnvioMensajesLogica envioMensajesLogica){
        return new ManageTrafficLightsImpl(serverSemaforo, envioMensajesLogica);
    }
    
    @Bean
    public ObjectMapper getObjectMapper(){
        return new ObjectMapper();
    }
    
    @Bean("envioMsn")
    @Scope("singleton")
    public EnvioMensajesLogica getEnvioMensajes(){
        return new EnvioMensajesLogica();
    }
    
    @Bean("serverSemaforo")
    @Scope("singleton")
    public ServerSemaforo getServerSemaforo(@Value("${configuration.puerto}") int puerto,
            @Qualifier("envioMsn") EnvioMensajesLogica envioMensajesLogica){
        return new ServerSemaforo(puerto, envioMensajesLogica);
    }
    
}