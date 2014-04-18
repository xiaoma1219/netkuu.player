package com.xm.netkuu.ui.frame;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.actionbarsherlock.app.SherlockFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.PageIndicator;
import com.xm.netkuu.activity.DetailActivity;
import com.xm.netkuu.data.VideoData;
import com.xm.netkuu.data.entry.Barlist;
import com.xm.netkuu.data.entry.Barlist.BarlistItem;
import com.xm.netkuu.data.entry.HomeFlash;
import com.xm.netkuu.data.net.NetData;
import com.xm.netkuu.player.R;
import com.xm.netkuu.ui.view.TwoWayView ;
import com.xm.netkuu.ui.view.VideoGridItemView;
import com.xm.netkuu.ui.widget.MessageDialog;
import com.xm.netkuu.ui.widget.RequestCatalogDataTask;
import com.xm.netkuu.ui.widget.RequestCatalogDataTask.CatalogData;

public class HomePageFragment extends SherlockFragment{
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
		View view = inflater.inflate(R.layout.home_page_frame, null);
		mInflater = inflater;
		
		mImageLoader = ImageLoader.getInstance();
		
		mImageLoadOption = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.default_video_image)
			.showImageOnFail(R.drawable.default_video_image)
			.showImageOnLoading(R.drawable.default_video_image)
			.cacheInMemory(true)
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
			new CatalogData(NetData.TYPE_TV, mContext.getString(R.string.title_tv_hots)),
			new CatalogData(NetData.TYPE_TV, mContext.getString(R.string.title_tv_nation_gt), NetData.CATALOG_TV_NATION, NetData.CATALOG_TV_GT),
			new CatalogData(NetData.TYPE_TV, mContext.getString(R.string.title_tv_abroad_rh), NetData.CATALOG_TV_OM, NetData.CATALOG_TV_RH),
			
			new CatalogData(NetData.TYPE_MOVIE, mContext.getString(R.string.title_movie_action_scifc), NetData.CATALOG_MOVIE_ACTION, NetData.CATALOG_MOVIE_SCIFI),
			new CatalogData(NetData.TYPE_MOVIE, mContext.getString(R.string.title_movie_humor_love), NetData.CATALOG_MOVIE_HUMOR, NetData.CATALOG_MOVIE_LOVE),
			new CatalogData(NetData.TYPE_MOVIE, mContext.getString(R.string.title_movie_funk), NetData.CATALOG_MOVIE_TERROR),
			
			new CatalogData(NetData.TYPE_CARTOON, mContext.getString(R.string.title_cartoon)),
			new CatalogData(NetData.TYPE_VARIETY, mContext.getString(R.string.title_variety)));
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
	
	private class LoadSilderDataTask extends AsyncTask<Void, Void, HomeFlash>{
		
		@Override
		protected void onPreExecute(){
			mLoadingProgressBar.setVisibility(View.VISIBLE);
			mContent.setVisibility(View.GONE);
		}
		
		@Override
		protected HomeFlash doInBackground(Void... params) {
			return VideoData.getHomeFalsh();
		}
		
		@Override
		protected void onPostExecute(HomeFlash result){
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
	
	private class LoadBarlistDataTask extends RequestCatalogDataTask{
		@Override
		protected void onProgressUpdate(RequestCatalogDataTask.CatalogData... datas){
			if(datas.length == 1){
				RequestCatalogDataTask.CatalogData data = datas[0];
				if(data != null && mContentLayout != null){
					View view = mInflater.inflate(R.layout.barlist_view, mContentLayout, false);
					TextView mTitle = (TextView) view.findViewById(R.id.title);
					mTitle.setText(data.mTitle);
					TwoWayView mListView = (TwoWayView) view.findViewById(R.id.barlist);
					BarlistAdapter mAdapter = new BarlistAdapter(mContext, data.mDataItems);
					mListView.setAdapter(mAdapter);
					mListView.setItemMargin(mItemMargin);
					mListView.setOnItemClickListener(new OnBarlistItemClickListener());
					mContentLayout.addView(view);
				}
			}
		}
	}
	private class SliderAdapter extends PagerAdapter{
		private static final int length = 12;
		private HomeFlash mHomeFlash = null;
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			//container.
			//container.removeViewAt(position);
		}
		
		public void setAdapterData(HomeFlash data){
			mHomeFlash = data;
			notifyDataSetChanged();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {	
			ImageView view = (ImageView) mInflater.inflate(R.layout.image_view_fill_width, container, false);
			mImageLoader.displayImage(NetData.HOST + mHomeFlash.getImg().get(position), view, mImageLoadOption);
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
		private List<Barlist.BarlistItem> mDataItems;
		
		public BarlistAdapter(Context context, List<Barlist.BarlistItem> items) {
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
			}
			else{
				itemView = (VideoGridItemView) view;
			}
			Barlist.BarlistItem item = (BarlistItem) this.getItem(position);
			itemView.setVideoName(item.getName());
			itemView.setVideoRate(item.getRate());
			itemView.setVideoBrief(item.getBrief());
			itemView.setVideoImage(mImageLoader, mImageLoadOption, NetData.image(item.getVid(), NetData.IMAGE_SMALL));
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
	
	private class OnBarlistItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Barlist.BarlistItem item = (Barlist.BarlistItem) parent.getAdapter().getItem(position);
			if(item != null){
				Context context = parent.getContext();
				Intent it = new Intent(context, DetailActivity.class);
				it.putExtra("vid", item.getVid());
				context.startActivity(it);
			}
		}
	}
}
