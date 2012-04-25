package ar.edu.unlp.info.lidi.isytoka.msg;

import java.io.Serializable;
import java.util.Observable;

import ar.edu.unlp.info.lidi.isytoka.util.Constants;



public class Message extends Observable implements Serializable {

    /** Tipos de mensaje puede ser Texto o Imagen */ 
    private int messageKind = -1;
    
    /** La persona que envia el mensaje */
    private String sender = "";

    /** La persona que envia el mensaje */
    private String senderIP = "";
    
    /** IP de la persona que recibe el mensaje */
    private String receiver = "";
    
    /** IP de la persona que recibe el mensaje */
    private String receiverIP = "";

    
    /** Contenido texto del mensaje */
    private Object messageContent = null;
    
    /** Indica si fue un mensaje masivo */
    private boolean toAll = false;
    
    /** 
     * Avisa a los dependents del nuevo mensaje
     */
    public void notifyNewMessage()
    {
        setChanged();
        notifyObservers(this);
    }
    
    /**
     * Realiza la copia campo a campo de un mensaje
     * @param sourceMessage
     * @param targetMessage
     */
    public static void copy(Message sourceMessage, Message targetMessage)
    {
        targetMessage.setMessageContent(sourceMessage.getMessageContent());
        targetMessage.setMessageKind(sourceMessage.getMessageKind());
        targetMessage.setReceiver(sourceMessage.getReceiver());
        targetMessage.setReceiverIP(sourceMessage.getReceiverIP());
        targetMessage.setSender(sourceMessage.getSender());
        targetMessage.setSenderIP(sourceMessage.getSenderIP());
        targetMessage.setToAll(sourceMessage.isToAll());
    }
    
    public String getMessageKindDescription()
    {
        switch (messageKind)
        {
            case  Constants.MESSAGE_KIND_TEXT: return "TEXTO";
//            case  Constants.MESSAGE_KIND_IMAGE: return "IMAGEN";
            case  Constants.MESSAGE_KIND_STATUS: return "ESTADO";
            default: return null;
        }
    }
    
    
    /** ---- GETTERS Y SETTERS ---- */
    
    public int getMessageKind()    {
        return messageKind;
    }
    
    public void setMessageKind(int messageKind)    {
        this.messageKind = messageKind;
    }

    public String toString()    {
        return "un mensaje";
    }

    public String getSender()    {
        return sender;
    }

    public void setSender(String sender)    {
        this.sender = sender;
    }

    public String getReceiver()    {
        return receiver;
    }
    
    public void setReceiver(String receiver)    {
        this.receiver = receiver;
    }

    public boolean isToAll()   {
        return toAll;
    }

    public void setToAll(boolean toAll)    {
        this.toAll = toAll;
    }


    public Object getMessageContent()    {
        return messageContent;
    }


    public void setMessageContent(Object content)    {
        this.messageContent = content;
    }

    public String getSenderIP()
    {
        return senderIP;
    }

    public void setSenderIP(String senderIP)
    {
        this.senderIP = senderIP;
    }

    public String getReceiverIP()
    {
        return receiverIP;
    }

    public void setReceiverIP(String receiverIP)
    {
        this.receiverIP = receiverIP;
    }
	
}
