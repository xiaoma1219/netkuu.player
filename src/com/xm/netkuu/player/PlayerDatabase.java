package com.xm.netkuu.player;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class PlayerDatabase {
	private static final String DB_NAME = "netkuu.player.db";  
    private static final String DB_HISTORT_TABLE = "player_history";  
    private static final int DB_VERSION = 1;  
    
    private static final String DB_HISTORT_CREATE = "CREATE TABLE IF NOT EXISTS " + DB_HISTORT_TABLE  + " ( "
    		+ PlayerHistory.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
    		+ PlayerHistory.COLUMN_NAME + " TEXT, "
    		+ PlayerHistory.COLUMN_URI + " TEXT, "
    		+ PlayerHistory.COLUMN_VID + " TEXT, "
    		+ PlayerHistory.COLUMN_MSEC + " INTEGER, "
    		+ PlayerHistory.COLUMN_DURATION + " INTEGER, "
    		+ PlayerHistory.COLUMN_EPISODE + " INTEGER, "
    		+ PlayerHistory.COLUMN_TIME + " BIGINT"
    		+ ")";
    
    private class DatabaseHelper extends SQLiteOpenHelper{
    	public DatabaseHelper(final Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DB_HISTORT_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
		}
    }
    
    private DatabaseHelper mOpenHelper = null;    

    public PlayerDatabase(Context context){
    	onCreate(context);
    }
    
	public boolean onCreate(Context context) {
		mOpenHelper = new DatabaseHelper(context);
		return true;
	}

	public Cursor query(String[] columns, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		return db.query(DB_HISTORT_TABLE, columns, selection, selectionArgs, null, null, sortOrder);
	}

	public long insert(ContentValues values) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		return db.insert(DB_HISTORT_TABLE, PlayerHistory.COLUMN_ID, values);
	}


	public int delete(String selection, String[] selectionArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		return db.delete(DB_HISTORT_TABLE, selection, selectionArgs);
	}

	public int update(ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		return db.update(DB_HISTORT_TABLE, values, selection, selectionArgs);
	}
	
	public long insert(String name, String uri, String vid, long duration, int episode){
		ContentValues values = new ContentValues();
		values.put(PlayerHistory.COLUMN_NAME, name);
		values.put(PlayerHistory.COLUMN_URI, uri);
		values.put(PlayerHistory.COLUMN_VID, vid);
		values.put(PlayerHistory.COLUMN_EPISODE, episode);
		values.put(PlayerHistory.COLUMN_MSEC, 0);
		values.put(PlayerHistory.COLUMN_DURATION, duration);
		values.put(PlayerHistory.COLUMN_TIME, System.currentTimeMillis());
		return this.insert(values);
	}
	
	public Cursor query(String[] columns, String uri){
		return this.query(columns, PlayerHistory.COLUMN_URI + "='" + uri + "'", null, null);
	}
	
	public Cursor query(String[] columns){
		return this.query(columns, null, null, null);
	}
	
	public int update(long id, long msec, long duration){
		ContentValues values = new ContentValues();
		values.put(PlayerHistory.COLUMN_MSEC, msec);
		values.put(PlayerHistory.COLUMN_DURATION, duration);
		values.put(PlayerHistory.COLUMN_TIME, System.currentTimeMillis());
		return this.update(values, PlayerHistory.COLUMN_ID + "='" + id + "'", null);
	}
}
