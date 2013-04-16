package org.eu.inchat.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import org.eu.inchat.R;

@SuppressLint("NewApi")
public class FragmentPreferencias extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preferencias);
	}
	
	
	
	

}
