package com.xm.netkuu.ui.frame;

import com.actionbarsherlock.app.SherlockFragment;
import com.xm.netkuu.player.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class VideoEpisodeFragment extends SherlockFragment{
	protected GridView mEpisodeView;
	protected LayoutInflater mInflater;
	protected String[] mVideoUrlItems;
	protected String mName;
	protected String mVid;
	protected int mBegin = 1;
	protected int mPage = 1;
	protected int mPageSize = 50;
	protected EpisodeAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.video_episode_list_frame, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		this.mInflater = LayoutInflater.from(this.getActivity());
		Bundle bundle = this.getArguments();
		this.mVid = bundle.getString("vid");
		this.mBegin = bundle.getInt("begin");
		if(this.mBegin <= 0)
			this.mBegin = 1;
		this.mName = bundle.getString("name");
		
		this.mVideoUrlItems = bundle.getStringArray("video_url_items");
		this.mAdapter = new EpisodeAdapter();
		this.mEpisodeView = (GridView) this.getView().findViewById(R.id.episode_list_view);
		this.mEpisodeView.setAdapter(this.mAdapter);
	}
	
	
	public void setUrls(String[] urls){
		this.mVideoUrlItems = urls;
		this.mAdapter.notifyDataSetChanged();
	}
	
	public int getEpisodeNum(int position){
		return this.mBegin + (this.mPage - 1) * mPageSize + position;
	}
	
	class EpisodeAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			return mVideoUrlItems.length;
		}

		@Override
		public Object getItem(int position) {
			return mVideoUrlItems[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			if(view == null){
				view = mInflater.inflate(R.layout.video_episode_list_item_view, parent, false);
			}
			((TextView)view).setText(Integer.toString(position + mBegin));
			if(mVideoUrlItems[position] == null || mVideoUrlItems[position].length() == 0){
				view.setEnabled(false);
			}
			else{
				view.setEnabled(true);
			}
			return view;
		}
	}
}
