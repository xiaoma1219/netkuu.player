package com.xm.netkuu.ui.fragment;

import com.xm.netkuu.player.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class VideoEpisodeFragment extends Fragment{
	protected GridView mEpisodeView;
	protected LayoutInflater mInflater;
	protected String[] mVideoUrlItems;
	protected String mName;
	protected String mVid;
	protected int mBegin = 1;
	protected int mPage = 1;
	protected int mPageSize = 50;
	protected EpisodeAdapter mAdapter;
	protected boolean[] mVisitedMask;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.mInflater = inflater;
		View view = mInflater.inflate(R.layout.video_episode_list_frame, container, false);;
		Bundle bundle = this.getArguments();
		this.mVid = bundle.getString("vid");
		this.mBegin = bundle.getInt("begin");
		if(this.mBegin <= 0)
			this.mBegin = 1;
		this.mName = bundle.getString("name");
		
		mVideoUrlItems = bundle.getStringArray("video_url_items");
		mVisitedMask = new boolean[mVideoUrlItems.length];
		for(int i = 0; i < mVisitedMask.length; i++){
			mVisitedMask[i] = false;
		}
		mAdapter = new EpisodeAdapter();
		mEpisodeView = (GridView) view.findViewById(R.id.episode_list_view);
		mEpisodeView.setAdapter(mAdapter);
		return view;
	}
	
	/*
	public void setUrls(String[] urls){
		this.mVideoUrlItems = urls;
		mVisitedMask = new boolean[mVideoUrlItems.length];
		for(int i = 0; i < mVisitedMask.length; i++){
			mVisitedMask[i] = false;
		}
		this.mAdapter.notifyDataSetChanged();
	}
	*/
	
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

			if(mVisitedMask.length > position && mVisitedMask[position]){
				view.setBackgroundResource(R.drawable.episode_list_item_visited_selector);
			}
			else{
				view.setBackgroundResource(R.drawable.episode_list_item_selector);
			}
			view.setEnabled(mVideoUrlItems[position] != null && mVideoUrlItems[position].length() > 0);
			return view;
		}
	}
}
