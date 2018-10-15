package com.android.bluetown.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.android.bluetown.LoginActivity;
import com.android.bluetown.MainActivity;
import com.android.bluetown.R;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;

/**
 * 
 * @{# ViewPagerAdapter.java Create on 2013-5-2 下午11:03:39
 * @Desc: 引导页面适配器
 * @Copyright: Copyright(c) 2013
 * @Version 1.0
 * @Author shenyz
 * 
 * 
 */
public class ViewPagerAdapter extends PagerAdapter {

	// 界面列表
	private List<View> views;
	private Activity activity;
	private SharePrefUtils prefUtils;

	public ViewPagerAdapter(List<View> views, Activity activity) {
		this.views = views;
		this.activity = activity;
		prefUtils = new SharePrefUtils(activity);
	}

	// 销毁arg1位置的界面
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(views.get(arg1));
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	// 获得当前界面数
	@Override
	public int getCount() {
		if (views != null) {
			return views.size();
		}
		return 0;
	}

	// 初始化arg1位置的界面
	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(views.get(arg1), 0);
		if (arg1 == views.size() - 1) {
			ImageView mStartWeiboImageButton = (ImageView) arg0
					.findViewById(R.id.enter);
			mStartWeiboImageButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					goHome();
				}

			});
		}
		return views.get(arg1);
	}

	private void goHome() {
		// 首次进入为游客身份
		// 跳转
		Intent intent = new Intent();
		intent.putExtra("userType", Constant.VISITOR);
		intent.setClass(activity, LoginActivity.class);
		activity.startActivity(intent);
		activity.finish();
	}

	// 判断是否由对象生成界面
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}

}
