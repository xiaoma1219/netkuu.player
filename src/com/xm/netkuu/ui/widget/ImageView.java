package com.xm.netkuu.ui.widget;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
public class ImageView extends android.widget.ImageView {
	private boolean mAdjustViewBounds;
	private int mMaxWidth;
	private int mMaxHeight;

	public ImageView(Context context) {
		super(context);
	}

	public ImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	// getAdjustViewBounds() was added in api level 16, so for backwards
	// compatibility sake...
	public void setAdjustViewBounds(boolean adjustViewBounds) {
		super.setAdjustViewBounds(adjustViewBounds);
		mAdjustViewBounds = adjustViewBounds;
	}

	@Override
	public void setMaxWidth(int maxWidth) {
		super.setMaxWidth(maxWidth);
		mMaxWidth = maxWidth;
	}

	@Override
	public void setMaxHeight(int maxHeight) {
		super.setMaxHeight(maxHeight);
		mMaxHeight = maxHeight;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Drawable drawable = getDrawable();
		boolean finish = false;
		if(mAdjustViewBounds){
			if(drawable != null){		        
				int w = drawable.getIntrinsicWidth();
				int h = drawable.getIntrinsicHeight();			
				
				int pleft = getPaddingLeft();
		        int pright = getPaddingRight();
		        int ptop = getPaddingTop();
		        int pbottom = getPaddingBottom();
		 
				 // Get the max possible width given our constraints
	            int widthSize = resolveAdjustedSize(w + pleft + pright, mMaxWidth, widthMeasureSpec);
	 
	            // Get the max possible height given our constraints
	            int heightSize = resolveAdjustedSize(h + ptop + pbottom, mMaxHeight, heightMeasureSpec);
	            
				float actualAspect = (float)(widthSize - pleft - pright) /
                        (heightSize - ptop - pbottom);
				float desiredAspect = (float) w / (float) h;

				if(desiredAspect != 0.0f && Math.abs(actualAspect - desiredAspect) > 0.0000001){
                    // Try adjusting width to be proportional to height
                    if (actualAspect < desiredAspect && heightSize > h) {
                        int newWidth = (int)(desiredAspect * (heightSize - ptop - pbottom)) +
                                pleft + pright;
                        if (newWidth > widthSize) {
                            widthSize = newWidth;
                            finish = true;
                        }
                    }
                    // Try adjusting height to be proportional to width
                    if (!finish && actualAspect > desiredAspect && widthSize > w) {
                        int newHeight = (int)((widthSize - ptop - pbottom) / desiredAspect) +
                                ptop + pbottom;
                        if (newHeight > heightSize) {
                            heightSize = newHeight;
                        }
                    }
            		if(finish){
            			setMeasuredDimension(widthSize, heightSize);
                    }
				}
			}
		}
		if(!finish){
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	private int resolveAdjustedSize(int desiredSize, int maxSize,
			int measureSpec) {
		int result = desiredSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize =  MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                /* Parent says we can be as big as we want. Just don't be larger
                   than max size imposed on ourselves.
                */
                result = Math.min(desiredSize, maxSize);
                break;
            case MeasureSpec.AT_MOST:
                // Parent says we can be as big as we want, up to specSize. 
                // Don't be larger than specSize, and don't be larger than 
                // the max size imposed on ourselves.
                result = Math.min(Math.min(desiredSize, specSize), maxSize);
                break;
            case MeasureSpec.EXACTLY:
                // No choice. Do what we are told.
                result = specSize;
                break;
        }
        return result;
	}
}
