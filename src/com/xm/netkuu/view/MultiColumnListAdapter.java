package com.xm.netkuu.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public abstract class MultiColumnListAdapter<T> extends BaseAdapter{

	private static final int TAG_VIEW_TYPE_KEY = 0x1001;
	
	private Context mContext;
	private ViewPool mViewPool;
	
	private List<RowInfo> mRows;	
	private List<T> mAdapterData;
	
	private MultiColumnListView mListView;
	
	public MultiColumnListAdapter(Context context){
		super();
		mContext = context;
		mViewPool = new ViewPool();
		mRows = new ArrayList<RowInfo>();
	}
	
	public void setAdapterData(List<T> adapterData){
		mAdapterData = adapterData;
	}
	
	public void appendAdapterData(List<T> adapterData){
		if(mAdapterData == null){
			mAdapterData = new ArrayList<T>();
		}
		mAdapterData.addAll(adapterData);
	}
	
	@Override
	public int getCount() {
		return mRows.size();
	}

	@Override
	public T getItem(int position) {
		return mAdapterData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		return getRowView(position, (ViewGroup)view, parent);
	}
	
	public View getRowView(int position, ViewGroup view, ViewGroup parent){
		if(view == null){
			view = new LinearLayout(mContext);
			view.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		}
		else{
			for(int i = 0; i < view.getChildCount(); i++){
				View child = view.getChildAt(i);
				Integer type = (Integer) child.getTag(TAG_VIEW_TYPE_KEY);
				mViewPool.put(type, view);
			}
			view.removeAllViews();
		}
		RowInfo rowInfo = getRowInfo(position);
		for(int idx = rowInfo.mStartIdx; idx <= rowInfo.mEndIdx; idx++){
			int type = getItemViewType(idx);
			View childView = getActualView(idx, mViewPool.get(type), view);
			childView.setTag(TAG_VIEW_TYPE_KEY, type);
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)childView.getLayoutParams();
			lp.width = 0;
			lp.weight = this.getRowSpan(idx) * 1.0f / this.mListView.getColumnNumber();
			childView.setLayoutParams(lp);
			view.addView(childView);
		}
		return view;
	}
	
	public abstract View getActualView(int position, View view, ViewGroup parent);
	
	public int getRowSpan(int position){
		return 1;
	}
	
	public RowInfo getRowInfo(int position){
		return mRows.get(position);
	}

	public void calculateRowInfo(){
		mRows.clear();
		mViewPool.clear();
		if(mAdapterData != null){
			int position = 0;
			while(position < mAdapterData.size()){
				RowInfo rowInfo = calculateRowInfo(position);
				position = rowInfo.mEndIdx + 1;
				mRows.add(rowInfo);
			}
		}
	}
	
	public RowInfo calculateRowInfo(int position){
		int columnNumber = mListView.getColumnNumber();
		RowInfo rowInfo = new RowInfo();
		rowInfo.mStartIdx = rowInfo.mEndIdx =  position;
		for(int i = 0; i < columnNumber; i += getRowSpan(position)){
			rowInfo.mEndIdx++;
		}
		return rowInfo;
	}
	
	public static class RowInfo{
		int mStartIdx;
		int mEndIdx;
	}
	
	public static class ViewHolder{
		int mType;
	}
}
