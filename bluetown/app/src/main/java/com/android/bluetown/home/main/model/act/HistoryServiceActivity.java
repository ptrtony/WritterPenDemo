package com.android.bluetown.home.main.model.act;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.os.Bundle;
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
import com.android.bluetown.adapter.HistoryServiceListAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.HistoryServiceIteamBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.listener.AbsHttpStringResponseListener;

import com.android.bluetown.result.HistoryServiceIteamResult;
import com.android.bluetown.utils.Constant;

/**
 * 
 * @ClassName: HistoryServiceActivity
 * @Description:TODO(自助报修-历史维修界面)
 * @author: shenyz
 * @date: 2015年8月6日 下午1:01:13
 * 
 */
public class HistoryServiceActivity extends TitleBarActivity implements
		OnHeaderRefreshListener, OnFooterLoadListener, OnItemClickListener {
    private AbPullToRefreshView mPullRefreshView;
	private ListView mListView;
	
	private List<HistoryServiceIteamBean> list;
	private HistoryServiceListAdapter adapter;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 10;
	private String userId;
	private FinalDb db;

	private List<MemberUser> users;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_history_service);
		BlueTownExitHelper.addActivity(this);
		initViews();
		getHistoryService();
	}

	/**
	 * 
	 * @Title: initViews
	 * @Description: TODO(初始化界面组件)
	 * @throws
	 */
	private void initViews() {
		db = FinalDb.create(this);
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		list = new ArrayList<HistoryServiceIteamBean>();
		mPullRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView = (ListView) findViewById(R.id.historyServiceList);
		mListView.setOnItemClickListener(this);
		mPullRefreshView.setOnHeaderRefreshListener(this);
		mPullRefreshView.setOnFooterLoadListener(this);
	}

	private void getHistoryService() {
		params.put("userId", userId);
		params.put("page", page + "");
		params.put("size", size + "");
		httpInstance.post(
				Constant.HOST_URL + Constant.Interface.GETREPAIR_LIST, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						HistoryServiceIteamResult result = (HistoryServiceIteamResult) AbJsonUtil
								.fromJson(s, HistoryServiceIteamResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							if (mPullRefreshView != null) {
								mPullRefreshView.onHeaderRefreshFinish();
								mPullRefreshView.onFooterLoadFinish();
							}
							Toast.makeText(HistoryServiceActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();

						}

					}

				});
	}

	protected void dealResult(HistoryServiceIteamResult result) {
		switch (listStatus) {
		case Constant.ListStatus.LOAD:
			list.addAll(result.getData().getRows());
			mPullRefreshView.onFooterLoadFinish();
			break;
		case Constant.ListStatus.INIT:
			list.clear();
			list.addAll(result.getData().getRows());
			break;
		case Constant.ListStatus.REFRESH:
			list.clear();
			list.addAll(result.getData().getRows());
			mPullRefreshView.onHeaderRefreshFinish();
			break;
		}

		if (adapter == null) {
			adapter = new HistoryServiceListAdapter(this, list);
			mListView.setAdapter(adapter);
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 
	 * @Title: initTitle
	 * @Description: 初始化标题栏
	 * @see com.android.bluetown.activity.TitleBarActivity#initTitle()
	 */
	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.history_self_service);
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		// 报修单后台处理状态
		intent.putExtra("dealStatus", list.get(position).getState());
		intent.putExtra("rid", list.get(position).getRid());
		intent.setClass(HistoryServiceActivity.this,
				HistoryServiceDeatilsActivity.class);
		startActivity(intent);
	}

		@Override
	public void onFooterLoad(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		listStatus = Constant.ListStatus.LOAD;
		page++;
		getHistoryService();
	}
	
	@Override
	public void onHeaderRefresh(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		listStatus = Constant.ListStatus.REFRESH;
		page = 1;
		getHistoryService();
	}

}
