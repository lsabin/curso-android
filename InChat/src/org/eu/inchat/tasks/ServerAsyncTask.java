package org.eu.inchat.tasks;

import org.eu.inchat.MessagesListener;
import org.eu.inchat.db.ContactsDAO;
import org.eu.inchat.model.Mensaje;
import org.eu.inchat.server.MessageSender;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class ServerAsyncTask extends AsyncTask<Mensaje, Void, Integer> {

	private static String host;
	private static int puerto;
	private static String ownerPhone;

	public static MessagesListener mListener;
	private static Context context;
	
	private ContactsDAO dao;

	public static ServerAsyncTask newInstance(MessagesListener listener,
			Context theContext) {
		mListener = listener;
		context = theContext;
		ServerAsyncTask l = new ServerAsyncTask();
		obtieneConfiguracion();
		return l;
	}
	
	private static void obtieneConfiguracion() {
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		host = prefs.getString("server_ip", "");
		puerto = Integer.parseInt(prefs.getString("server_port", "1"));
		ownerPhone = prefs.getString("user_id", "0");
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPuerto() {
		return puerto;
	}

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}

	public String getOwnerPhone() {
		return ownerPhone;
	}

	public void setOwnerPhone(String ownerPhone) {
		this.ownerPhone = ownerPhone;
	}

	@Override
	protected Integer doInBackground(Mensaje... params) {
		
		int sentMessages = 0;

		MessageSender sender = new MessageSender(host, puerto);
		
		
		for(int i = 0;i<params.length;i++) {
			Mensaje mensaje = params[i];
			
			try {
				Log.d(getClass().getName(), "remite: " + ownerPhone);
				insertaMensaje(mensaje);
				sender.sendMessage(ownerPhone, mensaje.getUserId(), mensaje.getTextoMensaje());
				sentMessages++;

			} catch (Exception e) {
				Log.e(getClass().getName(), "Error: " + e.getMessage());
			}
			
		}


		return sentMessages;

	}
	
	
	@Override
	protected void onPostExecute(Integer result) {
		mListener.onMessageSent(result);
	}	
	
	
	private void insertaMensaje(Mensaje mensaje) {
		ContactsDAO dao = new ContactsDAO(context);
		
		dao.open();
		dao.creaMensaje(mensaje, true);
		dao.close();
		
	}

}
