package com.xm.netkuu.ui.widget;

import com.xm.netkuu.data.entry.Total;
import com.xm.netkuu.data.entry.Total.Media;
import com.xm.netkuu.activity.DetailActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OnVideoGridItemClickListener implements OnItemClickListener{

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Total.Media media = (Media) parent.getAdapter().getItem(position);
		if(media != null){
			Context context = parent.getContext();
			Intent it = new Intent(context, DetailActivity.class);
			it.putExtra("vid", media.getVid());
			context.startActivity(it);
		}
	}

}
