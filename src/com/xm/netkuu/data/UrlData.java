package com.xm.netkuu.data;

import java.io.UnsupportedEncodingException;
import java.util.Locale;


public class UrlData {

	public static String HOST = "http://10.10.253.1/";
	public static String LOCAL = "/";

	public static final String TOTAL_FILE = "Total.xml";
	public static final String MOV_LIST_FILE = "barmovlist.xml";
	public static final String BOFANG_FILE = "bofang.xml";
	
	public static final String TOTAL = "mov/xml/" + TOTAL_FILE;
	public static final String MOV_LIST = "bar/" + MOV_LIST_FILE;
	
	public static final String BOFANG = "banner3/" + BOFANG_FILE;
	
	public static final String INDEX = "bar/list/2010index.xml";
	public static final String ZONG_YI = "bar/list/41_click.xml";
	public static final String DONG_MAN = "bar/list/37_click.xml";
	public static final String GONG_KAI_KE = "bar/list/52_click.xml";
	public static final String JI_LU_PIAN = "bar/list/53_click.xml";
	public static final String JIANG_ZUO = "bar/list/54_click.xml";
		
	public static final String MOV = "mov/";
	
	public static final String URL = "/url.xml";
	public static final String FILM = "/film.xml";

	public static final String IMAGE_NORMAL = "/1.jpg";
	public static final String IMAGE_SMALL = "/2.jpg";
	
	public static final String IMAGE_JZX1 = "/jzx1.jsp";
	
	/*
	public static final String IMAGE_PLAY1 = "/jzd1.jpg";
	public static final String IMAGE_PLAY2 = "/jzd2.jpg";
	public static final String IMAGE_PLAY3 = "/jzd3.jpg";
	*/
	
	public static final String HOME_FLASH = "banner1/flash.xml";
	public static final String XY_FLASH = "banner1/gongkaike/xyflash.xml";	
	public static final String OPEN_CLASS_FLASH = "gkk";
	public static final String DOCUMENTARY_FLASH = "jlp";
	public static final String LECTURES_FLASH = "jz";
	public static final String MOVIE_FLASH = "banner1/hrdp/flash.xml";
	public static final String TV_FLASH = "banner1/rbjc/flash.xml";	
	public static final String CARTOON_FLASH = "dongman.html";
	public static final String VARIETY_FLASH = "zongyi.html";

	public static final int CHANNEL_OPEN_CLASS = 52;//公开课
	public static final int CHANNEL_DOCUMENTARY = 53;//纪录片
	public static final int CHANNEL_LECTURES = 54;//讲座

	public static final int CHANNEL_MOVIE = 25;//电影
	public static final int CHANNEL_TV = 30;//电视剧
	public static final int CHANNEL_CARTOON = 37;//动漫
	public static final int CHANNEL_VARIETY = 41;//综艺
	
	public static final int CATALOG_TV_NATION = 18;
	public static final int CATALOG_TV_OM = 19;
	public static final int CATALOG_TV_RH = 20;
	public static final int CATALOG_TV_GT = 21;
	
	public static final int[] CHANNEL_TV_CATALOGS = {CATALOG_TV_NATION, CATALOG_TV_OM, CATALOG_TV_RH, CATALOG_TV_GT};

	public static final int CATALOG_MOVIE_HUMOR = 1;
	public static final int CATALOG_MOVIE_ACTION = 2;
	public static final int CATALOG_MOVIE_LOVE = 3;
	public static final int CATALOG_MOVIE_SCIFI = 4;
	public static final int CATALOG_MOVIE_TERROR = 8;
	
	public static final int[] CHANNEL_MOVIE_CATALOGS = {CATALOG_MOVIE_HUMOR, CATALOG_MOVIE_ACTION, CATALOG_MOVIE_LOVE, CATALOG_MOVIE_SCIFI, CATALOG_MOVIE_TERROR};
	
	private static String sUrlDelimiter = "-";
	public static final String[] sAvaliableUrlDelimiter = {"-", "|"};
	
	public static String setHostAddress(String host){
		host = host.trim().toLowerCase(Locale.getDefault());
		if(!host.startsWith("http://") && !host.startsWith("http://")){
			host = "http://" + host;
		}
		if(!host.endsWith("/")){
			host = host + "/";
		}
		HOST = host;
		return HOST;
	}
	
	public static void setUrlDelimiter(String delimiter){
		sUrlDelimiter = delimiter;
	}
	/*
	public static void getTotal(Context context){
		UrlClient.request(HOST + TOTAL, path(context, TOTAL_FILE));
	}
	
	public static void getMovList(Context context){
		UrlClient.request(HOST + MOV_LIST, path(context, MOV_LIST_FILE));
	}
	
	public static void getBoFang(Context context){
		UrlClient.request(HOST + BOFANG, path(context, BOFANG_FILE));
	}
	*/
	public static String getVideoUrlItem(String vid){
		return HOST + MOV + vid + URL;
	}
	
	public static String getBarlist(int channel, int pagesize){
		return readXml("bar-list-" + channel + "_adddate.xml", 0, pagesize <= 0?
				channel == CHANNEL_VARIETY || channel == CHANNEL_CARTOON ? 12 : 8 : pagesize, "m");
	}
	
	public static String getBarlist(int channel, int catlog, int pagesize){
		return readXml("bar-list-" + channel + "_" + catlog + "_adddate.xml", 0, pagesize <= 0?
				channel == CHANNEL_VARIETY || channel == CHANNEL_CARTOON ? 12 : 8 : pagesize, "m");
	}
	
	public static String getBarlist(int channel, String ss, int pagesize){
		return readXml("bar-list-" + channel + "_adddate.xml&ss=" + ss, 0, pagesize <= 0?
				channel == CHANNEL_VARIETY || channel == CHANNEL_CARTOON ? 12 : 8 : pagesize, "m");
	}
	
	public static String readXml(String xml, int num1, int num2, String type ){
		return HOST + "readxml.asp?num1=" + num1 + "&num2=" + num2 + "&xml=" + xml + "&typ=" + type;
	}
	
	public static String searchVideo(int page, int pagesize, String key ){
		try {
			key = java.net.URLEncoder.encode(key, "gbk");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return HOST + "readxml.asp?num1=" + (page - 1) * pagesize + "&num2=" + page * pagesize 
				+ "&xml=bar-list-Total.xml&typ=m&ss=a_" + key + sUrlDelimiter + "c" + sUrlDelimiter + "d";
	}
	
	public static String probeUrlDelimiter(String delimiter){
		return HOST + "readxml.asp?num1=0&num2=1&xml=bar-list-Total.xml&typ=m&ss=a_a" + delimiter + "c" + delimiter + "d";
	}
	
	public static String getPlayUrl(String vid, int episode){
		return HOST + "xy_new.asp?a=" + (episode - 1) + "&b=" + vid;
	}
	
	public static String getVideoDetail(String vid){
		return HOST + MOV + vid + FILM;
	}
	
	public static String image(String vid, String image){
		return HOST + MOV + vid + image;
	}
	
	public static String snapshot(String vid, int num){
		return HOST + MOV + vid + "/jzd" + num + ".jpg";
	}
}
