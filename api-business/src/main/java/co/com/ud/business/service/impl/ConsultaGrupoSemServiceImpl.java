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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Usuario
 */
@Service
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
            EstadoGrupoSemaforicoEnum estado = null;
            int numConexiones = getConexionesActivas();
            if (numConexiones == 0) {
                estado = EstadoGrupoSemaforicoEnum.ESPERA_CONEXIONES;
            }
            if (numConexiones == getNumConPlan()) {
                if (validarEjecutando()) {
                    estado = EstadoGrupoSemaforicoEnum.CORRIENDO;
                } else {
                    estado = EstadoGrupoSemaforicoEnum.CONEXIONES_COMPLETAS;
                }
            }
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
    
    private Integer getNumConPlan(){
        return this.manageTrafficLights.getPlanSemaforicoDto().getNumeroCentral();
    }
    
    private Boolean validarEjecutando(){
        return this.manageTrafficLights.getEjecucionCicloLogico().getContinuar();
    }
    
    private Integer getConexionesActivas(){
        if(Objects.nonNull(this.manageTrafficLights.getEnvioMensajesLogica().getCentralesSemaforicas()) 
                && !this.manageTrafficLights.getEnvioMensajesLogica().getCentralesSemaforicas().isEmpty()){
            return this.manageTrafficLights.getEnvioMensajesLogica().getCentralesSemaforicas().size();
        }
        return 0;
    }

}
