package com.android.bluetown.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;

import com.android.bluetown.bean.TypeBean;

/**
 * Created by wangzj01 on 2015/5/11.
 */
public class TabPageIndicatorAdapter extends FragmentPagerAdapter {
	private List<Fragment> mFragmentList;
	private ArrayList<TypeBean> TITLES = new ArrayList<TypeBean>();

	public TabPageIndicatorAdapter(FragmentManager fm,
			List<Fragment> fragments, ArrayList<TypeBean> TITLES) {
		super(fm);
		this.mFragmentList = fragments;
		this.TITLES = TITLES;
	}

	@Override
	public Fragment getItem(int position) {
		return (mFragmentList == null || mFragmentList.size() < TITLES.size()) ? null
				: mFragmentList.get(position);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		String typeName=TITLES.get(position % TITLES.size()).getTypeName();
		if (!TextUtils.isEmpty(typeName)) {
			if (typeName.equals("全部")) {
				typeName="活动中心";
			}
		}
		return typeName;
	}

	@Override
	public int getCount() {
		return TITLES.size();
	}
}
