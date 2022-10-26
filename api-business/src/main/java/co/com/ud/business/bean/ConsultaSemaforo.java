package co.com.ud.business.bean;

import co.com.ud.utiles.dto.InterseccionDto;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author sierraj
 */
public interface ConsultaSemaforo {
    
    Optional<Integer> consultaCicloActual();
    
    Optional<Integer> consultaSegundoActual();
    
    Optional<List<InterseccionDto>> getListIntersecciones();
    
}
