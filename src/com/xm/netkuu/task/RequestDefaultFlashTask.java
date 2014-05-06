package com.xm.netkuu.task;

import com.xm.netkuu.data.VideoData;
import com.xm.netkuu.data.entry.DefaultFlash;

import android.os.AsyncTask;

public class RequestDefaultFlashTask extends AsyncTask<String, Void, DefaultFlash>{

	@Override
	protected DefaultFlash doInBackground(String... params) {
		if(params.length == 1)
			return VideoData.getDefaultFlash(params[0]);
		return null;
	}

}
