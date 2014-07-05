package com.xm.netkuu.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xm.netkuu.data.UrlData;
import com.xm.netkuu.data.entry.HtmlFlash;
import com.xm.netkuu.player.R;

public class SliderHtmlAdapter extends AbstractSliderAdapter{

	private HtmlFlash mFlash = null;
	
	public SliderHtmlAdapter(Context context) {
		super(context);
	}
	
	public void setAdapterData(HtmlFlash data){
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
		HtmlFlash.HtmlFlashItem item = mFlash.getItems().get(position);
		mImageLoader.displayImage(UrlData.HOST + item.getImg(), view, mImageLoadOption);
		container.addView(view);
		return view;
	}
	
	@Override
	public int getCount() {
		return mFlash == null ? 0 : mFlash.getItems() == null ? 0 : mFlash.getItems().size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}	


}
