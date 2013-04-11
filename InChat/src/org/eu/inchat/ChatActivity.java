package org.eu.inchat;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class ChatActivity extends ListActivity {
	
	private List<String> chats;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_contacto);
		
		initListView(obtieneChats());
	}
	
	private List<String> obtieneChats() {
		List<String> chats = new ArrayList<String>();
		
		for (int i = 0; i < 10; i++) {
			chats.add("Conversacion " + i);
		}
		
		return chats;
	}
	
	private void initListView(List<String> chats) {
		
    	setListAdapter(new ArrayAdapter<String>(getApplicationContext(),
    			android.R.layout.simple_list_item_1, chats));		
		
	}
		

}
