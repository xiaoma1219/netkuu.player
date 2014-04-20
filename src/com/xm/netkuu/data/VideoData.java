package com.xm.netkuu.data;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.thoughtworks.xstream.XStream;
import com.xm.netkuu.data.entry.Barlist;
import com.xm.netkuu.data.entry.VideoDetail;
import com.xm.netkuu.data.entry.HomeFlash;
import com.xm.netkuu.data.entry.Total;
import com.xm.netkuu.data.entry.VideoUrlItem;
import com.xm.netkuu.data.net.UrlData;
import com.xm.netkuu.data.net.UrlClient;

public class VideoData {	
	public static VideoDetail getVideoDetail(String vid){
		XStream xStream = new XStream();
		xStream.processAnnotations(VideoDetail.class);
		//xStream.autodetectAnnotations(true);
		xStream.ignoreUnknownElements();
		try {
			return (VideoDetail) xStream.fromXML(new URL(UrlData.getVideoDetail(vid)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static VideoUrlItem getVideoUrlItem(String vid){
		XStream xStream = new XStream();
		xStream.processAnnotations(VideoUrlItem.class);
		try {
			return (VideoUrlItem) xStream.fromXML(new URL(UrlData.getVideoUrlItem(vid)));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getPlayUrl(String vid, int eposide){
		String Url = UrlClient.request(UrlData.getPlayUrl(vid, eposide));
		if(Url != null && Url.contains("|||")){
			return Url.substring(Url.indexOf("http"), Url.lastIndexOf("|||"));
		}
		return null;
	}
	
	public static HomeFlash getHomeFalsh(){
		XStream xStream = new XStream();
		xStream.processAnnotations(HomeFlash.class);
		//xStream.autodetectAnnotations(true);
		try {
			return (HomeFlash) xStream.fromXML(new URL(UrlData.HOST + UrlData.HOME_FLASH));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Total searchVideo(int page, int pagesize, String key){
		if(key == null || key.length() == 0)
			return null;
		if(key.length() > 50){
			key = key.substring(0, 50);
		}
		XStream xStream = new XStream();
		xStream.processAnnotations(Total.class);
		//xStream.autodetectAnnotations(true);
		xStream.ignoreUnknownElements();
		try {
			return (Total) xStream.fromXML(new URL(UrlData.searchVideo(page, pagesize, key)));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Barlist getBarlist(int type, Integer channel){
		XStream xStream = new XStream();
		xStream.processAnnotations(Barlist.class);
		//xStream.autodetectAnnotations(true);
		xStream.ignoreUnknownElements();
		try{
			if(channel == null)
				return (Barlist) xStream.fromXML(new URL(UrlData.getBarlistXml(type)));
			else
				return (Barlist) xStream.fromXML(new URL(UrlData.getBarlistXml(type, channel)));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/*		
	public static boolean InitTotal(Context context){
		return native_setTotalFile(NetData.path(context, NetData.TOTAL_FILE));
	}
	
	public static boolean InitMovs(Context context){
		return native_setMovFile(NetData.path(context, NetData.MOV_LIST_FILE));
	}
	
	public static FilmMov getFilmMov(String uid){
		return native_getFilmMov(uid);
	}
	
	public static int getTotalSize(){
		return native_getFilmCount();
	}
	
	public static int getMovSize(){
		return native_getMovCount();
	}	
	public static class Searcher{
		private String mSid;
		private int mSize;
		public Searcher(Context context){
			mSid = context.getClass().getName();
		}
		
		public Searcher(String sid){
			this.mSid = sid;
		}
		
		public void release(){
			native_releaseFindedData(mSid);
		}
		
		public int search(String kw, String catlog){
			mSize = native_find(kw, catlog, mSid);
			System.out.println(mSize);
			return mSize;
		}
		
		public int search(String kw){
			return search(kw, null);
		}
		
		public Film getSearchItem(int position){
			return native_getFindedItem(position, mSid);
		}
		
		public int size(){
			return mSize;
		}
		
		public int length(){
			return mSize;
		}
	}
	
	private static native int native_find(String key, String catlog, String owner);
	private static native void native_releaseFindedData(String owner);
	private static native void native_releaseAllFindedData();
	private static native Film native_getFindedItem(int position, String owner);
	private static native boolean native_setTotalFile(String path);
	private static native boolean native_setMovFile(String path);
	private static native FilmMov native_getFilmMov(String uid);
	
	private static native int native_getFilmCount();	
	private static native int native_getMovCount();

	static {
		System.loadLibrary("iconv");
		System.loadLibrary("xml2");
		System.loadLibrary("nv");
	}
*/
}
