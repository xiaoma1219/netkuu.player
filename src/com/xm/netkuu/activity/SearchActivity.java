package com.xm.netkuu.activity;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.xm.netkuu.data.VideoData;
import com.xm.netkuu.data.entry.Total;
import com.xm.netkuu.ui.widget.OnVideoGridItemClickListener;
import com.xm.netkuu.ui.widget.VideoSearchAdapter;
import com.xm.netkuu.player.R;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends SherlockActivity {
	private PullToRefreshGridView mVideoGrid;
	private TextView mSearchInfo;
	private VideoSearchAdapter mVideoAdapter;
	private ProgressBar mProgressBar;
	private int mPage = 0;
	private String mKey;
	private int mPagesize = 15;
	private int mTotalLength = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		//mSearcher = new VideoData.Searcher(this);
		
		mVideoGrid = (PullToRefreshGridView) findViewById(R.id.search_list_view);
		mVideoAdapter = new VideoSearchAdapter(this);
		mVideoGrid.setAdapter(mVideoAdapter);
		mVideoGrid.setOnItemClickListener(new OnVideoGridItemClickListener());
		
		mSearchInfo = (TextView) this.findViewById(R.id.search_info);
		
		mProgressBar = (ProgressBar) this.findViewById(R.id.loading_progress);
		Intent args = this.getIntent();
		mKey = args.getStringExtra("key");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);  
		getSupportActionBar().setHomeButtonEnabled(true);
		search(mKey);
		mVideoGrid.setOnRefreshListener(new OnRefreshListener2<GridView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
				Toast.makeText(SearchActivity.this, "Pull Down!", Toast.LENGTH_SHORT).show();
				mVideoGrid.onRefreshComplete();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
				if(mVideoAdapter.getCount() < mTotalLength)
					search(mKey);
				else{
					mVideoGrid.onRefreshComplete();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.search, menu);SearchView view = (SearchView)menu.findItem(R.id.menu_search).getActionView();
		view.setOnQueryTextListener(new OnQueryTextListener(){
			@Override
			public boolean onQueryTextSubmit(String query) {
				if(query.length() > 0){
					mVideoAdapter.clear();
					mTotalLength = 0;
					mVideoAdapter.notifyDataSetChanged();
					search(query);
				}
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});
		return true;
	}
		
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		if (item != null && item.getItemId() == android.R.id.home) {
			this.finish();
			return true;
		}
        return super.onOptionsItemSelected(item);
    }
	
	public void search(String key){
		mPage++;
		mKey = key;
		new RequestSearchTask().execute();
	}
	
	class RequestSearchTask extends AsyncTask<Void, Void, Total>{
		
		@Override
		protected void onPreExecute(){
			mProgressBar.setVisibility(View.VISIBLE);
			mVideoGrid.setVisibility(View.GONE);
			mVideoAdapter.clear();
			mVideoAdapter.notifyDataSetChanged();
		}
		
		@Override
		protected Total doInBackground(Void... params) {
			return VideoData.searchVideo(mPage, mPagesize, mKey);
		}

		@Override
		protected void onPostExecute(Total result) {
			mProgressBar.setVisibility(View.GONE);
			mVideoGrid.setVisibility(View.VISIBLE);
			mVideoGrid.onRefreshComplete();
			if(result == null){
				Toast.makeText(SearchActivity.this, R.string.msg_load_data_failed, Toast.LENGTH_LONG).show();
			}
			else{
				if(result.length() > mTotalLength){
					mTotalLength = result.length();
					mSearchInfo.setText("共搜索到视屏：" + mTotalLength + "个");
				}
				mVideoAdapter.addAll(result.getMedia());
				mVideoAdapter.notifyDataSetChanged();
			}
	    }
	}

}
