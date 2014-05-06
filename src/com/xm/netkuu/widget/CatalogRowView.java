package com.xm.netkuu.widget;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xm.netkuu.data.UrlData;
import com.xm.netkuu.data.entry.TotalVideo;
import com.xm.netkuu.player.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class CatalogRowView extends LinearLayout{
	
	private int mColumnCount = 1;
	private int mItemMargin = 0;
	private int mRow = 0;
	private OnItemClickListener mOnItemClickListener = null;
	
	public CatalogRowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(HORIZONTAL);
	}

	public CatalogRowView(Context context) {
		this(context, null);
	}
	
	public void setColumnCount(int columnCount){
		mColumnCount = columnCount;
	}
	
	public void setRow(int row){
		mRow = row;
	}
	
	public void setItemMargin(int itemMargin){
		mItemMargin = itemMargin;
	}
	
	public void setOnItemClickListener(OnItemClickListener l){
		mOnItemClickListener = l;
	}
	
	public void setRowView(int row, List<TotalVideo.VideoItem> items, int start, String image, ImageLoader loader, DisplayImageOptions options){
		int idx;
		setRow(row);
		int length = items.size() - start;
		for(idx = 0; idx < mColumnCount; idx++){
			setChildView(idx, idx < length ? items.get(start + idx) : null, image, loader, options);
		}
	}
	
	private View setChildView(int column,  TotalVideo.VideoItem item, String image, ImageLoader loader, DisplayImageOptions options){
		VideoGridItemView view = (VideoGridItemView) getChildAt(column);
		if(view == null){
			view = (VideoGridItemView) LayoutInflater.from(getContext()).inflate(R.layout.video_grid_item_view, null);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.weight = 1.0f / mColumnCount;
			lp.setMargins(mItemMargin, mItemMargin, mItemMargin, mItemMargin);
			view.setOnClickListener(new OnViewClickListener(column, item){
				@Override
				public void onClick(View v) {
					if(mOnItemClickListener != null){
						mOnItemClickListener.onItemClick(CatalogRowView.this, v, mRow, mColumn, mVideoItem);
					}
				}
			});
			addView(view, lp);
		}
		if(item != null){
			view.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
			String videoName = item.getName();
			view.setVideoName(videoName);
			view.setVideoRate(item.getRate());
			view.setVideoBrief(item.getBrief());
			if(videoName.indexOf("更新") > 0 || videoName.indexOf("集全") > 0){
				view.setVideoCount(videoName.substring(videoName.indexOf("(") + 1, videoName.indexOf(")")));
			}
			else{
				view.setVideoCount(item.getCatalog());
			}
			view.setVideoImage(loader, options, UrlData.image(item.getVid(), image));
		}
		else{
			view.getLayoutParams().height = 0;
		}
		return view;
	}

	public static interface OnItemClickListener{
		public void onItemClick(ViewGroup parent, View view, int row, int column, TotalVideo.VideoItem videoItem);
	}
	
	abstract class OnViewClickListener implements OnClickListener{
		protected int mColumn = 0;
		TotalVideo.VideoItem mVideoItem;
		public OnViewClickListener(int column, TotalVideo.VideoItem videoItem){
			mColumn = column;
			mVideoItem = videoItem;
		}
	};
}
