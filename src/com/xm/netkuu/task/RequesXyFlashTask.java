package com.xm.netkuu.task;


import android.os.AsyncTask;

import com.xm.netkuu.data.VideoData;
import com.xm.netkuu.data.entry.XyFlash;

public class RequesXyFlashTask extends AsyncTask<String, Void, XyFlash>{

	@Override
	protected XyFlash doInBackground(String... params) {
		if(params.length == 1)
			return VideoData.getXyFlash(params[0]);
		return null;
	}

}
