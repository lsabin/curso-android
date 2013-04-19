package org.eu.inchat.services;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.eu.inchat.InChatActivity;
import org.eu.inchat.MessagesListener;
import org.eu.inchat.R;
import org.eu.inchat.ReadMessagesListener;
import org.eu.inchat.db.ContactsDAO;
import org.eu.inchat.model.Mensaje;
import org.eu.inchat.tasks.ReadMessagesAsyncTask;
import org.eu.inchat.tasks.ServerAsyncTask;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

public class InChatService2 extends Service implements MessagesListener, ReadMessagesListener {
	/** For showing and hiding our notification. */
	NotificationManager mNM;
	/** Keeps track of all current registered clients. */
	ArrayList<Messenger> mClients = new ArrayList<Messenger>();
	/** Holds last value set by a client. */
	int mValue = 0;

	private static boolean isRunning = false;
	private Timer timer = new Timer();
	private int counter = 0;
	private final int incrementby = 1;

	/**
	 * Command to the service to register a client, receiving callbacks from the
	 * service. The Message's replyTo field must be a Messenger of the client
	 * where callbacks should be sent.
	 */
	public static final int MSG_REGISTER_CLIENT = 1;

	/**
	 * Command to the service to unregister a client, ot stop receiving
	 * callbacks from the service. The Message's replyTo field must be a
	 * Messenger of the client as previously given with MSG_REGISTER_CLIENT.
	 */
	public static final int MSG_UNREGISTER_CLIENT = 2;

	/**
	 * Command to service to set a new value. This can be sent to the service to
	 * supply a new value, and will be sent by the service to any registered
	 * clients with the new value.
	 */
	public static final int MSG_SET_MSG_VALUE = 6;
	public static final int MSG_SET_CHECK_INTERVAL = 7;
	private long checkInterval = 60000L;



	/**
	 * Handler of incoming messages from clients.
	 */
	static class IncomingHandler extends Handler {
		private final WeakReference<InChatService2> mService;

		IncomingHandler(InChatService2 inChatService2) {
			mService = new WeakReference<InChatService2>(inChatService2);
		}

		@Override
		public void handleMessage(Message msg) {
			InChatService2 service = mService.get();
			if (service != null) {
				service.handleMessage(msg);
			}

		}
	}

	private void sendMessageToUI(List<Mensaje> messages) {
		//saveMessagesToDB(messages);
		if (mClients.size() == 0) {

			showNotificationOnNewMessage(messages);
			return;
		}
		for (int i = mClients.size() - 1; i >= 0; i--) {
			try {
				Bundle b = new Bundle();
				for (Mensaje mm : messages) {
					b.putSerializable("message", mm);
					Message msg = Message.obtain(null, MSG_SET_MSG_VALUE);
					msg.setData(b);
					mClients.get(i).send(msg);
				}

			} catch (RemoteException e) {
				// The client is dead. Remove it from the list; we are going
				// through the list from back to front so this is safe to do
				// inside the loop.
				mClients.remove(i);
			}
		}
	}

	public void handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_REGISTER_CLIENT:
			mClients.add(msg.replyTo);
			break;
		case MSG_UNREGISTER_CLIENT:
			mClients.remove(msg.replyTo);
			break;
		case MSG_SET_MSG_VALUE:
			Mensaje mm = (Mensaje) msg.getData().getSerializable("message");
			Log.d("ServiceHandler",
					"Received MSG value from client: " + mm.getTextoMensaje());

			ServerAsyncTask smt = ServerAsyncTask.newInstance(
					this,getApplicationContext());
			smt.execute(mm);

