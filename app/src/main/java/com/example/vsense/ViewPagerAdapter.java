package com.example.vsense;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> list = new ArrayList<Fragment>();

	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
		super(fm);
		this.list =list;
	}
	
	@Override
	public Fragment getItem(int arg0) {
		return (list == null || list.size() == 0 ? null :list.get(arg0));
	}

	@Override
	public int getCount() {
		return list.size();
	}

}
