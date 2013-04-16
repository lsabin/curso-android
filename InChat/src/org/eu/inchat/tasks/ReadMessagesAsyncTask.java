package org.eu.inchat.tasks;

import java.util.ArrayList;
import java.util.List;

import org.eu.inchat.ReadMessagesListener;
import org.eu.inchat.model.Mensaje;
import org.eu.inchat.server.MessageSender;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class ReadMessagesAsyncTask extends AsyncTask<Void, Void, List<Mensaje>> {

	private static String host;
	private static int puerto;

	private static String ownerPhone;

	public static ReadMessagesListener mListener;
	private static Context context;
	
	
	private static void obtieneConfiguracion() {
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		host = prefs.getString("server_ip", "");
		puerto = Integer.parseInt(prefs.getString("server_port", "1"));
		ownerPhone = prefs.getString("user_id", "0");
	}

	public static ReadMessagesAsyncTask newInstance(ReadMessagesListener listener,
			Context theContext) {
		mListener = listener;
		context = theContext;
		ReadMessagesAsyncTask l = new ReadMessagesAsyncTask();
		
		obtieneConfiguracion();
		return l;
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
	protected List<Mensaje> doInBackground(Void... params) {
		
		List<Mensaje> mensajesRecibidos = new ArrayList<Mensaje>();

		MessageSender sender = new MessageSender(host, puerto);
		
		try {
			mensajesRecibidos = sender.readMessages(ownerPhone);
			
			Log.d(getClass().getName(),"Mensajes recibidos: " + mensajesRecibidos);

		} catch (Exception e) {
			Log.e(getClass().getName(), "Error: " + e.getMessage());
		}
			

		return mensajesRecibidos;

	}
	
	
	@Override
	protected void onPostExecute(List<Mensaje> resultado) {
		
		Log.d(getClass().getName(),"Mensajes recibidos: " + resultado);
		
		mListener.onMessagesReceived(resultado);
	}	

}
