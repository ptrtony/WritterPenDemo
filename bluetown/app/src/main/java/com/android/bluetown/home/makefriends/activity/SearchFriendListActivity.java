package com.android.bluetown.home.makefriends.activity;

import io.rong.imkit.RongIM;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.os.Bundle;
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
import com.android.bluetown.adapter.MakeFriendsAdapter;
import com.android.bluetown.bean.FriendsBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.listener.AbsHttpStringResponseListener;

import com.android.bluetown.result.FriendResult;
import com.android.bluetown.utils.Constant;

public class SearchFriendListActivity extends TitleBarActivity implements
		OnClickListener, OnHeaderRefreshListener, OnFooterLoadListener,
		OnItemClickListener {
	private AbPullToRefreshView mPullRefreshView;
	private ListView mListView;
	private MakeFriendsAdapter mAdapter;
	private ArrayList<FriendsBean> list;
	private String condition;
	private String userId;
	private FinalDb db;
	
	private List<MemberUser> users;
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
		initViews();
		condition = getIntent().getStringExtra("condition");
		mClearEditText.setText(condition);
		getData(condition);
	}

	/**
	 * 初始化界面组件
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
		list = new ArrayList<FriendsBean>();
		mPullRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView = (ListView) findViewById(R.id.companyInfoList);
		mListView.setOnItemClickListener(this);
		mPullRefreshView.setOnHeaderRefreshListener(this);
		mPullRefreshView.setOnFooterLoadListener(this);
		mClearEditText.setOnClickListener(this);
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBg(R.color.chat_tab_friend_color);
		setCustomSearchView(R.string.search_friend_hint);
		setRighTextView(R.string.search);
		righTextLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rightTextLayout:
			condition = mClearEditText.getText().toString();
			if (list != null) {
				list.clear();
			}
			page=1;
			getData(condition);
			break;
		case R.id.filter_edit:
			mClearEditText.setFocusable(true);
			mClearEditText.setFocusableInTouchMode(true);
			break;
		default:
			break;
		}
	}

	private void getData(String condition) {
		
		params.put("userId", userId);
		// 从我的好友中搜索
		params.put("type", "0");
		params.put("condition", condition);
		params.put("page", page + "");
		params.put("rows", size + "");
		httpInstance.post(Constant.HOST_URL + Constant.Interface.FRIEND_LIST,
				params, new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						FriendResult result = (FriendResult) AbJsonUtil
								.fromJson(s, FriendResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							if (mPullRefreshView != null) {
								mPullRefreshView.onFooterLoadFinish();
								mPullRefreshView.onHeaderRefreshFinish();
							}
							Toast.makeText(SearchFriendListActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();
						} else {
							if (mPullRefreshView != null) {
								mPullRefreshView.onFooterLoadFinish();
								mPullRefreshView.onHeaderRefreshFinish();
							}
							Toast.makeText(SearchFriendListActivity.this,
									result.getRepMsg(), Toast.LENGTH_LONG)
									.show();
						}

					}
				});
	}

	protected void dealResult(FriendResult result) {
		switch (listStatus) {
		case Constant.ListStatus.LOAD:
			list.addAll(result.getData().getFriend().getRows());
			mPullRefreshView.onFooterLoadFinish();
			break;
		case Constant.ListStatus.INIT:
			list.clear();
			list.addAll(result.getData().getFriend().getRows());
			break;
		case Constant.ListStatus.REFRESH:
			list.clear();
			list.addAll(result.getData().getFriend().getRows());
			mPullRefreshView.onHeaderRefreshFinish();
			break;
		}
		if (mAdapter==null) {
			mAdapter = new MakeFriendsAdapter(SearchFriendListActivity.this, list,
					Integer.parseInt("0"));
			mListView.setAdapter(mAdapter);
		}else {
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onFooterLoad(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		listStatus = Constant.ListStatus.LOAD;
		page++;
		getData(condition);
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		list.clear();
		listStatus = Constant.ListStatus.REFRESH;
		page = 1;
		getData(condition);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		RongIM.getInstance().startPrivateChat(SearchFriendListActivity.this,
				list.get(arg2).getUserId(), list.get(arg2).getNickName());
	}

}
