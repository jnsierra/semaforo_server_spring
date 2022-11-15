/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.com.ud.business.broker;

import java.util.Optional;

/**
 *
 * @author Usuario
 */
public interface MensajesBroker {
    Optional<Boolean> mensaje(String msn); 
    
    void iniciarHilo();
}