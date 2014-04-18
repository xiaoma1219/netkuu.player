package com.xm.netkuu.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class AdjustViewPager extends ViewPager {

	public AdjustViewPager(Context context) {
		super(context, null);
	}
	public AdjustViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	    // find the first child view
	    View view = getChildAt(0);
	    if (view != null) {
	        // measure the first child view with the specified measure spec
	        view.measure(widthMeasureSpec, heightMeasureSpec);
	    }

	    int size = getChildCount();
	    for (int i = 1; i < size; i++) {
	        View child = getChildAt(i);
	        if (child.getVisibility() != GONE) {
	            child.measure(widthMeasureSpec, heightMeasureSpec);
	        }
	    }

	    setMeasuredDimension(getMeasuredWidth(), measureHeight(heightMeasureSpec, view));
	}


	/**
	 * Determines the height of this view
	 *
	 * @param measureSpec A measureSpec packed into an int
	 * @param view the base view with already measured height
	 *
	 * @return The height of the view, honoring constraints from measureSpec
	 */
	private int measureHeight(int measureSpec, View view) {
	    int result = 0;
	    int specMode = MeasureSpec.getMode(measureSpec);
	    int specSize = MeasureSpec.getSize(measureSpec);

	    if (specMode == MeasureSpec.EXACTLY) {
	        result = specSize;
	    } else {
	        // set the height from the base view if available
	        if (view != null) {
	            result = view.getMeasuredHeight();
	        }
	        if (specMode == MeasureSpec.AT_MOST) {
	            result = Math.min(result, specSize);
	        }
	    }
	    return result;
	}

}
