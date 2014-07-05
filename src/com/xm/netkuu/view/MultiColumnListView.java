package com.xm.netkuu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class MultiColumnListView extends ListView{
	private static final int DEFAULT_COLUMN_COUNT = 2;
	
	private int mColumnNumber = DEFAULT_COLUMN_COUNT;
	private int mColumnWidth;
	
	public MultiColumnListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}

	public MultiColumnListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public MultiColumnListView(Context context) {
		super(context);
		init(null);
	}
	
	protected void init(AttributeSet attrs){
		
	}
	
	public int getColumnNumber(){
		return mColumnNumber;
	}
	
	public void setColumnNumber(int columnNumber){
		mColumnNumber = columnNumber;
	}

	public int getColumnWidth() {
		return mColumnWidth;
	}

	public void setColumnWidth(int columnWidth) {
		this.mColumnWidth = columnWidth;
	}
	
}
