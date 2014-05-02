package io.github.emanual.java.app.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
	FragmentManager fm;
	List<Fragment> fragments;
	String[] titles;

	public MainFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments,String[] titles) {
		super(fm);
		this.fm = fm;
		this.fragments = fragments;
		this.titles = titles;
	}

	@Override
	public Fragment getItem(int position) {

		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}

}
