package ar.edu.unlp.info.lidi.isytoka.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ar.edu.unlp.info.lidi.isytoka.msg.Message;
import ar.edu.unlp.info.lidi.isytoka.util.Constants;
import ar.edu.unlp.info.lidi.isytoka.util.Globals;

public class NetworkClient extends Network {

    /**
     * Constructor
     * @param ip del server al cual conectar
     * @param puerto al cual conectar
     */
    public NetworkClient(String ip, int puerto) {
        this.host = ip;
        this.port = puerto;
    }
    
    /**
     * Intenta conectar con el servidor a fin de enviar el mensaje
     * @return true si fue posible la conexion o false en caso contrario
     */
    public boolean connect() {
        try {
            socket = new Socket(host, port);
            toBuffer = new ObjectOutputStream(socket.getOutputStream());
            toBuffer.flush();
            fromBuffer = new ObjectInputStream(socket.getInputStream());       
            return true;
        }
        catch (Exception ex) { 
            System.err.println ("Error en NetworkClient: " +ex.getMessage()); 
            return false;
        } 
    }  
   
     /**
     * Prepara y envia un mensaje al destinatario previamente configurado
     * @param message mensaje a enviar (texto o imagen)
     * @return true si pudo ser enviado o false en caso contrario
     */
    public boolean sendMessage(Message message) {
        if (connect()) {
            write(message);
            close();
            return true;
        }        
        return false;
    }
    
    
    /**
     * Testea la existencia de conexion con un host
     * @return true si fue posible la conexion o false en caso contrario
     */
    public boolean testConnect() {
        try {
            // envio mensaje de prueba... 
            Message testMessage = new Message();
            testMessage.setMessageContent(true);
            testMessage.setMessageKind(Constants.MESSAGE_KIND_STATUS);
            testMessage.setReceiver(host);
            testMessage.setReceiverIP(host);
            testMessage.setSender(Globals.localHostName);
            testMessage.setSenderIP(Globals.localIP);
            testMessage.setToAll(false);
            
            if (sendMessage(testMessage))
                return true;
            else
                return false;
        }
        catch (Exception ex) {
            System.err.println ("Error en testConnect de NetworkClient: " + ex.getMessage());
            return false;
        } 
    }  
}
