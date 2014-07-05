package com.xm.netkuu.ui.fragment;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xm.netkuu.player.R;
import com.xm.netkuu.view.MultiColumnListAdapter;
import com.xm.netkuu.view.MultiColumnListView;
import com.xm.netkuu.widget.AbstractSliderAdapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public abstract class ChannalBaseFragment extends Fragment{

	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;
	private MultiColumnListView mListView;
	private DisplayImageOptions mVideoImageLoadOption;
	private AbstractSliderAdapter mSliderAdapter;	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		View view = mInflater.inflate(R.layout.fragment_channel, container, false);
		mImageLoader = ImageLoader.getInstance(); 
		
		mVideoImageLoadOption = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.default_video_image)
			.showImageOnFail(R.drawable.default_video_image)
			.showImageOnLoading(R.drawable.default_video_image)
			.cacheInMemory(true)
			.imageScaleType(com.nostra13.universalimageloader.core.assist.ImageScaleType.EXACTLY)
			.build();
		
		mListView =  (MultiColumnListView) view.findViewById(R.id.channel_list_view);
		
		return view;
	}
	
	public abstract void setSliderData(final AbstractSliderAdapter adapter);
	
	public abstract void setContentData();
	
}
