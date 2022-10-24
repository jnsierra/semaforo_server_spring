package co.com.ud.business.controller;

import co.com.ud.business.bean.ManageTrafficLights;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author sierraj
 */
@RestController
@RequestMapping("/v.1/executeSemaforo")
public class EjecucionSemaforoController {
    
    private ManageTrafficLights manageTrafficLights;

    @Autowired
    public EjecucionSemaforoController(ManageTrafficLights ManageTrafficLights) {
        this.manageTrafficLights = ManageTrafficLights;
    }
    
    
    @PostMapping(value = "/")
    public ResponseEntity<String> executeSemaforo(){
        Optional<String> response = manageTrafficLights.ejecutarSemaforo(); 
        if(response.isPresent()){
            return new ResponseEntity<>(response.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
