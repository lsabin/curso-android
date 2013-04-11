package org.eu.inchat;

import java.util.ArrayList;
import java.util.List;

import org.eu.inchat.adapters.ContactosAdapter;
import org.eu.inchat.adapters.InChatAdapter;
import org.eu.inchat.model.Contacto;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ContactosActivity extends ListActivity {
	
	private List<String> contactos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        setContentView(R.layout.contacts);
        
        contactos = obtieneContactos();
        initListView(contactos);
    }
	
	private List<String> obtieneContactos() {
		List<String> contactos = new ArrayList<String>();
		
		for (int i = 0; i < 10; i++) {
			contactos.add("Nombre " + i);
		}
		
		return contactos;
	}
	
	private void initListView(List<String> contactos) {
		
    	setListAdapter(new ContactosAdapter(getApplicationContext(),
    			R.layout.contact_item, contactos));
	}
	

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.in_chat, menu);
        return true;
    }
	
	
		

}
