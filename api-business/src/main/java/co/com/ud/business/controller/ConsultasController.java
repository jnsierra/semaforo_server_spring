package co.com.ud.business.controller;

import co.com.ud.business.bean.ConsultaSemaforo;
import co.com.ud.business.bean.ManageTrafficLights;
import co.com.ud.business.service.ValidateService;
import co.com.ud.utiles.dto.InterseccionDto;
import co.com.ud.utiles.dto.RespuestaMensaje;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author sierraj
 */
@Slf4j
@RestController
@RequestMapping("/v.1/consultaSemaforo")
public class ConsultasController {
    
    private ConsultaSemaforo consultaSemaforo;
    private ManageTrafficLights manageTrafficLights;
    private ValidateService validateService;

    @Autowired
    public ConsultasController(ConsultaSemaforo consultaSemaforo
            , ManageTrafficLights manageTrafficLights
            , ValidateService validateService) {
        this.consultaSemaforo = consultaSemaforo;
        this.manageTrafficLights = manageTrafficLights;
        this.validateService = validateService;
    }
    
    @GetMapping(value = "/ciclo/{interseccion}/")
    public ResponseEntity<RespuestaMensaje> consultaCiclo(@PathVariable("interseccion") Integer interseccion){
        Optional<RespuestaMensaje<Integer>> valida = validateService.validaInterseccion(interseccion);
        if(valida.isPresent()){
            return new ResponseEntity<>(valida.get(), HttpStatus.OK);
        }
        Optional<Integer> response = consultaSemaforo.consultaCicloActual();
        if(response.isPresent()){
            RespuestaMensaje<Integer> respuesta = RespuestaMensaje.<Integer>builder()
                    .code(1)
                    .mensaje("Ok")
                    .respuesta(response.get())
                    .build();
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping(value = "/iterador/")
    public ResponseEntity<Integer> consultaIterador(){
        Optional<Integer> response = consultaSemaforo.consultaSegundoActual();
        if(response.isPresent()){
            return new ResponseEntity<>(response.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping(value = "/interseccion/")
    public ResponseEntity<List<InterseccionDto>> getIntersecciones(){
        Optional<List<InterseccionDto>> response = consultaSemaforo.getListIntersecciones();
        if(response.isPresent()){
            return new ResponseEntity<>(response.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @GetMapping(value = "/numclientsconnect/")
    public ResponseEntity<RespuestaMensaje<Integer>> getClientConnect(){
        Optional<Integer> response = this.manageTrafficLights.obtenerNumeroConexionesCliente();
        if(response.isPresent()){
            return new ResponseEntity<>(RespuestaMensaje.<Integer>builder()
                    .code(1)
                    .mensaje("OK")
                    .respuesta(response.get())
                    .build(),  HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping(value = "/tiempo/")
    public ResponseEntity<RespuestaMensaje<Integer>> getTiempo(){
        int time = this.manageTrafficLights.getTiempoEjecucion().get();
        log.info("Llego al tiempo: {} ", time);
        return new ResponseEntity<>(RespuestaMensaje.<Integer>builder()
                .respuesta(time)
                .mensaje("Ok")
                .code(200)
        .build(), HttpStatus.OK);
    }
}