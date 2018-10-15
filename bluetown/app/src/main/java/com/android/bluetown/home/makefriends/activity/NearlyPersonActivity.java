package com.android.bluetown.home.makefriends.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
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
import com.android.bluetown.adapter.NearlayPersonAdapter;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.listener.AbsHttpStringResponseListener;

import com.android.bluetown.result.NearLyPersonResult;
import com.android.bluetown.result.NearLyPersonResult.NearlyPerson;
import com.android.bluetown.utils.Constant;

public class NearlyPersonActivity extends TitleBarActivity implements
		OnHeaderRefreshListener, OnFooterLoadListener, OnItemClickListener {
	private AbPullToRefreshView mAbPullToRefreshView;
	private ListView mListView;
	private NearlayPersonAdapter adapter;
	private List<NearlyPerson> list;
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
		addContentView(R.layout.ac_near_person);
		initView();
		getData();
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.nearly_person);
		setTitleLayoutBg(R.color.chat_tab_join_friend_color);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	private void initView() {
		// TODO Auto-generated method stub
		db = FinalDb.create(this);
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		list = new ArrayList<NearlyPerson>();
		mListView = (ListView) findViewById(R.id.companyInfoList);
		mAbPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView.setHeaderDividersEnabled(false);
		mListView.setFooterDividersEnabled(false);
		mListView.setOnItemClickListener(this);
		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);
	}

	private void getData() {
		params.put("userId", userId);
		params.put("page", page + "");
		params.put("rows", size + "");
		httpInstance.post(Constant.HOST_URL + Constant.Interface.NEARBY_PEOPLE,
				params, new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						NearLyPersonResult result = (NearLyPersonResult) AbJsonUtil
								.fromJson(s, NearLyPersonResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							Toast.makeText(NearlyPersonActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();
							finishRefresh();
						} else {
							Toast.makeText(NearlyPersonActivity.this,
									result.getRepMsg(), Toast.LENGTH_LONG)
									.show();
							finishRefresh();

						}

					}

				});
	}

	/**
	 * 停止刷新
	 */
	private void finishRefresh() {
		if (mAbPullToRefreshView != null) {
			mAbPullToRefreshView.onFooterLoadFinish();
			mAbPullToRefreshView.onHeaderRefreshFinish();
		}
	}

	protected void dealResult(NearLyPersonResult result) {
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
			adapter = new NearlayPersonAdapter(this, list);
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
		getData();
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		list.clear();
		listStatus = Constant.ListStatus.REFRESH;
		page = 1;
		getData();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
