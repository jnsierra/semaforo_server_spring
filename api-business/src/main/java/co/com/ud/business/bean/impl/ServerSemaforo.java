
package co.com.ud.business.bean.impl;

import co.com.ud.utiles.dto.PlanSemaforicoDto;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author sierraj
 */
@Slf4j
public class ServerSemaforo extends Thread{
    
    
    private int puerto;
    private int maximoConexiones = 10; // Maximo de conexiones simultaneas
    private ServerSocket servidor = null; 
    private Socket socket = null;
    @Setter @Getter
    private PlanSemaforicoDto planSemaforicoDto;
    @Setter
    private int numConexiones;
    private EnvioMensajesLogica envioMensajesLogica;
    

    public ServerSemaforo(int puerto, EnvioMensajesLogica envioMensajesLogica) {
        this.puerto = puerto;
        this.envioMensajesLogica = envioMensajesLogica;
        this.numConexiones = 1;
    }
    
    @Override
    public void run(){        
        try {
            // Se crea el serverSocket
            servidor = new ServerSocket(puerto, maximoConexiones);
            // Bucle infinito para esperar conexiones
            for (int i = 0 ;i < numConexiones + 1 ; i++ ) {
                log.info("Servidor a la espera de conexiones.");
                socket = servidor.accept();
                log.info("Cliente con la IP " + socket.getInetAddress().getHostName() + " conectado.");
                
                ConexionClienteSemaforo cc = new ConexionClienteSemaforo(socket);
                cc.setIdCliente(getPlanSemaforicoDto().getGrpSemaforico().get(i).getNro());
                cc.setNombre(getPlanSemaforicoDto().getGrpSemaforico().get(i).getNombre());
                cc.setSemaforos(getPlanSemaforicoDto().getGrpSemaforico().get(i).getSemaforos());
                cc.start();
                envioMensajesLogica.adicionarConexion(cc);  
            }
            System.out.println("Se han vinculado los grupos necesarios");
        } catch (IOException ex) {
            log.error("Error: " + ex.getMessage());
        } finally{
            try {
                socket.close();
                servidor.close();
            } catch (IOException ex) {
                log.error("Error al cerrar el servidor: " + ex.getMessage());
            }
        }
    }
    
    public Integer obtenerNumeroConexiones(){
        if(Objects.isNull(this.envioMensajesLogica.getCentralesSemaforicas())){
            return 0;
        }
        return this.envioMensajesLogica.getCentralesSemaforicas().size();
    }
}
