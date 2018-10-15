package com.android.bluetown.surround;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.MerchantListAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.bean.LocationInfo;
import com.android.bluetown.bean.Merchant;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.FoodMerchantListResult;
import com.android.bluetown.utils.Constant;


/**
 * 其他商家美食列表
 * 
 * @author shenyz
 * 
 */
public class OtherMerchantActivity extends TitleBarActivity implements
		OnItemClickListener, OnHeaderRefreshListener, OnFooterLoadListener {
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	protected int rows = 10;
	private ListView mListView;
	/** 分页 */
	private AbPullToRefreshView mPullToRefreshView;
	private MerchantListAdapter adapter;
	private List<Merchant> list;
	private String longitude, latitude, typeId, province, city;
	private FinalDb db;

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBgRes(R.color.title_bg_blue);
		righTextLayout.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_action_center);
		initView();
		getMerchantList();
	}

	/**
	 * 初始化界面组件
	 */
	private void initView() {
		db = FinalDb.create(this);
		initParams();
		typeId = getIntent().getStringExtra("typeId");
		if (!TextUtils.isEmpty(city)) {
			setTitleView(city.substring(0, city.indexOf("市")) + ".商家");
		} else {
			setTitleView("宁波.商家");
		}
		list = new ArrayList<>();
		mListView = (ListView) findViewById(R.id.companyInfoList);
		mPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView.setOnItemClickListener(this);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterLoadListener(this);
		mListView.setDividerHeight(0);
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

	private void getMerchantList() {
		// TODO Auto-generated method stub
		// longitude：经度(必填)
		// latitude：纬度(必填)
		// typeId：其他商家类型id
		// province：省(必填)
		// city：市(必填)
		params.put("longitude", longitude);
		params.put("latitude", latitude);
		params.put("typeId", typeId);
		params.put("province", province);
		params.put("city", city);
		params.put("page", page + "");
		params.put("rows", rows + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.OTHER_MERCHANT_LIST, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						FoodMerchantListResult result = (FoodMerchantListResult) AbJsonUtil
								.fromJson(s, FoodMerchantListResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							Toast.makeText(OtherMerchantActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Merchant value = (Merchant) arg0.getItemAtPosition(arg2);
		Intent intent = new Intent();
		intent.putExtra("meid", value.getMeid());
		BlueTownApp.DISH_TYPE= "other";
		intent.setClass(OtherMerchantActivity.this,
				MerchantDetailsActivity.class);
		startActivity(intent);
	}

}
