package org.eu.inchat.services;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.eu.inchat.model.Mensaje;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class InChatService extends Service {

	private NotificationManager notificationManager;
	private List<Messenger> clients = new ArrayList<Messenger>();

	public static final int MSG_REGISTER_CLIENT = 1;
	public static final int MSG_UNREGISTER_CLIENT = 2;
	public static final int MSG_SET_MESSAGE = 3;
	public static final int MSG_SET_CHECK_INTERVAL = 4;

	private long checkInterval = 60000L;
	
	final Messenger messenger = new Messenger(new IncomingHandler(this));

	static class IncomingHandler extends Handler {
		private final WeakReference<InChatService> service;

		public IncomingHandler(InChatService service) {
			this.service = new WeakReference<InChatService>(service);
		}

		@Override
		public void handleMessage(Message msg) {
			InChatService service = this.service.get();
			if (service != null) {
				service.handleMessage(msg);

			}
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		return messenger.getBinder();
	}

	
	
	
	@Override
	public void onCreate() {
		
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		showNotification();
		
		//TODO: lanzar temporizador para comprobar nuevos mensajes
		
	}
	
	




	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}




	public void handleMessage(Message msg) {

		switch (msg.what) {
		case MSG_REGISTER_CLIENT:
			clients.add(msg.replyTo);
			break;

		case MSG_UNREGISTER_CLIENT:
			clients.remove(msg.replyTo);
			break;
			
		case MSG_SET_MESSAGE:
			Mensaje mensaje = (Mensaje) msg.getData().getSerializable("mensaje");
			Log.d(getClass().getName(), "Enviando mensaje .... ");
			
			//TODO: Metodo para enviar el mensaje
			
			break;
			
		case MSG_SET_CHECK_INTERVAL:
			//TODO: cambiar el intervalo de comprobación
			
			break;
			

		default:
			break;
		}

	}

	public long getCheckInterval() {
		return checkInterval;
	}

	public void setCheckInterval(long checkInterval) {
		this.checkInterval = checkInterval;
	}
	
	private void showNotification() {
		
	}
	
	
	private void newMessageNotification(List<Mensaje> mensajes) {
		
		if (clients.isEmpty()) {
			showNewMessageNotification(mensajes);
		} else {
			
			for(int i = 0; i < clients.size(); i++) {
				Bundle bundle = new Bundle();
				for (Mensaje mensaje : mensajes) {
					bundle.putSerializable("mensaje",mensaje);
					Message message = Message.obtain(null, MSG_SET_MESSAGE);
					message.setData(bundle);
					
					try {
						clients.get(i).send(message);
					} catch (RemoteException e) {
						//si falla la comunicacion se elimina el cliente (ya que no estará activo)
						clients.remove(i);
						e.printStackTrace();
					}
				}
			}
		}
	}




	private void showNewMessageNotification(List<Mensaje> mensajes) {
		// TODO Auto-generated method stub
		
	}
	

}
