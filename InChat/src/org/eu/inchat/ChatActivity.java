package org.eu.inchat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eu.inchat.adapters.ChatAdapter;
import org.eu.inchat.model.Contacto;
import org.eu.inchat.model.Mensaje;
import org.eu.inchat.server.MessageSender;
import org.eu.inchat.tasks.LoadContactsAsyncTask;
import org.eu.inchat.tasks.ServerAsyncTask;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ChatActivity extends ListActivity implements MessagesListener {
	
	private List<String> chats;
	private Contacto contacto;
	private List<Mensaje> mensajes = new ArrayList<Mensaje>();
	
	
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
	
	
	private void enviaMensaje(String mensaje) {
		
		
		
		ServerAsyncTask task = ServerAsyncTask.newInstance(this);
		
		String[] params = new String[]{"9003",contacto.getNumeroTelefono(), mensaje};
		task.execute(params);
		
		
		Mensaje nuevoMensaje = new Mensaje(mensaje, new Date(), true, contacto.getNumeroTelefono());
		ChatAdapter adapter = (ChatAdapter) getListAdapter();
		
		/* Se pueden modificar la lista de mensajes o añadir con el metodo add
		 * del adapter
		 */
		//Opcion 1: mensajes.add(new Mensaje(mensaje, new Date(), true, contacto.getNumeroTelefono()));
		adapter.add(nuevoMensaje); //Otra forma de hacerlo:
		
		//Notifica que hay nuevo mensaje al adapter
		adapter.notifyDataSetChanged();
		
	}

	@Override
	public void onMessageSent() {
		
		String mensaje = "ENVIADO!";
		
		Mensaje nuevoMensaje = new Mensaje(mensaje, new Date(), true, contacto.getNumeroTelefono());
		ChatAdapter adapter = (ChatAdapter) getListAdapter();
		adapter.add(nuevoMensaje);
		
		//Notifica que hay nuevo mensaje al adapter
		adapter.notifyDataSetChanged();		
		
	}
	
	
		

}
