package com.xm.netkuu.task;

import android.os.AsyncTask;

import com.xm.netkuu.data.VideoData;
import com.xm.netkuu.data.entry.HtmlFlash;

public class RequestHtmlFlashTask extends AsyncTask<String, Void, HtmlFlash>{

	@Override
	protected HtmlFlash doInBackground(String... params) {
		if(params.length == 1)
			return VideoData.getHtmlFlash(params[0]);
		return null;
	}

}
