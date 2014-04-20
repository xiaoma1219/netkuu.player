package com.xm.netkuu.ui.widget;

import com.xm.netkuu.player.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class NavigationDrawer extends ExpandableListView{

	public static final int NAVIGATION_HOME = 0;
	public static final int NAVIGATION_CHANNEL = NAVIGATION_HOME + 1;
	public static final int NAVIGATION_LIBRARY = NAVIGATION_CHANNEL + 1;
	public static final int NAVIGATION_DOWNLOAD = NAVIGATION_LIBRARY + 1;
	public static final int NAVIGATION_HISTORY = NAVIGATION_DOWNLOAD + 1;
	public static final int NAVIGATION_OPTIONS = NAVIGATION_HISTORY + 1;
	public static final int NAVIGATION_QUIT = NAVIGATION_OPTIONS + 1;
	
	private Context mContext;
	private ExpandableListAdapter mAdapter;
	private OnGroupClickListener mOnGroupClickListener;
	//private int mSelectedPosition = -1;
	
	public NavigationDrawer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		makeAdapter();
	}
	
	public NavigationDrawer(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		makeAdapter();
	}
	
	public NavigationDrawer(Context context) {
		super(context);
		mContext = context;
		makeAdapter();
	}
	
	public long getGroupId(int groupPosition){
		return mAdapter.getGroupId(groupPosition);
	}
	
	@Override
	public void setAdapter(ExpandableListAdapter adapter){
		this.mAdapter = adapter;
		super.setAdapter(adapter);
	}
/*	
	@Override
	public boolean setSelectedChild(int groupPosition, int childPosition, boolean shouldExpandGroup){
		super.setSelectedChild(groupPosition, childPosition, shouldExpandGroup);

		setItemSelected(getSelectedView(), true);
		return true;
	}
	
	@Override
	public void setSelectedGroup(int groupPosition){
		super.setSelectedGroup(groupPosition);
		setItemSelected(getSelectedView(), true);
	}
	
	private void setItemSelected(View view, boolean selected){
		System.out.println(view);
		if(view != null){
			System.out.println(view.getClass());
			view.setSelected(selected);
		}
	}
*/
	private void  makeAdapter(){
		setGroupIndicator(null);
		setAdapter(new MenuDrawAdapter(mContext));
		for (int i = 0; i < mAdapter.getGroupCount(); i++) {  
            expandGroup(i);  
        }

		setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				if(mOnGroupClickListener != null){
					mOnGroupClickListener.onGroupClick(parent, v, groupPosition, id);
				}
				return true;
			}
		});
		/* Does not work
		setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(mSelectedPosition >= 0 && mAdapter.getGroupCount() > 0 ){
					View lastView = getChildAt(mSelectedPosition);
					lastView.setSelected(false);
				}
				mSelectedPosition = position;
				View lastView = getChildAt(mSelectedPosition);
				System.out.println(lastView.getClass());
				lastView.setSelected(true);
			}			
		});
		*/
	}
	
	public class MenuDrawAdapter extends BaseExpandableListAdapter{
		
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
			}
			return view;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}
}
