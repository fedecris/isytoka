package ar.edu.unlp.info.lidi.isytoka.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

import ar.edu.unlp.info.lidi.isytoka.msg.Message;

public class NetworkServer extends Network implements Runnable {
	    
    /**
     * Crea la conexion server a fin de escruchar mensajes etrantes
     * @param puerto por el cual entraran los mensajes
     */
    public NetworkServer(int puerto) {
        try {
            port = puerto;
            serverConn = new ServerSocket(port);   
            message = new Message();
        }
        catch (Exception ex) { System.err.println ("Error en instanciar NetworkServer: " + ex.getMessage()); }
    }

    /**
     * Loop general de la aplicación de escucha de mensajes
     * Queda en espera de mensajes entrantes, luego itera indefinidamente
     */
    @Override
    public synchronized void run() {
        while   (true) {
            try {
                // Escuchar conexiones entrantes
                if (listen()) {
                    Message newMessage = (Message)receive();
                    if (newMessage == null)
                        return;
                
                    // Atender según correspondan
                    Message.copy(newMessage, message);
                    message.notifyNewMessage();
                }
            }
            catch (Exception e) { System.err.println("Error en run de NetworkServer: "+e.getMessage()); }
        }
    }
    
    /**
     * Reinicia el sever socket
     */
    public void restartServer() {
    	try {
    		closeServer();
    		serverConn = new ServerSocket(port);
    	}
    	catch (Exception e) { System.err.println("Error en restartServer():" + e.getMessage()); }
    }
    
    /**
     * Esperar la recepción de mensajes y setear los buffers correspondientes
     */
    public boolean listen() {
        try {   
            socket = serverConn.accept();
            toBuffer = new ObjectOutputStream(socket.getOutputStream());
            toBuffer.flush();
            fromBuffer = new ObjectInputStream(socket.getInputStream());
            return true;
        }
        catch (Exception ex) { 
            System.err.println ("Error en listen de NetworkServer: " + ex.getMessage());
            return false;
        }
    }  
    
   
    /** --- GETTERS Y SETTERS */ 
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
