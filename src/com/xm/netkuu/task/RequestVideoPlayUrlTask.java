package com.xm.netkuu.task;

import com.xm.netkuu.data.VideoData;

import android.os.AsyncTask;

public class RequestVideoPlayUrlTask extends AsyncTask<Object, Void, String>{

	@Override
	protected String doInBackground(Object... params) {
		if(params.length == 2){
			String vid = (String) params[0];
			Integer episode = (Integer) params[1];
			return VideoData.getPlayUrl(vid, episode);
		}
		return null;
	}
	
}
