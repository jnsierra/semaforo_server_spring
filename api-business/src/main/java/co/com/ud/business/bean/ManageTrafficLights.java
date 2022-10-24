/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.com.ud.business.bean;

import co.com.ud.business.bean.impl.EjecucionCicloLogico;
import co.com.ud.utiles.dto.PlanSemaforicoDto;
import java.util.Optional;

/**
 *
 * @author sierraj
 */
public interface ManageTrafficLights {
    
    void setPlanSemaforicoDto(PlanSemaforicoDto planSemaforicoDto);
    
    Optional<Boolean> arrancaHilos();
    
    Optional<String> ejecutarSemaforo();
    
    EjecucionCicloLogico getEjecucionCicloLogico();
    
}
