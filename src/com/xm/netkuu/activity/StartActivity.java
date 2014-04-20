package com.xm.netkuu.activity;

import java.util.Locale;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.xm.netkuu.data.net.UrlClient;
import com.xm.netkuu.data.net.UrlData;
import com.xm.netkuu.player.R;
import com.xm.netkuu.ui.widget.InputHostAddressDialog;
import com.xm.netkuu.ui.widget.MessageDialog;
import com.xm.netkuu.util.Verification;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class StartActivity extends SherlockFragmentActivity {
	private TextView mProgressInfo;
	private View mProgressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		mProgressInfo = (TextView) findViewById(R.id.progress_info);
		mProgressBar = findViewById(R.id.loading_progress);
		SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
		String hostAddr = preference.getString(getString(R.string.host_addr_key), null);
		if(hostAddr == null || hostAddr.length() < 5){
			inputHostAddress();
		}
		else{
			setHostAddress(hostAddr, true);
		}		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	private void inputHostAddress(){
		InputHostAddressDialog mDialog = new InputHostAddressDialog();
		
		mDialog.setOnDismissListener(new InputHostAddressDialog.OnDismissListener() {
			@Override
			public void onDismiss(InputHostAddressDialog dialog) {
				if(dialog.isQuit()){
					finish();
				}
				else{
					String hostAddr = dialog.getHostAddress();
					if(hostAddr != null && hostAddr.length() > 5){
						setHostAddress(hostAddr, false);
					}
					else{
						finish();
					}
				}
			}
		});
		mDialog.show(getSupportFragmentManager(), null);
	}
	
	private void setHostAddress(String hostAddr, boolean fromPreferences){
		if(!fromPreferences){
			hostAddr = hostAddr.trim().toLowerCase(Locale.getDefault());
			if(Verification.verifyHostAddress(hostAddr)){
				SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
				hostAddr = UrlData.setHostAddress(hostAddr);
				Editor edit = preference.edit();
				edit.putString(getString(R.string.host_addr_key), hostAddr);
				edit.commit();
				probeSearchDelimiter();
			}
			else{
				inputHostAddress();
			}
		}
		else{
			UrlData.setHostAddress(hostAddr);
			probeSearchDelimiter();
		}
	}
	
	private void probeSearchDelimiter(){
		new AsyncTask<String, Void, String>(){

			@Override
			protected void onPreExecute(){
				mProgressInfo.setVisibility(View.VISIBLE);
				mProgressBar.setVisibility(View.VISIBLE);
				mProgressInfo.setText(R.string.probe_url_delimiter);
			}
			
			@Override
			protected String doInBackground(String... delimiters) {
				for(String delimiter : delimiters){
					String response = UrlClient.request(UrlData.probeUrlDelimiter(delimiter));
					if(response != null && response.length() > 0 && response.lastIndexOf("<l>") > 0){
						response = response.substring(response.lastIndexOf("<l>"), response.lastIndexOf("</l>"));
						response = response.substring(response.lastIndexOf("<a>") + 3, response.lastIndexOf("</a>"));
						if(Integer.parseInt(response) > 0)
							return delimiter;
					}
				}
				return null;
			}
			@Override
			protected void onPostExecute(String delimiter){
				if(delimiter != null && delimiter.length() > 0){
					UrlData.setUrlDelimiter(delimiter);
					mProgressInfo.setText(R.string.finish);
					startActivity(new Intent(StartActivity.this, MainActivity.class));
					StartActivity.this.finish();
				}
				else{
					MessageDialog.error(getSupportFragmentManager(), R.string.msg_probe_url_failed);
				}
			}
		}.execute(UrlData.sAvaliableUrlDelimiter);
	}
}
