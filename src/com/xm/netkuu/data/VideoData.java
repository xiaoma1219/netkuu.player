package com.xm.netkuu.data;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.thoughtworks.xstream.XStream;
import com.xm.netkuu.data.entry.HtmlFlash;
import com.xm.netkuu.data.entry.XyFlash;
import com.xm.netkuu.data.entry.TotalVideo;
import com.xm.netkuu.data.entry.VideoDetail;
import com.xm.netkuu.data.entry.DefaultFlash;
import com.xm.netkuu.data.entry.Total;
import com.xm.netkuu.data.entry.VideoUrlItem;

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
	
	/*
	public static Flash getHomeFlash(){
		return getFlash(UrlData.HOME_FLASH);
	}
	

	public static Flash getMovieFlash(){
		return getFlash(UrlData.MOVIE_FLASH);
	}

	public static Flash getTvFlash(){
		return getFlash(UrlData.TV_FLASH);
	}
	*/
	
	public static DefaultFlash getDefaultFlash(String url){
		XStream xStream = new XStream();
		xStream.processAnnotations(DefaultFlash.class);
		//xStream.autodetectAnnotations(true);
		try {
			return (DefaultFlash) xStream.fromXML(new URL(UrlData.HOST + url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	public static List<XyFlash> getOpenClassFlash(){
		return getXyFlash("gkk");
	}
	
	public static List<XyFlash> getDocumentaryFlash(){
		return getXyFlash("jlp");
	}
	
	public static List<XyFlash> getLecturesFlash(){
		return getXyFlash("jz");
	}
	*/
	
	public static XyFlash getXyFlash(String channel){
		XStream xStream = new XStream();
		xStream.processAnnotations(XyFlash.class);
		xStream.alias(channel, XyFlash.XyFlashItem.class);
		xStream.addImplicitCollection(XyFlash.class, "items", channel, XyFlash.XyFlashItem.class);
		xStream.ignoreUnknownElements();
		try {
			return  (XyFlash) xStream.fromXML(new URL(UrlData.HOST + UrlData.XY_FLASH));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	public static HtmlFlash getCartoonFlash(){
		return getHtmlFlash(UrlData.CARTOON_FLASH);
	}
	
	public static HtmlFlash getVarietyFlash(){
		return getHtmlFlash(UrlData.VARIETY_FLASH);
	}
	*/
	
	public static HtmlFlash getHtmlFlash(String url){
		XStream xStream = new XStream();
		xStream.processAnnotations(HtmlFlash.class);
		xStream.ignoreUnknownElements();
		//xStream.autodetectAnnotations(true);
		String response = UrlClient.request(UrlData.HOST + url);
		response = response.substring(response.indexOf("flink"));
		response = response.substring(response.indexOf("\"") + 1, response.indexOf(";") - 1);
		response = "<?xml version='1.0' encoding='gb2312'?><root>" 
				+ response.replace("\\\"", "\"").replace("</a>", "</img></a>").replace("class", "attr")
				+ "</root>";
		System.out.println(response);
		return (HtmlFlash) xStream.fromXML(response);
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
	
	public static TotalVideo getBarlist(int channel, int catalog, int pagesize){
		XStream xStream = new XStream();
		xStream.processAnnotations(TotalVideo.class);
		//xStream.autodetectAnnotations(true);
		xStream.ignoreUnknownElements();
		try{
			if(catalog >= 0)
				return (TotalVideo) xStream.fromXML(new URL(UrlData.getBarlist(channel, catalog, pagesize)));
			else
				return (TotalVideo) xStream.fromXML(new URL(UrlData.getBarlist(channel, pagesize)));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static TotalVideo getBarlist(int channel, String ss, int pagesize){
		XStream xStream = new XStream();
		xStream.processAnnotations(TotalVideo.class);
		//xStream.autodetectAnnotations(true);
		xStream.ignoreUnknownElements();
		try{
			if(ss == null)
				return (TotalVideo) xStream.fromXML(new URL(UrlData.getBarlist(channel, pagesize)));
			else
				return (TotalVideo) xStream.fromXML(new URL(UrlData.getBarlist(channel, ss, pagesize)));
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
