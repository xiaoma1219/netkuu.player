package com.xm.netkuu.view;

import java.util.Stack;

import android.util.SparseArray;
import android.view.View;

public class ViewPool{
	private SparseArray<Stack<View>> mStack;
	
	public ViewPool(){
		mStack = new SparseArray<Stack<View>>();
	}
	
	public View get(Integer type){
		if(mStack.size() > 0){
			Stack<View> s = mStack.get(type);
			if(s != null && s.size() > 0)
				return s.pop();
		}
		return null;
	}
	
	public void put(Integer type, View view){
		Stack<View> s = mStack.get(type);
		if(s == null)
			s = new Stack<View>();
		s.push(view);
	}
	
	public void clear(){
		mStack.clear();
	}
}
