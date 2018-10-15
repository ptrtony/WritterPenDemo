package com.android.bluetown.surround;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.android.bluetown.R;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.CartItem;
import com.android.bluetown.bean.RecommendDish;
import com.android.bluetown.utils.ShareUtils;
import com.android.bluetown.view.PointBannerView;

@SuppressWarnings("unchecked")
public class RecommendDishDetailsActivity extends FragmentActivity {
	// Http请求
	protected AbHttpUtil httpInstance;
	// Http请求参数
	protected AbRequestParams params;
	private ViewPager dishDetailsViewPager;
	private List<RecommendDish> dishes;
	private int dishIndex;
	private Handler handler;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_recommed_dish_details);
		BlueTownExitHelper.addActivity(this);
		initParams();
		initUIView();
	}

	/**
	 * 初始化参数
	 */
	private void initParams() {
		handler = BlueTownApp.getHanler();
		dishIndex = getIntent().getIntExtra("dishIndex", 0);
		dishes = (List<RecommendDish>) getIntent().getSerializableExtra(
				"dishList");
		initHttpRequest();
	}

	/**
	 * 初始化界面组件
	 */
	private void initUIView() {
		dishDetailsViewPager = (ViewPager) findViewById(R.id.dishDetailsViewPager);
		dishDetailsViewPager.setAdapter(new MyViewPagerAdapter(this));
		dishDetailsViewPager.setOnPageChangeListener(new PagerListener());
		dishDetailsViewPager.setCurrentItem(dishIndex);
	}

	private void initHttpRequest() {
		// TODO Auto-generated method stub
		if (httpInstance == null) {
			httpInstance = AbHttpUtil.getInstance(this);
			httpInstance.setEasySSLEnabled(true);
		}

		if (params == null) {
			params = new AbRequestParams();
		}
	}

	class MyViewPagerAdapter extends PagerAdapter {
		private LayoutInflater inflater;

		public MyViewPagerAdapter(Context montext) {
			inflater = LayoutInflater.from(montext);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dishes.size();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.support.v4.view.PagerAdapter#instantiateItem(android.view
		 * .View, int)
		 */
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			View view = inflater.inflate(R.layout.dish_detail_item, container,
					false);
			PointBannerView bannerView = (PointBannerView) view
					.findViewById(R.id.item_dish_banner_view);
			final ImageView closeView = (ImageView) view
					.findViewById(R.id.item_dish_banner_close);
			final TextView shareView = (TextView) view
					.findViewById(R.id.item_tv_photo_share);
			final TextView dishName = (TextView) view
					.findViewById(R.id.item_tv_content_name);
			final TextView price = (TextView) view
					.findViewById(R.id.item_tv_guoshi_price);
			final TextView favPrice = (TextView) view
					.findViewById(R.id.item_tv_content_price);
			final Button comeOneDish = (Button) view
					.findViewById(R.id.item_tv_add_one);
			final ImageView minus = (ImageView) view
					.findViewById(R.id.item_iv_minus_food);
			final ImageView add = (ImageView) view
					.findViewById(R.id.item_iv_add_food);
			final TextView des = (TextView) view
					.findViewById(R.id.item_tv_dish_des);
			final TextView foodCountV = (TextView) view
					.findViewById(R.id.item_tv_content_num);
			final RelativeLayout opLayout = (RelativeLayout) view
					.findViewById(R.id.item_rl_operation_layout);
			final RecommendDish item = dishes.get(position);
			OnClickListener clickListener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// 选择的菜的数量
					String strCount = foodCountV.getText().toString();
					int dishCount = Integer.parseInt(strCount);
					Message message = handler.obtainMessage();
					switch (v.getId()) {
					case R.id.item_dish_banner_close:
						finish();
						break;
					case R.id.item_tv_add_one:
						comeOneDish.setVisibility(View.GONE);
						opLayout.setVisibility(View.VISIBLE);
						foodCountV.setText("1");
						message.arg2 = dishIndex;
						message.what = CartItem.ADD_OPERARION;
						handler.sendMessage(message);
						break;
					case R.id.item_iv_add_food:
						foodCountV.setText((dishCount + 1) + "");
						message.arg2 = dishIndex;
						message.what = CartItem.ADD_OPERARION;
						handler.sendMessage(message);
						break;
					case R.id.item_iv_minus_food:
						if (dishCount <= 1) {
							if (dishCount == 0) {
								break;
							}
							foodCountV.setText("0");
							comeOneDish.setVisibility(View.VISIBLE);
							opLayout.setVisibility(View.GONE);
						} else {
							foodCountV.setText((dishCount - 1) + "");
						}
						message.arg2 = dishIndex;
						message.what = CartItem.MINUS_OPERARION;
						handler.sendMessage(message);
						break;
					case R.id.item_tv_photo_share:
						// 分享
						ShareUtils.showShare(RecommendDishDetailsActivity.this,
								BlueTownApp.SHARE_TITLE,
								BlueTownApp.SHARE_CONTENT,
								BlueTownApp.SHARE_IMAGE);
						break;

					default:
						break;
					}
				}
			};
			closeView.setOnClickListener(clickListener);
			comeOneDish.setOnClickListener(clickListener);
			shareView.setOnClickListener(clickListener);
			minus.setOnClickListener(clickListener);
			add.setOnClickListener(clickListener);
			container.addView(view, 0);
			if (item.getDisImgList() != null && item.getDisImgList().size() > 0) {
				// 设置图片集的数据
				bannerView.setImageUrls(item.getDisImgList());
			} else {
				List<String> list = new ArrayList<>();
				list.add(item.getDisImg());
				bannerView.setImageUrls(list);
			}
			foodCountV.setText("0");
			comeOneDish.setVisibility(View.VISIBLE);
			opLayout.setVisibility(View.GONE);
			dishName.setText(item.getDishesName());
			price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			price.setText("￥" + item.getPrice());
			favPrice.setText("￥" + item.getFavorablePrice());
			des.setText(item.getMenuContent());
			BlueTownApp.SHARE_TITLE = item.getDishesName();
			BlueTownApp.SHARE_CONTENT = "这道菜很不错，推荐给你！";
			BlueTownApp.SHARE_IMAGE = item.getDisImg();
			return view;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.support.v4.view.PagerAdapter#destroyItem(android.view.View,
		 * int, java.lang.Object)
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0.equals(arg1);
		}

	}

	class PagerListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			dishIndex = arg0;

		}

	}

}
