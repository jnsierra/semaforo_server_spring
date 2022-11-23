package co.com.ud.business.bean.impl;

import co.com.ud.utiles.dto.GrupoSemaforicoDto;
import co.com.ud.utiles.dto.PasoDto;
import co.com.ud.utiles.dto.PlanSemaforicoDto;
import co.com.ud.utiles.service.ServerUtilities;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import lombok.Data;

/**
 *
 * @author sierraj
 */
@Data
public class EjecucionCicloLogico extends Thread {

    private PlanSemaforicoDto planSemaforicoDto;
    private EnvioMensajesLogica envioMsn;
    private Integer ciclo;
    private Integer iterador;
    private Boolean continuar;

    public EjecucionCicloLogico(EnvioMensajesLogica envioMsn, PlanSemaforicoDto planSemeforicoDto) {
        this.continuar = Boolean.FALSE;
        this.ciclo = 1;
        this.envioMsn = envioMsn;
        this.planSemaforicoDto = planSemeforicoDto;
        this.iterador = 0;
    }

    @Override
    public void run() {
        this.cicloArranque();
        while (continuar) {
            for (iterador = 1; iterador <= planSemaforicoDto.getCicloIntersecion() && continuar; iterador++) {
                
                this.enviaSeñal(planSemaforicoDto, envioMsn, iterador);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(EjecucionCicloLogico.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            ciclo++;
        }
        if(!continuar){
            apagarSemaforo();
        }
    }

    private void cicloArranque() {
        //Obtengo la lista de cetrales
        List<Integer> centrales = envioMsn.getCentralesSemaforicas().stream().parallel().map(item -> item.getIdCliente())
                .collect(Collectors.toList());
        for (int i = 0; i < 4; i++) {
            for (Integer item : centrales) {
                //Envio a todos los semaforos la señal de naranja intermitente
                envioMsn.enviarMensaje(item, "170");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(EjecucionCicloLogico.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void apagarSemaforo() {
        List<Integer> centrales = envioMsn.getCentralesSemaforicas().stream().parallel().map(item -> item.getIdCliente())
                .collect(Collectors.toList());
        for (Integer item : centrales) {
            //Envio a todos los semaforos la señal de naranja intermitente
            envioMsn.enviarMensaje(item, "0");
        }
    }

    public Boolean validateConnections() {
        if (Objects.nonNull(envioMsn)
                && !validacionNumeroConectados(planSemaforicoDto, envioMsn.getCentralesSemaforicas().size())) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean validacionNumeroConectados(PlanSemaforicoDto planSemaforicoDto, Integer numConectados) {
        if (numConectados.equals(planSemaforicoDto.getNumeroCentral())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean enviaSeñal(PlanSemaforicoDto planSemaforicoDto, EnvioMensajesLogica envioMsn, Integer tiempo) {
        //Iteramos sobre cada uno de los grupos semaforicos
        Integer numGrpSemaforicos = planSemaforicoDto.getNumeroCentral(); // Este es el numero de grupos semaforicos que existe para la simulacion
        for (int i = 0; i < numGrpSemaforicos; i++) {
            GrupoSemaforicoDto item = planSemaforicoDto.getGrpSemaforico().get(i);
            //ID a enviar
            Integer id = item.getNro();
            String mensaje = this.buscaMensajeLista(item, tiempo);
            if (Objects.nonNull(mensaje)) {
                mensaje = ServerUtilities.eliminarCerosIzq(mensaje);
                int decimal = Integer.parseInt(mensaje, 2);
                envioMsn.enviarMensaje(id, "" + decimal);
            }
        }
        return Boolean.TRUE;
    }

    public String buscaMensajeLista(GrupoSemaforicoDto grupoSemaforicoDto, Integer tiempo) {
        Optional<PasoDto> item = grupoSemaforicoDto.getPasos().stream().parallel().filter(s -> tiempo.equals(s.getTiempo()))
                .findFirst();
        if (item.isPresent()) {
            return item.get().getAccion();
        }
        return null;
    }

}
