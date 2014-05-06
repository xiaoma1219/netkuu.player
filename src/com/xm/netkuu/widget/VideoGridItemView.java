package com.xm.netkuu.widget;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xm.netkuu.player.R;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VideoGridItemView extends LinearLayout{
	protected String vid;
	
	private static final double sImageViewRatio = 0.75;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public VideoGridItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public VideoGridItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public VideoGridItemView(Context context) {
		super(context);
	}
	
	public void setVid(String vid){
		this.vid = vid;
	}
	
	public ImageView getVideoImageView(){
		return (ImageView)findViewById(R.id.video_image);
	}
	
	public void setVideoName(String name){
		((TextView)findViewById(R.id.video_name)).setText(name);
	}
	
	public void setVideoCount(int count){
		TextView view = ((TextView)findViewById(R.id.video_count));
		if(count >= 0){
			view.setVisibility(View.VISIBLE);
			view.setText("��" + count + "����Ƶ");
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
	
	public void setVideoRate(Integer rate){
		if(rate != null)
			((TextView)findViewById(R.id.video_rate)).setText(Float.toString(rate / 10.0f));
	}
	
	public void setVideoBrief(String brief){
		((TextView)this.findViewById(R.id.video_brief)).setText(brief);
	}
	
	public void setVideoImage(ImageLoader loader, DisplayImageOptions options, String uri){
		ImageView view = (ImageView)findViewById(R.id.video_image);
		loader.displayImage(uri, view, options);
	}
	
	public int setVideoImageViewWidth(int width){
		ImageView imageView = getVideoImageView();
		ViewGroup.LayoutParams lp = imageView.getLayoutParams();
		lp.width = width;
		lp.height = (int)(width / sImageViewRatio);
		imageView.setLayoutParams(lp);
		return lp.height;
	}
	
	public int setVideoImageViewHeight(int height){
		ImageView imageView = getVideoImageView();
		ViewGroup.LayoutParams lp = imageView.getLayoutParams();
		lp.height = height;
		lp.width = (int)(height * sImageViewRatio);
		imageView.setLayoutParams(lp);
		return lp.width;
	}
	
	public void refreshHeight(int height){
		ViewGroup.LayoutParams lp = getLayoutParams();
		lp.height = height;
		System.out.println(height);
		this.setLayoutParams(lp);
	}
}
