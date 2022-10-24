/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.com.ud.utiles.dto;

import java.util.List;
import lombok.Data;

/**
 *
 * @author sierraj
 */
@Data
public class GrupoSemaforicoDto {
    
    private Integer nro;
    private String nombre;
    private List<SemaforoDto> semaforos;
    private List<PasoDto> pasos;
    
}
