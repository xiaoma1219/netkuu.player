package com.xm.downloads;

import java.lang.reflect.Method;

import android.annotation.TargetApi;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;


public class DownloadManager {

	public static final String ACTION_DOWNLOAD_COMPLETED = "com.xm.action.DOWNLOAD_COMPLETED";

	public static final String ACTION_NOTIFICATION_CLICKED = "com.xm.action.DOWNLOAD_NOTIFICATION_CLICKED";

	public static final String EXTRA_DOWNLOAD_ID = "extra_download_id";
	
    public static final String METHOD_NAME_PAUSE_DOWNLOAD  = "pauseDownload";
    public static final String METHOD_NAME_RESUME_DOWNLOAD = "resumeDownload";
	
	private static Method      mPauseMethod               = null;
    private static Method      mResumeMethod              = null;
	
	protected android.app.DownloadManager mDownloadManager;
	
	public DownloadManager(Context context){
		//context.startService(new Intent(context.getApplicationContext(), DownloadService.class));
		mDownloadManager = (android.app.DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
	}
	
	public static DownloadManager getInstance(Context context){
		return new DownloadManager(context);		
	}
	
	public long enqueue(String uri, String name, int epi, String vid){
		Request request = new Request(Uri.parse(uri));
		request.setAllowedNetworkTypes(Request.NETWORK_WIFI);
		String externd = uri.substring(uri.lastIndexOf('.'));
		String file = name;
		String dir = "NetVideo" ;
		if(epi != 0){
			file += epi;
			dir = dir + "/" + name;
		}
		request.setTitle(file);
		request.setDestinationInExternalPublicDir(dir, file + "." + externd);
		request.setMimeType("media/video");
		request.setDescription(vid);
		return mDownloadManager.enqueue(request);
	}
	
    public int pauseDownload(long... ids) {
        initPauseMethod();
        if (mPauseMethod == null) {
            return -1;
        }

        try {
            return ((Integer)mPauseMethod.invoke(mDownloadManager, ids)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    public int resumeDownload(long... ids) {
        initResumeMethod();
        if (mPauseMethod == null) {
            return -1;
        }

        try {
            return ((Integer)mResumeMethod.invoke(mDownloadManager, ids)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
	
	public void remove(long... ids){
		mDownloadManager.remove(ids);
	}
	
	public State query(){
		State mState = new State();
		mState.query(mDownloadManager);
		return mState;
	}
	
	public State query(long... id){
		State mState = new State();
		mState.query(mDownloadManager, id);
		return mState;
	}
	
	public static class State{
		Cursor mCursor;
		
		public void query(android.app.DownloadManager dm){
			android.app.DownloadManager.Query mState = new android.app.DownloadManager.Query();			
			mCursor = dm.query(mState);
		}
		
		public void query(android.app.DownloadManager dm, long... id){
			android.app.DownloadManager.Query mState = new android.app.DownloadManager.Query();
			mState.setFilterById(id);
			mCursor = dm.query(mState);
		}
		
		public boolean moveToNext(){
			return mCursor.moveToNext();
		}
		
		public boolean isBeforeFirst(){
			return mCursor.isBeforeFirst();
		}
		
		public boolean isAfterLast(){
			return mCursor.isAfterLast();
		}
		
		public long getId(){
			return mCursor.getLong(mCursor.getColumnIndex(android.app.DownloadManager.COLUMN_ID));
		}
		
		public String getUri(){
			return mCursor.getString(mCursor.getColumnIndex(android.app.DownloadManager.COLUMN_URI));
		}
		
		public long getTotalBytes(){
			return mCursor.getLong(mCursor.getColumnIndex(android.app.DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
		}
		
		public long getCurrentBytes(){
			return mCursor.getLong(mCursor.getColumnIndex(android.app.DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
		}
		
		public int getErrorCode(){
			return mCursor.getInt(mCursor.getColumnIndex(android.app.DownloadManager.COLUMN_REASON));
		}
		
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		public String getFileName(){
			return mCursor.getString(mCursor.getColumnIndex(android.app.DownloadManager.COLUMN_LOCAL_FILENAME));
		}
		
		public String getLocalUri(){
			return mCursor.getString(mCursor.getColumnIndex(android.app.DownloadManager.COLUMN_LOCAL_URI));
		}
		
		public String getProviderUri(){
			return mCursor.getString(mCursor.getColumnIndex(android.app.DownloadManager.COLUMN_MEDIAPROVIDER_URI));
		}
		
		public String getTitle(){
			return mCursor.getString(mCursor.getColumnIndex(android.app.DownloadManager.COLUMN_TITLE));
		}
		
		public String getDescription(){
			return mCursor.getString(mCursor.getColumnIndex(android.app.DownloadManager.COLUMN_DESCRIPTION));
		}
		
		public long getLastModified(){
			return mCursor.getLong(mCursor.getColumnIndex(android.app.DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP));
		}
		
		public int getStatus(){
			return mCursor.getInt(mCursor.getColumnIndex(android.app.DownloadManager.COLUMN_STATUS));
		}
		
		public String getMimeType(){
			return mCursor.getString(mCursor.getColumnIndex(android.app.DownloadManager.COLUMN_MEDIA_TYPE));
		}
		
		public void close(){
			mCursor.close();
		}
	}
	
	private static void initPauseMethod() {
        if (mPauseMethod == null) {
        	try {
                mPauseMethod = android.app.DownloadManager.class.getMethod(METHOD_NAME_PAUSE_DOWNLOAD, long[].class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void initResumeMethod() {
        if (mResumeMethod == null) {
        	try {
                mResumeMethod = android.app.DownloadManager.class.getMethod(METHOD_NAME_RESUME_DOWNLOAD, long[].class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
