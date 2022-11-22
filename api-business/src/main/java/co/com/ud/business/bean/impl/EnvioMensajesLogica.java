package co.com.ud.business.bean.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 *
 * @author sierraj
 */
public class EnvioMensajesLogica {

    @Getter
    private List<ConexionClienteSemaforo> centralesSemaforicas;

    public EnvioMensajesLogica() {
        this.centralesSemaforicas = new ArrayList<>();
    }

    public void adicionarConexion(ConexionClienteSemaforo cc) {
        centralesSemaforicas.add(cc);
    }

    public Optional<Boolean> enviarMensaje(Integer idCliente, String mensaje) {
        if (Objects.nonNull(getCentralesSemaforicas()) && !centralesSemaforicas.isEmpty()) {
            List<ConexionClienteSemaforo> semaforo = centralesSemaforicas.stream().parallel()
                    .filter(item -> idCliente.equals(item.getIdCliente()))
                    .collect(Collectors.toList());
            if (Objects.nonNull(semaforo) && !semaforo.isEmpty()) {
                semaforo.forEach(item -> item.enviarMSN(mensaje));
                return Optional.of(Boolean.TRUE);
            }
            return Optional.of(Boolean.FALSE);
        }
        return Optional.empty();
    }
}
