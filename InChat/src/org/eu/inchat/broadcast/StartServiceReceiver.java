package org.eu.inchat.broadcast;


import org.eu.inchat.services.InChatService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartServiceReceiver extends BroadcastReceiver {
	
	@Override
	  public void onReceive(Context context, Intent intent) {
	    Intent service = new Intent(context, InChatService.class);
	    context.startService(service);
	  }
	
}
