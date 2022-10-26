package co.com.ud.business.bean.impl;

import co.com.ud.business.bean.ConsultaSemaforo;
import co.com.ud.business.bean.ManageTrafficLights;
import co.com.ud.utiles.dto.InterseccionDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author sierraj
 */
@Component
public class ConsultaSemaforoImpl implements ConsultaSemaforo{
    
    private ManageTrafficLights manageTrafficLights;

    @Autowired
    public ConsultaSemaforoImpl(ManageTrafficLights manageTrafficLights) {
        this.manageTrafficLights = manageTrafficLights;
    }
    
    @Override
    public Optional<Integer> consultaCicloActual() {
        return Optional.of(manageTrafficLights.getEjecucionCicloLogico().getCiclo());
    }

    @Override
    public Optional<Integer> consultaSegundoActual() {
        return Optional.of(manageTrafficLights.getEjecucionCicloLogico().getIterador());
    }

    @Override
    public Optional<List<InterseccionDto>> getListIntersecciones() {
        ArrayList<InterseccionDto> listaIntersecciones = new ArrayList<>();
        listaIntersecciones.add(InterseccionDto.builder()
                .id("1")
                .nombre("Calle 34 Carrera 28")
                .build());
        listaIntersecciones.add(InterseccionDto.builder()
                .id("2")
                .nombre("Carrera 80 Diagonal 43 sur")
                .build());
        listaIntersecciones.add(InterseccionDto.builder()
                .id("3")
                .nombre("Calle 13 calle 41")
                .build());
        return Optional.of(listaIntersecciones);
    }

  
    
}
