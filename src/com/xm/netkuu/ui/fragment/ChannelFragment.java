package com.xm.netkuu.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.PageIndicator;
import com.xm.netkuu.data.UrlData;
import com.xm.netkuu.data.entry.TotalVideo;
import com.xm.netkuu.player.R;
import com.xm.netkuu.task.RequestCatalogTask;
import com.xm.netkuu.task.RequestCatalogTask.Request;
import com.xm.netkuu.ui.activity.DetailActivity;
import com.xm.netkuu.widget.AbstractSliderAdapter;
import com.xm.netkuu.widget.CatalogRowView;
import com.xm.netkuu.widget.CatalogRowView.OnItemClickListener;
import com.xm.netkuu.widget.CatalogRowViewAdapter;
import com.xm.netkuu.widget.DefaultSliderAdapter;
import com.xm.netkuu.widget.HtmlSliderAdapter;
import com.xm.netkuu.widget.ListSliderAdapter;

public class ChannelFragment extends Fragment{
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mVideoImageLoadOption;
	private AbstractSliderAdapter mSliderAdapter;
	private ChannelAdapter mChannelAdapter;
	private int mRowColumnCount;
	private ListView mView;
	private int mChannel;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		View view = mInflater.inflate(R.layout.fragment_channel, container, false);
		mImageLoader = ImageLoader.getInstance(); 
		
		mVideoImageLoadOption = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.default_video_image)
			.showImageOnFail(R.drawable.default_video_image)
			.showImageOnLoading(R.drawable.default_video_image)
			.cacheInMemory(true)
			.imageScaleType(com.nostra13.universalimageloader.core.assist.ImageScaleType.EXACTLY)
			.build();
		
		mChannel = getArguments().getInt("channel");
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		DisplayMetrics dm = new DisplayMetrics(); 
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm); 
		mRowColumnCount = dm.widthPixels / this.getResources().getDimensionPixelSize(R.dimen.vide_grid_item_min_width);

		mView =  (ListView) getView().findViewById(R.id.channel_list_view);
		mChannelAdapter = new ChannelAdapter();
		
		int[] catalogs = {};
		switch(mChannel){
		case UrlData.CHANNEL_OPEN_CLASS:
			mSliderAdapter = new ListSliderAdapter(getActivity());
			break;
		case UrlData.CHANNEL_DOCUMENTARY:
			mSliderAdapter = new ListSliderAdapter(getActivity());
			break;
		case UrlData.CHANNEL_LECTURES:
			mSliderAdapter = new ListSliderAdapter(getActivity());
			break;
		case UrlData.CHANNEL_TV:
			mSliderAdapter = new DefaultSliderAdapter(getActivity());
			catalogs = UrlData.CHANNEL_TV_CATALOGS;
			break;
		case UrlData.CHANNEL_MOVIE:
			catalogs = UrlData.CHANNEL_MOVIE_CATALOGS;
			break;
		case UrlData.CHANNEL_CARTOON:
			mSliderAdapter = new HtmlSliderAdapter(getActivity());
			break;
		case UrlData.CHANNEL_VARIETY:
			mSliderAdapter = new HtmlSliderAdapter(getActivity());
			break;
		}
		Request[] requests;
		if(catalogs.length > 0){
			requests = new Request[catalogs.length];
			for(int i = 0; i < catalogs.length; i++){
				requests[i] = new Request(mChannel, mRowColumnCount * 2, null, catalogs[i]);
			}
		}
		else{
			requests = new Request[1];
			requests[0] =  new Request(mChannel, mRowColumnCount * 12);
		}
		new LoadCatalogDataTask().execute(requests);
		
		mView.setAdapter(mChannelAdapter);
	}
	
	private class ChannelAdapter extends CatalogRowViewAdapter{

		@Override
		public View getTitleView(int position, View view, ViewGroup parent) {
			if(view == null){
				view = mInflater.inflate(R.layout.catalog_title_view, parent, false);
			}
			RowData rowData = (RowData) this.getItem(position);
			((TextView)view.findViewById(R.id.catalog_title)).setText(getResponseCatalog(rowData.mCatalogPosition).mTitle);
			return view;
		}

		@Override
		public View getSliderView(int position, View view, ViewGroup parent) {
			if(view == null){
				view = mInflater.inflate(R.layout.image_slider_view, parent, false);
				ViewPager mViewPager = (ViewPager) view.findViewById(R.id.home_slider_pager);
				mViewPager.setAdapter(mSliderAdapter);
				PageIndicator mPageIndicator = (PageIndicator) view.findViewById(R.id.home_slider_indicator);
				mPageIndicator.setViewPager(mViewPager);
			}
			return view;
		}

		@Override
		public View getRowView(int position, View view, ViewGroup parent) {
			CatalogRowView gridView;
			RowData rowData = (RowData) this.getItem(position);
			if(view == null){
				view = new CatalogRowView(getActivity());
				view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
				view.setBackgroundColor(Color.WHITE);
			}
			gridView = (CatalogRowView) view;

			gridView.setColumnCount(rowData.mColumnCount);
			gridView.setItemMargin(10);
			gridView.setOnItemClickListener(new OnColumnItemClick());
			gridView.setRowView(position, getResponseCatalog(rowData.mCatalogPosition).mReponseItems, rowData.mStart, UrlData.IMAGE_SMALL, mImageLoader, mVideoImageLoadOption);
			return view;
		}

		@Override
		public int getRowColumnCount(int type) {
			switch(type){
			case VIEW_TYPE_ROW:
				return mRowColumnCount;
			}
			return 1;
		}
	}
	
	private class LoadCatalogDataTask extends RequestCatalogTask{
		@Override
		protected void onProgressUpdate(RequestCatalogTask.Response... datas){
			if(datas.length == 1){
				RequestCatalogTask.Response data = datas[0];
				mChannelAdapter.add(data);
				mChannelAdapter.notifyDataSetChanged();
			}
		}
	}

	private class OnColumnItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(ViewGroup parent, View view, int row,
				int column, TotalVideo.VideoItem videoItem) {
			Context context = parent.getContext();
			Intent it = new Intent(context, DetailActivity.class);
			it.putExtra("vid", videoItem.getVid());
			context.startActivity(it);
		}
	}
}
