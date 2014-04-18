package com.xm.netkuu.activity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.xm.netkuu.player.R;
import com.xm.netkuu.ui.frame.HomePageFragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends SherlockFragmentActivity {

	private SherlockActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
	private ArrayAdapter<String> mAdapter;
	private String mTitle;
	private String mDrawerTitle;
	private SherlockFragment mContent;
	private ActionBar mActionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mDrawerTitle = this.getString(R.string.menu_title);
		mDrawerLayout = (DrawerLayout)this.findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		mActionBar = getSupportActionBar();
		ListView menu = (ListView)this.findViewById(R.id.left_menu);
		menu.setOnItemClickListener(new DrawerItemClickListener());
		mAdapter = new ArrayAdapter<String>(this, R.layout.menu_list_item);
		
		mAdapter.add(this.getString(R.string.menu_home));
		mAdapter.add(this.getString(R.string.menu_history));
		mAdapter.add(this.getString(R.string.menu_download));
		mAdapter.add(this.getString(R.string.menu_config));
		
		menu.setAdapter(mAdapter);
		mDrawerToggle = new SherlockActionBarDrawerToggle(this, this.mDrawerLayout, 
				R.drawable.ic_drawer, R.string.text, R.string.text);
		mDrawerLayout.setDrawerListener(mDrawerToggle);		
		mActionBar.setDisplayHomeAsUpEnabled(true);  
		mActionBar.setHomeButtonEnabled(true);
		selectItem(0);
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
    
    private void selectItem(int position) {
        String item = this.mAdapter.getItem(position);
        switchContent(item);
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
    }
    

    private void switchContent(String name, Bundle args){
        mTitle = name;
        this.mActionBar.setTitle(mTitle);
        SherlockFragment frame = null;
    	if(this.getString(R.string.menu_home).equals(name)){
    		frame = new HomePageFragment();
        }else if(this.getString(R.string.menu_history).equals(name)){
        	
        }else if(this.getString(R.string.menu_download).equals(name)){
        	startActivity(new Intent(MainActivity.this, DownloadActivity.class));
        }else if(this.getString(R.string.menu_config).equals(name)){
        	
        }
    	if(frame != null){
    		if(args != null){
    			frame.setArguments(args);
    		}
    		switchContent(frame);
    	}
    }
    
    private void switchContent(String name){
    	switchContent(name, null);
    }
    
    private void switchContent(SherlockFragment fragment){
    	if(!fragment.equals(mContent)){
	    	mContent = fragment;
			getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.content_frame, fragment)
				.commit();
    	}
    }    
    
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
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
