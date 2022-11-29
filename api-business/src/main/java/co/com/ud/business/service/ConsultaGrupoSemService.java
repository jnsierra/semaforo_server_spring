/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.com.ud.business.service;

import co.com.ud.utiles.dto.MensajeBrokerDto;
import co.com.ud.utiles.enumeration.EstadoGrupoSemaforicoEnum;
import java.util.Optional;

/**
 *
 * @author Usuario
 */
public interface ConsultaGrupoSemService {

    Optional<String> sendEstadoGrupoSemaforico(MensajeBrokerDto mensajeBrokerDto);
    
    Optional<String> sendNumConexionesGrupoSemaforico(MensajeBrokerDto mensajeBrokerDto);
    
    Optional<String> sendTiempoEjecucionGrupoSemaforico(MensajeBrokerDto mensajeBrokerDto);
    
    Optional<String> ejecutarGrpSemaforico(MensajeBrokerDto mensajeBrokerDto);
    
    EstadoGrupoSemaforicoEnum getEstadoSemaforico();

}
