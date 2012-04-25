package ar.edu.unlp.info.lidi.isytoka.util;

import java.util.Vector;

import ar.edu.unlp.info.lidi.isytoka.network.Host;

public class Globals {

    /**
     * Lista de hosts.  
     */
    public static Vector<Host> hosts = new Vector<Host>();

    /** IP y hostname locales */ 
    public static String localIP = "";
    public static String localHostName = "";
    
    /** Estado Isytok */
    public static boolean online = false; 
    
    /** Aplication Isytok */
    public static boolean running = true;
}
