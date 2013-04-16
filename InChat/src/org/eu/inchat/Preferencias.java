package org.eu.inchat;

import org.eu.inchat.fragments.FragmentPreferencias;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.app.FragmentActivity;

public class Preferencias extends PreferenceActivity {

	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preferencias);
		
	/*	Para usar fragmentos 
		 getFragmentManager().beginTransaction()
			.replace(android.R.id.content, new FragmentPreferencias())
			.commit();		*/
	}
	
	
	

}
