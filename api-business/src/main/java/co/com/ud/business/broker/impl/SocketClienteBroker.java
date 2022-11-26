package co.com.ud.business.broker.impl;

import co.com.ud.business.bean.ManageTrafficLights;
import co.com.ud.business.service.CargarJsonService;
import co.com.ud.business.service.ConsultaGrupoSemService;
import co.com.ud.utiles.dto.MensajeBrokerDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Usuario
 */
@Slf4j
public class SocketClienteBroker extends Thread {

    private Socket socket;
    private String ip;
    private int puerto;
    private DataInputStream entradaDatos = null;
    private DataOutputStream salidaDatos;

    @Getter
    @Setter
    private Integer idCentral;

    private CargarJsonService cargarJsonService;
    private ManageTrafficLights manageTrafficLights;
    private ConsultaGrupoSemService consultaGrupoSemService;
    private ManageInMsnBrokerImpl manageInMsnBrokerImpl;

    public SocketClienteBroker(String ip, 
            int puerto, 
            CargarJsonService cargarJsonService,
            ManageTrafficLights manageTrafficLights,
            ConsultaGrupoSemService consultaGrupoSemService) {
        this.ip = ip;
        this.puerto = puerto;
        this.cargarJsonService = cargarJsonService;
        this.manageTrafficLights = manageTrafficLights;
        this.consultaGrupoSemService = consultaGrupoSemService;
        this.manageInMsnBrokerImpl = new ManageInMsnBrokerImpl();
        this.connectServer();
    }

    private void connectServer() {
        try {
            this.socket = new Socket(this.ip, this.puerto);
            log.info("Se conecta con el servidor {} con el puerto: {}", this.ip, this.puerto);
            entradaDatos = new DataInputStream(socket.getInputStream());
            salidaDatos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(SocketClienteBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        int i = 0;
        String mensaje = "";
        while (true) {
            try {
                mensaje = entradaDatos.readUTF();
                log.info("Este es el mensaje de entrada: {}", mensaje);
                this.manageMsn(mensaje);
                log.info("Esta es la iteracion: {} ", i);
            } catch (IOException ex) {
                log.error(null, ex);
            }
            i++;
        }
    }

    public void manageMsn(String msn) {
        Optional<String> tipo = manageInMsnBrokerImpl.tipoMsn(msn);
        if (tipo.isPresent()) {
            if ("SISTEMA".equalsIgnoreCase(tipo.get())) {
                this.ejecutarInicioSistema(msn);
            } else if ("CONSULTA".equalsIgnoreCase(tipo.get())) {
                //Obtenemos el mensaje enviado
                Optional<MensajeBrokerDto> mensaje = manageInMsnBrokerImpl.generateMsn(manageInMsnBrokerImpl.extracItemString(1, msn));
                if(mensaje.isPresent()){
                    this.ejecutaAccion(mensaje.get());
                }
                log.info("Mensaje no valido: {} ", msn);
            }
        }
        log.info("Mensaje: ( {} ), no identificado.", msn);
    }

    public void ejecutarInicioSistema(String msn) {
        if ("ID".equalsIgnoreCase(manageInMsnBrokerImpl.extracItemString(1, msn))) {
            //Seteamos el Id del grupo semaforico
            this.setIdCentral(Integer.valueOf(manageInMsnBrokerImpl.extracItemString(2, msn)));
            //Cargamos el Json con el plan a ejecutar
            Optional<Boolean> response = cargarJsonService.cargoJson();
            log.info("Esta es la respuesta al cargar Json: {}", response.isPresent() ? response.get().toString() : "Error al cargar json");
            if (response.isPresent()) {
                response = cargarJsonService.cargarJsonSistema(this.getIdCentral());
                log.info("Esta es la respuesta al cargar Json Especifico: {}", response.isPresent() ? response.get().toString() : "Error al cargar json");
                if (response.isPresent()) {
                    response = manageTrafficLights.arrancaHilos();
                    log.info("Esta es la respuesta al arrancar los hilos: {}", response.isPresent() ? response.get().toString() : "Error al cargar json");
                    log.info("Este es el id {}", this.idCentral);
                }
            }
        }
    }
    
    public void ejecutaAccion(MensajeBrokerDto msn){
        Optional<String> mensajeRta = Optional.empty();
        switch (msn.getMensaje()) {
            case "MSNCONSULTAESTADO":
                mensajeRta = consultaGrupoSemService.sendEstadoGrupoSemaforico(msn);
                break;
            case "MSNCONSULTANUMCON":
                mensajeRta = consultaGrupoSemService.sendNumConexionesGrupoSemaforico(msn);
                break;
            case "MSNCONSULTATIEMEJECUCION":
                mensajeRta = consultaGrupoSemService.sendTiempoEjecucionGrupoSemaforico(msn);
                break;
            case "MSNEJECUTARGRPSEMAFORICO":
                mensajeRta = consultaGrupoSemService.ejecutarGrpSemaforico(msn);
                break;
            default:
                throw new AssertionError();
        }
        if (mensajeRta.isPresent()) {
            this.enviarMSNBroker(mensajeRta.get());
            return ;
        }
        log.info("No fue posible ejecutar la accion: {}", msn.toString());
    }

    public void enviarMSNBroker(String mensaje) {
        try {
            log.info("Se envia el siguiente msn: {} ", mensaje);
            // Envia el mensaje al cliente
            salidaDatos.writeUTF(mensaje);
            salidaDatos.flush();
            log.info("*******************************");
        } catch (IOException ex) {
            log.error("Error al enviar mensaje al cliente (" + ex.getMessage() + ").");
        }
    }
}