			break;
		case MSG_SET_CHECK_INTERVAL:
			long check = msg.getData().getLong("checkInterval");
			Log.d(getClass().getName(), "Setting check interval to: " + check);
			checkInterval = check;
			if (timer != null) {
				timer.cancel();
			}
			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					onTimerTick();
				}
			}, 0, checkInterval);
			break;
		default:

		}

	}

	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	final Messenger mMessenger = new Messenger(new IncomingHandler(this));

	@Override
	public void onCreate() {
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// Display a notification about us starting.
		showNotification();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				onTimerTick();
			}
		}, 0, checkInterval);
		isRunning = true;
	}

	@Override
	public void onDestroy() {
		// Cancel the persistent notification.
		mNM.cancel(1234);

		// Tell the user we stopped.
		Toast.makeText(this, "Servicio detenido",
				Toast.LENGTH_SHORT).show();
		if (timer != null) {
			timer.cancel();
		}
		counter = 0;
		Log.i("MessengerService", "Service Stopped.");
		isRunning = false;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("MessengerService", "Received start id " + startId + ": "
				+ intent);
		return START_STICKY; // run until explicitly stopped.
	}

	/**
	 * When binding to the service, we return an interface to our messenger for
	 * sending messages to the service.
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();
	}

	/**
	 * Show a notification while this service is running.
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void showNotification() {
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, InChatActivity.class), 0);

		Notification noti = new Notification.Builder(this)
				.setContentTitle("Servicio arrancado")
				.setContentText("InChat service")
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(contentIntent).build();

		// Hide the notification after its selected
		noti.flags |= Notification.FLAG_AUTO_CANCEL;
		mNM.notify(0, noti);
	}

	/**
	 * Show a notification while this service is running.
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void showNotificationOnNewMessage(List<Mensaje> messages) {
		// In this sample, we'll use the same text for the ticker and the
		// expanded notification

		for (Mensaje mm : messages) {

			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					new Intent(this, InChatActivity.class), 0);

			Notification noti = new Notification.Builder(this)
					.setContentTitle(getText(R.string.app_name))
					.setContentText(
							getContactDisplayNameByNumber(mm.getUserId()) + ": "
									+ mm.getTextoMensaje())
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentIntent(contentIntent).build();

			// Hide the notification after its selected
			noti.flags |= Notification.FLAG_AUTO_CANCEL;
			// Set default vibration
			noti.defaults |= Notification.DEFAULT_VIBRATE;
			// Set default notification sound
			noti.defaults |= Notification.DEFAULT_SOUND;
			mNM.notify(0, noti);
		}
	}

	public static boolean isRunning() {
		return isRunning;
	}

	private void onTimerTick() {
		Log.i("TimerTick", "Timer doing work." + counter);
		try {

			ReadMessagesAsyncTask rmt = ReadMessagesAsyncTask.newInstance(
					 this,getApplicationContext());
			rmt.execute();

			counter += incrementby;

		} catch (Throwable t) { // you should always ultimately catch all
								// exceptions in timer tasks.
			Log.e("TimerTick", "Timer Tick Failed.", t);
		}
	}

	@Deprecated
	private void saveMessagesToDB(List<Mensaje> mm) {
		
		ContactsDAO dao = new ContactsDAO(getApplicationContext());
		for (Mensaje m : mm) {
			dao.creaMensaje(m, false);
		}
	}

	public String getContactDisplayNameByNumber(String number) {
		Uri uri = Uri.withAppendedPath(
				ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(number));
		String name = "?";

		ContentResolver contentResolver = getContentResolver();
		Cursor contactLookup = contentResolver.query(uri, new String[] {
				BaseColumns._ID, ContactsContract.PhoneLookup.DISPLAY_NAME },
				null, null, null);

		try {
			if (contactLookup != null && contactLookup.getCount() > 0) {
				contactLookup.moveToNext();
				name = contactLookup.getString(contactLookup
						.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
				// String contactId =
				// contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
			}
		} finally {
			if (contactLookup != null) {
				contactLookup.close();
			}
		}

		return name;
	}


	@Override
	public void onMessageSent(Integer result) {
		Log.d(getClass().getName(), "Sent message: " + result);
		
	}

	@Override
	public void onMessagesReceived(List<Mensaje> result) {
		if (result.size() != 0) {
			sendMessageToUI(result);
		}
		
	}

}
