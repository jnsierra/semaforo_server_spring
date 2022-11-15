package co.com.ud.business.service.impl;

import co.com.ud.business.bean.ManageTrafficLights;
import co.com.ud.business.service.ValidateService;
import co.com.ud.utiles.dto.RespuestaMensaje;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author sierraj
 */
@Component
public class ValidateServiceImpl implements ValidateService{
    
    private ManageTrafficLights manageTrafficLights;

    @Autowired
    public ValidateServiceImpl(ManageTrafficLights manageTrafficLights) {
        this.manageTrafficLights = manageTrafficLights;
    }
    
    @Override
    public Optional<RespuestaMensaje<Integer>>validaInterseccion(Integer interseccion) {
        Optional<Boolean> validate = manageTrafficLights.validaInterseccion(interseccion);
        RespuestaMensaje<Integer> respuesta = null;
        if(!validate.isPresent() ){
            respuesta = RespuestaMensaje.<Integer>builder()
                    .code(-1)
                    .mensaje("Proceso no iniciado")
                    .build();
        }else{
            if(!validate.get()){
                respuesta = RespuestaMensaje.<Integer>builder()
                    .code(-1)
                    .mensaje("Interseccion no usada en este server")
                    .build();
                
            }    
        }
        return Objects.isNull(respuesta) ? Optional.empty() : Optional.of(respuesta);
    }

    
}
