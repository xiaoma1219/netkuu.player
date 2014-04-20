package com.xm.netkuu.activity;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.TabPageIndicator;
import com.xm.netkuu.data.VideoData;
import com.xm.netkuu.data.entry.VideoDetail;
import com.xm.netkuu.data.entry.VideoUrlItem;
import com.xm.netkuu.data.net.UrlData;
import com.xm.netkuu.player.R;
import com.xm.netkuu.ui.frame.BriefFragment;
import com.xm.netkuu.ui.frame.VideoEpisodeDownloadFragment;
import com.xm.netkuu.ui.frame.VideoEpisodePlayFragment;
import com.xm.netkuu.ui.widget.MessageDialog;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends SherlockFragmentActivity {
	private String mVid;
	private BriefAdapter mBriefAdapter; 
	private boolean mVisible = false;
	private VideoDetail mVideoDetail;
	protected String[] mVideoUrlsItems;
	TabPageIndicator mDetailIndicator;
	ViewPager mDetailPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		Intent intent = this.getIntent();
		this.mVid = intent.getStringExtra("vid");		

		mBriefAdapter = new BriefAdapter(this.getSupportFragmentManager());
		
		mDetailPager = (ViewPager) findViewById(R.id.detail_pager);
		mDetailPager.setAdapter(this.mBriefAdapter);

		mDetailIndicator = (TabPageIndicator) findViewById(R.id.detail_indicator);
		mDetailIndicator.setViewPager(mDetailPager);
				
		ImageLoader loader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.default_video_image)
			.showImageOnFail(R.drawable.default_video_image)
			.showImageOnLoading(R.drawable.default_video_image)
			.cacheInMemory(true)
			.build();
		ImageView view = (ImageView)findViewById(R.id.video_image);
		loader.displayImage(UrlData.image(mVid, UrlData.IMAGE_NORMAL), view, options);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);  
		getSupportActionBar().setHomeButtonEnabled(true);
		
		new GetDataTask().execute(mVid);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	@Override 
    public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private class BriefAdapter extends FragmentPagerAdapter{
		private String[] mTitle = new String[]{"简介", "播放", "下载"};
		public BriefAdapter(FragmentManager fm) {
			super(fm);
		}

	 @Override
        public CharSequence getPageTitle(int position) {
		 return mTitle[position];
        }
		 
		@Override
		public Fragment getItem(int position) {
			Fragment frame = null;
			Bundle bundle = new Bundle();
			bundle.putString("vid", mVid);
			bundle.putString("name", mVideoDetail.getName());
			switch(position){
			case 0:
				frame = new BriefFragment();
				bundle.putInt("jz", mVideoDetail.getJz());
				bundle.putString("brief", mVideoDetail.getBrief());
				break;
			case 1:
				frame = new VideoEpisodePlayFragment();
				bundle.putStringArray("video_url_items", mVideoUrlsItems);
				break;
			case 2:
				frame = new VideoEpisodeDownloadFragment();
				bundle.putStringArray("video_url_items", mVideoUrlsItems);
				break;
			}
			frame.setArguments(bundle);
			return frame;
		}

		@Override
		public int getCount() {
			if(mVisible)
				return mTitle.length;
			else
				return 0;
		}
	}
	
	public void makeViews(VideoDetail detail){
		if(detail != null){
			((TextView)findViewById(R.id.video_name)).setText(detail.getName());
			((TextView)findViewById(R.id.video_director)).setText("导演: " + detail.getDirector());
			((TextView)findViewById(R.id.video_actor)).setText("演员: " + detail.getActor());
			((TextView)findViewById(R.id.video_type)).setText(detail.getType());
			((TextView)findViewById(R.id.video_region)).setText("区域: " + detail.getRegion());
			((TextView)findViewById(R.id.video_channel)).setText("频道: " + detail.getChannel());
			((TextView)findViewById(R.id.video_publish)).setText("上映时间： " + detail.getPublish());
			((TextView)findViewById(R.id.video_adddate)).setText("更新时间： " + detail.getAdddate());
		}
	}

	class GetDataTask extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... params) {
			String vid = params[0];
			if(vid != null &&vid.length() > 0){
				mVideoDetail = VideoData.getVideoDetail(vid);
				if(mVideoDetail != null){
					VideoUrlItem items = VideoData.getVideoUrlItem(vid);
					if(items != null){
						mVideoUrlsItems = items.getItemsArray();
					}
					else{
						mVideoUrlsItems = new String[0];
					}
				}
			}
			return null;
		}
		
		@Override
        protected void onPostExecute(Void v){
			if(mVideoDetail == null){
				MessageDialog.error(getSupportFragmentManager(), R.string.msg_network_request_failed);
			}
			else{
				if(mVideoUrlsItems == null){
					mVideoUrlsItems = new String[0];
				}
				findViewById(R.id.detail_frame).setVisibility(View.VISIBLE);
				findViewById(R.id.loading_progress).setVisibility(View.GONE);
				makeViews(mVideoDetail);
				mVisible = true;
				mBriefAdapter.notifyDataSetChanged();
				mDetailIndicator.notifyDataSetChanged();
			}
		}
	}
}
