package org.eu.inchat;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

public class InChatActivity extends ListActivity {
	
	private List<String> historico;
	
	private OnClickListener listenerNuevoChat = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Log.d(getClass().getName(),"Inicia activity contactos....");
		}
	};

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
    
    
    private List<String> initHistory() {
    	List<String> historico = new ArrayList<String>();
    	
    	for (int i = 0; i < 10; i++) {
    		historico.add("Conversacion " + i);
    	}
    	
    	return historico; 
    }
    
    private void initListView(List<String> historico) {
    	
    	setListAdapter(new ArrayAdapter<String>(getApplicationContext(),
    			android.R.layout.simple_list_item_1, historico));
    	
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.in_chat, menu);
        return true;
    }


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		Log.d(getClass().getName(),"Chat seleccionado: " + historico.get(position));
	}
    
    
    
    
    
}
