package com.xm.downloads;

public class DownloadState {
	public long mId;
	public int mStatus;
	public String mUri;
	public String mTitle;
	public String mLocalUri;
	public String mMimeType;
	public long mTotalBytes;
	public String mDescripe;
	public long mCurrentBytes;
	public float mSpeed;
	public long mLastModified;
	
	public DownloadState(DownloadManager.State mState){
		mId = mState.getId();
		mStatus = mState.getStatus();
		mUri = mState.getUri();
		mTitle = mState.getTitle();
		mLocalUri = mState.getLocalUri();
		mMimeType = mState.getMimeType();
		mTotalBytes = mState.getTotalBytes();
		mCurrentBytes = mState.getCurrentBytes();
		mDescripe = mState.getDescription();
		mLastModified = 0L; //mState.getLastModified();
	}
	
	public void update(DownloadManager.State mState){
		mStatus = mState.getStatus();
		long mDownloadBytes = mState.getCurrentBytes();
		long mModifiedTime = mState.getLastModified();
		if(mStatus == android.app.DownloadManager.STATUS_RUNNING && mLastModified > 0){
			mSpeed = (mDownloadBytes - mCurrentBytes) / (mModifiedTime - mLastModified);
		}
		else{
			mSpeed = 0;
		}
		mLastModified = mModifiedTime;
		mCurrentBytes = mDownloadBytes;
	}
}
