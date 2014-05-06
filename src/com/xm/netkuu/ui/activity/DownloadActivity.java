package com.xm.netkuu.ui.activity;

import com.xm.netkuu.player.R;
import com.xm.downloads.DownloadManager;
import com.xm.downloads.DownloadManager.State;
import com.xm.downloads.DownloadState;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DownloadActivity extends Activity {
	private DownloadManager mDownloadManager;
	private LayoutInflater mInflater;
	private DownloadAdapter	mAdapter;
	private ListView mListView;
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			long id = intent.getLongExtra(android.app.DownloadManager.EXTRA_DOWNLOAD_ID, -1);
			System.out.println(id);
		}
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_download);
		mDownloadManager = DownloadManager.getInstance(this);
		this.mInflater = LayoutInflater.from(this);
		this.mAdapter = new DownloadAdapter(this, 0);
		this.mListView = ((ListView)this.findViewById(R.id.download_list_view));
		this.mListView.setAdapter(this.mAdapter);
		//registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		State query = mDownloadManager.query();		
		while(query.moveToNext()){
			this.mAdapter.add(new DownloadChangeObserver(new DownloadState(query)));
		}
		query.close();
		registerReceiver(mReceiver, new IntentFilter(android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.download, menu);
		return true;
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		unregisterReceiver(mReceiver);
		int length = this.mAdapter.getCount();
		System.out.println(length);
		for(int i = 0; i < length; i++){
			getContentResolver().unregisterContentObserver(this.mAdapter.getItem(i));
		}
	}
	
	class DownloadAdapter extends ArrayAdapter<DownloadChangeObserver>{		
		public DownloadAdapter(Context context, int resource) {
			super(context, resource);
		}

		@Override
		public void add(DownloadChangeObserver object){
			String uri = object.getState().mUri;
			if(uri != null){
				getContentResolver().registerContentObserver(Uri.parse(uri), true, object);
				super.add(object);
			}
		}
		
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			if(view == null){
				view = mInflater.inflate(R.layout.download_list_view_item, parent, false);
			}
			DownloadState status = this.getItem(position).getState();
			((TextView)view.findViewById(R.id.download_name)).setText(status.mTitle);
			if(status.mStatus == android.app.DownloadManager.STATUS_SUCCESSFUL){
				((TextView)view.findViewById(R.id.download_speed)).setText("");
				((TextView)view.findViewById(R.id.download_percent)).setText("Íê³É");
			}
			else{
				((TextView)view.findViewById(R.id.download_speed)).setText(Float.toString(status.mSpeed));
				((TextView)view.findViewById(R.id.download_percent)).setText(status.mCurrentBytes / status.mTotalBytes * 100 + "%");
			}
			((TextView)view.findViewById(R.id.download_total)).setText(((int)(status.mTotalBytes / 1024.0f / 1024.0f * 100 + 0.5f)) / 100.0f + "M");
			return view;
		}
	}
	
	class DownloadChangeObserver extends ContentObserver {
		private DownloadState mState;
		
        public DownloadChangeObserver(DownloadState mState) {
        	super(null);
        	this.mState = mState;
        }
  
        @Override
        public void onChange(boolean selfChange) {
        	//mState(this.status);
        	State mQuery = mDownloadManager.query(mState.mId);
        	if(mQuery.moveToNext()){
        		mState.update(mQuery);
        	}
        	mQuery.close();
        }

		public DownloadState getState() {
			return mState;
		}
    }
    
}
