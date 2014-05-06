package com.xm.netkuu.task;

import java.util.List;

import android.os.AsyncTask;

import com.xm.netkuu.data.VideoData;
import com.xm.netkuu.data.entry.TotalVideo;

public class RequestCatalogTask extends AsyncTask<RequestCatalogTask.Request, RequestCatalogTask.Response, Void>{
	public static class Request{
		public Request(int channel, int pagesize, String title, int... catalog){
			mChannel = channel;
			mTitle = title;
			mPageSize = pagesize;
			mRequestItems = catalog;
			mSearchStrings = null;
		}
		
		public Request(int channel, int pagesize, String title, String... ss){
			mChannel = channel;
			mTitle = title;
			mPageSize = pagesize;
			mSearchStrings = ss;
			mRequestItems = null;
		}
		
		public Request(int channel, String title, int... catalog){
			mChannel = channel;
			mTitle = title;
			mPageSize = 0;
			mRequestItems = catalog;
			mSearchStrings = null;
		}
		

		public Request(int channel, String title, String... ss){
			mChannel = channel;
			mTitle = title;
			mPageSize = 0;
			mSearchStrings = ss;
			mRequestItems = null;
		}
		
		public int mChannel;
		public String mTitle;
		public int mPageSize;
		private int mRequestItems[];
		private String mSearchStrings[];
		public List<TotalVideo.VideoItem> mReponseItems;
	}
	
	public static class Response{
		public Response(Request request, List<TotalVideo.VideoItem> reponse){
			mChannel = request.mChannel;
			mTitle = request.mTitle != null ? request.mTitle : 
				reponse.isEmpty() ? "" : reponse.get(0).getCatalog();
			mReponseItems = reponse;
		}
		public boolean isEmpty(){
			return mReponseItems != null && mReponseItems.isEmpty();
		}
		public int mChannel;
		public String mTitle;
		public List<TotalVideo.VideoItem> mReponseItems;
	}
	
	@Override
	protected Void doInBackground(RequestCatalogTask.Request... params) {
		for(int i = 0; i < params.length; i++){
			Request request = params[i];
			
			List<TotalVideo.VideoItem> items = null;
			if(request.mRequestItems != null){
				for(int j = 0; j < request.mRequestItems.length; j++){
					TotalVideo list = VideoData.getBarlist(request.mChannel, request.mRequestItems[j], request.mPageSize);
					if(list != null){
						if(items == null){
							items = list.getItems();
						}
						else{
							items.addAll(list.getItems());
						}
					}
				}
			}
			else{
				TotalVideo list = VideoData.getBarlist(request.mChannel, null, request.mPageSize);
				if(list != null){
					items = list.getItems();
				}
			}
			publishProgress(new Response(request, items));
		}
		return null;
	}	
	
	private RequestCatalogTask.Response requestCatalogs(Request request){
		List<TotalVideo.VideoItem> items = null;
		for(int j = 0; j < request.mRequestItems.length; j++){
			TotalVideo list = VideoData.getBarlist(request.mChannel, request.mRequestItems[j], request.mPageSize);
			if(list != null){
				if(items == null){
					items = list.getItems();
				}
				else{
					items.addAll(list.getItems());
				}
			}
		}
		return new Response(request, items);
	}
	
	private RequestCatalogTask.Response requestSearchString(Request request){
		List<TotalVideo.VideoItem> items = null;
		for(int j = 0; j < request.mSearchStrings.length; j++){
			//TotalVideo list = VideoData.getBarlist(request.mChannel, request.mRequestItems[j], request.mPageSize);
			TotalVideo list = VideoData.searchVideo(0, request.mPageSize, request.mSearchStrings[j]);
			if(list != null){
				if(items == null){
					items = list.getItems();
				}
				else{
					items.addAll(list.getItems());
				}
			}
		}
		return new Response(request, items);
	}
	
	@Override
	protected void onPostExecute(Void result){
	}
}
