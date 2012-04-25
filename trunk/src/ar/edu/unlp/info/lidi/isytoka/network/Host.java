package ar.edu.unlp.info.lidi.isytoka.network;

import ar.edu.unlp.info.lidi.isytoka.util.Globals;

public class Host {

	private String hostName;
	private String hostIP;
	private boolean connected;
	
	
	public String toString() {
	    return hostName + " (" + hostIP + ") - " + (connected?"Online":"Offline");
	}

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostIP() {
        return hostIP;
    }

    public void setHostIP(String hostIP) {
        this.hostIP = hostIP;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
    
    public Host(String hostName, String hostIP, boolean connected) {
        this.hostName = hostName;
        this.hostIP = hostIP;
        this.connected = connected;
    }

    
    /**
     * Busca un host a partir de un hostname
     * @param hostName el hostname a buscar
     * @return la instancia de Host encontrada o null en caso contrario
     */
    public static Host findHost(String hostName) {
        if (hostName == null)
            return null;
        
        boolean found = false;
        Host retHost = null;
        for (int i = 0 ; i < Globals.hosts.size() && !found ; i++)
            if (Globals.hosts.elementAt(i).getHostName().equalsIgnoreCase(hostName))
            {
                retHost = Globals.hosts.elementAt(i);
                found = true;
            }
        return retHost;
    }
	
    /**
     * Busca un host a partir de un hostIP
     * @param hostName el hostname a buscar
     * @return la instancia de Host encontrada o null en caso contrario
     */
    public static Host findHostByIP(String hostIP) {
        if (hostIP == null)
            return null;
        
        boolean found = false;
        Host retHost = null;
        for (int i = 0 ; i < Globals.hosts.size() && !found ; i++)
            if (Globals.hosts.elementAt(i).getHostIP().equalsIgnoreCase(hostIP))
            {
                retHost = Globals.hosts.elementAt(i);
                found = true;
            }
        return retHost;
    }
    
    
    /** 
     * Especificación de equals
     */
    public boolean equals(Object anObject) {
    	if (anObject == null)
    		return false;
    	
    	Host host = (Host)anObject;
    	if (getHostIP() == null && host.getHostIP() != null ||
    		getHostName() == null && host.getHostName() != null ||
    		getHostIP() != null && host.getHostIP() == null ||
    		getHostName() != null && host.getHostName() == null)
    	return false;
    	
    	return getHostName().equals(((Host)host).getHostName()) &&
    			getHostIP().equals(((Host)host).getHostIP());
    }
	
}
