/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.com.ud.business.broker.impl;

import co.com.ud.business.service.CargarJsonService;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

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
    @Getter
    @Setter
    private Integer idCentral;
    @Autowired
    private CargarJsonService cargarJsonService;

    public SocketClienteBroker(String ip, int puerto, CargarJsonService cargarJsonService) {
        this.ip = ip;
        this.puerto = puerto;
        this.cargarJsonService = cargarJsonService;
        this.connectServer();
    }

    private void connectServer() {
        try {
            this.socket = new Socket(this.ip, this.puerto);
            log.info("Se conecta con el servidor {} con el puerto: {}", this.ip, this.puerto);
            entradaDatos = new DataInputStream(socket.getInputStream());
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
                        }
                    }
                }
            }
        }
    }
}
