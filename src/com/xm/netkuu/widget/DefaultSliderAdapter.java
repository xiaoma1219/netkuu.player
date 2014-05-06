package com.xm.netkuu.widget;

import com.xm.netkuu.data.UrlData;
import com.xm.netkuu.data.entry.DefaultFlash;
import com.xm.netkuu.player.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class DefaultSliderAdapter extends AbstractSliderAdapter{

	private DefaultFlash mFlash = null;
	private int mLength = 12;
	
	public DefaultSliderAdapter(Context context) {
		super(context);
	}
	
	public void setAdapterData(DefaultFlash data){
		mFlash = data;
		notifyDataSetChanged();
	}
	
	@Override  
    public void destroyItem(ViewGroup container, int position,  
            Object object) {  
        //container.removeView(viewList.get(position));  
    }

	@Override
	public Object instantiateItem(ViewGroup container, int position) {	
		ImageView view = (ImageView) mInflater.inflate(R.layout.image_view_fill_width, container, false);
		mImageLoader.displayImage(UrlData.HOST + mFlash.getImg().get(position), view, mImageLoadOption);
		container.addView(view);
		return view;
	}
	
	@Override
	public int getCount() {
		return mFlash == null ? 0 : mFlash.getImg() == null ? 0 : Math.min(mFlash.getImg().size(), mLength);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}	

}
