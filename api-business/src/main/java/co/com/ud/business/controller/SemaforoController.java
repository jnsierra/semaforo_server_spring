package co.com.ud.business.controller;

import co.com.ud.business.bean.ManageTrafficLights;
import co.com.ud.business.service.CargarJsonService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author sierraj
 */
@RestController
@RequestMapping("/v.1/semaforo")
public class SemaforoController {

    private CargarJsonService cargarJsonService;
    private ManageTrafficLights manageTrafficLights;
    
    @Autowired
    public SemaforoController(CargarJsonService cargarJsonService, ManageTrafficLights manageTrafficLights) {
        this.cargarJsonService = cargarJsonService;
        this.manageTrafficLights = manageTrafficLights;
    }
    
    @PostMapping(value = "/{interseccion}/")
    public ResponseEntity<Boolean> cargarDocumentos(@PathVariable("interseccion") String interseccion){
        Optional<Boolean> response = cargarJsonService.cargoJson(interseccion);
        if(response.isPresent()){
            return new ResponseEntity<>(response.get() ,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping(value = "/")
    public ResponseEntity<Boolean> iniciarRecoleccionClientes(){
        Optional<Boolean> response = manageTrafficLights.arrancaHilos();
        if(response.isPresent()){
            return new ResponseEntity<>(response.get() ,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}