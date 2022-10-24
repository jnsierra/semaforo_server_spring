/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.com.ud.business.bean.impl;

import co.com.ud.business.bean.ManageTrafficLights;
import co.com.ud.utiles.dto.PlanSemaforicoDto;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author sierraj
 */
public class ManageTrafficLightsImpl implements ManageTrafficLights{
    
    @Getter @Setter
    private PlanSemaforicoDto planSemaforicoDto;    
    private ServerSemaforo serverSemaforo;
    private EnvioMensajesLogica envioMensajesLogica;
    @Getter
    private EjecucionCicloLogico ejecucionCicloLogico;

    
    public ManageTrafficLightsImpl(ServerSemaforo serverSemaforo, EnvioMensajesLogica envioMensajesLogica) {
        this.serverSemaforo = serverSemaforo;
        this.envioMensajesLogica = envioMensajesLogica;
    }
    
    
    public Optional<Boolean> arrancaHilos(){
        serverSemaforo.setPlanSemaforicoDto(getPlanSemaforicoDto());
        this.serverSemaforo.setNumConexiones(getPlanSemaforicoDto().getNumeroCentral());
        serverSemaforo.start();
        return Optional.of(Boolean.TRUE);
    }

    @Override
    public Optional<String> ejecutarSemaforo() {
        //Agrego el numero de hilos que se deben acceder
        this.serverSemaforo.setNumConexiones(getPlanSemaforicoDto().getNumeroCentral());
        ejecucionCicloLogico = new EjecucionCicloLogico(envioMensajesLogica, getPlanSemaforicoDto());
        if(Objects.nonNull(getPlanSemaforicoDto())){
            if(ejecucionCicloLogico.validateConnections()){
                ejecucionCicloLogico.setContinuar(Boolean.TRUE);
                ejecucionCicloLogico.start();
            }
            return Optional.of("No existen las conexiones necesarias para iniciar");
        }
        return Optional.of("No se a cargado el plan a ejecutar");
    }   
}