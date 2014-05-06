package com.xm.netkuu.data;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class UrlClient {
	static int MAX_BUFF_SIZE = 4096;

	public static int request(String url, String path) {
		int size = 0;
		InputStream in = null;
		BufferedOutputStream out = null;
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) connect(url);
			File f = new File(path);
			
			if (!f.getParentFile().exists())
				f.getParentFile().mkdirs();
			else if (f.exists()) {//
				connection.setIfModifiedSince(f.lastModified());
			}
			connection.connect();
			if(connection.getResponseCode() == 200){
				if(f.exists())
					f.delete();
				in = connection.getInputStream();
				int off = 0;
				size = connection.getContentLength();
				if (size <= 0)
					return 0;
				out = new BufferedOutputStream(new FileOutputStream(f.getPath()));
				while (off < size) {// 获取文件
					byte[] buffer = new byte[MAX_BUFF_SIZE];
					int buff_size = in.read(buffer, 0, MAX_BUFF_SIZE);
					off += buff_size;
					out.write(buffer, 0, buff_size);
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) in.close();
				if (out != null) { out.flush(); out.close(); }
				if(connection != null){ connection.disconnect(); }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return size;
	}
	public static URLConnection connect(String url){
		URLConnection connection = null;
		try {
			connection = new URL(url).openConnection();
			connection.setRequestProperty("Accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("Accept-Language", "zh-Hans-CN,zh-Hans;q=0.5");
			connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			connection.setRequestProperty("DNT", "1");
			connection.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	public static InputStream getInputStream(String url){
		HttpURLConnection connection = (HttpURLConnection) connect(url);
		try {
			connection.connect();
			if(connection.getResponseCode() == 200){
				return connection.getInputStream();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static URLConnection connect(String uri, String file){
		try {
			HttpURLConnection connection = (HttpURLConnection) connect(uri);
			File f = new File(file);
			
			if (!f.getParentFile().exists())
				f.getParentFile().mkdirs();
			else if (f.exists()) {//
				connection.setIfModifiedSince(f.lastModified());
			}
				connection.connect();
			if(connection.getResponseCode() == 200){
				if(f.exists())
					f.delete();
				return connection;	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String request(String url){
		String reponseData = "";
		InputStream in = null;
		HttpURLConnection connection = (HttpURLConnection) connect(url);
		connection.setConnectTimeout(5000);
		try {
			if(connection != null){
				connection.connect();
				if(connection.getResponseCode() == 200){
					in = connection.getInputStream();
					int buff_size = 0;
					byte[] buffer = new byte[MAX_BUFF_SIZE];
					while((buff_size = in.read(buffer, 0, MAX_BUFF_SIZE)) > 0){
						reponseData += new String(buffer, 0, buff_size);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return reponseData;
	}
}
