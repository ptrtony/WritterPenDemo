package com.android.bluetown.home.hot.model.act;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.android.bluetown.adapter.CompanyInfoPublishAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.bean.CompanyInfoPublishBean;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.home.main.model.act.ActionCenterSearchActivity;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.CompanyInfoPublishResult;
import com.android.bluetown.utils.Constant;

/**
 * 
 * @ClassName: SearchDemandActivity
 * @Description:TODO(企业需求-需求列表-查询需求)
 * @author: shenyz
 * @date: 2015年8月17日 上午11:47:47
 * 
 */
public class SearchDemandActivity extends TitleBarActivity implements
		OnClickListener, OnItemClickListener, OnHeaderRefreshListener,
		OnFooterLoadListener {
	private ListView publishInfoSearchList;
	private AbPullToRefreshView mPullToRefreshView;
	private List<CompanyInfoPublishBean> list;
	private CompanyInfoPublishAdapter mAdapter;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 10;
	private SharePrefUtils mPrefUtils;
	Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int position = msg.arg1;
			CompanyInfoPublishBean demand = list.get(position);
			switch (msg.what) {
			case CompanyInfoPublishBean.DEMAND_COLLECT:
				demand.setIsCollect("1");
				list.set(position, demand);
				mAdapter.notifyDataSetChanged();
				break;
			case CompanyInfoPublishBean.CANCEL_DEMAND_COLLECT:
				demand.setIsCollect("0");
				list.set(position, demand);
				mAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_company_info_public_search);
		initUIView();
	}

	private void initUIView() {
		BlueTownApp.setHanler(hander);
		mPrefUtils = new SharePrefUtils(this);
		list = new ArrayList<CompanyInfoPublishBean>();
		mPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		publishInfoSearchList = (ListView) findViewById(R.id.publishInfoSearchList);
		publishInfoSearchList.setOnItemClickListener(this);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterLoadListener(this);
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setSearchView(R.string.recruit);
		setTitleLayoutBg(R.color.title_bg_blue);
		setRighTextView(R.string.search_for);
		righTextLayout.setOnClickListener(this);
	}

	/**
	 * 
	 * @Title: getData
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param searchValue
	 * @throws
	 */
	private void getData(String searchValue) {
		// TODO Auto-generated method stub
		params.put("searchValue", searchValue);
		params.put("gardenId",
				mPrefUtils.getString(SharePrefUtils.GARDEN_ID, ""));
		params.put("page", page + "");
		params.put("rows", size + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.COMPANY_DEMAND_PUBLISH_LIST, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						CompanyInfoPublishResult result = (CompanyInfoPublishResult) AbJsonUtil
								.fromJson(s, CompanyInfoPublishResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							if (mPullToRefreshView != null) {
								mPullToRefreshView.onFooterLoadFinish();
								mPullToRefreshView.onHeaderRefreshFinish();
							}
							if (mAdapter != null) {
								mAdapter.notifyDataSetChanged();
							}

							Toast.makeText(SearchDemandActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();

						}

					}
				});
	}

	protected void dealResult(CompanyInfoPublishResult result) {
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
			// 使用自定义的Adapter
			mAdapter = new CompanyInfoPublishAdapter(this, list);
			publishInfoSearchList.setAdapter(mAdapter);
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(SearchDemandActivity.this, DemandDetailsActivity.class);
		intent.putExtra("infodetails", list.get(position));
		intent.putExtra("position", position);
		startActivity(intent);

	}

	@Override
	public void onFooterLoad(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		listStatus = Constant.ListStatus.LOAD;
		page++;
		getData(null);

	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		listStatus = Constant.ListStatus.REFRESH;
		list.clear();
		page = 1;
		getData(null);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rightTextLayout:
			if (TextUtils.isEmpty(searchView.getText().toString())) {
				TipDialog.showDialog(SearchDemandActivity.this, R.string.tip,
						R.string.confirm, R.string.search_empty);
				return;
			}
			getData(searchView.getText().toString());
			break;

		default:
			break;
		}
	}

}
