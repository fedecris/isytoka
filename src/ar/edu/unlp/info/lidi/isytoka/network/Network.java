package ar.edu.unlp.info.lidi.isytoka.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import ar.edu.unlp.info.lidi.isytoka.msg.Message;

public class Network {

    /** Host y puerto de conexion */
    protected String host;
    protected int port;     
    
    /** Socket cliente y servidor */
    protected Socket socket;
    protected ServerSocket serverConn;
    
    /** Lectura / escritura del buffer */
    protected ObjectOutputStream toBuffer = null;
    protected ObjectInputStream fromBuffer = null;
    
    /** Mensaje de envio o recepción */
    protected Message message;

    /**
     * Escribe datos a la salida
     * @param datos
     */
    public void write(Message datos) {
        try {
            toBuffer.writeObject(datos);
            toBuffer.flush();          
        }
        catch (Exception ex) { 
            System.err.println ("Error al escribir en Network:" + ex.getMessage());
        }
    }   
    
    /**
     * Lee datos de la entrada
     */
    public Message receive() {
        Message datos = null;
        try {
            datos = (Message)fromBuffer.readObject();
        }   
        catch (Exception ex) { System.err.println ("Error al recibir en Network: " + ex.getMessage()); }
        return datos;
    }  
    
    /**
     * Cierra el socket 
     */
    public void close() {
        try {       
            socket.close();
        }
        catch (Exception ex) { System.err.println ("Error al cerrar en Network: " + ex.getMessage()); }      
    }
    
    /**
     * Cierra el server socket 
     */
    public void closeServer() {
        try {       
            socket.close();
            serverConn.close();
        }
        catch (Exception ex) { System.err.println ("Error al cerrar server en Network: " + ex.getMessage()); }      
    }
    
	
}
