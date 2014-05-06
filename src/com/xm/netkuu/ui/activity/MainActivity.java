package com.xm.netkuu.ui.activity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.xm.netkuu.data.UrlData;
import com.xm.netkuu.player.R;
import com.xm.netkuu.ui.fragment.ChannelFragment;
import com.xm.netkuu.ui.fragment.HomeFragment;
import com.xm.netkuu.widget.NavigationDrawerView;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ExpandableListView;

public class MainActivity extends SherlockFragmentActivity {

	private SherlockActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
	private String mTitle;
	private String mDrawerTitle;
	private SherlockFragment mCurrentFrame;
	private int mCurrentNavigation = 0;
	private int mCurrentNavigationChild;
	private ActionBar mActionBar;
	private NavigationDrawerView mMenuNavigation ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mDrawerTitle = getString(R.string.menu_title);
		mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		mActionBar = getSupportActionBar();
		mMenuNavigation = (NavigationDrawerView)findViewById(R.id.navigation);
		mMenuNavigation.setOnGroupClickListener(new NavigationGroupClickListener());
		mMenuNavigation.setOnChildClickListener(new NavigationGroupChildClickListener());
		
		mDrawerToggle = new SherlockActionBarDrawerToggle(this, mDrawerLayout, 
				R.drawable.ic_drawer, R.string.text, R.string.text);
		mDrawerLayout.setDrawerListener(mDrawerToggle);		
		mActionBar.setDisplayHomeAsUpEnabled(true);  
		mActionBar.setHomeButtonEnabled(true);
		mMenuNavigation.setSelectedGroup(NavigationDrawerView.NAVIGATION_HOME);
		switchContent(new HomeFragment());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		SearchView view = (SearchView)menu.findItem(R.id.menu_search).getActionView();
		view.setOnQueryTextListener(new OnQueryTextListener(){
			@Override
			public boolean onQueryTextSubmit(String query) {
				if(query.length() > 0){
					Intent intent = new Intent(MainActivity.this, SearchActivity.class);
					intent.putExtra("key", query);
					startActivity(intent);
				}
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}			
		});
		return true;
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        return super.onOptionsItemSelected(item);
    }    

    
    private void switchContent(SherlockFragment fragment){
    	if(!fragment.equals(mCurrentFrame)){
	    	mCurrentFrame = fragment;
			getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.content_frame, fragment)
				.commit();
    	}
    }    
    
    private class NavigationGroupChildClickListener implements ExpandableListView.OnChildClickListener{
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			mCurrentNavigation = (int) mMenuNavigation.getGroupId(groupPosition);
			if(id != mCurrentNavigationChild){
				if(mCurrentNavigation == NavigationDrawerView.NAVIGATION_CHANNEL){
					mCurrentNavigationChild = (int)id;
					switch(mCurrentNavigationChild){
		        	case UrlData.CHANNEL_OPEN_CLASS:
		        	case UrlData.CHANNEL_DOCUMENTARY:
		        	case UrlData.CHANNEL_LECTURES:
		        	case UrlData.CHANNEL_MOVIE:
		        	case UrlData.CHANNEL_TV:
		        	case UrlData.CHANNEL_VARIETY:
		        	case UrlData.CHANNEL_CARTOON:
		        	}
					ChannelFragment frame = new ChannelFragment();
					Bundle args = new Bundle();
					args.putInt("channel", mCurrentNavigationChild);
					frame.setArguments(args);
	        		switchContent(frame);

		            mDrawerLayout.closeDrawer(GravityCompat.START);
		            return true;
				}
			}
			return false;
		}
    	
    }
    
    private class NavigationGroupClickListener implements ExpandableListView.OnGroupClickListener {
		@Override
		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {
			int navigation = (int)mMenuNavigation.getGroupId(groupPosition);
			if(navigation != mCurrentNavigation){
				mCurrentNavigation = navigation;
				switch(mCurrentNavigation){
				case NavigationDrawerView.NAVIGATION_HOME:
					switchContent(new HomeFragment());
					break;
				case NavigationDrawerView.NAVIGATION_CHANNEL:
					break;
				case NavigationDrawerView.NAVIGATION_LIBRARY:
					break;
				case NavigationDrawerView.NAVIGATION_DOWNLOAD:
					startActivity(new Intent(MainActivity.this, DownloadActivity.class));
					break;
				case NavigationDrawerView.NAVIGATION_HISTORY:
					break;
				case NavigationDrawerView.NAVIGATION_OPTIONS:
					startActivity(new Intent(MainActivity.this, SettingsActivity.class));
					break;
				case NavigationDrawerView.NAVIGATION_QUIT:
					finish();
					break;
				}
            mDrawerLayout.closeDrawer(GravityCompat.START);
			}
			return true;
		}
    }
    
    private class SherlockActionBarDrawerToggle extends ActionBarDrawerToggle {
        public SherlockActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout,
                int drawerImageRes, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, drawerImageRes, openDrawerContentDescRes, closeDrawerContentDescRes);
            mDrawerLayout = drawerLayout;
        }
        
        public void onDrawerClosed(View view) {
            mActionBar.setTitle(mTitle);
            supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
        }

        public void onDrawerOpened(View drawerView) {
        	mActionBar.setTitle(mDrawerTitle);
            supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
        }

        public boolean onOptionsItemSelected(MenuItem item) {
            if (item != null && item.getItemId() == android.R.id.home && isDrawerIndicatorEnabled()) {
                if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
            }
            return false;
        }
    }
}
