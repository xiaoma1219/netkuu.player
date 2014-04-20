package com.xm.netkuu.activity;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.xm.netkuu.player.R;

import android.os.Bundle;
import android.preference.DialogPreference;

public class SettingsActivity extends SherlockPreferenceActivity {

	DialogPreference performance;
	@SuppressWarnings("deprecation")
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		this.addPreferencesFromResource(R.xml.settings);
	}
}
