package org.eu.inchat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eu.inchat.adapters.ContactosAdapter;
import org.eu.inchat.adapters.InChatAdapter;
import org.eu.inchat.model.Contacto;
import org.eu.inchat.model.Mensaje;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ContactosActivity extends ListActivity {
	
	private List<Contacto> contactos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        setContentView(R.layout.contacts);
        
        contactos = obtieneContactos();
        initListView(contactos);
    }

	
    private List<Contacto> obtieneContactos() {
    	List<Contacto> contactos = new ArrayList<Contacto>();
    	List<Mensaje> mensajes = new ArrayList<Mensaje>();
    	
    	for (int i = 1; i < 11; i++) {
    		mensajes.add(new Mensaje("Mensaje " + i,new Date(),false,"x"));

    	}
    	
    	for (int i = 1; i < 11; i++) {
    		Contacto contacto = new Contacto("Nombre contacto " + i,"Numero " + i, null);
    		contacto.setUltimaConexion(new Date());
    		contacto.setMensajes(mensajes);
    		contacto.setMensajeEstado("Disponible");
    		contactos.add(contacto);
    	}
    	
    	return contactos; 
    }	

	
	private void initListView(List<Contacto> contactos) {
		
    	setListAdapter(new ContactosAdapter(getApplicationContext(),
    			R.layout.contact_item, contactos));
	}
	
	
	
	

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		abreChatContacto(position);
		
	}
	
	private void abreChatContacto(int position) {
		
		Contacto chat = contactos.get(position);
		
		//Pasar el id del chat a la activity para que recupere los datos de la conversación
		
		Intent i = new Intent(this, ChatActivity.class);
		startActivity(i);
	}	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.in_chat, menu);
        return true;
    }
	
	
		

}
