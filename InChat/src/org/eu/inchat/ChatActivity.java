package org.eu.inchat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.eu.inchat.adapters.ChatAdapter;
import org.eu.inchat.model.Contacto;
import org.eu.inchat.model.Mensaje;
import org.eu.inchat.tasks.ReadMessagesAsyncTask;
import org.eu.inchat.tasks.ServerAsyncTask;

import android.app.ListActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ChatActivity extends ListActivity implements MessagesListener, ReadMessagesListener {
	
	private List<String> chats;
	private Contacto contacto;
	private List<Mensaje> mensajes = new ArrayList<Mensaje>();
	private Timer timer;
	private String ownerPhone = "9003";
	
	
	private OnClickListener listenerEnvioMensaje = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.d(getClass().getName(), "Inicia activity contactos....");
			
			//Obtiene el texto del mensaje
			EditText texto = (EditText) findViewById(R.id.chat_textoMensaje);
			String textoMensaje = texto.getText().toString();
			
			enviaMensaje(textoMensaje);
			
			texto.setText("");

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_contacto);
		
		initScreen();
		
		mensajes = obtieneChats();
		initListView(mensajes);
		
		initButtons();
	}
	
	private void initScreen() {
		contacto = (Contacto) getIntent().getSerializableExtra("contacto");
		TextView textoNombre = (TextView) findViewById(R.id.textoNombreContacto);
		textoNombre.setText(contacto.getNombre());
	}
	
	private List<Mensaje> obtieneChats() {
		if(contacto.getMensajes() != null) {
			mensajes = contacto.getMensajes();
		}
		
		return mensajes;
	}
	
	private void initListView(List<Mensaje> mensajes) {
    	setListAdapter(new ChatAdapter(getApplicationContext(),
    			R.layout.chat_item, mensajes));
	}
	
	private void initButtons() {
		
		ImageButton botonSend = (ImageButton) findViewById(R.id.imageSend);
		botonSend.setOnClickListener(listenerEnvioMensaje);
	}
	
	
	private void enviaMensaje(String textoMensaje) {
		
		ServerAsyncTask task = ServerAsyncTask.newInstance(this, getApplicationContext());
		
		task.setOwnerPhone(ownerPhone);
		Mensaje mensaje = new Mensaje(textoMensaje, new Date(), true, contacto.getNumeroTelefono());
		mensaje.setContactoDestino(contacto);
		task.execute(mensaje);
		
		ChatAdapter adapter = (ChatAdapter) getListAdapter();
		
		/* Se puede modificar la lista de mensajes o añadir con el metodo add
		 * del adapter
		 */
		//Opcion 1: mensajes.add(new Mensaje(mensaje, new Date(), true, contacto.getNumeroTelefono()));
		adapter.add(mensaje); //Otra forma de hacerlo:
		
		//Notifica que hay nuevo mensaje al adapter
		adapter.notifyDataSetChanged();
		
	}

	@Override
	public void onMessageSent(Integer resultado) {
		
		Log.d(getClass().getName(), "Mensaje ENVIADO!");
		
		
	}
	
	
	@Override
	protected void onResume() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				// Leer los mensajes
				Log.d(getClass().getName(), "leyendo los mensajes");
				readMessages();
			}
		}, 0, 20000L);
		super.onResume();
	}
	
	
	private void readMessages() {
		ReadMessagesAsyncTask task = ReadMessagesAsyncTask.newInstance(this, getApplicationContext());
		task.setOwnerPhone(ownerPhone);
		
		task.execute();
	}

	@Override
	public void onMessagesReceived(List<Mensaje> mensajesRecibidos) {
		
		
		Log.d(getClass().getName(),"Mensajes recibidos: " + mensajesRecibidos);
		
		List<Mensaje> mensajesChat = new ArrayList<Mensaje>();
		
		ChatAdapter adapter = (ChatAdapter) getListAdapter();
		
		
		//Se filtran los mensajes enviados por el contacto seleccionado
		if (mensajesRecibidos != null && !mensajesRecibidos.isEmpty()) {
			for (Mensaje mensajeRecibido : mensajesRecibidos) {
				
				Log.d(getClass().getName(), "contacto: " + contacto.getNumeroTelefono());
				
				
				String numeroTelefono = mensajeRecibido.getUserId();
				Log.d(getClass().getName(), "remitente mensaje: " + numeroTelefono);
				
				if(contacto.getNumeroTelefono().equals(numeroTelefono)) {
					mensajesChat.add(mensajeRecibido);
				}
			}
		}
		
		
		for(Mensaje mensajito : mensajesChat) {
			Log.d(getClass().getName(),"Mensajes chat: " + mensajito.getTextoMensaje());	
		}
		

		adapter.addAll(mensajesRecibidos);
		getListView().invalidateViews();
		adapter.notifyDataSetChanged();

	}
		

}
