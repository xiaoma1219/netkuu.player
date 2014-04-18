package com.xm.netkuu.data.net;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import android.os.AsyncTask;

public class NetGetTask extends AsyncTask<String, Integer, Void>{

	public interface OnTaskUpdateListener{
		public void update(int position, int downloadbytes, int totalbytes);
		public void post();
		public boolean after();
		public void error(String msg);
	}
	
	private OnTaskUpdateListener mUpdateListener = null;
	
	public void setOnTaskUpdateListener(OnTaskUpdateListener l){
		mUpdateListener = l;
	}
	
	@Override
	protected Void doInBackground(String... params) {
		if(params.length % 2 == 1){
			if(mUpdateListener != null){
				mUpdateListener.error("param error");
			}
			return null;
		}
		for(int i = 0; i < params.length / 2; i++){
			String uri = params[i * 2];
			String file = params[i * 2 + 1];
			HttpURLConnection connection = (HttpURLConnection) UrlClient.connect(uri, file);
			InputStream is = null;
			BufferedOutputStream os = null;
			try {
				if(connection != null && connection.getResponseCode() == 200){
					os = new BufferedOutputStream(new FileOutputStream(file));
					is = connection.getInputStream();
					int off = 0;
					int size = connection.getContentLength();
					byte[] buffer = new byte[UrlClient.MAX_BUFF_SIZE];
					String buff_str = "";
					while (off < size) {// 获取文件头
						int buff_size = is.read(buffer, 0, 50);
						off += buff_size;
						publishProgress(i, off, size);
						buff_str += new String(buffer, 0 ,buff_size, "gbk");
						if(buff_str.contains("GB2312")){
							buff_str = buff_str.replace("GB2312", "utf-8");
							byte[] byte_buff = buff_str.getBytes("utf-8");
							os.write(byte_buff, 0, byte_buff.length);
							break;
						}else if(buff_str.contains("gb2312")){
							buff_str = buff_str.replace("gb2312", "utf-8");
							byte[] byte_buff = buff_str.getBytes("utf-8");
							os.write(byte_buff, 0, byte_buff.length);
							break;
						}
					}
					while (off < size) {// 获取文件
						int buff_size = is.read(buffer, 0, UrlClient.MAX_BUFF_SIZE);
						off += buff_size;
						publishProgress(i, off, size);
						buff_str = new String(buffer, 0 ,buff_size, "gbk");
						byte[] byte_buff = buff_str.getBytes("utf-8");
						os.write(byte_buff, 0, byte_buff.length);
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				File f = new File(file);
				if(f.exists()) f.delete();
			} finally{
				try {
					if (is != null)  is.close();
					if (os != null) { os.flush(); os.close(); }
					if(connection != null) connection.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(mUpdateListener != null){
			publishProgress(params.length / 2 + 1, 0, 0);
			if(!mUpdateListener.after()){
				mUpdateListener.error("解析文件错误");
			}
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... progress){
		if(mUpdateListener != null && progress.length == 3)
			mUpdateListener.update(progress[0], progress[1], progress[2]);
	}
	
	@Override
	protected void onPostExecute(Void result){
		if(mUpdateListener != null)
			mUpdateListener.post();
	}
}
