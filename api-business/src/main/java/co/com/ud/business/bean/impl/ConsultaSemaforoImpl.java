/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.com.ud.business.bean.impl;

import co.com.ud.business.bean.ConsultaSemaforo;
import co.com.ud.business.bean.ManageTrafficLights;
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
    
}
