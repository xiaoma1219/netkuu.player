package com.xm.netkuu.ui.frame;


import com.xm.netkuu.player.PlayerActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class VideoEpisodePlayFragment extends VideoEpisodeFragment{
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		super.mEpisodeView.setOnItemClickListener(new OnClickListener());
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
