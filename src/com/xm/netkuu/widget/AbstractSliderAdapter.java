package com.xm.netkuu.widget;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xm.netkuu.player.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;

public abstract class AbstractSliderAdapter  extends PagerAdapter{
	protected DisplayImageOptions mImageLoadOption;
	protected ImageLoader mImageLoader;

	protected LayoutInflater mInflater;
	
	public AbstractSliderAdapter(Context context){
		mInflater = LayoutInflater.from(context);
		mImageLoader = ImageLoader.getInstance(); 
		mImageLoadOption = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.default_video_image)
		.showImageOnFail(R.drawable.default_video_image)
		.showImageOnLoading(R.drawable.default_video_image)
		.cacheInMemory(true)
		.imageScaleType(com.nostra13.universalimageloader.core.assist.ImageScaleType.EXACTLY)
		.build();
	}

}
