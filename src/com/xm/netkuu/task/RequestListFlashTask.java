package com.xm.netkuu.task;

import java.util.List;

import android.os.AsyncTask;

import com.xm.netkuu.data.VideoData;
import com.xm.netkuu.data.entry.ListFlash;

public class RequestListFlashTask extends AsyncTask<String, Void, List<ListFlash>>{

	@Override
	protected List<ListFlash> doInBackground(String... params) {
		if(params.length == 1)
			return VideoData.getListFlash(params[0]);
		return null;
	}

}
