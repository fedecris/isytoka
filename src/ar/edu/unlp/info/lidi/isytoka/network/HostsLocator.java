package ar.edu.unlp.info.lidi.isytoka.network;

import java.net.ServerSocket;

import ar.edu.unlp.info.lidi.isytoka.util.Constants;

public class HostsLocator {

	
    /**
     * Verifica si el puerto server ya se encuentra abierto (usado para app-singleton)
     * @return true si esta abierto, o false en caso contrario
     */
    public static boolean isPortAvailable() 
    {  
        try 
        {  
             ServerSocket srv = new ServerSocket(Constants.PORT_TCP);  
               
             srv.close();  
             srv = null;  
             return true;  
        } 
        catch (Exception e)        {  
            return false;  
        }  
              
    }

}
