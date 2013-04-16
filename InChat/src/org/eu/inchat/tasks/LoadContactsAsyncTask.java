package org.eu.inchat.tasks;

import java.util.ArrayList;
import java.util.List;

import org.eu.inchat.Contactos;
import org.eu.inchat.LoadContactsListener;
import org.eu.inchat.model.Contacto;

import android.content.Context;
import android.os.AsyncTask;

public class LoadContactsAsyncTask extends AsyncTask<Context, Void, List<Contacto>>{

	public static LoadContactsListener mListener;
	
	public static LoadContactsAsyncTask newInstance(LoadContactsListener listener) {
		mListener = listener;
		LoadContactsAsyncTask l = new LoadContactsAsyncTask();
		return l;
	}
	
	@Override
	protected List<Contacto> doInBackground(Context... params) {
		
		List<Contacto> contactos = Contactos.obtieneContactosReales(params[0]);
		
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return contactos;
	}
	
	

	@Override
	protected void onPostExecute(List<Contacto> result) {
		mListener.onContactsLoaded(result);
	}

	@Override
	protected void onPreExecute() {
		mListener.comienzaCargaContactos();
	}
	
	
  	


	
	
}
