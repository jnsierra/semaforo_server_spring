/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.com.ud.business.bean;

import java.util.Optional;

/**
 *
 * @author sierraj
 */
public interface ConsultaSemaforo {
    
    Optional<Integer> consultaCicloActual();
    
    Optional<Integer> consultaSegundoActual();
    
}
