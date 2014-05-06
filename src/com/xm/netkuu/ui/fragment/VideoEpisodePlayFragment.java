package com.xm.netkuu.ui.fragment;


import com.xm.netkuu.player.PlayerActivity;
import com.xm.netkuu.player.PlayerHistory;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class VideoEpisodePlayFragment extends VideoEpisodeFragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		PlayerHistory.Query mQuery = PlayerHistory.queryByVid(inflater.getContext(), mVid);
		while(mQuery.next()){
			int episode = mQuery.getEpisode();
			System.out.println(episode);
			if(episode < mVisitedMask.length){
				mVisitedMask[episode - 1] = true;
			}
		}
		mEpisodeView.setOnItemClickListener(new OnClickListener());
		return view;
	}
	
	class OnClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			int epi = getEpisodeNum(position);
			String url = mVideoUrlItems[epi - mBegin];
			if(url == null || url.length() == 0){
				return;
			}
			Intent intent = new Intent(getActivity(), PlayerActivity.class);
			intent.putExtra("name", mName);
			intent.putExtra("vid", mVid);
			intent.putExtra("eposide", epi);
			getActivity().startActivity(intent);
		}
	}
}
