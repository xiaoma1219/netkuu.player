package com.xm.netkuu.player;

import android.content.Context;
import android.database.Cursor;

public class PlayerHistory {
	
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "video_name";
	public static final String COLUMN_URI = "video_uri";
	public static final String COLUMN_MSEC = "last_played_msec";
	public static final String COLUMN_TIME = "last_accses_time";
	public static final String COLUMN_DURATION = "duration";
	public static final String COLUMN_VID = "vid";
	public static final String COLUMN_EPISODE = "episode";
	
	PlayerDatabase mDatabase;
	
	public PlayerHistory(Context context){
		mDatabase = new PlayerDatabase(context);
	}
	
	public static Query queryByVid(Context context, String vid){
		return new Query(new PlayerDatabase(context).query(null, PlayerHistory.COLUMN_VID + "='" + vid + "'",
				null, PlayerHistory.COLUMN_EPISODE + " DESC "));
	}
	
	public Query query(String url){
		return new Query(mDatabase.query(null, url));
	}
	
	public Query query(){
		return new Query(mDatabase.query(null));
	}
	
	public int update(long id, long msec, long duration){
		return mDatabase.update(id, msec, duration);
	}
	
	public long insert(String name, String uri, String vid, long duration, int eposide){
		return mDatabase.insert(name, uri, vid, duration, eposide);
	}
	
	public static class Query{
		private Cursor mCursor = null;
		
		public Query(Cursor cursor){
			mCursor = cursor;
		}
		
		public void close(){
			if(mCursor != null)
				mCursor.close();
		}
		
		public boolean hasNext(){
			return mCursor != null && !mCursor.isAfterLast();
		}
		
		public boolean next(){
			return mCursor != null && mCursor.moveToNext();
		}
		
		public long getId(){
			return  mCursor != null ? mCursor.getLong(mCursor.getColumnIndex(COLUMN_ID)) : -1L;
		}
		
		public String getName(){
			return mCursor != null ? mCursor.getString(mCursor.getColumnIndex(COLUMN_NAME)) : "";
		}
		
		public String getVid(){
			return mCursor != null ? mCursor.getString(mCursor.getColumnIndex(COLUMN_VID)) : "";
		}
		
		public String getUri(){
			return mCursor != null ? mCursor.getString(mCursor.getColumnIndex(COLUMN_URI)) : "";
		}
		
		public long getPlayedMsec(){
			return mCursor != null ? mCursor.getLong(mCursor.getColumnIndex(COLUMN_MSEC)) : -1L;
		}
		
		public long getDuration(){
			return mCursor != null ? mCursor.getLong(mCursor.getColumnIndex(COLUMN_DURATION)) : -1L;
		}
		
		public long getAccessTime(){
			return mCursor != null ? mCursor.getLong(mCursor.getColumnIndex(COLUMN_TIME)) : -1L;
		}
		
		public int getEpisode(){
			return mCursor != null ? mCursor.getInt(mCursor.getColumnIndex(COLUMN_EPISODE)) : -1;
		}
	}
}
