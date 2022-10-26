package co.com.ud.business.service.impl;

import co.com.ud.business.bean.ManageTrafficLights;
import co.com.ud.business.service.CargarJsonService;
import co.com.ud.utiles.dto.PlanSemaforicoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author sierraj
 */
@Component
public class CargarJsonServiceImpl implements CargarJsonService{

    private ManageTrafficLights manageTrafficLights;
    private ObjectMapper objectMapper;
    
    
    @Autowired
    public CargarJsonServiceImpl(ManageTrafficLights manageTrafficLights,
            ObjectMapper objectMapper) {
        this.manageTrafficLights = manageTrafficLights;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<Boolean> cargoJson(String interseccion) {
        try {
            PlanSemaforicoDto planSemaforicoDto = objectMapper.readValue(new File(interseccionToPathJsonFile(interseccion)), PlanSemaforicoDto.class);
            manageTrafficLights.setPlanSemaforicoDto(planSemaforicoDto);
            manageTrafficLights.setIdInterseccion(Integer.valueOf(interseccion));
        } catch (IOException ex) {
            ex.printStackTrace();
            return Optional.of(Boolean.FALSE);
        }
        return Optional.of(Boolean.TRUE);
    }
    
    private String interseccionToPathJsonFile(String interseccion){
        if("1".equalsIgnoreCase(interseccion)){
            return "src/main/resources/plan_semaforico_1.json";
        }
        if("2".equalsIgnoreCase(interseccion)){
            return "src/main/resources/plan_semaforico_2.json";
        }
        if("3".equalsIgnoreCase(interseccion)){
            return "src/main/resources/plan_semaforico_3.json";
        }
        return "";
    }
    
}
