package io.github.emanual.app.ui;

import io.github.emanual.app.R;
import io.github.emanual.app.adapter.MainFragmentPagerAdapter;
import io.github.emanual.app.fragment.LearnClub;
import io.github.emanual.app.fragment.NewFeeds;

import java.util.ArrayList;
import java.util.List;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;

public class Main extends BaseActivity implements TabListener,OnPageChangeListener {
	ActionBar mActionBar;
	ViewPager viewPager;
	List<Fragment> fragments = new ArrayList<Fragment>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_main);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initLayout() {
		viewPager =(ViewPager)_getView(R.id.viewpager);
		
		mActionBar = getActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mActionBar.addTab(mActionBar.newTab().setText("新鲜事").setTabListener(this));
		mActionBar.addTab(mActionBar.newTab().setText("学习库").setTabListener(this));
		
		viewPager =(ViewPager)_getView(R.id.viewpager);
		fragments.add(new NewFeeds());
		fragments.add(new LearnClub());
		viewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager(), fragments));
		viewPager.setOnPageChangeListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_share:
			toast("share");
			return true;
		case R.id.action_settings:
			startActivity(new Intent(getContext(), Setting.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		 
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		 
		
	}

	@Override
	public void onPageScrollStateChanged(int state) {

		
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		
	}

	@Override
	public void onPageSelected(int position) {
		mActionBar.setSelectedNavigationItem(position);
	}

}
