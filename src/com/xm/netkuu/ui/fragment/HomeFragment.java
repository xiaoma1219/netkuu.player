package com.xm.netkuu.ui.fragment;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.PageIndicator;
import com.xm.netkuu.data.UrlData;
import com.xm.netkuu.data.VideoData;
import com.xm.netkuu.data.entry.TotalVideo;
import com.xm.netkuu.data.entry.TotalVideo.VideoItem;
import com.xm.netkuu.data.entry.DefaultFlash;
import com.xm.netkuu.player.R;
import com.xm.netkuu.task.RequestCatalogTask;
import com.xm.netkuu.ui.activity.DetailActivity;
import com.xm.netkuu.widget.MessageDialog;
import com.xm.netkuu.widget.TwoWayView;
import com.xm.netkuu.widget.VideoGridItemView;

public class HomeFragment extends Fragment{
	private Context mContext;
	private LayoutInflater mInflater;
	private DisplayImageOptions mImageLoadOption;
	private ImageLoader mImageLoader;
	private SliderAdapter mSliderAdapter;
	
	private View mLoadingProgressBar;
	private View mContent;
	private ViewGroup mContentLayout;
	private int mItemMargin;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, null);
		mInflater = inflater;
		
		mImageLoader = ImageLoader.getInstance();
		
		mImageLoadOption = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.default_video_image)
			.showImageOnFail(R.drawable.default_video_image)
			.showImageOnLoading(R.drawable.default_video_image)
			.cacheInMemory(true)
			.imageScaleType(com.nostra13.universalimageloader.core.assist.ImageScaleType.EXACTLY)
			.build();
		

		makeViews(view);
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	}
	
	private void makeViews(View view){
		mContext = view.getContext();
		mItemMargin = mContext.getResources().getDimensionPixelSize(R.dimen.video_horizontal_list_item_margin);
		
		mLoadingProgressBar = view.findViewById(R.id.loading_progress);
		mContent = view.findViewById(R.id.content);
		mContentLayout = (ViewGroup)view.findViewById(R.id.content_layout);	
		makeSliderView(mContentLayout);
		new LoadBarlistDataTask().execute(
			new RequestCatalogTask.Request(UrlData.CHANNEL_TV, mContext.getString(R.string.title_tv_hots), -1),
			new RequestCatalogTask.Request(UrlData.CHANNEL_TV, mContext.getString(R.string.title_tv_nation_gt), UrlData.CATALOG_TV_NATION, UrlData.CATALOG_TV_GT),
			new RequestCatalogTask.Request(UrlData.CHANNEL_TV, mContext.getString(R.string.title_tv_abroad_rh), UrlData.CATALOG_TV_OM, UrlData.CATALOG_TV_RH),
			
			new RequestCatalogTask.Request(UrlData.CHANNEL_MOVIE, mContext.getString(R.string.title_movie_action_scifc), UrlData.CATALOG_MOVIE_ACTION, UrlData.CATALOG_MOVIE_SCIFI),
			new RequestCatalogTask.Request(UrlData.CHANNEL_MOVIE, mContext.getString(R.string.title_movie_humor_love), UrlData.CATALOG_MOVIE_HUMOR, UrlData.CATALOG_MOVIE_LOVE),
			new RequestCatalogTask.Request(UrlData.CHANNEL_MOVIE, mContext.getString(R.string.title_movie_funk), UrlData.CATALOG_MOVIE_TERROR),
			
			new RequestCatalogTask.Request(UrlData.CHANNEL_CARTOON, mContext.getString(R.string.title_cartoon), -1),
			new RequestCatalogTask.Request(UrlData.CHANNEL_VARIETY, mContext.getString(R.string.title_variety), -1));
	}
	
	private void makeSliderView(ViewGroup parent){
		View view = mInflater.inflate(R.layout.image_slider_view, parent, false);
		ViewPager mViewPager = (ViewPager) view.findViewById(R.id.home_slider_pager);
		mSliderAdapter = new SliderAdapter();
		mViewPager.setAdapter(mSliderAdapter);
		PageIndicator mPageIndicator = (PageIndicator) view.findViewById(R.id.home_slider_indicator);
		mPageIndicator.setViewPager(mViewPager);
		parent.addView(view);
		new LoadSilderDataTask().execute();
	}
	
	private class LoadSilderDataTask extends AsyncTask<Void, Void, DefaultFlash>{
		
		@Override
		protected void onPreExecute(){
			mLoadingProgressBar.setVisibility(View.VISIBLE);
			mContent.setVisibility(View.GONE);
		}
		
		@Override
		protected DefaultFlash doInBackground(Void... params) {
			return VideoData.getDefaultFlash(UrlData.HOME_FLASH);
		}
		
		@Override
		protected void onPostExecute(DefaultFlash result){
			if(result != null){
				if(mSliderAdapter != null)
					mSliderAdapter.setAdapterData(result);
				mLoadingProgressBar.setVisibility(View.GONE);
				mContent.setVisibility(View.VISIBLE);
			}
			else{
				MessageDialog.error(getFragmentManager(), R.string.msg_network_request_failed);
			}
		}
	}
	
	private class LoadBarlistDataTask extends RequestCatalogTask{
		@Override
		protected void onProgressUpdate(RequestCatalogTask.Response... datas){
			if(datas.length == 1){
				RequestCatalogTask.Response data = datas[0];
				if(data != null && mContentLayout != null){
					View view = mInflater.inflate(R.layout.barlist_view, mContentLayout, false);
					TextView mTitle = (TextView) view.findViewById(R.id.title);
					mTitle.setText(data.mTitle);
					TwoWayView mListView = (TwoWayView) view.findViewById(R.id.barlist);
					BarlistAdapter mAdapter = new BarlistAdapter(mContext, data.mReponseItems);
					mListView.setAdapter(mAdapter);
					mListView.setItemMargin(mItemMargin);
					mListView.setOnItemClickListener(new OnVideoItemClickListener());
					mContentLayout.addView(view);
				}
			}
		}
	}
	private class SliderAdapter extends PagerAdapter{
		private static final int length = 12;
		private DefaultFlash mHomeFlash = null;
				
		public void setAdapterData(DefaultFlash data){
			mHomeFlash = data;
			notifyDataSetChanged();
		}
		
		@Override  
        public void destroyItem(ViewGroup container, int position,  
                Object object) {  
            //container.removeView(viewList.get(position));  
        }

		@Override
		public Object instantiateItem(ViewGroup container, int position) {	
			ImageView view = (ImageView) mInflater.inflate(R.layout.image_view_fill_width, container, false);
			mImageLoader.displayImage(UrlData.HOST + mHomeFlash.getImg().get(position), view, mImageLoadOption);
			container.addView(view);
			return view;
		}
		
		@Override
		public int getCount() {
			return mHomeFlash == null ? 0 : length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}	
	}
	
	private class BarlistAdapter extends BaseAdapter{
		private int mItemWidth;
		private List<TotalVideo.VideoItem> mDataItems;
		
		public BarlistAdapter(Context context, List<TotalVideo.VideoItem> items) {
			mItemWidth = context.getResources().getDimensionPixelSize(R.dimen.video_horizontal_list_item_width);
			mDataItems = items;
		}
		
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			VideoGridItemView itemView;
			if(view == null){
				itemView = (VideoGridItemView) mInflater.inflate(R.layout.video_grid_item_view, parent, false);
				ViewGroup.LayoutParams params = itemView.getLayoutParams();
				params.width = mItemWidth;
				itemView.setLayoutParams(params);
				itemView.setVideoImageViewWidth(mItemWidth);
			}
			else{
				itemView = (VideoGridItemView) view;
			}
			TotalVideo.VideoItem item = (VideoItem) this.getItem(position);
			String videoName = item.getName();
			itemView.setVideoName(videoName);
			itemView.setVideoRate(item.getRate());
			itemView.setVideoBrief(item.getBrief());
			if(videoName.indexOf("更新") > 0 || videoName.indexOf("集全") > 0){
				itemView.setVideoCount(videoName.substring(videoName.indexOf("(") + 1, videoName.indexOf(")")));
			}
			else{
				itemView.setVideoCount(item.getCatalog());
			}
			itemView.setVideoImage(mImageLoader, mImageLoadOption, UrlData.image(item.getVid(), UrlData.IMAGE_SMALL));
			return itemView;
		}

		@Override
		public int getCount() {
			return mDataItems != null ? mDataItems.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			return mDataItems != null ? mDataItems.get(position) : null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}
	
	private class OnVideoItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			TotalVideo.VideoItem item = (TotalVideo.VideoItem) parent.getAdapter().getItem(position);
			if(item != null){
				Context context = parent.getContext();
				Intent it = new Intent(context, DetailActivity.class);
				it.putExtra("vid", item.getVid());
				context.startActivity(it);
			}
		}
	}
}
