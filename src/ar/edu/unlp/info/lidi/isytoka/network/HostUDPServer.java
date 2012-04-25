package ar.edu.unlp.info.lidi.isytoka.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Properties;

import android.content.Context;
import ar.edu.unlp.info.lidi.isytoka.util.Constants;
import ar.edu.unlp.info.lidi.isytoka.util.Globals;

public class HostUDPServer extends Thread {
	
	protected DatagramSocket socket = null;
	
	public HostUDPServer(Context context) {
        try {
            // Leer el archivo almacenado
            InputStream input = context.getAssets().open(Constants.PROPERTIES_PREFERENCES);
            Properties prop = new Properties();
            prop.load(input);
            
            // Recuperar el nombre e IP
            Globals.localIP = (String)prop.get(Constants.PROP_PREFERENCE_HOSTIP);
            Globals.localHostName = (String)prop.get(Constants.PROP_PREFERENCE_HOSTNAME);
            
            System.out.println( " Host properties: " + Globals.localHostName + "(" + Globals.localIP + ")" );
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
	}

	public void run() {
	    while (Globals.running) {
	        try {
	    		// delay inicial antes de empezar el envio de estado 
	    		sleep(Constants.FIRST_REFRESH_TIME_MS);

	    		// enviar status actual
	    		sendPing(null);

	            // actualizar el status cada cierto intervalo de tiempo
                sleep(Constants.STATUS_REFRESH_TIME_MS - Constants.FIRST_REFRESH_TIME_MS);
	        }
            catch (InterruptedException e) { e.printStackTrace(); }
	        catch (IOException e) { e.printStackTrace(); }
	        catch (Exception e) { e.printStackTrace(); }
	    }
		try {
			sendPing(new Boolean(false));
			if (socket != null)
				socket.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	protected void sendPing(Boolean status) throws Exception {
		int online = -1;
		
		if (status == null)
			online = Globals.online ? Constants.PING_STATUS_ONLINE : Constants.PING_STATUS_OFFLINE;
		else
			online = status ? Constants.PING_STATUS_ONLINE : Constants.PING_STATUS_OFFLINE;
		
		// enviar mi estado actual
        byte[] buf = new byte[256];
        String dString = null;
        dString = Globals.localHostName + "::" + Globals.localIP + "::" + online + "::"; 
        buf = dString.getBytes();

        InetAddress group = InetAddress.getByName(Constants.GROUP_IP);
        DatagramPacket packet;
        packet = new DatagramPacket(buf, buf.length, group, Constants.PORT_UDP);
        MulticastSocket socket = new MulticastSocket(Constants.PORT_UDP);
        socket.send(packet);
	}
	
}

