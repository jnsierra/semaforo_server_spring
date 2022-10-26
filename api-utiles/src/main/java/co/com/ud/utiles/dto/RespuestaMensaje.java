/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.com.ud.utiles.dto;

import lombok.Builder;
import lombok.Data;

/**
 *
 * @author sierraj
 */
@Data
@Builder
public class RespuestaMensaje<T> {
    
    private Integer code;
    private String mensaje;
    private T respuesta;
    
}
