package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.GridView;

import com.android.bluetown.view.NoScrollGridView;

public class CanteenViewPagerAdapter extends PagerAdapter {
	private Context context;
	private List<NoScrollGridView> mGridViews;

	public CanteenViewPagerAdapter() {
		// TODO Auto-generated constructor stub
	}

	public CanteenViewPagerAdapter(Context context,
			List<NoScrollGridView> mGridViews) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mGridViews = mGridViews;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		

	}

	/**
	 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
	 */
	@Override
	public Object instantiateItem(View container, int position) {
		position = position % mGridViews.size();
		if (position < 0) {
			position = mGridViews.size() + position;
		}
		GridView mGridView = mGridViews.get(position);
		//如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。  
		ViewParent vp = mGridView.getParent();
		if (vp != null) {
			ViewGroup parent = (ViewGroup) vp;
			parent.removeView(mGridView);
		}
		((ViewPager) container).addView(mGridView);
		return mGridView;
	}

}
