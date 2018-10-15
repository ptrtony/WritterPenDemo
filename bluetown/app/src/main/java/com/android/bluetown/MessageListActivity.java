package com.android.bluetown;

import java.util.ArrayList;
import java.util.List;

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
import com.android.bluetown.adapter.MessageCenterListAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.IndexBanner;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.MessageListResult;
import com.android.bluetown.utils.Constant;

/**
 * 
 * @ClassName: 首页消息中心
 * @Description:TODO(HomeActivity-消息中心)
 * @author: shenyz
 * @date: 2015年7月31日 上午10:29:59
 * 
 */
public class MessageListActivity extends TitleBarActivity implements
		OnHeaderRefreshListener, OnFooterLoadListener, OnItemClickListener {
	private AbPullToRefreshView mAbPullToRefreshView;
	private ListView mListView;
	private MessageCenterListAdapter adapter;
	private List<IndexBanner> list;
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
		addContentView(R.layout.ac_near_person);
		BlueTownExitHelper.addActivity(this);
		initUIView();
		getData();

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
		setTitleLayoutBg(R.color.title_bg_blue);
		setTitleView("通知公告");
		righTextLayout.setVisibility(View.INVISIBLE);

	}

	/**
	 * 
	 * @Title: initUIView
	 * @Description: TODO(初始化界面組件)
	 * @throws
	 */
	private void initUIView() {
		list = new ArrayList<IndexBanner>();
		mListView = (ListView) findViewById(R.id.companyInfoList);
		mAbPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView.setHeaderDividersEnabled(false);
		mListView.setFooterDividersEnabled(false);
		mListView.setOnItemClickListener(this);
		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);

	}

	private void getData() {
		params.put("page", page + "");
		params.put("rows", size + "");
		httpInstance.post(Constant.HOST_URL + Constant.Interface.MobiHomePageAction_queryAllHomePagePictureList,
				params, new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						MessageListResult result = (MessageListResult) AbJsonUtil
								.fromJson(s, MessageListResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							if (mAbPullToRefreshView != null) {
								mAbPullToRefreshView.onHeaderRefreshFinish();
								mAbPullToRefreshView.onFooterLoadFinish();
							}

							Toast.makeText(MessageListActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();

						}

					}
					@Override
					public void onFailure(int arg0, String arg1, Throwable arg2) {
						// TODO Auto-generated method stub
						mAbPullToRefreshView.onHeaderRefreshFinish();
						mAbPullToRefreshView.onFooterLoadFinish();
					}
				});
	}

	protected void dealResult(MessageListResult result) {
		switch (listStatus) {
		case Constant.ListStatus.LOAD:
			list.addAll(result.getData().getRows());
			mAbPullToRefreshView.onFooterLoadFinish();
			break;
		case Constant.ListStatus.INIT:
			list.clear();
			list.addAll(result.getData().getRows());
			break;
		case Constant.ListStatus.REFRESH:
			list.clear();
			list.addAll(result.getData().getRows());
			mAbPullToRefreshView.onHeaderRefreshFinish();
			break;
		}
		if (adapter == null) {
			adapter = new MessageCenterListAdapter(this, list);
			mListView.setAdapter(adapter);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onFooterLoad(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		listStatus = Constant.ListStatus.LOAD;
		page++;
		getData();
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
//		list.clear();
		listStatus = Constant.ListStatus.REFRESH;
		page = 1;
		getData();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("hid", list.get(arg2).getHid());
		intent.setClass(MessageListActivity.this, MessageDetailsActivity.class);
		startActivity(intent);
	}

}
