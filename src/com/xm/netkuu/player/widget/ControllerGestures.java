package com.xm.netkuu.player.widget;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;

public class ControllerGestures{


	public static final int GESTRUES_STATE_NONE = 0;
	public static final int GESTRUES_STATE_VOL = 1;
	public static final int GESTRUES_STATE_SEEK = 2;
	
	private boolean mGestureEnabled;

	private int mGesturesState = GESTRUES_STATE_NONE;

	private GestureDetectorCompat mDoubleTapGestureDetector;
	private GestureDetectorCompat mTapGestureDetector;
	private ScaleGestureDetector mScaleDetector;

	private Context mContext;

	public ControllerGestures(Context ctx) {
		mContext = ctx;
		mDoubleTapGestureDetector = new GestureDetectorCompat(mContext, new DoubleTapGestureListener());
		mTapGestureDetector = new GestureDetectorCompat(mContext, new TapGestureListener());
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (mControllerGesturesListener == null)
			return false;

		if (mTapGestureDetector.onTouchEvent(event))
			return true;

		if (event.getPointerCount() > 1) {
			try {
				if (mScaleDetector != null && mScaleDetector.onTouchEvent(event))
					return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (mDoubleTapGestureDetector.onTouchEvent(event))
			return true;

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_UP:
			mControllerGesturesListener.onGestureEnd(mGesturesState);
			break;
		}

		return false;
	}

	private class TapGestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onSingleTapConfirmed(MotionEvent event) {
			if (mControllerGesturesListener != null)
				mControllerGesturesListener.onSingleTap();
			return true;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			if (mControllerGesturesListener != null && mGestureEnabled)
				mControllerGesturesListener.onLongPress();
		}
	}

	private class DoubleTapGestureListener extends SimpleOnGestureListener {
				
		@Override
		public boolean onDown(MotionEvent event) {
			mGesturesState = GESTRUES_STATE_NONE;
			return super.onDown(event);
		}

		@SuppressWarnings("deprecation")
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			if (mControllerGesturesListener != null && mGestureEnabled && e1 != null && e2 != null) {
				if (mGesturesState == GESTRUES_STATE_NONE) {
					Display display = ((WindowManager) mContext .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
					int windowWidth = display.getWidth();
					if(Math.abs(distanceX) < Math.abs(distanceY) && (
							e1.getX() <= windowWidth * 0.3f || e1.getX() >= windowWidth * 0.7f)){
						mGesturesState = GESTRUES_STATE_VOL;
					}
					else{
						mGesturesState = GESTRUES_STATE_SEEK;
					}
					mControllerGesturesListener.onGestureBegin(mGesturesState);
				}
				float mOldX = e1.getX(), mOldY = e1.getY();
				//float mX = e2.getX(0), mY = e2.getY(0);
				if(mGesturesState == GESTRUES_STATE_SEEK){
					float mDist = e2.getX(0) - mOldX;
					DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
					mControllerGesturesListener.onSeek(mDist / metrics.density);
				}
				else if (Math.abs(e2.getY(0) - mOldY) * 2 > Math.abs(e2.getX(0) - mOldX)) {
					Display display = ((WindowManager) mContext .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
					int windowWidth = display.getWidth();
					int windowHeight = display.getHeight();
					if (mOldX > windowWidth * 4.0 / 5) {
						mControllerGesturesListener.onRightSlide((mOldY - e2.getY(0)) / windowHeight);
					} else if (mOldX < windowWidth / 5.0) {
						mControllerGesturesListener.onLeftSlide((mOldY - e2.getY(0)) / windowHeight);
					}
				}
			}
			return super.onScroll(e1, e2, distanceX, distanceY);
		}

		@Override
		public boolean onDoubleTap(MotionEvent event) {
			if (mControllerGesturesListener != null && mGestureEnabled)
				mControllerGesturesListener.onDoubleTap();
			return super.onDoubleTap(event);
		}
	}

	public void lock(boolean lock){
		mGestureEnabled = !lock;
	}
	
	public void setControllerGesturesListener(ControllerGesturesListener l) {
		mControllerGesturesListener = l;
	}
	
	public void setControllerGesturesListener(ControllerGesturesListener l, boolean lock) {
		mControllerGesturesListener = l;
		mGestureEnabled = !lock;
	}

	private ControllerGesturesListener mControllerGesturesListener;
	
	public interface ControllerGesturesListener {
		public void onGestureBegin(int state);

		public void onGestureEnd(int state);

		public void onLeftSlide(float percent);

		public void onRightSlide(float percent);

		public void onSingleTap();

		public void onDoubleTap();

		public void onLongPress();
		
		public void onSeek(float dist);
	}
}
