/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.com.ud.business.controller;

import co.com.ud.business.bean.ConsultaSemaforo;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author sierraj
 */
@RestController
@RequestMapping("/v.1/consultaSemaforo")
public class ConsultasController {
    
    private ConsultaSemaforo consultaSemaforo;

    @Autowired
    public ConsultasController(ConsultaSemaforo consultaSemaforo) {
        this.consultaSemaforo = consultaSemaforo;
    }
    
    @GetMapping(value = "/ciclo/")
    public ResponseEntity<Integer> consultaCiclo(){
        Optional<Integer> response = consultaSemaforo.consultaCicloActual();
        if(response.isPresent()){
            return new ResponseEntity<>(response.get(), HttpStatus.OK);
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
}