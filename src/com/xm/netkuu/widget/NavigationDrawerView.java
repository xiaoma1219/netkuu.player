package com.xm.netkuu.widget;

import com.xm.netkuu.player.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class NavigationDrawerView extends ExpandableListView{

	public static final int NAVIGATION_HOME = 0;
	public static final int NAVIGATION_CHANNEL = NAVIGATION_HOME + 1;
	public static final int NAVIGATION_LIBRARY = NAVIGATION_CHANNEL + 1;
	public static final int NAVIGATION_DOWNLOAD = NAVIGATION_LIBRARY + 1;
	public static final int NAVIGATION_HISTORY = NAVIGATION_DOWNLOAD + 1;
	public static final int NAVIGATION_OPTIONS = NAVIGATION_HISTORY + 1;
	public static final int NAVIGATION_QUIT = NAVIGATION_OPTIONS + 1;
	
	private Context mContext;
	private MenuDrawAdapter mAdapter;
	private OnGroupClickListener mOnGroupClickListener;
	private OnChildClickListener mOnChildClickListener;
	
	public NavigationDrawerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		makeAdapter();
	}
	
	public NavigationDrawerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		makeAdapter();
	}
	
	public NavigationDrawerView(Context context) {
		super(context);
		mContext = context;
		makeAdapter();
	}
	
	public long getGroupId(int groupPosition){
		return mAdapter.getGroupId(groupPosition);
	}
	
	public void setSelectedGroup(int groupPosition){
		if(mAdapter != null){
			mAdapter.setSelectedPosition(groupPosition, -1);
		}
	}
	
	public void setSelectedChild(int groupPosition, int childPosition){
		if(mAdapter != null){
			mAdapter.setSelectedPosition(groupPosition, childPosition);
		}
	}
	
	public void setSelectedPosition(int groupPosition, int childPosition){
		if(mAdapter != null){
			mAdapter.setSelectedPosition(groupPosition, childPosition);
		}
	}
	
	@SuppressWarnings("unused")
	private void setSelectedItemView(View view, int groupPosition, int childPosition){
		if(view != null && mAdapter != null && mAdapter.isItemSelectable(groupPosition, childPosition)){
			View lastview = getSelectedItemView();
			if(lastview != null){
				lastview.setSelected(false);
			}
			view.setSelected(true);
			mAdapter.setSelectedPosition(groupPosition, childPosition);
		}
	}
	
	public View getSelectedItemView(){
		if(mAdapter != null)
			return getChildAt(mAdapter.getSelectedPosition());
		return null;
	}
	
	@Override
	public void setAdapter(ExpandableListAdapter adapter){
		this.mAdapter = (MenuDrawAdapter)adapter;
		super.setAdapter(adapter);
	}
	
	private void  makeAdapter(){
		setGroupIndicator(null);
		setAdapter(new MenuDrawAdapter(mContext));
		for (int i = 0; i < mAdapter.getGroupCount(); i++) {  
            expandGroup(i);  
        }

		super.setOnGroupClickListener(new OnGroupClickListener() {			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				//setSelectedItemView(v, groupPosition, -1);
				if(mOnGroupClickListener != null){
					return mOnGroupClickListener.onGroupClick(parent, v, groupPosition, id);
				}
				return false;
			}
		});
		super.setOnChildClickListener(new OnChildClickListener(){

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				//setSelectedItemView(v, groupPosition, childPosition);
				if(mOnChildClickListener != null){
					return mOnChildClickListener.onChildClick(parent, v, groupPosition, childPosition, id);
				}
				return false;
			}
		});
	}
	
	@Override
	public void setOnGroupClickListener(OnGroupClickListener l){
		mOnGroupClickListener = l;
	}
	
	@Override
	public void setOnChildClickListener(OnChildClickListener l){
		mOnChildClickListener = l;
	}
	
	public static class MenuDrawAdapter extends BaseExpandableListAdapter{
		
		private int mSelectedPosition = -1;
		private String[] mGroupNavigation = null;
		private String[] mChannelTitle = null; // = mContext.getResources().getStringArray(R.array.navigation);
		private int[] mChannelId = null; //= mContext.getResources().getIntArray(R.array.navigation_id);
		private Context mContext;
		
		public MenuDrawAdapter(Context context){
			mContext = context;
			mGroupNavigation = mContext.getResources().getStringArray(R.array.group_navigation);
			mChannelTitle = mContext.getResources().getStringArray(R.array.channel);
			mChannelId = mContext.getResources().getIntArray(R.array.channel_id);
		}

		@Override
		public int getGroupCount() {
			return mGroupNavigation == null ? 0:  mGroupNavigation.length;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return groupPosition == NAVIGATION_CHANNEL && mChannelTitle != null ? mChannelTitle.length : 0 ;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return mGroupNavigation[groupPosition];
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return groupPosition == NAVIGATION_CHANNEL ? mChannelTitle[childPosition] : null;
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return groupPosition == NAVIGATION_CHANNEL ? mChannelId[childPosition] : childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View view, ViewGroup parent) {
			if(view == null){
				view = new TextView(mContext);
				view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				int hp = mContext.getResources().getDimensionPixelSize(R.dimen.navigation_item_horizontal_padding);
				int vp = mContext.getResources().getDimensionPixelSize(R.dimen.navigation_item_vertical_padding);
				view.setPadding(hp, vp, hp, vp);
			}
			int groupId = (int)getGroupId(groupPosition);
			if(groupId == NAVIGATION_CHANNEL){
				view.setEnabled(false);
			}
			else{
				view.setEnabled(true);
			}
			switch(groupId){
			case NAVIGATION_OPTIONS:
			case NAVIGATION_QUIT:
				view.setBackgroundResource(R.drawable.navigation_item_dark_selector);
				((TextView)view).setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.navigation_item_text_small));
				break;
			default:
				((TextView)view).setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.navigation_item_text_size));
				view.setBackgroundResource(R.drawable.navigation_item_light_stroked_selector);
			}
			((TextView)view).setText(getGroup(groupPosition).toString());
			setViewSelected(groupPosition, view);
			return view;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View view, ViewGroup parent) {
			if(groupPosition == NAVIGATION_CHANNEL){
				if(view == null){
					view = new TextView(mContext);
					view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
					int hp = mContext.getResources().getDimensionPixelSize(R.dimen.navigation_child_item_horizontal_padding);
					int vp = mContext.getResources().getDimensionPixelSize(R.dimen.navigation_child_item_vertical_padding);
					view.setPadding(hp, vp, hp, vp);
					view.setBackgroundResource(R.drawable.navigation_item_light_selector);
					((TextView)view).setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.navigation_item_text_small));
				}
				((TextView)view).setText(getChild(groupPosition, childPosition).toString());
				setViewSelected(groupPosition, childPosition, view);
			}
			return view;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
		
		public boolean isGroupSelectable(int groupPosition){
			int groupId = (int) this.getGroupId(groupPosition);
			return groupId == NAVIGATION_HOME;
		}
		
		public boolean isItemSelectable(int groupPosition, int childPosition){
			if(childPosition == -1)
				return isGroupSelectable(groupPosition);
			else 
				return isChildSelectable(groupPosition, childPosition);
		}
		
		public void setSelectedPosition(int groupPosition, int childPosition){
			if(isItemSelectable(groupPosition, childPosition)){
				mSelectedPosition = getItemAbsolutePosition(groupPosition, childPosition);
			}
		}
		
		private void setViewSelected(int groupPosition, int childPosition, View view){
			view.setSelected(getItemAbsolutePosition(groupPosition, childPosition) == mSelectedPosition);
		}
		
		private void setViewSelected(int groupPosition, View view){
			System.out.println(getItemAbsolutePosition(groupPosition, -1));
			view.setSelected(getItemAbsolutePosition(groupPosition, -1) == mSelectedPosition);
			
		}
		
		public int getItemAbsolutePosition(int groupPosition, int childPosition){
			int position = -1;
			for(int i = 0; i <= groupPosition; i++){
				position++;
				for(int j = 0; (i < groupPosition && j < getChildrenCount(groupPosition)) || (i == groupPosition && j <= childPosition); j++){
					position++;
				}
			}
			return position;
		}
		
		public int getSelectedPosition(){
			return mSelectedPosition;
		}
	}
}
