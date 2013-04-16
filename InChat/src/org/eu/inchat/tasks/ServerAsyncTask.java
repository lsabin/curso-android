package org.eu.inchat.tasks;

import org.eu.inchat.LoadContactsListener;
import org.eu.inchat.MessagesListener;
import org.eu.inchat.server.MessageSender;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ServerAsyncTask extends AsyncTask<String, Void, Integer> {
	
	private String host = "sun.corenetworks.es";
	private int puerto = 1234;
	
	public static MessagesListener mListener;
	
	public static ServerAsyncTask newInstance(MessagesListener listener) {
		mListener = listener;
		ServerAsyncTask l = new ServerAsyncTask();
		return l;
	}

	@Override
	protected Integer doInBackground(String ... params) {
		
		
		String remitente = params[0];
		String receptor = params[1];
		String mensaje = params[2];
		
		
		MessageSender sender = new MessageSender(host, puerto);
		
		String mensajeProtocolo = sender.createMessage(remitente, receptor, mensaje);
		
		try {
			sender.sendMessage(mensajeProtocolo);
			mListener.onMessageSent();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		
		
		return Integer.valueOf(0);
		
	}
	
	

}
