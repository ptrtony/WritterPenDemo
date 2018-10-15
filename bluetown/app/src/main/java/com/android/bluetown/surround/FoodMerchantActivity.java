package com.android.bluetown.surround;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.MerchantListAdapter;
import com.android.bluetown.adapter.MerchantTypeAdapter;
import com.android.bluetown.bean.LocationInfo;
import com.android.bluetown.bean.Merchant;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.FoodClassResult;
import com.android.bluetown.result.FoodClassResult.FoodClass;
import com.android.bluetown.result.FoodClassResult.FoodSubClass;
import com.android.bluetown.result.FoodMerchantListResult;
import com.android.bluetown.result.WholeCityClassResult;
import com.android.bluetown.result.WholeCityClassResult.WholeCityClass;
import com.android.bluetown.result.WholeCityClassResult.WholeCitySubClass;
import com.android.bluetown.utils.Constant;

/**
 * 美食类商家列表
 * 
 * @author shenyz
 * 
 */
public class FoodMerchantActivity extends TitleBarActivity implements
		OnClickListener, OnItemClickListener, OnHeaderRefreshListener,
		OnFooterLoadListener {
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int rows = 10;
	/** 分页 */
	private AbPullToRefreshView mPullToRefreshView;
	private MerchantListAdapter adapter;
	private MerchantTypeAdapter leftAdapter;
	private MerchantTypeAdapter rightAdapter;
	private ListView mListView;
	private List<Merchant> list;
	/** 全部分类 */
	private RelativeLayout foodType;
	/** 区域分类 */
	private RelativeLayout areaType;
	/** 综合排序 */
	private RelativeLayout sortType;

	/** 全部分类icn */
	private ImageView foodTypeIcn;
	/** 区域分类icn */
	private ImageView areaTypeIcn;
	/** 综合排序icn */
	private ImageView sortTypeIcn;

	/** 综合排序文字 */
	private TextView foodTypeName;
	/** 区域分类文字 */
	private TextView areaTypeName;
	/** 全城分类文字 */
	private TextView sortTypeName;

	private LinearLayout typeLayout;
	private LinearLayout typeLeftLayout;
	private LinearLayout typeRightLayout;
	private ListView TypeLeftView;
	private ListView typeRightView;
	/**
	 * 网络
	 */
	private LinearLayout invalidLayout;
	private LinearLayout noDataLayout;

	private LinearLayout topLayout;
	private FrameLayout centerLayout;
	/** 当前选择美食类 */
	private int lastSelectFoodIndex;
	/** 当前选择城市商圈 */
	private int lastSelectCityIndex;
	/** 当前选择综合分类 */
	private int lastSelectOrderIndex;

	/** 当前选择美食子类 */
	private int lastSelectFoodSubIndex;
	/** 当前选择城市商圈 子类 */
	private int lastSelectCitySubIndex;

	/** 当前选择分类的标志 */
	private String naviFlag = "food";
	private Resources resources;
	private String longitude;
	private String latitude;
	private String typeId;
	private String twoTypeId;
	private String province;
	private String city;
	private String region;
	private String hotBusiness;
	private String order = "0";
	// 搜索的条件
	private String searchKey;
	private FinalDb db;

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBgRes(R.color.title_bg_blue);
		setRightImageView(R.drawable.ic_map);
		rightImageLayout.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_merchant_list);
		initUI();
		getMerchantList();

	}

	/**
	 * 初始化界面
	 */
	public void initUI() {
		// TODO Auto-generated method stub
		try {
			searchKey = getIntent().getStringExtra("search");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db = FinalDb.create(this);
		initParams();
		resources = getResources();
		list = new ArrayList<>();
		mPullToRefreshView = (AbPullToRefreshView) this
				.findViewById(R.id.m_PullRefreshView_salespage);
		mListView = (ListView) this.findViewById(R.id.lv_salespages);
		foodType = (RelativeLayout) this.findViewById(R.id.merchant_food_type);
		areaType = (RelativeLayout) this.findViewById(R.id.merchant_area_type);
		sortType = (RelativeLayout) this.findViewById(R.id.merchant_sort_type);
		foodTypeName = (TextView) findViewById(R.id.food_type_name);
		foodTypeIcn = (ImageView) findViewById(R.id.food_type_icn);
		areaTypeName = (TextView) findViewById(R.id.area_type_name);
		areaTypeIcn = (ImageView) findViewById(R.id.area_type_icn);
		sortTypeName = (TextView) findViewById(R.id.sort_type_name);
		sortTypeIcn = (ImageView) findViewById(R.id.sort_type_icn);
		typeLayout = (LinearLayout) findViewById(R.id.merchant_type_layout);
		TypeLeftView = (ListView) findViewById(R.id.merchant_left_list);
		typeRightView = (ListView) findViewById(R.id.merchant_right_list);
		typeLeftLayout = (LinearLayout) findViewById(R.id.left_type_data);
		typeRightLayout = (LinearLayout) findViewById(R.id.right_type_data);
		topLayout = (LinearLayout) findViewById(R.id.rl_salestop);
		centerLayout = (FrameLayout) findViewById(R.id.fl_merchant_center_layout);
		foodType.setOnClickListener(this);
		areaType.setOnClickListener(this);
		sortType.setOnClickListener(this);
		TypeLeftView.setOnItemClickListener(leftItemClickListener);
		typeRightView.setOnItemClickListener(rightItemClickListener);
		mListView.setOnItemClickListener(this);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterLoadListener(this);
		if (TextUtils.isEmpty(searchKey)) {
			if (!TextUtils.isEmpty(city)) {
				setTitleView(city.substring(0, city.indexOf("市")) + ".商家");
			} else {
				setTitleView("宁波.商家");
			}
		} else {
			setTitleView(R.string.search);
		}

	}

	/**
	 * 初始化请求参数
	 */
		private void initParams() {
		List<LocationInfo> infos = db.findAll(LocationInfo.class);
		if (infos != null && infos.size() != 0) {
			LocationInfo info = infos.get(0);
			if (info!=null) {
				latitude = info.getLatitude()+"";
				longitude = info.getLongitude() + "";
				city = info.getCity();
				province =  info.getProvince();
			}
		}
	}

	/**
	 * 获取美食总分类列表
	 */
	private void getFoodType() {
		// TODO Auto-generated method stub
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.FOOD_CLASS_TYPE, params,
				new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						FoodClassResult result = (FoodClassResult) AbJsonUtil
								.fromJson(s, FoodClassResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							List<FoodClass> foodAllClass = result.getData();
							if (foodAllClass != null && foodAllClass.size() > 0) {
								leftAdapter = new MerchantTypeAdapter(
										FoodMerchantActivity.this,
										foodAllClass, true, "food");
								TypeLeftView.setAdapter(leftAdapter);
								leftAdapter.setDefSelec(lastSelectFoodIndex);
								leftAdapter.notifyDataSetInvalidated();
							}
							// 默认显示第一项的子分类
							List<FoodSubClass> subClasses = foodAllClass.get(
									lastSelectFoodIndex).getMerchantTypeList();
							if (subClasses != null && subClasses.size() > 0) {
								rightAdapter = new MerchantTypeAdapter(
										FoodMerchantActivity.this, subClasses,
										false, "food");
								typeRightView.setAdapter(rightAdapter);
								rightAdapter
										.setDefSelec(lastSelectFoodSubIndex);
								rightAdapter.notifyDataSetChanged();
							}
						}

					}
				});
	}

	/**
	 * 获取美食全城总分类列表( province:商家省（必填） city：市 （必填）)
	 */
	private void getWholeCityType(String province, String city) {
		// TODO Auto-generated method stub
		params.put("province", province);
		params.put("city", city);
		httpInstance.post(
				Constant.HOST_URL + Constant.Interface.FOOD_CITY_TYPE, params,
				new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						WholeCityClassResult result = (WholeCityClassResult) AbJsonUtil
								.fromJson(s, WholeCityClassResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							typeRightView.setBackgroundColor(resources
									.getColor(R.color.canteen_type_seleted_color));
							TypeLeftView.setBackgroundColor(resources
									.getColor(R.color.white));
							foodTypeIcn
									.setImageResource(R.drawable.ic_arrow_more_normal);
							foodTypeName.setTextColor(getResources().getColor(
									R.color.font_gray));
							areaTypeIcn
									.setImageResource(R.drawable.ic_arrow_more_p);
							areaTypeName.setTextColor(getResources().getColor(
									R.color.font_light_red));
							sortTypeIcn
									.setImageResource(R.drawable.ic_arrow_more_normal);
							sortTypeName.setTextColor(getResources().getColor(
									R.color.font_gray));
							typeLayout.setVisibility(View.VISIBLE);
							typeLeftLayout.setVisibility(View.VISIBLE);
							List<WholeCityClass> cityClasses = result.getData();
							if (cityClasses != null && cityClasses.size() > 0) {
								leftAdapter = new MerchantTypeAdapter(
										FoodMerchantActivity.this, cityClasses,
										true, "city");
								TypeLeftView.setAdapter(leftAdapter);
								leftAdapter.setDefSelec(lastSelectCityIndex);
								leftAdapter.notifyDataSetInvalidated();
							}
							// 默认显示第一项的子分类
							List<WholeCitySubClass> subClasses = cityClasses
									.get(lastSelectCityIndex)
									.getHotBusinessList();
							if (subClasses != null && subClasses.size() > 0) {
								rightAdapter = new MerchantTypeAdapter(
										FoodMerchantActivity.this, subClasses,
										false, "city");
								rightAdapter
										.setDefSelec(lastSelectCitySubIndex);
								typeRightView.setAdapter(rightAdapter);
								rightAdapter.notifyDataSetChanged();
							}
						} else {
							foodTypeIcn
									.setImageResource(R.drawable.ic_arrow_more_normal);
							foodTypeName.setTextColor(getResources().getColor(
									R.color.font_gray));
							areaTypeIcn
									.setImageResource(R.drawable.ic_arrow_more_normal);
							areaTypeName.setTextColor(getResources().getColor(
									R.color.font_gray));
							sortTypeIcn
									.setImageResource(R.drawable.ic_arrow_more_normal);
							sortTypeName.setTextColor(getResources().getColor(
									R.color.font_gray));
							typeLayout.setVisibility(View.INVISIBLE);
							typeLeftLayout.setVisibility(View.INVISIBLE);
						}

					}
				});
	}

	/**
	 * 
	 */
	private void getMerchantList() {
		// TODO Auto-generated method stub
		// longitude：经度(必填)
		// latitude：纬度(必填)
		// typeId：一经商家类型id
		// twoTypeId：二级商家类型Id
		// province:商家省（ 传实值）
		// city：市 （传实值）
		// region：区域（传实值）
		// hotBusiness：商家热门区域（传实值）
		// 综合查询：order(0离我最近1好评优先2最新发布3价格最低4价格最高)
		// merchantName 餐厅查询餐厅名称
		params.put("merchantName", searchKey);
		params.put("longitude", longitude);
		params.put("latitude", latitude);
		params.put("typeId", typeId);
		params.put("twoTypeId", twoTypeId);
		params.put("province", province);
		params.put("city", city);
		params.put("region", region);
		params.put("hotBusiness", hotBusiness);
		params.put("order", order);
		params.put("page", page + "");
		params.put("rows", rows + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.FOOD_MERCHANT_LIST, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						FoodMerchantListResult result = (FoodMerchantListResult) AbJsonUtil
								.fromJson(s, FoodMerchantListResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							mPullToRefreshView.onHeaderRefreshFinish();
							mPullToRefreshView.onFooterLoadFinish();
							if (adapter != null) {
								adapter.notifyDataSetChanged();
							}
							Toast.makeText(FoodMerchantActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();
						} else {
							mPullToRefreshView.onHeaderRefreshFinish();
							mPullToRefreshView.onFooterLoadFinish();
							if (adapter != null) {
								adapter.notifyDataSetChanged();
							}

						}

					}
				});
	}

	protected void dealResult(FoodMerchantListResult result) {
		// TODO Auto-generated method stub
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
		if (adapter == null) {
			adapter = new MerchantListAdapter(this, list);
			mListView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.merchant_food_type:
			typeRightView.setBackgroundColor(resources
					.getColor(R.color.canteen_type_seleted_color));
			TypeLeftView.setBackgroundColor(resources.getColor(R.color.white));
			foodTypeIcn.setImageResource(R.drawable.ic_arrow_more_p);
			foodTypeName.setTextColor(getResources().getColor(
					R.color.font_light_red));
			areaTypeIcn.setImageResource(R.drawable.ic_arrow_more_normal);
			areaTypeName.setTextColor(getResources()
					.getColor(R.color.font_gray));
			sortTypeIcn.setImageResource(R.drawable.ic_arrow_more_normal);
			sortTypeName.setTextColor(getResources()
					.getColor(R.color.font_gray));
			typeLayout.setVisibility(View.VISIBLE);
			typeLeftLayout.setVisibility(View.VISIBLE);
			naviFlag = "food";
			getFoodType();
			break;
		case R.id.merchant_area_type:
			naviFlag = "city";
			getWholeCityType(province, city);
			break;
		case R.id.merchant_sort_type:
			foodTypeIcn.setImageResource(R.drawable.ic_arrow_more_normal);
			foodTypeName.setTextColor(getResources()
					.getColor(R.color.font_gray));
			areaTypeIcn.setImageResource(R.drawable.ic_arrow_more_normal);
			areaTypeName.setTextColor(getResources()
					.getColor(R.color.font_gray));
			sortTypeIcn.setImageResource(R.drawable.ic_arrow_more_p);
			sortTypeName.setTextColor(getResources().getColor(
					R.color.font_light_red));
			typeRightView.setBackgroundColor(resources.getColor(R.color.white));
			typeLayout.setVisibility(View.VISIBLE);
			typeLeftLayout.setVisibility(View.INVISIBLE);
			typeRightLayout.setVisibility(View.VISIBLE);
			naviFlag = "order";
			String[] orders = getResources().getStringArray(R.array.order);
			List<String> syntheticOrderList = Arrays.asList(orders);
			if (syntheticOrderList != null && syntheticOrderList.size() > 0) {
				rightAdapter = new MerchantTypeAdapter(
						FoodMerchantActivity.this, syntheticOrderList, false,
						"order");
				typeRightView.setAdapter(rightAdapter);
				rightAdapter.setDefSelec(lastSelectOrderIndex);
				rightAdapter.notifyDataSetInvalidated();
			}
			break;
		case R.id.rightImageLayout:
			startActivity(new Intent(FoodMerchantActivity.this,
					NearbyMerchantPoiActivity.class));
			break;
		default:
			break;
		}
	}

	private OnItemClickListener leftItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			setLastClickView(arg0, arg2);
			setCurrentClickView(arg1, arg0, arg2, naviFlag);

		}

		/**
		 * 设置上次点击的View的状态
		 * 
		 * @param arg0
		 */
		private void setLastClickView(AdapterView<?> arg0, int arg2) {
			View tempObject = null;
			if (naviFlag.equals("food")) {
				tempObject = arg0.getChildAt(lastSelectFoodIndex);
				lastSelectFoodIndex = arg2;
			} else {
				tempObject = arg0.getChildAt(lastSelectCityIndex);
				lastSelectCityIndex = arg2;
			}
			tempObject.setBackgroundColor(resources.getColor(R.color.white));
			TextView typeName = (TextView) tempObject
					.findViewById(R.id.merchant_left_name);
			ImageView typeFlag = (ImageView) tempObject
					.findViewById(R.id.merchant_left_icn);
			typeName.setTextColor(resources.getColor(R.color.font_gray));
			typeFlag.setImageResource(R.drawable.arrow_left);
		}

		/**
		 * 设置当前点击View的状态
		 * 
		 * @param arg1
		 */
		private void setCurrentClickView(View arg1, AdapterView<?> arg0,
				int arg2, String typeFlag) {
			arg1.setBackgroundResource(R.drawable.food_type_bg);
			TextView currentTypeName = (TextView) arg1
					.findViewById(R.id.merchant_left_name);
			ImageView currentTypeFlag = (ImageView) arg1
					.findViewById(R.id.merchant_left_icn);
			currentTypeName.setTextColor(resources
					.getColor(R.color.font_light_red));
			currentTypeFlag.setImageResource(R.drawable.arrow_left_p);
			if (!TextUtils.isEmpty(typeFlag)) {
				// 美食子分类
				if (naviFlag.equals("food")) {
					// 每次点击根分类的时候，初始化上次点击的索引
					lastSelectFoodSubIndex = 0;
					FoodClass foodClass = (FoodClass) arg0
							.getItemAtPosition(arg2);
					List<FoodSubClass> subClasses = foodClass
							.getMerchantTypeList();
					if (subClasses != null && subClasses.size() > 0) {
						rightAdapter = new MerchantTypeAdapter(
								FoodMerchantActivity.this, subClasses, false,
								naviFlag);
						typeRightView.setAdapter(rightAdapter);
						rightAdapter.notifyDataSetChanged();
					}
					typeId = foodClass.getTid();
				} else {
					// 每次点击根分类的时候，初始化上次点击的索引
					lastSelectCitySubIndex = 0;
					// 全城子分类
					WholeCityClass cityClass = (WholeCityClass) arg0
							.getItemAtPosition(arg2);
					List<WholeCitySubClass> subClasses = cityClass
							.getHotBusinessList();
					if (subClasses != null && subClasses.size() > 0) {
						rightAdapter = new MerchantTypeAdapter(
								FoodMerchantActivity.this, subClasses, false,
								naviFlag);
						typeRightView.setAdapter(rightAdapter);
						rightAdapter.notifyDataSetChanged();
					}
					region = cityClass.getRegion();
				}
			}

		}
	};

	private OnItemClickListener rightItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			if (naviFlag.equals("order")) {
				// 每次点击根分类的时候，初始化上次点击的索引
				lastSelectOrderIndex = 0;
				// 综合排序
				setLastClickView(arg0);
				setCurrentClickView(arg1, arg0);
				lastSelectOrderIndex = arg2;
				order = arg2 + "";
				typeLayout.setVisibility(View.GONE);
				sortTypeIcn.setImageResource(R.drawable.ic_arrow_more_normal);
				sortTypeName.setText(arg0.getItemAtPosition(arg2).toString());
				sortTypeName
						.setTextColor(resources.getColor(R.color.font_gray));
				list.clear();
				page = 1;
				getMerchantList();
			} else if (naviFlag.equals("city")) {
				// 全城分类
				View tempObject = arg0.getChildAt(lastSelectCitySubIndex);
				TextView typeName = (TextView) tempObject
						.findViewById(R.id.merchant_left_name);
				typeName.setTextColor(resources.getColor(R.color.font_gray));
				TextView currentTypeName = (TextView) arg1
						.findViewById(R.id.merchant_left_name);
				currentTypeName.setTextColor(resources
						.getColor(R.color.font_light_red));
				lastSelectCitySubIndex = arg2;
				typeLayout.setVisibility(View.GONE);
				WholeCitySubClass subClass = (WholeCitySubClass) arg0
						.getItemAtPosition(arg2);
				hotBusiness = subClass.getHotRegion();
				areaTypeIcn.setImageResource(R.drawable.ic_arrow_more_normal);
				areaTypeName.setText(hotBusiness);
				areaTypeName
						.setTextColor(resources.getColor(R.color.font_gray));
				list.clear();
				page = 1;
				getMerchantList();
			} else if (naviFlag.equals("food")) {
				// 美食子分类
				View tempObject = arg0.getChildAt(lastSelectFoodSubIndex);
				TextView typeName = (TextView) tempObject
						.findViewById(R.id.merchant_left_name);
				typeName.setTextColor(resources.getColor(R.color.font_gray));
				TextView currentTypeName = (TextView) arg1
						.findViewById(R.id.merchant_left_name);
				currentTypeName.setTextColor(resources
						.getColor(R.color.font_light_red));
				lastSelectFoodSubIndex = arg2;
				typeLayout.setVisibility(View.GONE);
				FoodSubClass subClass = (FoodSubClass) arg0
						.getItemAtPosition(arg2);
				twoTypeId = subClass.getTypeId();
				foodTypeIcn.setImageResource(R.drawable.ic_arrow_more_normal);
				foodTypeName.setText(subClass.getTypeName());
				foodTypeName
						.setTextColor(resources.getColor(R.color.font_gray));
				list.clear();
				page = 1;
				getMerchantList();
			}

		}

		/**
		 * 设置上次点击的View的状态（全程分类）
		 * 
		 * @param arg0
		 */
		private void setLastClickView(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			View tempObject = arg0.getChildAt(lastSelectOrderIndex);
			tempObject.setBackgroundColor(resources.getColor(R.color.white));
			TextView typeName = (TextView) tempObject
					.findViewById(R.id.merchant_left_name);
			typeName.setTextColor(resources.getColor(R.color.font_gray));
		}

		/**
		 * 设置当前点击View的状态（全程分类）
		 * 
		 * @param arg1
		 */
		private void setCurrentClickView(View arg1, AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			arg1.setBackgroundResource(R.drawable.food_type_bg);
			TextView currentTypeName = (TextView) arg1
					.findViewById(R.id.merchant_left_name);
			currentTypeName.setTextColor(resources
					.getColor(R.color.font_light_red));
		}
	};

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Merchant bean = (Merchant) arg0.getItemAtPosition(arg2);
		Intent intent = new Intent();
		intent.putExtra("meid", bean.getMeid());
		intent.putExtra("flag", "food");
		intent.setClass(FoodMerchantActivity.this,
				MerchantDetailsActivity.class);
		startActivity(intent);

	}

	@Override
	public void onFooterLoad(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		listStatus = Constant.ListStatus.LOAD;
		page++;
		initParams();
		getMerchantList();

	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		list.clear();
		listStatus = Constant.ListStatus.REFRESH;
		page = 1;
		initParams();
		getMerchantList();
	}

	protected void onDestroy() {
		super.onDestroy();
		lastSelectOrderIndex = 0;
		lastSelectFoodIndex = 0;
		lastSelectCityIndex = 0;
		lastSelectCitySubIndex = 0;
		lastSelectFoodSubIndex = 0;
	};

}
