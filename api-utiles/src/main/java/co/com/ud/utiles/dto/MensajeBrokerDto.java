/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.com.ud.utiles.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Usuario
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MensajeBrokerDto {
    
    private Integer idTransaccion;
    private Integer idInterseccion;
    private String mensaje;
    
}
