package ar.edu.unlp.info.lidi.isytoka.msg;

import ar.edu.unlp.info.lidi.isytoka.msg.Message;
import ar.edu.unlp.info.lidi.isytoka.network.Host;
import ar.edu.unlp.info.lidi.isytoka.network.NetworkClient;
import ar.edu.unlp.info.lidi.isytoka.util.Constants;
import ar.edu.unlp.info.lidi.isytoka.network.StatusManager;

public class MessageSender implements Runnable {

    private Message message;
    
    @Override
    public void run() {
        NetworkClient sender = new NetworkClient(message.getReceiverIP(), Constants.PORT_TCP);
        if (!sender.sendMessage(message)) {
            sendErrorMessage(message);
            StatusManager.updateAnotherHostStatus(Host.findHostByIP(message.getReceiverIP()), false);
        }
    }

    public MessageSender(Message message) {
        this.message = message;
    }
    
    /**
     * Envia un mensaje de error al no poder enviar un mensaje o imagen
     * @param message
     */
    private void sendErrorMessage(Message message) {
        // No avisar mensajes de error para cambios de status
        if (message.getMessageKind() == Constants.MESSAGE_KIND_STATUS) {
            System.err.println(" ** ERROR ENVIANDO "+ message.getMessageKindDescription() +" a " + message.getReceiver() + " ** ");
            return;
        }
        
        // Nuevo mensaje de error
        Message errorMessage = new Message();
        errorMessage.setSender(message.getSender());
        errorMessage.setSenderIP(message.getSenderIP());
        errorMessage.setReceiver(message.getSender());
        errorMessage.setReceiverIP(message.getSenderIP());
        errorMessage.setMessageContent(" <font color=red><i> ** ERROR ENVIANDO "+ message.getMessageKindDescription() +" a " + message.getReceiver() + " ** </i></font>");
        errorMessage.setMessageKind(Constants.MESSAGE_KIND_TEXT);
        errorMessage.setToAll(false);
        
        new Thread(new MessageSender(errorMessage)).start();
    }
    
	
}
