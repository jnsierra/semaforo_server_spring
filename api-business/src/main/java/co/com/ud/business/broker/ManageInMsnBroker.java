/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.com.ud.business.broker;

import co.com.ud.utiles.dto.MensajeBrokerDto;
import java.util.Optional;

/**
 *
 * @author Jesus.Sierra
 */
public interface ManageInMsnBroker {
    
    Optional<String> tipoMsn(String msn);
    
    String extracItemString(Integer item, String msn);
    
    Optional<MensajeBrokerDto>  generateMsn(String msn);
    
}