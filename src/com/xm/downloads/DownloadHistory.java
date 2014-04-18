package com.xm.downloads;

import android.database.sqlite.SQLiteDatabase;

public class DownloadHistory {
	
	public static final String DB_NANE = "downloads.db";
	public static final String TABLE_NAME = "downloads";
	
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_VID = "vid";
	public static final String COLUMN_STATUS = "status";
	public static final String COLUMN_URI = "uri";
	public static final String COLUMN_LOCAL_URI = "local_uri";

	public static final int STATUS_UNDECODE = 0;
	public static final int STATUS_DECODED = 1000;
	
	public static class State{
		public long mId;
		public String mVid;
		public int  mStatus;
		public String mUri;
		public String mLocalUri;		
	}
	
	public void save(State state){
	}
	
	public void update(DownloadManager.State state){
		
	}
	
	public void delete(long id){
		
	}
	
	public void update(int status){
		
	}
	
	public static void create(SQLiteDatabase db){
		
	}
	
	public static void upgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		
	}
}
