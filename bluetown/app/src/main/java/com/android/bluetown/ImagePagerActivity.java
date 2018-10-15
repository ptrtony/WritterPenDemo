package com.android.bluetown;

import java.util.ArrayList;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.utils.StatusBarUtils;
import com.android.bluetown.view.HackyViewPager;

public class ImagePagerActivity extends FragmentActivity {
	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index";
	public static final String EXTRA_IMAGE_URLS = "image_urls";
	private HackyViewPager mPager;
	private int pagerPosition;
	private TextView indicator;
	// 是否显示下标
	private int indicatorIndex;
	private String flag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.image_detail_pager);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			StatusBarUtils.getInstance().initStatusBar(true, this);
		}
		BlueTownExitHelper.getInstance().addActivity(this);
		pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
		ArrayList urls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);
		try {
			indicatorIndex = getIntent().getIntExtra("indicator", 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mPager = (HackyViewPager) findViewById(R.id.pager);
		mPager.setPageMargin(10);
		ImagePagerAdapter mAdapter = new ImagePagerAdapter(
				getSupportFragmentManager(), urls);
		mPager.setAdapter(mAdapter);
		indicator = (TextView) findViewById(R.id.indicator);
		CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
				.getAdapter().getCount());
		indicator.setText(text);
		if (indicatorIndex == 1) {
			indicator.setVisibility(View.INVISIBLE);
		} else {
			indicator.setVisibility(View.VISIBLE);
		}

		// 更新下标
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				CharSequence text = getString(R.string.viewpager_indicator,
						arg0 + 1, mPager.getAdapter().getCount());
				indicator.setText(text);
			}

		});
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		mPager.setCurrentItem(pagerPosition);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public ArrayList fileList;

		public ImagePagerAdapter(FragmentManager fm, ArrayList fileList) {
			super(fm);
			this.fileList = fileList;
		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.size();
		}

		@Override
		public Fragment getItem(int position) {
			if (fileList != null) {
				String url = (String) fileList.get(position);
				if (!TextUtils.isEmpty(url)) {
					return ImageDetailFragment.newInstance(url);
				} else {
					return ImageDetailFragment.newInstance("drawable://"
							+ R.drawable.ic_msg_empty);
				}
			}
			return null;

		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			overridePendingTransition(0, R.anim.slide_down_out);
			return false;
		}
		return false;
	}

}