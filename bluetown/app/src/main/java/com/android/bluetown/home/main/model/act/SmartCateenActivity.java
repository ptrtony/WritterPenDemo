package com.android.bluetown.home.main.model.act;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.CanteensAdapter2;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.CanteenBean;
import com.android.bluetown.bean.LocationInfo;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.surround.MerchantDetailsActivity;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.LocationUtil;
import com.android.bluetown.utils.ParseJSONTools;

/**
 * @author hedi
 * @data: 2016年6月7日 上午11:00:15
 * @Description: TODO<智慧餐厅 >
 */
public class SmartCateenActivity extends TitleBarActivity implements
		OnItemClickListener{
	/** 企业信息展示列表的ListView */
	private GridView mListView;
	private CanteensAdapter2 adapter;
	private List<CanteenBean> list;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 100;
	private double latitude = 0.0;
	private double longitude = 0.0;
	private String userId;
	private FinalDb db;
	private List<MemberUser> users;
	private SharePrefUtils prefUtils;
	private String province, city, garden;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_smart_cateen);
		BlueTownExitHelper.addActivity(this);
		initView();
		getCanteenData();

	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("智慧食堂");
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	private void initView() {
		prefUtils = new SharePrefUtils(this);
		db = FinalDb.create(this);
		mListView = (GridView) findViewById(R.id.gridview);
		mListView.setOnItemClickListener(this);
		list = new ArrayList<CanteenBean>();
		adapter = new CanteensAdapter2(this, list);
		mListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private void getCanteenData() {
		initParamsInfo();
		params.put("province", province);
		params.put("city", city);
		params.put("garden", garden);
		httpInstance.post(Constant.HOST_URL + Constant.Interface.CANTEEN_LIST,
				params, new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						JSONObject json;
						try {
							json = new JSONObject(s);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								JSONArray data = json.optJSONArray("data");
								for (int j = 0; j < data.length(); j++) {
									JSONObject itemObj = data.optJSONObject(j);
									CanteenBean cb = (CanteenBean) ParseJSONTools
											.getInstance().fromJsonToJava(
													itemObj, CanteenBean.class);
									list.add(cb);
								}
								adapter.notifyDataSetChanged();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						

					}
				});

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		CanteenBean bean = (CanteenBean) parent.getItemAtPosition(position);
		Intent intent = new Intent();
		intent.putExtra("meid", bean.getMeid());
		BlueTownApp.DISH_TYPE = "canteen";
		intent.setClass(SmartCateenActivity.this, MerchantDetailsActivity.class);
		startActivity(intent);

	}

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

	
}
