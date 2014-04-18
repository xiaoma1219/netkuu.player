package com.xm.netkuu.ui.view;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xm.netkuu.player.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VideoGridItemView extends LinearLayout{
	protected String vid;
	
	private static final double sImageViewRatio = 0.75;
	
	public VideoGridItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setVid(String vid){
		this.vid = vid;
	}
	
	public ImageView getVideoImageView(){
		return (ImageView)this.findViewById(R.id.video_image);
	}
	
	public void setVideoName(String name){
		((TextView)findViewById(R.id.video_name)).setText(name);
	}
	
	public void setVideoCount(int count){
		TextView view = ((TextView)findViewById(R.id.video_count));
		if(count >= 0){
			view.setVisibility(View.VISIBLE);
			view.setText("¹²" + count + "¸öÊÓÆµ");
		}
		else{
			view.setVisibility(View.GONE);
		}
	}
	
	public void setVideoCount(String count){
		TextView view = ((TextView)findViewById(R.id.video_count));
		if(count != null && count.length() > 0){
			view.setVisibility(View.VISIBLE);
			view.setText(count);
		}
		else{
			view.setVisibility(View.GONE);
		}
	}
	
	public void setVideoRate(int rate){
		((TextView)findViewById(R.id.video_rate)).setText(Float.toString(rate / 10.0f));
	}
	
	public void setVideoBrief(String brief){
		((TextView)this.findViewById(R.id.video_brief)).setText(brief);
	}
	
	public void setVideoImage(ImageLoader loader, DisplayImageOptions options, String uri){
		ImageView view = (ImageView)findViewById(R.id.video_image);
		loader.displayImage(uri, view, options);
	}
	
	public void setVideoImageViewWidth(int width){
		ImageView imageView = getVideoImageView();
		ViewGroup.LayoutParams lp = imageView.getLayoutParams();
		lp.width = width;
		lp.height = (int)(width / sImageViewRatio);
		imageView.setLayoutParams(lp);
	}
	
	public void setVideoImageViewHeight(int height){
		ImageView imageView = getVideoImageView();
		ViewGroup.LayoutParams lp = imageView.getLayoutParams();
		lp.height = height;
		lp.width = (int)(height * sImageViewRatio);
		imageView.setLayoutParams(lp);
	}
	
	/*
	private String brief(String brief){
		return brief;
	}
	*/
}
