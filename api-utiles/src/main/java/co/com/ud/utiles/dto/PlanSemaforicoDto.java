/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.com.ud.utiles.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

/**
 *
 * @author sierraj
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanSemaforicoDto {
    
    private String nombreInterseccion;
    private Integer numeroCentral; 
    private List<GrupoSemaforicoDto> grpSemaforico;
    private Integer cicloIntersecion;
    
}
