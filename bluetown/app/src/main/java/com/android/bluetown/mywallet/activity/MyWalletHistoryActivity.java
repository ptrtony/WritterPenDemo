package com.android.bluetown.mywallet.activity;

import java.util.ArrayList;
import java.util.List;

import me.next.tagview.TagCloudView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.AccountSearchActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.R.color;
import com.android.bluetown.R.id;
import com.android.bluetown.R.layout;
import com.android.bluetown.adapter.BalanceDetailAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.SettingMessageItemBean;
import com.android.bluetown.home.main.model.act.CompanyDetailsActivity;
import com.android.bluetown.utils.Constant;

//历史账单
public class MyWalletHistoryActivity extends TitleBarActivity implements
		OnClickListener, OnHeaderRefreshListener, OnFooterLoadListener,
		OnItemClickListener, TagCloudView.OnTagClickListener {
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
		addContentView(layout.ac_company_show);
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
		setBackImageView();
		setTitleView("历史账单");
		setTitleLayoutBg(color.title_bg_blue);
		searchBtnLy.setVisibility(View.VISIBLE);
		searchBtnLy.setOnClickListener(this);
	}

	/**
	 * 
	 * @Title: initUIView
	 * @Description: TODO(初始化界面組件)
	 * @throws
	 */
	private void initUIView() {
		mPullToRefreshView = (AbPullToRefreshView) findViewById(id.mPullRefreshView);
		mListView = (ListView) findViewById(id.companyInfoList);
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
	public void onTagClick(int position) {
		// TODO Auto-generated method stub
		if (position == -1) {
			Toast.makeText(getApplicationContext(), "沒有这个标签",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getApplicationContext(),
					"标题 position : " + position, Toast.LENGTH_SHORT).show();
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
		startActivity(new Intent(MyWalletHistoryActivity.this,
				CompanyDetailsActivity.class));
	}

	@Override
	public void onClick(View v) {

		// TODO Auto-generated method stub
		switch (v.getId()) {
		case id.searchBtnLy:
			startActivity(new Intent(MyWalletHistoryActivity.this,
					AccountSearchActivity.class));
			break;

		default:
			break;
		}

	}

}
