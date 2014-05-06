package com.xm.netkuu.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xm.netkuu.data.UrlData;
import com.xm.netkuu.player.R;

public class BriefFragment extends SherlockFragment{	
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mImageOptions;
	private String mVid;
	private static final int sSnapshotsCount = 3;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		mVid = (String) this.getArguments().get("vid");
		mImageLoader = ImageLoader.getInstance();
		mImageOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_video_image)
				.showImageOnFail(R.drawable.default_video_image)
				.showImageForEmptyUri(R.drawable.default_video_image)
				.cacheInMemory(true)
				.build();
		
		View view = inflater.inflate(R.layout.video_brief_frame, null);
		
		String brief = (String) this.getArguments().get("brief");
		((TextView)view.findViewById(R.id.brief)).setText(brief);
		setupSnapshotsView((ViewGroup)view.findViewById(R.id.video_brief_layout));
		return view;
	}
	
	private void setupSnapshotsView(ViewGroup parent){
		Context context = mInflater.getContext();
		int padding = context.getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
		for(int i = 0; i < sSnapshotsCount; i++){
			ImageView view = (ImageView) mInflater.inflate(R.layout.image_view_fill_width,
					parent, false);
			
			view.setPadding(0, padding, 0, 0);
			mImageLoader.displayImage(UrlData.snapshot(mVid, i + 1),
					(ImageView)view, mImageOptions);
			parent.addView(view);
		}
	}
}
