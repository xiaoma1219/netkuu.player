package com.xm.netkuu.player;

import java.util.List;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.xm.netkuu.player.R;
import com.xm.netkuu.player.widget.MediaController;
import com.xm.netkuu.ui.widget.MessageDialog;
import com.xm.netkuu.ui.widget.RequestVideoPlayUrlTask;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnSeekCompleteListener;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.widget.VideoView;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class PlayerActivity extends SherlockFragmentActivity implements OnBufferingUpdateListener, OnSeekCompleteListener, OnCompletionListener, OnErrorListener, OnInfoListener {
	
	private static final IntentFilter BATTERY_FILTER = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	private static final IntentFilter SCREEN_FILTER = new IntentFilter(Intent.ACTION_SCREEN_ON);
	private static final IntentFilter USER_PRESENT_FILTER = new IntentFilter(Intent.ACTION_USER_PRESENT);
	static {
		SCREEN_FILTER.addAction(Intent.ACTION_SCREEN_OFF);
	}
	
	private VideoView mVideoView;
	private MediaController mMediaController = null;
	
	private String mPlayUrl;
	private String mVideoName;
	private Integer mCurrentEposide;
	private String mVid;
	
	private boolean mPaused = false;
	private boolean mVideoOpened = false;
	private boolean mInitialized = false;
	
	private View mVideoLoadingView;
	private TextView mVideoLoadingInfo;
	
	private ScreenReceiver mScreenReceiver = new ScreenReceiver();
	private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
				if(mMediaController != null && mMediaController.isShowing()){
					int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
					int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
					int percent = scale > 0 ? level * 100 / scale : 0;
					if (percent > 100)
						percent = 100;
					mMediaController.setBatteryLevel(percent);
				}
			}
		}
	};
	
	private BroadcastReceiver mUserPresentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (isRootActivity()) {
				playerStart();
			}
		}
	};
	
	private PlayerHistory mPlayHistory;
	private long mHistoryId = -1L;
	private long mLastProgress = -1L;
	private static final int MSG_UPDATE_PROGRESS = 1001;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case MSG_UPDATE_PROGRESS:
				sendEmptyMessageDelayed(MSG_UPDATE_PROGRESS, 1000);
				updatePlayProgress();
				break;
			}
		}
	};
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_player);
		
		registerReceiver(mBatteryReceiver, BATTERY_FILTER);
		registerReceiver(mScreenReceiver, SCREEN_FILTER);
		registerReceiver(mUserPresentReceiver, USER_PRESENT_FILTER);

		mVideoLoadingView = findViewById(R.id.video_loading);
		mVideoLoadingInfo = (TextView) findViewById(R.id.video_loading_text);
		
		mVideoView = (VideoView) findViewById(R.id.video_view);	
		
		mVideoView.setOnCompletionListener(this);
		mVideoView.setOnInfoListener(this);
		
		mVideoView.requestFocus();
		mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mediaPlayer) {
				mediaPlayer.setPlaybackSpeed(1.0f);
			}
		});
		initialize();		
		getIntentValue(getIntent());
		openVideo(mVid, mCurrentEposide);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	@Override
	public void onStart(){
		super.onStart();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		playerStart();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		playerPause();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		mVideoOpened = false;
		unregisterReceiver(mBatteryReceiver);
		unregisterReceiver(mScreenReceiver);
		unregisterReceiver(mUserPresentReceiver);
	}	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		mVideoView.setVisibility(View.GONE);
		if (isInitialized()){
			mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, mVideoView.getVideoAspectRatio());
			
		}
		mVideoView.setVisibility(View.VISIBLE);
		super.onConfigurationChanged(newConfig);
	}
	
	public void initialize(){
		if(!Vitamio.isInitialized(this)){
			Vitamio.initialize(this, R.raw.libarm);
		}
		if(Vitamio.isInitialized(this)){
			mInitialized = true;
		}		
	}
	
	public boolean isInitialized(){
		return mInitialized;
	}
	
	public void getIntentValue(Intent intent){
		mVideoName = intent.getStringExtra("name");
		mVid = intent.getStringExtra("vid");
		mCurrentEposide = intent.getIntExtra("eposide", 0);
	}
	
	public void openVideo(String vid, Integer episode){
		new RequestPlayUrl().execute(vid, episode);
	}
	
	public void openVideo(){
		if(mInitialized && mScreenReceiver.mScreenOn && !mVideoOpened && mPlayUrl != null){
			if(mMediaController != null && mMediaController.isShowing()){
				mMediaController.hide();
				mMediaController = null;
			}
			mMediaController = new MediaController(this);
			mVideoView.setMediaController(mMediaController);
			mVideoView.setVideoPath( mPlayUrl );
			long progress = getPlayProgress();
			if(progress > 0){
				mVideoView.seekTo(progress);
			}
			mMediaController.setFileName(mVideoName);
			mVideoOpened = true;
			mHandler.sendEmptyMessage(MSG_UPDATE_PROGRESS);//记录播放位置
		}
	}
	
	public void playerPause(){
		if(mInitialized){
			mPaused = true;
			mVideoView.pause();
		}
	}
	
	public void playerStart(){
		if(mInitialized && mScreenReceiver.mScreenOn){
			if(mVideoOpened){
				if(mPaused && !mVideoView.isPlaying()){
					mPaused = false;
					mVideoView.start();
				}
			}
			else
				openVideo();
		}
	}
	
	private boolean isRootActivity() {
		ActivityManager am = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> list = am
				.getRunningAppProcesses();
		if (list.size() == 0)
			return false;
		for (ActivityManager.RunningAppProcessInfo process : list) {
			if (process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
					&& process.processName.equals(getClass().getName())) {
				return true;
			}
		}
		return false;
	}
	
	private void updatePlayProgress(){
		if(mPlayUrl != null){
			if(mPlayHistory == null){
				mPlayHistory = new PlayerHistory(this);
			}	
		}
		long progress = mVideoView.getCurrentPosition();
		if(mLastProgress != progress && progress > 0 && mHistoryId > 0){
			mPlayHistory.update(mHistoryId, progress, mVideoView.getDuration());
		}
	}
	
	private long getPlayProgress(){
		if(mPlayHistory == null){
			mPlayHistory = new PlayerHistory(this);
		}
		PlayerHistory.Query mQuery = mPlayHistory.query(mPlayUrl);
		if(mQuery.next()){
			mHistoryId = mQuery.getId();
			long progress = mQuery.getPlayedMsec();
			mQuery.close();
			return progress;
		}
		else{
			mHistoryId = mPlayHistory.insert(mVideoName, mPlayUrl, mVid, mVideoView.getDuration(), mCurrentEposide);
			return -1L;
		}
	}
	
	private class ScreenReceiver extends BroadcastReceiver {
		private boolean mScreenOn = true;
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				mScreenOn = false;
				playerPause();
			}
			else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				mScreenOn = true;
			}
		}
	};
	
	private class RequestPlayUrl extends RequestVideoPlayUrlTask{
		@Override
		protected void onPostExecute(String result){
			if(result == null){
				MessageDialog.error(getSupportFragmentManager(), R.string.msg_request_paly_url_failed);
			}
			else{
				mPlayUrl = result;
				openVideo();
			}
		}
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			playerPause();
			mVideoLoadingView.setVisibility(View.VISIBLE);
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			mVideoLoadingView.setVisibility(View.GONE);
			playerStart();
			break;
		case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
			if(mMediaController != null && mMediaController.isShowing()){
				mMediaController.setDownloadRate(extra);
			}
			break;
		}
		return true;
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		return true;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		mVideoOpened = false;
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		mVideoLoadingInfo.setText("已加载:" + percent + "%");		
	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {
		mHandler.removeMessages(MSG_UPDATE_PROGRESS);
		mHandler.sendEmptyMessage(MSG_UPDATE_PROGRESS);
	}
}
