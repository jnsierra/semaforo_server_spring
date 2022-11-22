/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
    
    private ObjectMapper objectMapper;
    
    private ConsultaGrupoSemService consultaGrupoSemService;
    
    public SocketClienteBroker(String ip, int puerto, CargarJsonService cargarJsonService
    , ManageTrafficLights manageTrafficLights
    , ObjectMapper objectMapper
    , ConsultaGrupoSemService consultaGrupoSemService) {
        this.ip = ip;
        this.puerto = puerto;
        this.cargarJsonService = cargarJsonService;
        this.manageTrafficLights = manageTrafficLights;
        this.objectMapper = objectMapper;
        this.consultaGrupoSemService = consultaGrupoSemService;
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
                this.interpretaMSN(mensaje);
                log.info("Esta es la iteracion: {} ", i);
            } catch (IOException ex) {
                log.error(null, ex);
            }
            i++;
        }
    }

    public void interpretaMSN(String msn) {
        if (Objects.nonNull(msn)) {
            log.info("Mensaje enviado: {}", msn);
            String[] parts = msn.split("\\|");
            if (parts.length > 0) {
                if ("MSNSISTEMA".equalsIgnoreCase(parts[0])) {
                    //Ejecutamos la accion del sistema
                    if ("ID".equalsIgnoreCase(parts[1])) {
                        this.setIdCentral(Integer.parseInt(parts[2]));
                        Optional<Boolean> response = cargarJsonService.cargoJson();
                        log.info("Esta es la respuesta al cargar Json: {}", response.isPresent() ? response.get().toString() : "Error al cargar json");
                        if (response.isPresent()) {
                            response = cargarJsonService.cargarJsonSistema(this.getIdCentral());
                            log.info("Esta es la respuesta al cargar Json Especifico: {}", response.isPresent() ? response.get().toString() : "Error al cargar json");
                            if(response.isPresent()){
                                response = manageTrafficLights.arrancaHilos();
                                log.info("Esta es la respuesta al arrancar los hilos: {}", response.isPresent() ? response.get().toString() : "Error al cargar json");
                                log.info("Este es el id {}", this.idCentral);
                            }
                        }
                    }
                }else if("MSNCONSULTAESTADO".equalsIgnoreCase(parts[0])){
                    log.info("Este es el mensaje enviado {} ", parts[1]);
                    try {
                        MensajeBrokerDto mensaje = this.objectMapper.readValue(parts[1], MensajeBrokerDto.class);
                        log.info(mensaje.toString()); 
                        Optional<String> mensajeRta = consultaGrupoSemService.sendEstadoGrupoSemaforico(mensaje);
                        if(mensajeRta.isPresent()){
                            this.enviarMSNBroker(mensajeRta.get());
                        }
                    } catch (JsonProcessingException ex) {
                        Logger.getLogger(SocketClienteBroker.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
    
    public void enviarMSNBroker(String mensaje){
        try {
            // Envia el mensaje al cliente
            salidaDatos.writeUTF(mensaje);
        } catch (IOException ex) {
            log.error("Error al enviar mensaje al cliente (" + ex.getMessage() + ").");
        }
    }
    
    
}
