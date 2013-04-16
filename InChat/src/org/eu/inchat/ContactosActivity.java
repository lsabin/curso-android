package org.eu.inchat;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eu.inchat.adapters.ContactosAdapter;
import org.eu.inchat.model.Contacto;
import org.eu.inchat.model.Mensaje;
import org.eu.inchat.tasks.LoadContactsAsyncTask;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class ContactosActivity extends ListActivity implements LoadContactsListener {
	
	private List<Contacto> contactos;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        setContentView(R.layout.contacts);
        
        findViewById(R.id.textoProgreso).setVisibility(View.GONE);
        TextView textoProgreso = (TextView) findViewById(R.id.textoProgreso);
        textoProgreso.setText("Cargando contactos...");
        findViewById(android.R.id.list).setVisibility(View.GONE);
        
        /*contactos = new ArrayList<Contacto>();
        new LoadContactsTask().execute(new URL[0]);
        */
        
        //Otra opcion
        if(contactos == null || contactos.isEmpty()) {
	        LoadContactsAsyncTask task = LoadContactsAsyncTask.newInstance(this);
	        task.execute(this);
        }
        
        
        
    }

	
	@Deprecated
    private List<Contacto> obtieneContactos() {
    	List<Contacto> contactos = obtieneContactosReales();
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

	
	private void initListView() {
		
    	setListAdapter(new ContactosAdapter(getApplicationContext(),
    			R.layout.contact_item, contactos));
	}
	
	
	
	

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		abreChatContacto(position);
		
	}
	
	private void abreChatContacto(int position) {
		
		Contacto contacto = contactos.get(position);
		
		Log.d(getClass().getName(), contacto.getNumeroTelefono());

		
		Intent i = new Intent(this, ChatActivity.class);
		i.putExtra("contacto", contacto);
		startActivity(i);		
		
	}	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.in_chat, menu);
        return true;
    }
    
	protected List<Contacto> obtieneContactosReales() {

		return Contactos.obtieneContactosReales(getApplicationContext());

	}    
	
	
	public void comienzaCargaContactos() {
		 dialog = ProgressDialog.show(this, "Download", "downloading");		
	}
	
	
	
	
	
	
	@Override
	public void onContactsLoaded(List<Contacto> contacts) {
		
		dialog.dismiss();
		
        findViewById(android.R.id.list).setVisibility(View.VISIBLE);
		contactos = contacts;
		
		creaMensajesPrueba(contactos);
		
		initListView();		
		
	}

	
	
	private List<Mensaje> creaMensajesPrueba(List<Contacto> contactos) {
		
		List<Mensaje> conversacion = new ArrayList<Mensaje>();		

		// Crear mensajes para un par de contactos de prueba
		Mensaje[] conversacion1 = new Mensaje[] {
				new Mensaje("Hola", new Date(new Date().getTime() + 1), true,
						"1"),
				new Mensaje("Hola", new Date(new Date().getTime() + 1), false,
						"1"),
				new Mensaje("Que tal?", new Date(new Date().getTime() + 1),
						true, "1"),
				new Mensaje("Bien", new Date(new Date().getTime() + 1), false,
						"1"),
				new Mensaje("Y tú?", new Date(new Date().getTime() + 1), false,
						"1"),
				new Mensaje(
						"Pues muy bien porque bla bla bla bla bla  bla bla",
						new Date(new Date().getTime() + 1), true, "1"), };
		
		for(int i = 0; i < conversacion1.length; i++) {
			conversacion.add(conversacion1[i]);
		}
		

		for (Contacto contacto : contactos) {
			
			String numeroTelefono = contacto.getNumeroTelefono();
			numeroTelefono = numeroTelefono.replaceAll("-","");
			numeroTelefono = numeroTelefono.replaceAll(" ","");
			
			if ("986710903".equals(numeroTelefono)) {
				contacto.setMensajes(conversacion);
			} else if ("622098721".equals(numeroTelefono)) {
				contacto.setMensajes(conversacion);
			} else if ("981344287".equals(numeroTelefono)) {
				contacto.setMensajes(conversacion);
			} else {
				contacto.setMensajes(new ArrayList<Mensaje>());
			}

		}

		return conversacion;
	}	




	@Deprecated
	private class LoadContactsTask extends AsyncTask<URL, Integer, List<Contacto>> {

		@Override
		protected List<Contacto> doInBackground(URL... arg0) {

			contactos = obtieneContactosReales();
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return contactos;
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}


		@Override
		protected void onPostExecute(List<Contacto> result) {
			
			
			
	        findViewById(R.id.textoProgreso).setVisibility(View.GONE);
	        findViewById(android.R.id.list).setVisibility(View.VISIBLE);
			
			initListView();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
		
		
		
		
		
	}
	
	
		

}
