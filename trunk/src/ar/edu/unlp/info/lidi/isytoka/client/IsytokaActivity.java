package ar.edu.unlp.info.lidi.isytoka.client;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import ar.edu.unlp.info.lidi.isytoka.R;
import ar.edu.unlp.info.lidi.isytoka.msg.Message;
import ar.edu.unlp.info.lidi.isytoka.msg.MessageSender;
import ar.edu.unlp.info.lidi.isytoka.network.Host;
import ar.edu.unlp.info.lidi.isytoka.network.HostUDPClient;
import ar.edu.unlp.info.lidi.isytoka.network.HostUDPServer;
import ar.edu.unlp.info.lidi.isytoka.network.HostsLocator;
import ar.edu.unlp.info.lidi.isytoka.network.NetworkServer;
import ar.edu.unlp.info.lidi.isytoka.network.StatusManager;
import ar.edu.unlp.info.lidi.isytoka.util.Constants;
import ar.edu.unlp.info.lidi.isytoka.util.Globals;

public class IsytokaActivity extends Activity implements Observer {
    
    /** Servicio de escucha */
    private NetworkServer listener;
    
    /** Combo de hosts */
    Spinner spinner = null;
    
    /** Mensaje a enviar */
    String textToSend = null;
    
    /** Edit para el mensaje a enviar */
    EditText messageText = null;
    
    /** Main activity */
    Activity owner = null;
	
    
    
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /** Asociar el boton de enviar con su evento */
        Button btn = (Button)findViewById(R.id.sendButton);
        ClickEvent clickEvent = new ClickEvent(); 
        btn.setOnClickListener(clickEvent);
        
        /** Asociar el campo de mensajes con su evento */
        EditText editText2 = (EditText)findViewById(R.id.messageText);
        KeyListener keyListener = new KeyListener(); 
        editText2.setOnKeyListener(keyListener);

        init();
        
        System.out.println(getLocalIpAddress());
        
