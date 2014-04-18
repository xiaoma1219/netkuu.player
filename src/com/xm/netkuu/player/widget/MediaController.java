package com.xm.netkuu.player.widget;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.xm.netkuu.player.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageButton;
import android.widget.TextView;

public class MediaController extends io.vov.vitamio.widget.MediaController implements ControllerGestures.ControllerGesturesListener{
	private Context mContext;
	private View mRoot;
	private View mAnchor;
	
	private ImageButton mPauseButton;
	
	private ImageButton mLock;
	private TextView 	mDateTime;
	private TextView	mBatteryLevel;
	private ImageButton	mMenu;
	
	private ImageButton mNext;
	private TextView	mDownloadRate;
	
	private MediaPlayerControl mPlayer;
	
	private ControllerGestures mGestrues;
	
	private boolean 	mLocked = false;
	private boolean		mHasVirtualBtns = false;
	
	private View.OnClickListener mNextClickListener = null;
	private View.OnClickListener mMenuClickListener = null;

	private static final int TIME_TICK_INTERVAL = 1000;
	private static final int DEFAULT_TIME_OUT = 3000;

	/*
	private static final int FADE_OUT = 1;
	private static final int SHOW_PROGRESS = 2;
	*/
	private static final int MSG_HIDE_SYSTEM_UI = 3;
	private static final int MSG_TIME_TICK = 4;
	private static final int MSG_HIDE_CONTROLLER = 5;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_TIME_TICK:
				setDateTime();
				sendEmptyMessageDelayed(MSG_TIME_TICK, TIME_TICK_INTERVAL);
				break;

