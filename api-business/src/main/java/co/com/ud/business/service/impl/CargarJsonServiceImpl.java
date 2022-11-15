package co.com.ud.business.service.impl;

import co.com.ud.business.bean.ManageTrafficLights;
import co.com.ud.business.service.CargarJsonService;
import co.com.ud.utiles.dto.PlanSemaforicoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author sierraj
 */
@Component("CargarJsonService")
@Scope("singleton")
public class CargarJsonServiceImpl implements CargarJsonService {

    private static final Logger log = LoggerFactory.getLogger(CargarJsonServiceImpl.class);

    private ManageTrafficLights manageTrafficLights;
    private ObjectMapper objectMapper;

    private PlanSemaforicoDto planSemaforico_Uno;
    private PlanSemaforicoDto planSemaforico_Dos;
    private PlanSemaforicoDto planSemaforico_Tres;

    @Autowired
    public CargarJsonServiceImpl(ManageTrafficLights manageTrafficLights,
            ObjectMapper objectMapper) {
        this.manageTrafficLights = manageTrafficLights;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<Boolean> cargoJson() {
        if (!this.validaObjetos()) {
            return Optional.of(Boolean.FALSE); 
        }
        return Optional.of(Boolean.TRUE);
    }

    public Boolean validaObjetos() {
        if (Objects.isNull(planSemaforico_Uno)) {
            this.planSemaforico_Uno = this.readFiles(1);
        }
        if (Objects.isNull(planSemaforico_Dos)) {
            this.planSemaforico_Dos = this.readFiles(2);
        }
        if (Objects.isNull(planSemaforico_Tres)) {
            this.planSemaforico_Tres = this.readFiles(2);
        }
        return Boolean.TRUE;
    }

    public PlanSemaforicoDto readFiles(Integer interseccion) {
        PlanSemaforicoDto planSemaforicoDto = null;
        try {
            planSemaforicoDto = objectMapper.readValue(new File(interseccionToPathJsonFile(interseccion.toString())), PlanSemaforicoDto.class);
            manageTrafficLights.setPlanSemaforicoDto(planSemaforicoDto);
            manageTrafficLights.setIdInterseccion(interseccion);
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return planSemaforicoDto;
    }

    private String interseccionToPathJsonFile(String interseccion) {
        if ("1".equalsIgnoreCase(interseccion)) {
            return "src/main/resources/plan_semaforico_1.json";
        }
        if ("2".equalsIgnoreCase(interseccion)) {
            return "src/main/resources/plan_semaforico_2.json";
        }
        if ("3".equalsIgnoreCase(interseccion)) {
            return "src/main/resources/plan_semaforico_3.json";
        }
        return "";
    }

    @Override
    public Optional<Boolean> cargarJsonSistema(Integer interseccion) {
        if(Objects.isNull(planSemaforico_Uno)){
            return Optional.of(Boolean.FALSE);
        }
        if(interseccion.equals(1)){
            this.manageTrafficLights.setPlanSemaforicoDto(planSemaforico_Uno);
        }
        if(interseccion.equals(2)){
            this.manageTrafficLights.setPlanSemaforicoDto(planSemaforico_Dos);
        }
        if(interseccion.equals(3)){
            this.manageTrafficLights.setPlanSemaforicoDto(planSemaforico_Tres);
        }
        this.manageTrafficLights.setIdInterseccion(interseccion);
        return Optional.of(Boolean.TRUE);
    }

}