        try
        {
        	startServer();
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	System.exit(1);
        }
    }
    
    
    /**
     * Valida instancia e incializa hosts
     */
    private void init()
    {
        System.out.println("Initializing...");
        owner = this;
        
        HostUDPServer server = new HostUDPServer(getBaseContext());
        server.start();
        
        HostUDPClient client = new HostUDPClient();
        client.start();

        // App-singleton
        System.out.println("Chequeando instancia de aplicación...");
        if (!HostsLocator.isPortAvailable())
        {
            String msg = "Ya existe una instancia de Isytok abierta (puerto en uso)... ";
            System.err.println(msg);
            Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
            System.exit(-1);
        }
        System.out.println("OK!");
    }
    
    
    /**
     * Iniciar el framework para chat
     * @return true si pudo levantar el server, o falso en caso contrario
     */
    private void startServer() throws Exception
    {
        // Iniciar el server
        listener = new NetworkServer(Constants.PORT_TCP);
    	listener.getMessage().addObserver(this);
        new Thread(listener).start();
        StatusManager.broadCastNewStatus(true);
    }
    
    
    /**
     *	Evento al enviar 
     */
	public class ClickEvent implements OnClickListener {
		@Override
		public void onClick(View v) {
			sendText();
		}
	}

	/**
	 *	Evento al teclear
	 */
	public class KeyListener implements OnKeyListener {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// Al presionar enter, darle bola, sino no
			if  (KeyEvent.KEYCODE_ENTER  == keyCode)
				sendText();
			return true;
		}
		
	}
	
	/**
	 * Envia el texto
	 */
	public void sendText()
	{
		// Leer el estado actual del mensaje a enviar
		messageText = (EditText)findViewById(R.id.messageText);
		// Si hay algo para enviar lo envia
		if (messageText.getText().toString().trim().length()>0) {
			textToSend = messageText.getText().toString();
			procesarEnviar();
		}
	}
	
	
    /**
     * Procesa los eventos de enter y click en enviar
     */
    public void procesarEnviar()
    {
        // si no estoy online, no puedo enviar!
        if (!Globals.online)
        {
        	Toast.makeText(getBaseContext(), " ** ESTAS OFFLINE! **", Toast.LENGTH_LONG).show();
            return;
        }
        
        // si el destinatario no esta online, no puedo enviarle
        Host host = (Host)spinner.getSelectedItem();
        if (!host.isConnected())
            return;
        String target = host.getHostName();
        if (target.equalsIgnoreCase(Constants.MASS_MESSAGE_TEXT))
            sendMassMessage(textToSend);
        else
            sendMessage(((Host)spinner.getSelectedItem()).getHostName(), ((Host)spinner.getSelectedItem()).getHostIP(), textToSend, false);
        messageText.setText("");
    }
	
    
    /**
     * Envia un mensaje 
     * @param host el destinatario
     * @param content mensaje a enviar
     */
    private  void sendMessage(String hostName, String host, String content, boolean massive)
    {
        if (content.trim().length()==0)
            return;
       
        System.out.println("Enviando " + content );
        if (!massive)
        	appendTextToChat(Globals.localHostName.toUpperCase() + " a " + hostName.toUpperCase() + ": " + content);
        
        // crear el nuevo mensaje a enviar
        Message message = new Message();
        message.setMessageKind(Constants.MESSAGE_KIND_TEXT);
        message.setMessageContent(content);
        message.setReceiver(hostName);
        message.setReceiverIP(host);
        message.setSender(Globals.localHostName);
        message.setSenderIP(Globals.localIP);
        message.setToAll(massive);
        
        new Thread(new MessageSender(message)).start();
        if (!massive)
            textToSend = null;
    }
	
    
    /**
     * Envio de mensaje masivo a todos los destinatarios del comboBox
     * @param message
     */
    private void sendMassMessage(String message)
    {
    	appendTextToChat(Globals.localHostName.toUpperCase() +" a " + Constants.MASS_MESSAGE_TEXT + ": " + message);
    	new Thread(new MassMessageSender(message)).start();
   }
    
    
    /**
     * Thread aparte para el envio de mensajes masivos
	 */
    public class MassMessageSender implements Runnable
    {
    	String massMessageContent;
    	
    	public MassMessageSender(String content)
    	{
    		massMessageContent = content;
    	}

		@Override
		public void run() 
		{
	        for (int i=0; i < Globals.hosts.size() ; i++)
	            if (!Globals.hosts.elementAt(i).getHostName().equalsIgnoreCase(Globals.localHostName) && Globals.hosts.elementAt(i).isConnected())
	            {
	                sendMessage(Globals.hosts.elementAt(i).getHostName(), Globals.hosts.elementAt(i).getHostIP(), massMessageContent, true);
	                try {Thread.sleep(Constants.threadSafeDelayMS);} catch (Exception e) { System.out.println("Error en run de ChatFrame: " + e.getMessage()); }
	            }
            textToSend = null;	        
		}
    }
    
	/**
	 * Agrega una linea en la ventana de chat
	 */
    String textToAppend = null; 	// FIXME: ESTO ES THREAD UNSAFE!
	public void appendTextToChat(String text)
	{
		textToAppend = text;
        // Only the original thread that created a view hierarchy can touch its views
        runOnUiThread(new Runnable() {
            public void run() {
            	EditText mainDialogText = (EditText)findViewById(R.id.mainDialogText);
        		mainDialogText.append(  DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date()) + ": " + textToAppend + "\n");
           }
       });
	}
	
	
    /**
     * Recarga las opciones a mostrar en el combo de hosts
     */
    private void reloadComboOptions()
    {
        // Only the original thread that created a view hierarchy can touch its views
        runOnUiThread(new Runnable() {
            public void run() {

	    	// Recuperar y limpiar el spinner
	    	spinner = (Spinner)findViewById(R.id.hostSelector);
	    	spinner.setSelection(Adapter.NO_SELECTION);
	
		    	// Resevar espacio para las entradas, cargar la de envio masivo, y luego cada host
		    	Host[] array_spinner=new Host[Globals.hosts.size()+1];
		    	array_spinner[0] = new Host(Constants.MASS_MESSAGE_TEXT, Constants.MASS_MESSAGE_TEXT, true);
		        Iterator<Host> it = Globals.hosts.iterator();
		        int i=1;
		        while (it.hasNext())
		        	array_spinner[i++] = it.next();	
		    	
		        // Generar el adapter a partir del array_spinner y cargarlo
		    	ArrayAdapter<Host> adapter = new ArrayAdapter<Host>(owner, android.R.layout.simple_spinner_item, array_spinner);
		    	spinner.setAdapter(adapter);
            }
        });

    }


	@Override
	public void update(Observable o, Object arg) {

        Message message = (Message)arg;
        
        // si es un mensaje de texto...
        if (message.getMessageKind() == Constants.MESSAGE_KIND_TEXT)
        {
            // incorporar al chatLog, (si no es mio)
            if (!message.isToAll() || !message.getSender().equalsIgnoreCase(Globals.localHostName))
               // appendText( getTimeStr() + " <b>[" + message.getSender().toUpperCase() + " a "+ (message.isToAll()?Constants.MASS_MESSAGE_TEXT:message.getReceiver().toUpperCase()) +"]</b> " + (String)message.getMessageContent(), "green");
            	appendTextToChat(message.getSender().toUpperCase() + " a " + (message.isToAll()?Constants.MASS_MESSAGE_TEXT:message.getReceiver().toUpperCase()) +": " + (String)message.getMessageContent());

//	TODO: SETEAR HOST DESTINO COMO HOST ORIGEN DEL MENSAJE 
//  """"""""""""""""""""""""""""""""""""""""""""""""""""""
//            // si es mensaje masivo, actualizar el Combobox a masivo
//            if (message.isToAll())
//                spinner.se .setSelectedIndex(0);
//            // si no es un mensaje de mi propia maquina, actualizar el Combobox a masivo            
//            else if (!message.getSender().equalsIgnoreCase(Globals.localHostName))
//                hostComboBox.setSelectedItem(Host.findHost(message.getSender()));
            
            // forzar la visualización
            setVisible(true); 
        }
        else if (message.getMessageKind() == Constants.MESSAGE_KIND_STATUS)
        {
            boolean newStatus = (Boolean)message.getMessageContent();
            Host host = Host.findHost(message.getSender());
            if (host == null)
            {
            	System.out.println("ChatFrame.update() Imposible encontrar el host a actualizar");
            	return;
            }
            host.setConnected(newStatus);
            appendTextToChat(" ** " + message.getSender().toUpperCase() + " ESTA " + (newStatus?"ONLINE!":"OFFLINE"));
            reloadComboOptions();
        }
		
	}
}