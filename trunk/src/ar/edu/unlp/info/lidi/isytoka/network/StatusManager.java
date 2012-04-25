package ar.edu.unlp.info.lidi.isytoka.network;

import ar.edu.unlp.info.lidi.isytoka.msg.Message;
import ar.edu.unlp.info.lidi.isytoka.msg.MessageSender;
import ar.edu.unlp.info.lidi.isytoka.network.Host;
import ar.edu.unlp.info.lidi.isytoka.util.Constants;
import ar.edu.unlp.info.lidi.isytoka.util.Globals;

public class StatusManager {
	
boolean newStatus;
    
    public StatusManager(boolean status) {
        newStatus = status;
    }
    

    /**
     * Entrada principal a la actualizacion de estados al resto de hosts
     * @param status
     */
    public static void broadCastNewStatus(boolean status) {
        Globals.online = status;   
    }

    
    public static void updateAnotherHostStatus(Host host, boolean status) {
        // Nuevo mensaje de estado
        Message errorMessage = new Message();
        errorMessage.setSender(host.getHostName());
        errorMessage.setSenderIP(host.getHostIP());
        errorMessage.setReceiver(Globals.localHostName);
        errorMessage.setReceiverIP(Globals.localIP);
        errorMessage.setMessageContent(status);
        errorMessage.setMessageKind(Constants.MESSAGE_KIND_STATUS);
        errorMessage.setToAll(false);
        
        new Thread(new MessageSender(errorMessage)).start();

    }
    

}
