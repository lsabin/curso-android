package org.eu.inchat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eu.inchat.adapters.InChatAdapter;
import org.eu.inchat.model.Contacto;
import org.eu.inchat.model.Mensaje;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

public class InChatActivity extends ListActivity {
	
	private List<Contacto> historico;
	
	private OnClickListener listenerNuevoChat = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Log.d(getClass().getName(),"Inicia activity contactos....");
			
			nuevoChat();
			
			
		}
	};
	
	private void nuevoChat() {
		
		Intent i = new Intent(this,ContactosActivity.class);
		startActivity(i);
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_chat);
        
        initButtons();
        
        historico = initHistory();
        initListView(historico);
        
        
        
    }
    
    private void initButtons() {
    	ImageButton botonAddChat = (ImageButton) findViewById(R.id.imageAddChat);
    	botonAddChat.setOnClickListener(listenerNuevoChat);
    }
    
    
    private List<Contacto> initHistory() {
    	List<Contacto> historico = new ArrayList<Contacto>();
    	List<Mensaje> mensajes = new ArrayList<Mensaje>();
    	
    	for (int i = 1; i < 11; i++) {
    		mensajes.add(new Mensaje("Mensaje " + i,new Date(),false,"x"));

    	}
    	
    	for (int i = 1; i < 11; i++) {
    		Contacto contacto = new Contacto("Nombre contacto " + i,"Numero " + i, null);
    		contacto.setUltimaConexion(new Date());
    		
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    		
    		contacto.setMensajes(mensajes);
    		historico.add(contacto);
    	}
    	
    	return historico; 
    }
    
    private void initListView(List<Contacto> historico) {
    	
    	/*setListAdapter(new ArrayAdapter<String>(getApplicationContext(),
    			android.R.layout.activity_list_item, historico));
    			*/
    	
    	setListAdapter(new InChatAdapter(getApplicationContext(),
    			R.layout.activity_inchat_listitem, historico));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.in_chat, menu);
        return true;
    }


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Log.d(getClass().getName(),"Chat seleccionado: " + historico.get(position));
		
		abreChatContacto(position);
		
	}
	
	private void abreChatContacto(int position) {
		
		Contacto chat = historico.get(position);
		
		//Pasar el id del chat a la activity para que recupere los datos de la conversación
		
		Intent i = new Intent(this, ChatActivity.class);
		startActivity(i);
	}
    
}
