package com.android.bluetown;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.adapter.BalanceDetailAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.SettingMessageItemBean;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.home.main.model.act.CompanyDetailsActivity;
import com.android.bluetown.utils.Constant;

public class AccountSearchActivity extends TitleBarActivity implements
		OnClickListener, OnHeaderRefreshListener, OnFooterLoadListener,
		OnItemClickListener {
	private ListView mListView;
	private AbPullToRefreshView mPullToRefreshView;
	private BalanceDetailAdapter adapter;
	private List<SettingMessageItemBean> list;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_company_show);
		BlueTownExitHelper.addActivity(this);
		initUIView();
		setData();

	}

	/**
	 * 
	 * @Title: initTitle
	 * @Description:初始化标题栏
	 * @see com.android.bluetown.activity.TitleBarActivity#initTitle()
	 */
	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setSearchView(R.string.company_name_search_hint);
		setTitleLayoutBg(R.color.title_bg_blue);
		setRighTextView(R.string.search);
		righTextLayout.setOnClickListener(this);
	}

	/**
	 * 
	 * @Title: initUIView
	 * @Description: TODO(初始化界面組件)
	 * @throws
	 */
	private void initUIView() {
		mPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView = (ListView) findViewById(R.id.companyInfoList);
		mListView.setOnItemClickListener(this);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterLoadListener(this);
	}

	private void setData() {
		// TODO Auto-generated method stub
		list = new ArrayList<SettingMessageItemBean>();
		for (int i = 0; i < 20; i++) {
			list.add(new SettingMessageItemBean("", "线下条形码支付", "2", "今天12:30",
					"+1111"));

		}
		adapter = new BalanceDetailAdapter(this, list);
		mListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rightTextLayout:
			if (TextUtils.isEmpty(searchView.getText().toString())) {
				TipDialog.showDialog(AccountSearchActivity.this,
						SweetAlertDialog.ERROR_TYPE, R.string.search_empty);
				return;
			}

			break;

		default:
			break;
		}
	}

	@Override
	public void onFooterLoad(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		mPullToRefreshView.onFooterLoadFinish();
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		mPullToRefreshView.onHeaderRefreshFinish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		startActivity(new Intent(AccountSearchActivity.this,
				CompanyDetailsActivity.class));

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
