package com.xm.netkuu.activity;

import com.xm.netkuu.player.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		this.addPreferencesFromResource(R.xml.settings);
	}
}
