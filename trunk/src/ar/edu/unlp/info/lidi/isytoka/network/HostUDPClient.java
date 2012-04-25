package ar.edu.unlp.info.lidi.isytoka.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import ar.edu.unlp.info.lidi.isytoka.util.Constants;
import ar.edu.unlp.info.lidi.isytoka.util.Globals;

public class HostUDPClient extends Thread {
	
	public void run() {
		try {
			MulticastSocket socket = new MulticastSocket(Constants.PORT_UDP);
			InetAddress group = InetAddress.getByName(Constants.GROUP_IP);
			socket.joinGroup(group);

			DatagramPacket packet;
			while (Globals.running) {
			    byte[] buf = new byte[256];
			    packet = new DatagramPacket(buf, buf.length);
			    socket.receive(packet);

			    String received = new String(packet.getData());
			    System.out.println("Ping recibido: " + received);
			    
			    managePing(received);
			}
			socket.leaveGroup(group);
			socket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}
	

	protected void managePing(String received) {
		// parsear el ping con el host y estado
	    String[] values = received.split("::");
	    boolean new_status = Constants.PING_STATUS_ONLINE==Integer.parseInt(values[2]);
	    Host host = new Host(values[0], values[1], new_status);
	    boolean changed = false;
	    
	    // ya existia el host?
	    if (Globals.hosts.contains(host)) {
	    	// hubo algun cambio?
	    	boolean old_status = Globals.hosts.elementAt(Globals.hosts.indexOf(host)).isConnected();
	    	if (old_status != new_status) {
	    		Globals.hosts.elementAt(Globals.hosts.indexOf(host)).setConnected(new_status);
	    		changed = true;
	    	}
	    }
	    else {
	    	Globals.hosts.add(host);
	    	changed = true;
	    }
	    
	    // si hubo cambios, avisar con un self-message para que el client se entere
	    if (changed)
	    	StatusManager.updateAnotherHostStatus(host, new_status);
	}

}
