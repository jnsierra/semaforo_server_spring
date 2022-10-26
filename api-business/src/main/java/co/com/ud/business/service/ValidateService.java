/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.com.ud.business.service;

import co.com.ud.utiles.dto.RespuestaMensaje;
import java.util.Optional;

/**
 *
 * @author sierraj
 */
public interface ValidateService {
    
    Optional<RespuestaMensaje<Integer>> validaInterseccion(Integer id);
    
}
