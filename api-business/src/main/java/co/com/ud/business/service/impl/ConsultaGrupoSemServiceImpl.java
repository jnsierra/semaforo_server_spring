package co.com.ud.business.service.impl;

import co.com.ud.business.bean.ManageTrafficLights;
import co.com.ud.business.service.ConsultaGrupoSemService;
import co.com.ud.utiles.dto.MensajeBrokerDto;
import co.com.ud.utiles.enumeration.EstadoGrupoSemaforicoEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Usuario
 */
@Service
@Slf4j
public class ConsultaGrupoSemServiceImpl implements ConsultaGrupoSemService {

    private ManageTrafficLights manageTrafficLights;
    private ObjectMapper objectMapper;
    
    @Autowired
    public ConsultaGrupoSemServiceImpl(ManageTrafficLights manageTrafficLights,
            ObjectMapper objectMapper) {
        this.manageTrafficLights = manageTrafficLights;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<String> sendEstadoGrupoSemaforico(MensajeBrokerDto mensajeBrokerDto) {
        try {
            EstadoGrupoSemaforicoEnum estado = this.getEstadoSemaforico();            
            Optional<MensajeBrokerDto> respuesta = Optional.of( MensajeBrokerDto.builder()
                    .idInterseccion(mensajeBrokerDto.getIdInterseccion())
                    .idTransaccion(mensajeBrokerDto.getIdTransaccion())
                    .mensaje(estado.toString())
                    .build() );
            String rtaJson = "MSNRTACONSULTAESTADO|" + objectMapper.writeValueAsString(respuesta.get());
            return Optional.of(rtaJson);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ConsultaGrupoSemServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Optional.empty();
    }
    
    public EstadoGrupoSemaforicoEnum getEstadoSemaforico(){
        EstadoGrupoSemaforicoEnum estado = null;
        int numConexiones = getConexionesActivas();
            if (numConexiones == 0) {
                estado = EstadoGrupoSemaforicoEnum.ESPERA_CONEXIONES;
            }
            if(numConexiones > 0 && numConexiones < getNumConPlan()){
                estado = EstadoGrupoSemaforicoEnum.ESPERA_CONEXIONES;
            }
            if (numConexiones == getNumConPlan()) {
                if (validarEjecutando()) {
                    estado = EstadoGrupoSemaforicoEnum.CORRIENDO;
                } else {
                    estado = EstadoGrupoSemaforicoEnum.CONEXIONES_COMPLETAS;
                }
            }
       return estado;
    }
    
    private Integer getNumConPlan(){
        return this.manageTrafficLights.getPlanSemaforicoDto().getNumeroCentral();
    }
    
    private Boolean validarEjecutando(){
        if(Objects.nonNull(this.manageTrafficLights.getEjecucionCicloLogico())){
            return this.manageTrafficLights.getEjecucionCicloLogico().getContinuar();
        }
        return Boolean.FALSE;
    }
    
    private Integer getConexionesActivas(){
        if(Objects.nonNull(this.manageTrafficLights.getEnvioMensajesLogica().getCentralesSemaforicas()) 
                && !this.manageTrafficLights.getEnvioMensajesLogica().getCentralesSemaforicas().isEmpty()){
            return this.manageTrafficLights.getEnvioMensajesLogica().getCentralesSemaforicas().size();
        }
        return 0;
    }

    @Override
    public Optional<String> sendNumConexionesGrupoSemaforico(MensajeBrokerDto mensajeBrokerDto) {
        try {
            int numConexiones = getConexionesActivas();            
            Optional<MensajeBrokerDto> respuesta = Optional.of( MensajeBrokerDto.builder()
                    .idInterseccion(mensajeBrokerDto.getIdInterseccion())
                    .idTransaccion(mensajeBrokerDto.getIdTransaccion())
                    .mensaje(numConexiones+"")
                    .build() );
            String rtaJson = "MSNRTACONSULTANUMCON|" + objectMapper.writeValueAsString(respuesta.get());
            return Optional.of(rtaJson);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ConsultaGrupoSemServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> sendTiempoEjecucionGrupoSemaforico(MensajeBrokerDto mensajeBrokerDto) {
        try {
            int time = this.manageTrafficLights.getTiempoEjecucion().get();
            Optional<MensajeBrokerDto> respuesta = Optional.of( MensajeBrokerDto.builder()
                    .idInterseccion(mensajeBrokerDto.getIdInterseccion())
                    .idTransaccion(mensajeBrokerDto.getIdTransaccion())
                    .mensaje(time+"")
                    .build() );
            String rtaJson = "MSNRTACONSULTATIEMEJECUCION|" + objectMapper.writeValueAsString(respuesta.get());
            return Optional.of(rtaJson);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ConsultaGrupoSemServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> ejecutarGrpSemaforico(MensajeBrokerDto mensajeBrokerDto) {
        Optional<String> response = this.manageTrafficLights.ejecutarSemaforo();
        if(response.isPresent()){
            log.info("Esta es la respuesta {}", response.get());
        }
        Boolean responseB = Boolean.FALSE;
        if(response.isPresent()){
            if("Ok".equalsIgnoreCase(response.get())){
                responseB = Boolean.TRUE;
            }
        }
        Optional<MensajeBrokerDto> respuesta = Optional.of( MensajeBrokerDto.builder()
                    .idInterseccion(mensajeBrokerDto.getIdInterseccion())
                    .idTransaccion(mensajeBrokerDto.getIdTransaccion())
                    .mensaje(responseB.toString())
                    .build() );
        String rtaJson;
        try {
            rtaJson = "MSNRTAEJECUTARGRPSEMAFORICO|" + objectMapper.writeValueAsString(respuesta.get());
            return Optional.of(rtaJson);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ConsultaGrupoSemServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Optional.empty();        
    }

}
