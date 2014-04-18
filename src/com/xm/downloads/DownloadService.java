package com.xm.downloads;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.xm.downloads.DownloadManager.State;
import com.xm.downloads.IDownload;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;

public class DownloadService extends Service {

	protected DownloadManager mDownloadManager;

	private IDownload.Stub mBinder = new IDownload.Stub() {

		@Override
		public void beat() throws RemoteException {
			System.out.println("Here is a beat!!!");
		}

	};
	
	BroadcastReceiver mCompeleteReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			long reference = intent.getLongExtra(
					android.app.DownloadManager.EXTRA_DOWNLOAD_ID, -1);
			if(reference > 0){
				State mQuery = mDownloadManager.query(reference);
				if(!mQuery.moveToNext()){
					mQuery.close();
					return;
				}
				String mLocalUri = mQuery.getLocalUri();
				mQuery.close();
				int mDecodeLength = 0;
				if(mLocalUri.endsWith("mkv")){
					mDecodeLength = 140;
				}
				if(mDecodeLength == 0)
					return;
				try {
					RandomAccessFile write = new RandomAccessFile(mLocalUri, "rw");
					byte[] buffer = new byte[mDecodeLength];
					int offset = 0;
					while(offset < mDecodeLength){
						int len = write.read(buffer, offset, mDecodeLength);
						for(int i = 0; i < len; i++){
							buffer[i] = (byte) (buffer[i] ^ 0xff);
						}
						write.write(buffer, offset, len);
						offset += len;
					}
					write.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("Service Created");
		mDownloadManager = DownloadManager.getInstance(this.getApplicationContext());
		
		registerReceiver(mCompeleteReceiver, new IntentFilter(
				android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("Service Stoped");
		State mQuery = mDownloadManager.query();
		while(mQuery.moveToNext()){
			mDownloadManager.pauseDownload(mQuery.getId());
		}
		mQuery.close();
		unregisterReceiver(mCompeleteReceiver);
	}
}
