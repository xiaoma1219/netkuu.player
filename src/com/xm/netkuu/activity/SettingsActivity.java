package com.xm.netkuu.activity;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.xm.netkuu.data.net.UrlData;
import com.xm.netkuu.player.R;
import com.xm.netkuu.util.Verification;

import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
//import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

public class SettingsActivity extends SherlockPreferenceActivity {

	DialogPreference performance;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		final String hostAddrKey = getString(R.string.host_addr_key);
		EditTextPreference mEditTextPreference = (EditTextPreference) findPreference(hostAddrKey);
		mEditTextPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener(){

			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				String hostAddress = newValue.toString();
				if(Verification.verifyHostAddress(hostAddress)){
					hostAddress = UrlData.setHostAddress(hostAddress);				
					return true;
				}
				else{
					Toast.makeText(SettingsActivity.this, "主机地址输入错误（你是否忘记了\"http://\"）", Toast.LENGTH_LONG).show();
					return false;
				}
			}			
		});
		/*
		mEditTextPreference.setOnPreferenceClickListener(new OnPreferenceClickListener(){

			@Override
			public boolean onPreferenceClick(Preference preference) {
				String hostAddress = UrlData.HOST;
				preference.getEditor().putString(hostAddrKey, hostAddress).commit();
				return true;
			}
		});
		*/
	}
}