			case MSG_HIDE_CONTROLLER:
				hide();
				break;
			case MSG_HIDE_SYSTEM_UI:
				if (!isShowing())
					showSystemUi(false);
				break;
			}

		}
	};

	private View.OnClickListener mOnLockClickListener = new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			if(mLocked)
				unlock();
			else
				lock();
			show();
		}
	};
	
	public MediaController(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mRoot = this;
		mGestrues = new ControllerGestures(mContext);
		mGestrues.setControllerGesturesListener(this);
	}
	
	public MediaController(Context context) {
		super(context);		
		mContext = context;
		mGestrues = new ControllerGestures(mContext);
		mGestrues.setControllerGesturesListener(this);
	}
	
	@Override
	public void onFinishInflate() {
		super.onFinishInflate();
		if (mRoot != null)
			initControllerView(mRoot);
	}
	
	@Override
	public void setAnchorView(View view) {
		view = view.getRootView();
		mAnchor = view;
		super.setAnchorView(mAnchor);
		initControllerView(mRoot);
		setOnSystemUiVisibilityChangeListener();
		showSystemUi(false);
	}

	@Override
	protected View makeControllerView() {
		mRoot = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.mediacontroller, this);
		return mRoot;
	}
	
	private void initControllerView(View v){		
		mPauseButton = (ImageButton) v.findViewById(R.id.mediacontroller_play_pause);
		
		mLock = (ImageButton) v.findViewById(R.id.mediacontroller_lock);
		
		mLock.setOnClickListener(mOnLockClickListener);
		
		mDateTime = (TextView) v.findViewById(R.id.mediacontroller_date_time);
		mBatteryLevel = (TextView) v.findViewById(R.id.mediacontroller_battery_level);
		mMenu = (ImageButton) v.findViewById(R.id.mediacontroller_video_menu);
		if(mMenuClickListener != null){
			mMenu.setOnClickListener(mMenuClickListener);
		}
		mNext = (ImageButton) v.findViewById(R.id.mediacontroller_next);
		if(mNextClickListener != null){
			mNext.setOnClickListener(mNextClickListener);
		}
		mDownloadRate = (TextView) v.findViewById(R.id.mediacontroller_download_rate);
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void setOnSystemUiVisibilityChangeListener(){
		mHasVirtualBtns = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH 
				&& !ViewConfiguration.get(mContext).hasPermanentMenuKey();
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mAnchor.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
				@Override
				public void onSystemUiVisibilityChange(int visibility) {
					//System.out.println("onSystemUiVisibilityChange: " + visibility);
					if ((visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {
						mHandler.sendEmptyMessageDelayed(MSG_HIDE_SYSTEM_UI, DEFAULT_TIME_OUT);
						if(mHasVirtualBtns && !isShowing()){
							show();
						}
					}
				}
			});
		}
	}
	
	@Override
	public void show(int timeout){
		if(!isShowing()){
			mHandler.removeMessages(MSG_HIDE_SYSTEM_UI);
			showSystemUi(true);
		}
		super.show(timeout);
		if(timeout > 0){
			mHandler.removeMessages(MSG_HIDE_CONTROLLER);
			mHandler.sendEmptyMessage(MSG_TIME_TICK);
		}
	}
	
	@Override
	public void hide(){
		hide(false);
	}
	
	public void hide(boolean isback){
		super.hide();
		if(isback){
			mHandler.sendEmptyMessageDelayed(MSG_HIDE_SYSTEM_UI, DEFAULT_TIME_OUT);
		}
		else{
			showSystemUi(false);
		}
		mHandler.removeMessages(MSG_TIME_TICK);
	}
	
	public void toggle(){
		if(isShowing()){
			mHandler.sendEmptyMessage(MSG_HIDE_CONTROLLER);
		}
		else{
			show();
		}
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void showSystemUi(boolean visible) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			if(!visible){
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
					mAnchor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION 
							| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION 
							| View.SYSTEM_UI_FLAG_LOW_PROFILE);
				else{
					mAnchor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION 
							| View.SYSTEM_UI_FLAG_LOW_PROFILE);
				}
			}
			else{
				mRoot.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
			}
		}
	}
	
	public void lock(){
		mLocked = true;
		setEnabled(false);
		mLock.setImageResource(R.drawable.mediacontroller_lock);
		mPauseButton.setVisibility(View.GONE);
		mNext.setVisibility(View.GONE);
		mMenu.setVisibility(View.GONE);
	}
	
	public void unlock(){
		mLocked = false;
		setEnabled(true);
		mLock.setImageResource(R.drawable.mediacontroller_unlock);
		mPauseButton.setVisibility(View.VISIBLE);
		mNext.setVisibility(View.VISIBLE);
		mMenu.setVisibility(View.VISIBLE);
	}
	
	public boolean isLocked(){
		return mLocked;
	}
	
	public void setDownloadRate(int rate){
		mDownloadRate.setVisibility(View.VISIBLE);
		mDownloadRate.setText(rate + "b/s");
	}
	
	public void setBatteryLevel(int percent){
		mBatteryLevel.setText(percent + "%");
	}
	
	public void setDateTime(){
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
		if(mDateTime != null)
			mDateTime.setText(format.format(System.currentTimeMillis()));
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//System.out.println("onTouchEvent");
		mHandler.removeMessages(MSG_HIDE_SYSTEM_UI);
		mHandler.sendEmptyMessageDelayed(MSG_HIDE_SYSTEM_UI, DEFAULT_TIME_OUT);
		//toggle();
		return mGestrues.onTouchEvent(event);
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keyCode = event.getKeyCode();

		/*
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_MUTE:
			return super.dispatchKeyEvent(event);
		case KeyEvent.KEYCODE_VOLUME_UP:
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			mVolume = mAM.getStreamVolume(AudioManager.STREAM_MUSIC);
			int step = keyCode == KeyEvent.KEYCODE_VOLUME_UP ? 1 : -1;
			setVolume(mVolume + step);
			mHandler.removeMessages(MSG_HIDE_OPERATION_VOLLUM);
			mHandler.sendEmptyMessageDelayed(MSG_HIDE_OPERATION_VOLLUM, 500);
			return true;
		}
		*/
		
		if (isLocked()) {
			show();
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(mPlayer != null && mPlayer.isPlaying()){
				mPlayer.pause();
			}
			hide(true);
			return true;
		} else {
			return super.dispatchKeyEvent(event);
		}
	}
	
	public void setMediaPlayer(MediaPlayerControl player) {
	    mPlayer = player;
	    super.setMediaPlayer(player);
	  }

	/*
	 * (Genstrues Controller)
	 */
	@Override
	public void onGestureBegin(int state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGestureEnd(int state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLeftSlide(float percent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRightSlide(float percent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSingleTap() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDoubleTap() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLongPress() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSeek(float dist) {
				
	}
}
