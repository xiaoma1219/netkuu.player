package com.xm.netkuu.view;

import android.view.View;

public interface ViewObjectFactory<T extends View> {
	public T getView();
}
