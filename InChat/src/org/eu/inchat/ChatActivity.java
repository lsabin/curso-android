package org.eu.inchat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.eu.inchat.adapters.ChatAdapter;
import org.eu.inchat.db.ContactsDAO;
import org.eu.inchat.model.Contacto;
import org.eu.inchat.model.Mensaje;
import org.eu.inchat.services.InChatService2;
import org.eu.inchat.tasks.ReadMessagesAsyncTask;
import org.eu.inchat.tasks.ServerAsyncTask;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatActivity extends ListActivity implements MessagesListener,
		ReadMessagesListener {

	private List<String> chats;
	private Contacto contacto;
	private List<Mensaje> mensajes = new ArrayList<Mensaje>();
	private Timer timer;
	private String ownerPhone = "9003";
	private Messenger service;

	private OnClickListener listenerEnvioMensaje = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.d(getClass().getName(), "Inicia activity contactos....");

			// Obtiene el texto del mensaje
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
		
		
		//Icono
		ImageView imagen = (ImageView) findViewById(R.id.imageIconoContacto);
		
		if (contacto.getIcono() != null && contacto.getIcono().length > 0) {
			Bitmap bm = BitmapFactory.decodeByteArray(contacto.getIcono(), 0, contacto.getIcono().length);
			imagen.setImageBitmap(bm);
		} else {
			imagen.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));	
		}

		
		
	}

	private List<Mensaje> obtieneChats() {
		if (contacto.getMensajes() != null) {
			mensajes = contacto.getMensajes();
		}

		return mensajes;
	}

	private void initListView(List<Mensaje> mensajes) {
		setListAdapter(new ChatAdapter(getApplicationContext(),
				R.layout.chat_item, mensajes));

		registerForContextMenu(getListView());
	}

	private void initButtons() {

		ImageButton botonSend = (ImageButton) findViewById(R.id.imageSend);
		botonSend.setOnClickListener(listenerEnvioMensaje);
	}

	private void enviaMensaje(String textoMensaje) {

		Mensaje mensaje = new Mensaje(textoMensaje, new Date(), true, contacto.getNumeroTelefono());
		mensaje.setContactoDestino(contacto);
		
		/* Codigo anterior usando un AsyncTask 
		ServerAsyncTask task = ServerAsyncTask.newInstance(this,getApplicationContext());
		task.setOwnerPhone(ownerPhone);
		task.execute(mensaje);
		*/
		
		//Ahora el envio lo hace el servicio
		Bundle bundle = new Bundle();
		bundle.putSerializable("message", mensaje);
		Message msg = Message.obtain(null, InChatService2.MSG_SET_MSG_VALUE);
		msg.setData(bundle);
		
		try {
			service.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		ChatAdapter adapter = (ChatAdapter) getListAdapter();

		/*
		 * Se puede modificar la lista de mensajes o añadir con el metodo add
		 * del adapter
		 */
		// Opcion 1: mensajes.add(new Mensaje(mensaje, new Date(), true,
		// contacto.getNumeroTelefono()));
		adapter.add(mensaje); // Otra forma de hacerlo:

		// Notifica que hay nuevo mensaje al adapter
		adapter.notifyDataSetChanged();

	}

	@Override
	public void onMessageSent(Integer resultado) {

		Log.d(getClass().getName(), "Mensaje ENVIADO!");

	}

	@Override
	protected void onResume() {
		
		/* Temporizador usado en lugar del Service
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				// Leer los mensajes
				Log.d(getClass().getName(), "leyendo los mensajes");
				readMessages();
			}
		}, 0, 20000L);
		*/
		
		doBindService();
		
		super.onResume();
	}
	
	

	@Override
	protected void onPause() {
		doUnbindService();
		//timer.cancel(); //Codigo anterior
		super.onPause();
	}

	private void readMessages() {
		ReadMessagesAsyncTask task = ReadMessagesAsyncTask.newInstance(this,
				getApplicationContext());
		task.setOwnerPhone(ownerPhone);

		task.execute();
	}

	@Override
	public void onMessagesReceived(List<Mensaje> mensajesRecibidos) {

		Log.d(getClass().getName(), "Mensajes recibidos: " + mensajesRecibidos);

		List<Mensaje> mensajesChat = new ArrayList<Mensaje>();

		ChatAdapter adapter = (ChatAdapter) getListAdapter();

		// Se filtran los mensajes enviados por el contacto seleccionado
		if (mensajesRecibidos != null && !mensajesRecibidos.isEmpty()) {
			for (Mensaje mensajeRecibido : mensajesRecibidos) {

				Log.d(getClass().getName(),
						"contacto: " + contacto.getNumeroTelefono());

				String numeroTelefono = mensajeRecibido.getUserId();
				Log.d(getClass().getName(), "remitente mensaje: "
						+ numeroTelefono);

				if (contacto.getNumeroTelefono().equals(numeroTelefono)) {
					mensajeRecibido.setContactoDestino(contacto);
					mensajesChat.add(mensajeRecibido);
				}
			}
		}

		for (Mensaje mensajeConversacion : mensajesChat) {
			Log.d(getClass().getName(),
					"Mensajes chat: " + mensajeConversacion.getTextoMensaje());
			insertaMensaje(mensajeConversacion);
		}

		adapter.addAll(mensajesRecibidos);
		getListView().invalidateViews();
		adapter.notifyDataSetChanged();

	}

	private void insertaMensaje(Mensaje mensaje) {
		ContactsDAO dao = new ContactsDAO(this);

		dao.open();
		dao.creaMensaje(mensaje, false);
		dao.close();

	}

	/**
	 * Implementacion de menu contextual
	 */

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		getMenuInflater().inflate(R.menu.context_chat_menu, menu);
		menu.setHeaderTitle("Opciones");
		menu.setHeaderIcon(R.drawable.ic_launcher);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int position = info.position;
		
		switch (item.getItemId()) {
		case R.id.action_borrar:
			Log.i(getClass().getName(), "mensaje para borrar: " + contacto.getMensajes().get(position).getTextoMensaje());

			return true;
		default:
			break;
		}

		return super.onContextItemSelected(item);

	}
	
	
	
	/** Uso del servicio para envio/recepcion de mensajes
	 * 
	 */
	
	private void doBindService() {
		Intent intent = new Intent(this, InChatService2.class);
		bindService(intent, connection, BIND_AUTO_CREATE);
	}
	
	private void doUnbindService() {
		
		if (service != null) {
			Message msg = Message.obtain(null, InChatService2.MSG_UNREGISTER_CLIENT);
			msg.replyTo = messenger;
			
			try {
				service.send(msg);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	class IncomingHandler extends Handler {
		
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case InChatService2.MSG_SET_MSG_VALUE:
				Mensaje mensaje = (Mensaje) msg.getData().getSerializable("mensaje");
				Log.d(getClass().getName(), "Enviando mensaje .... ");
				
				//Mostrar en la pantalla el mensaje
				if(mensaje.getUserId().equals(contacto.getNumeroTelefono())) {
					contacto.getMensajes().add(mensaje);
					getListView().invalidateViews();
				}
				
				
				break;

			default:
				super.handleMessage(msg);
				break;
			}
		}		
	}
	
	
	
	final Messenger messenger = new Messenger(new IncomingHandler());
	
	private final ServiceConnection connection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			service = null; 
			Log.d(getClass().getName(), "desconectado del servicio de mensajeria");
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder target) {
			
			service = new Messenger(target);
			Log.d(getClass().getName(), "conectado al servicio de mensajeria");
			
			
			Message msg = Message.obtain(null, InChatService2.MSG_REGISTER_CLIENT);
			msg.replyTo = messenger;
			
			try {
				service.send(msg);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
			//TODO: reducir el check interval
			
		}
	};
	

}
