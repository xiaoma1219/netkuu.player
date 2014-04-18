package com.xm.netkuu.ui.frame;

import io.vov.vitamio.utils.Log;

import java.util.ArrayList;
import java.util.List;

import com.xm.downloads.DownloadManager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class VideoEpisodeDownloadFragment extends VideoEpisodeFragment{
	protected DownloadManager mDownloadManager;
	protected List<Integer> mDownloadedEpisode;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		mDownloadedEpisode = new ArrayList<Integer>();
		mDownloadManager = DownloadManager.getInstance(this.getActivity());
		this.mEpisodeView.setOnItemClickListener(new OnClickListener());
	}
	
	public int downloaded(int epi){
		for(int i = 0; i < this.mDownloadedEpisode.size(); i++){
			if(this.mDownloadedEpisode.get(i) == epi)
				return i;
		}
		return -1;
	}
	
	class OnClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long row) {
			int epi = position + mBegin;
			if(downloaded(epi) >= 0){
				return;
			}
			String uid = mVideoUrlItems[position];
			if(uid == null || uid.length() == 0)
				return;
			String path = null;//VideoData.getFilmMov(uid).getFile();
			if(path != null){
				Log.d("Download Path" + path);
				mDownloadManager.enqueue(path, mName, epi, uid);
				mDownloadedEpisode.add(epi);
			}
		}
	}
}
