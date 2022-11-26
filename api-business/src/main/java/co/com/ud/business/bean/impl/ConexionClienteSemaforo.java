package co.com.ud.business.bean.impl;

import co.com.ud.utiles.dto.SemaforoDto;
import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author sierraj
 */
@Slf4j
public class ConexionClienteSemaforo extends Thread {

    private Socket socket;
    private DataInputStream entradaDatos;
    private DataOutputStream salidaDatos;
    @Getter @Setter
    private Integer idCliente;
    @Getter @Setter
    private String nombre;
    @Getter @Setter
    private List<SemaforoDto> semaforos;
    
    private Boolean mensajeInicioEnviado;

    public ConexionClienteSemaforo(Socket socket) {
        this.socket = socket;
        this.mensajeInicioEnviado = Boolean.FALSE;
        try {
            entradaDatos = new DataInputStream(socket.getInputStream());
            salidaDatos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            log.error("Error al crear los stream de entrada y salida : " + ex.getMessage());
        }
    }

    @Override
    public void run() {
        String mensajeRecibido;
        boolean conectado = true;
        this.enviarMensajeBienvenida();
        // Se apunta a la lista de observadores de mensajes
        //mensajes.addObserver(this);
        while (conectado) {
            try {
                // Lee un mensaje enviado por el cliente
                mensajeRecibido = entradaDatos.readUTF();
                // Pone el mensaje recibido en mensajes para que se notifique 
                // a sus observadores que hay un nuevo mensaje.
            } catch (IOException ex) {
                log.info("Cliente con la IP " + socket.getInetAddress().getHostName() + " desconectado.");
                conectado = false;
                // Si se ha producido un error al recibir datos del cliente se cierra la conexion con el.
                try {
                    entradaDatos.close();
                    salidaDatos.close();
                } catch (IOException ex2) {
                    log.error("Error al cerrar los stream de entrada y salida :" + ex2.getMessage());
                    ex2.printStackTrace();
                }
            }
        }
    }
    
    public void enviarMSN(String mensaje){
        try {
            // Envia el mensaje al cliente
            salidaDatos.writeUTF(mensaje);
            salidaDatos.flush();
        } catch (IOException ex) {
            log.error("Error al enviar mensaje al cliente (" + ex.getMessage() + ").");
        }
    }
    
    private void enviarMensajeBienvenida(){
        if(!mensajeInicioEnviado){
            enviarMSN("MSNSISTEMA|" + this.getIdCliente() + "|" +  this.getNombre());
            this.mensajeInicioEnviado = Boolean.TRUE;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            this.enviarMensajeInicializacionSemaforos();
        }
    }
    
    private void enviarMensajeInicializacionSemaforos(){
        Gson gson = new Gson();
        enviarMSN("MSNINISEMAFORO|"+ gson.toJson(this.semaforos));
    }

}