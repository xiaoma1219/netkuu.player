package com.xm.netkuu.ui.widget;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xm.netkuu.data.entry.Total;
import com.xm.netkuu.data.entry.Total.Media;
import com.xm.netkuu.data.net.NetData;
import com.xm.netkuu.player.R;
import com.xm.netkuu.ui.view.VideoGridItemView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class VideoSearchAdapter extends BaseAdapter{

	private List<Total.Media> mMedia;
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mImageOptions;
	
	public VideoSearchAdapter(Context context){
		mInflater = LayoutInflater.from(context);
		mImageLoader = ImageLoader.getInstance();
		mImageOptions = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.default_video_image)
			.showImageOnFail(R.drawable.default_video_image)
			.showImageOnLoading(R.drawable.default_video_image)
			.cacheInMemory(true)
			.build();
	}

	public void addAll(List<Total.Media> media){
		if(mMedia != null)
			mMedia.addAll(media);
		else{
			mMedia = media;
		}
	}
	
	public void clear(){
		if(mMedia != null)
			mMedia.clear();
	}
	
	@Override
	public int getCount() {
		return mMedia != null ? mMedia.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		if(mMedia != null)
			return mMedia.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = mInflater.inflate(R.layout.video_grid_item_view, null);
		}
		VideoGridItemView videoGridView = ((VideoGridItemView)view);
		Total.Media media = (Media) getItem(position);
		if(media != null){
			String videoName = media.getName();
			videoGridView.setVideoName(videoName);
			videoGridView.setVideoBrief(media.getBrief());
			if(videoName.indexOf("更新") > 0 || videoName.indexOf("集全") > 0){
				videoGridView.setVideoCount(videoName.substring(videoName.indexOf("(") + 1, videoName.indexOf(")")));
			}
			else{
				videoGridView.setVideoCount(media.getCatalog());
			}
			//videoGridView.setVideoImageViewWidth(videoGridView.getWidth());
			videoGridView.setVideoImage(mImageLoader, mImageOptions, NetData.image(media.getVid(), 
					NetData.IMAGE_SMALL));
		}
		return view;
	}
}
