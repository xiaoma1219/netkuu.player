package com.xm.netkuu.ui.widget;

import java.util.List;

import android.os.AsyncTask;

import com.xm.netkuu.data.VideoData;
import com.xm.netkuu.data.entry.Barlist;

public class RequestCatalogDataTask extends AsyncTask<RequestCatalogDataTask.CatalogData, RequestCatalogDataTask.CatalogData, Void>{
	public static class CatalogData{
		public CatalogData(int type, String title, int... catalog){
			mType = type;
			mTitle = title;
			mCatalogs = catalog;
		}
		public int mType;
		public String mTitle;
		public int mCatalogs[];
		public List<Barlist.BarlistItem> mDataItems;
	}
	
	@Override
	protected Void doInBackground(RequestCatalogDataTask.CatalogData... params) {
		for(int i = 0; i < params.length; i++){
			CatalogData catalogData = params[i];
			
			List<Barlist.BarlistItem> items = null;
			if(catalogData.mCatalogs.length > 0){
				for(int j = 0; j < catalogData.mCatalogs.length; j++){
					Barlist list = VideoData.getBarlist(catalogData.mType, catalogData.mCatalogs[j]);
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
				Barlist list = VideoData.getBarlist(catalogData.mType, null);
				if(list != null){
					items = list.getItems();
				}
			}
			catalogData.mDataItems = items;
			publishProgress(catalogData);
		}
		return null;
	}	
	
	@Override
	protected void onPostExecute(Void result){
	}
}
