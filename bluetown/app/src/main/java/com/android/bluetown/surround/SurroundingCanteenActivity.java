package com.android.bluetown.surround;

import io.rong.imkit.RongIM;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.CanteenViewPagerAdapter;
import com.android.bluetown.adapter.CanteensAdapter;
import com.android.bluetown.adapter.HotMerchantsListAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.CanteenBean;
import com.android.bluetown.bean.LocationInfo;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.MerchantBean;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.CanteenListResult;
import com.android.bluetown.result.HotMerchantResult;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.DisplayUtils;
import com.android.bluetown.utils.HomeUtils;
import com.android.bluetown.utils.LocationUtil;
import com.android.bluetown.view.NoScrollGridView;
import com.android.bluetown.view.WrapContentHeightViewPager;

/**
 * 周边餐饮
 *
 * @author shenyz
 *
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
@SuppressLint("InflateParams")
public class SurroundingCanteenActivity extends TitleBarActivity implements
		OnClickListener, OnHeaderRefreshListener, OnFooterLoadListener,
		OnItemClickListener, OnPageChangeListener {
	// 查看食堂左右滑动的ViewPager
	private WrapContentHeightViewPager mViewPager;
	// 食堂类型的点的布局
	private ViewGroup mPointViewGroup;
	// 美食分类9块的GridView
	private NoScrollGridView mGridView;
	private ListView mListView;
	private LayoutInflater mInflater;
	private AbPullToRefreshView mPullToRefreshView;
	private HotMerchantsListAdapter mAdapter;
	// 热门商家的数据集合
	private ArrayList<MerchantBean> list;
	// 食堂类型的数据集合
	private ArrayList<CanteenBean> canteenBeans;
	/**
	 * 装点点的ImageView数组
	 */
	private ImageView[] tips;
	/**
	 * 装GridView集合
	 */
	private ArrayList<NoScrollGridView> mGridViews;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	private String province, city, garden, longitude, latitude;
	private FinalDb db;
	private SharePrefUtils prefUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_surround_canteen);
		BlueTownExitHelper.addActivity(this);
		initUIView();
		getData();
	}

	/**
	 * 初始化UI界面组件
	 */
	private void initUIView() {
		prefUtils = new SharePrefUtils(this);
		db = FinalDb.create(this);
		list = new ArrayList<MerchantBean>();
		canteenBeans = new ArrayList<CanteenBean>();
		mInflater = LayoutInflater.from(this);
		View view = mInflater.inflate(R.layout.ac_surround_canteen_top, null);
		mPointViewGroup = (ViewGroup) view.findViewById(R.id.viewGroup);
		mViewPager = (WrapContentHeightViewPager) view
				.findViewById(R.id.canteenViewPager);
		mViewPager.setVisibility(View.GONE);
		getCanteenData();
		mGridView = (NoScrollGridView) view
				.findViewById(R.id.foodTypesGrivView);
		// 去掉GridView点击时默认的黄色
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		// 设置适配器
		HomeUtils.setModelsLAdapter(this, mGridView, "canteen");
		// 设置item点击事件
		mGridView.setOnItemClickListener(mainModeOnItemClick);
		mListView = (ListView) findViewById(R.id.mHotMerchantList);
//		mListView.addHeaderView(view);
		mListView.setAdapter(null);
		mListView.setOnItemClickListener(this);
		mPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterLoadListener(this);
	}

	/**
	 * 获取食堂的数据
	 *
	 * @param province
	 *            :商家省（ 传实值）(必填)
	 * @param city
	 *            ：市 （传实值）(必填)
	 * @param garden
	 *            ：园区（传实值）必填
	 */
	private void getCanteenData() {
		initParamsInfo();
		params.put("province", province);
		params.put("city", city);
		params.put("garden", garden);
		httpInstance.post(Constant.HOST_URL + Constant.Interface.CANTEEN_LIST,
				params, new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						CanteenListResult result = (CanteenListResult) AbJsonUtil
								.fromJson(s, CanteenListResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							canteenBeans = (ArrayList<CanteenBean>) result
									.getData();
							setCanteenData();
						}

					}
					@Override
					public void onFailure(int arg0, String arg1, Throwable arg2) {
						// TODO Auto-generated method stub
						mPullToRefreshView.onFooterLoadFinish();
						mPullToRefreshView.onHeaderRefreshFinish();
					}
				});


	}

	/**
	 * 初始化请求参数信息
	 */
	private void initParamsInfo() {
		List<LocationInfo> infos = db.findAll(LocationInfo.class);
		LocationInfo info = null;
		if (infos != null && infos.size() != 0) {
			info = infos.get(0);
			if (info != null) {
				city = info.getCity();
				province = info.getProvince();
			} else {
				db.deleteAll(LocationInfo.class);
			}
		}
		// 定位失败
		if (TextUtils.isEmpty(city)) {
			// 默认显示当前园区的省市
			city = prefUtils.getString(SharePrefUtils.CITY, "");
			province = prefUtils.getString(SharePrefUtils.PROVINCE, "");
			// 如果未定位成功，则继续定位
			LocationUtil.getInstance(getApplicationContext()).startLoc();
		}
		List<MemberUser> users = db.findAll(MemberUser.class);
		MemberUser user = null;
		if (users != null && users.size() != 0) {
			user = users.get(0);
			if (user != null) {
				garden = user.getHotRegion();
			}
		}

		if (TextUtils.isEmpty(garden)) {
			// 默认显示当前园区名称
			garden = prefUtils.getString(SharePrefUtils.GARDEN, "");

		}

	}

	/**
	 * 设置食堂信息
	 */
	private void setCanteenData() {
		if (mPointViewGroup != null) {
			mPointViewGroup.removeAllViews();
		}
		// 查找布局文件用LayoutInflater.inflate
		LayoutInflater inflater = getLayoutInflater();
		if (canteenBeans != null && canteenBeans.size() > 0) {
			int pageCount = (canteenBeans.size()) / 3;
			int remind = (canteenBeans.size()) % 3;
			if (remind != 0) {
				pageCount = pageCount + 1;
			}
			// 将点点加入到ViewGroup中
			tips = new ImageView[pageCount];
			for (int i = 0; i < tips.length; i++) {
				ImageView imageView = new ImageView(this);
				imageView.setLayoutParams(new LayoutParams(10, 10));
				tips[i] = imageView;
				if (i == 0) {
					tips[i].setBackgroundResource(R.drawable.slide_hide_dian);
				} else {
					tips[i].setBackgroundResource(R.drawable.slide_display_dian);
				}
//				LayoutParams layoutParams = new LayoutParams(
//						new LayoutParams(LayoutParams.WRAP_CONTENT,
//								LayoutParams.WRAP_CONTENT));
//				layoutParams.leftMargin = 5;
//				layoutParams.rightMargin = 5;
				mPointViewGroup.addView(imageView);
			}
			// 将view装入数组
			mGridViews = new ArrayList<NoScrollGridView>();
			if (mGridViews != null) {
				mGridViews.clear();
			}
			int start = 0;
			if (remind == 0) {
				for (int i = 0; i < pageCount; i++) {
					NoScrollGridView view1 = (NoScrollGridView) inflater
							.inflate(R.layout.item_canteen_gridview, null);
					List<CanteenBean> itemCanteens = canteenBeans.subList(
							start, start + 3);
					CanteensAdapter adapter = new CanteensAdapter(
							SurroundingCanteenActivity.this, itemCanteens);
					view1.setAdapter(adapter);
					// 去掉GridView点击时默认的黄色
					view1.setSelector(new ColorDrawable(Color.TRANSPARENT));
					mGridViews.add(view1);
					start = start + 3;
					view1.setOnItemClickListener(canteenModeOnItemClick);
				}
			}else {
				for (int i = 0; i < pageCount; i++) {
					if (i < pageCount - 1) {
						NoScrollGridView view1 = (NoScrollGridView) inflater
								.inflate(R.layout.item_canteen_gridview, null);
						List<CanteenBean> itemCanteens = canteenBeans.subList(
								start, start + 3);
						CanteensAdapter adapter = new CanteensAdapter(
								SurroundingCanteenActivity.this, itemCanteens);
						view1.setAdapter(adapter);
						// 去掉GridView点击时默认的黄色
						view1.setSelector(new ColorDrawable(Color.TRANSPARENT));
						mGridViews.add(view1);
						start = start + 3;
						view1.setOnItemClickListener(canteenModeOnItemClick);
					} else {
						// 最后一页
						NoScrollGridView view1 = (NoScrollGridView) inflater
								.inflate(R.layout.item_canteen_gridview, null);
						List<CanteenBean> itemCanteens = canteenBeans.subList(
								start, start + remind);
						CanteensAdapter adapter = new CanteensAdapter(
								SurroundingCanteenActivity.this, itemCanteens);
						view1.setAdapter(adapter);
						// 去掉GridView点击时默认的黄色
						view1.setSelector(new ColorDrawable(Color.TRANSPARENT));
						mGridViews.add(view1);
						view1.setOnItemClickListener(canteenModeOnItemClick);
					}

				}

			}

			// 设置Adapter
			mViewPager
					.setAdapter(new CanteenViewPagerAdapter(this, mGridViews));
			// 设置监听，主要是设置点点的背景
			mViewPager.setOnPageChangeListener(this);
			mViewPager.setCurrentItem(0);
			setImageBackground(0);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.rightTextLayout:
				String searchValue = mClearEditText.getText().toString();
				Intent intent = new Intent();
				intent.setClass(SurroundingCanteenActivity.this,
						FoodSearchActivity.class);
				intent.putExtra("searchValue", searchValue);
				startActivity(intent);
				break;
			case R.id.filter_edit:
				mClearEditText.setFocusable(true);
				mClearEditText.setFocusableInTouchMode(true);
				break;
			default:
				break;
		}

	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		backImageLayout.setVisibility(View.GONE);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, 1);
		params.leftMargin = DisplayUtils.dip2px(this, 15);
		params.gravity = Gravity.CENTER_VERTICAL;
		mClearEditText.setLayoutParams(params);
		setCustomSearchView(R.string.canteen_search_hint);
		mClearEditText.setOnClickListener(this);
		setRighTextView(R.string.search);
		righTextLayout.setOnClickListener(this);
		setTitleLayoutBgRes(R.color.title_bg_blue);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		// 退出应用的时候，必须调用我们的disconnect()方法，而不是logout()。这样退出后我们这边才会启动push进程。
		RongIM.getInstance().disconnect();
		LocationUtil.getInstance(getApplicationContext()).stopLoc();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	/**
	 * 初始化请求参数
	 */
	private void initParams() {
		List<LocationInfo> infos = db.findAll(LocationInfo.class);
		if (infos != null && infos.size() != 0) {
			LocationInfo info = infos.get(0);
			if (info != null) {
				latitude = info.getLatitude() + "";
				longitude = info.getLongitude() + "";
				city = info.getCity();
				province = info.getProvince();
			}
		}
	}

	/**
	 * @param longitude
	 *            ：经度(必填)
	 * @param latitude
	 *            ：纬度(必填)
	 * @param province
	 *            :商家省（ 传实值）(必填)
	 * @param city
	 *            ：市 （传实值）(必填)
	 */
	private void getData() {
		initParams();
		params.put("longitude", longitude);
		params.put("latitude", latitude);
		params.put("province", province);
		params.put("city", city);
		params.put("garden", prefUtils.getString(SharePrefUtils.GARDEN_ID, ""));
		params.put("page", page + "");
		params.put("rows", "10");
		httpInstance.post(Constant.HOST_URL
						+ Constant.Interface.HOT_MERCHANT_LIST, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						HotMerchantResult result = (HotMerchantResult) AbJsonUtil
								.fromJson(s, HotMerchantResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							mPullToRefreshView.onFooterLoadFinish();
							mPullToRefreshView.onHeaderRefreshFinish();
							Toast.makeText(SurroundingCanteenActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();
						} else {
							mPullToRefreshView.onFooterLoadFinish();
							mPullToRefreshView.onHeaderRefreshFinish();
						}

					}
					@Override
					public void onFailure(int arg0, String arg1, Throwable arg2) {
						// TODO Auto-generated method stub
						mPullToRefreshView.onFooterLoadFinish();
						mPullToRefreshView.onHeaderRefreshFinish();
					}
				});
	}

	protected void dealResult(HotMerchantResult result) {
		switch (listStatus) {
			case Constant.ListStatus.LOAD:
				list.addAll(result.getData().getRows());
				mPullToRefreshView.onFooterLoadFinish();
				break;
			case Constant.ListStatus.INIT:
				list.clear();
				list.addAll(result.getData().getRows());
				break;
			case Constant.ListStatus.REFRESH:
				list.clear();
				list.addAll(result.getData().getRows());
				mPullToRefreshView.onHeaderRefreshFinish();
				break;
		}
		if (mAdapter == null) {
			mAdapter = new HotMerchantsListAdapter(this, list);
			mListView.setAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onFooterLoad(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		listStatus = Constant.ListStatus.LOAD;
		page++;

		getCanteenData();
		getData();
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		list.clear();
		listStatus = Constant.ListStatus.REFRESH;
		page = 1;
		getCanteenData();
		getData();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		// MerchantBean merchantBean = (MerchantBean) parent
		// .getItemAtPosition(position - 1);
		MerchantBean merchantBean = list.get(position);
		if ( position != -1) {
			Intent intent = new Intent();
			BlueTownApp.DISH_TYPE = "food";
			intent.putExtra("meid", merchantBean.getMeid());
			intent.setClass(SurroundingCanteenActivity.this,
					MerchantDetailsActivity.class);
			startActivity(intent);
		}
	}

	/**
	 * 主要版块的OnItemClickListener
	 */
	private OnItemClickListener mainModeOnItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {
			// TODO Auto-generated method stub
			if (arg2 == 0) {
				// 美食
				Intent intent = new Intent();
				BlueTownApp.DISH_TYPE = "food";
				intent.putExtra("posotion", arg2);
				intent.setClass(SurroundingCanteenActivity.this,
						FoodMerchantActivity.class);
				startActivity(intent);
			} else {
				// 其他美食
				Intent intent = new Intent();
				BlueTownApp.DISH_TYPE = "other";
				intent.putExtra("typeId", arg2 + "");
				intent.setClass(SurroundingCanteenActivity.this,
						OtherMerchantActivity.class);
				startActivity(intent);
			}
		}
	};

	/**
	 * 食堂OnItemClickListener
	 */
	private OnItemClickListener canteenModeOnItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {
			// TODO Auto-generated method stub
			CanteenBean bean = (CanteenBean) arg0.getItemAtPosition(arg2);
			Intent intent = new Intent();
			intent.putExtra("meid", bean.getMeid());
			BlueTownApp.DISH_TYPE = "canteen";
			intent.setClass(SurroundingCanteenActivity.this,
					MerchantDetailsActivity.class);
			startActivity(intent);
		}
	};

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		setImageBackground(arg0 % mGridViews.size());
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	/**
	 * 设置选中的tip的背景
	 *
	 * @param selectItems
	 */
	private void setImageBackground(int selectItems) {
		for (int i = 0; i < tips.length; i++) {
			if (i == selectItems) {
				tips[i].setBackgroundResource(R.drawable.display_dot_p);
			} else {
				tips[i].setBackgroundResource(R.drawable.hide_dot);
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
