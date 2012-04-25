package ar.edu.unlp.info.lidi.isytoka.util;

public class Constants {

    /** Application display name */
    public static final String APPLICATION_NAME_MSG = "Isytoka";
    public static final String APPLICATION_BYEBYE_MSG = "Bye";
    
    /** Busqueda de IPs dentro de la LAN */
    public static final int maxSearchIP = 16;
    
    /** Puertos de acceso a la aplicación */
    public static final int PORT_TCP = 9999;
    public static final int PORT_UDP = 9998;
    public static final String GROUP_IP = "230.0.0.1";
    
    /** Demora para evitar colisiones */
    public static final int threadSafeDelayMS = 100;
    public static final int hostDetectionTimeout = 3000;
    public static final int FIRST_REFRESH_TIME_MS = 1000;
    public static final int STATUS_REFRESH_TIME_MS = 10000;
    
    /** Mensaje para broadcast */
    public static final String MASS_MESSAGE = "*";
    public static final String MASS_MESSAGE_TEXT = "*";
    
    /** Archivos de imagenes, configuracion, etc. */
    public static final String PROPERTIES_PREFERENCES = "preferences.properties";
    
    public static final String PROP_PREFERENCE_HOSTIP = "HOST_IP";
    public static final String PROP_PREFERENCE_HOSTNAME = "HOST_NAME";

    /** Tipos de mensajes que pueden enviarse o recibirse */
    public static final int MESSAGE_KIND_TEXT = 1;
//    public static final int MESSAGE_KIND_IMAGE = 2;
    public static final int MESSAGE_KIND_STATUS = 3;
    
    public static final int PING_STATUS_ONLINE = 1;
    public static final int PING_STATUS_OFFLINE = 0;
    
    
	
}
