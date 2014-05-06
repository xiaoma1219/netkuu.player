package com.xm.netkuu.widget;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.xm.netkuu.task.RequestCatalogTask;

public abstract class CatalogRowViewAdapter extends BaseAdapter{
	
	protected class RowData{
		public int mCatalogPosition;
		public int mStart;
		public int mColumnCount;
		public int mViewType;
	};
	
	public static final int VIEW_TYPE_SLIDER = 0;
	public static final int VIEW_TYPE_ROW = 1;
	public static final int VIEW_TYPE_TITLE = 2;
	
	private static final int sViewTypeCount = 3;
	
	private List<RequestCatalogTask.Response> mResponseCatalogs;
	private List<RowData> mRowDatas;
	
	public CatalogRowViewAdapter(){
		mResponseCatalogs = new ArrayList<RequestCatalogTask.Response>();
		mRowDatas = new ArrayList<RowData>();
	}
	
	public void add(RequestCatalogTask.Response item){
		if(!item.isEmpty()){
			int mRowColumnCount = getRowColumnCount(VIEW_TYPE_ROW);
			int idx = mResponseCatalogs.size();
			mResponseCatalogs.add(item);
			RowData rowData = new RowData();
			rowData.mColumnCount = 1;
			rowData.mCatalogPosition = idx;
			rowData.mViewType = VIEW_TYPE_TITLE;
			mRowDatas.add(rowData);
			for(int i = 0; i < item.mReponseItems.size(); i+= mRowColumnCount){
				rowData = new RowData();
				rowData.mColumnCount = mRowColumnCount;
				rowData.mCatalogPosition = idx;
				rowData.mStart = i;
				rowData.mViewType = VIEW_TYPE_ROW;
				mRowDatas.add(rowData);
			}
		}
	}
	
	public abstract int getRowColumnCount(int type);
	
	@Override
	public int getCount() {
		return mRowDatas.size() + 1;
	}

	@Override
	public int getItemViewType(int position) {
        return position == 0 ? 
        		VIEW_TYPE_SLIDER : mRowDatas.get(position - 1).mViewType;
    }

	@Override
    public int getViewTypeCount() {
        return sViewTypeCount;
    }
	
	@Override
	public Object getItem(int position) {
		return mRowDatas.get(position - 1);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public RequestCatalogTask.Response getResponseCatalog(int catalogPosition){
		return mResponseCatalogs.get(catalogPosition);
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		switch(getItemViewType(position)){
		case VIEW_TYPE_SLIDER:
			return getSliderView(position, view, parent);
		case VIEW_TYPE_TITLE:
			return getTitleView(position, view, parent);
		default:
			return getRowView(position, view, parent);
		}
	}
	
	public abstract View getTitleView(int position, View view, ViewGroup parent);
	public abstract View getSliderView(int position, View view, ViewGroup parent);
	public abstract View getRowView(int position, View view, ViewGroup parent);
}
