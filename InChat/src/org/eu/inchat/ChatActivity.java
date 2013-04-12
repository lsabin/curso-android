package org.eu.inchat;

import java.util.ArrayList;
import java.util.List;

import org.eu.inchat.adapters.ChatAdapter;
import org.eu.inchat.model.Contacto;
import org.eu.inchat.model.Mensaje;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ChatActivity extends ListActivity {
	
	private List<String> chats;
	private Contacto contacto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_contacto);
		
		initScreen();
		initListView(obtieneChats());
	}
	
	private void initScreen() {
		contacto = (Contacto) getIntent().getSerializableExtra("contacto");
		TextView textoNombre = (TextView) findViewById(R.id.textoNombreContacto);
		textoNombre.setText(contacto.getNombre());
	}
	
	private List<Mensaje> obtieneChats() {
		return contacto.getMensajes();
	}
	
	private void initListView(List<Mensaje> mensajes) {
    	setListAdapter(new ChatAdapter(getApplicationContext(),
    			R.layout.chat_item, mensajes));
	}
		

}
