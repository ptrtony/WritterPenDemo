package com.android.bluetown.home.makefriends.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.annotation.SuppressLint;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.FriendSearchAdapter;
import com.android.bluetown.bean.FriendsBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.listener.AbsHttpStringResponseListener;

import com.android.bluetown.result.FriendResult;
import com.android.bluetown.sortlistview.ClearEditText;
import com.android.bluetown.utils.Constant;

@SuppressLint("ResourceAsColor")
public class FriendSearchActivity extends TitleBarActivity implements
		OnClickListener, OnHeaderRefreshListener, OnFooterLoadListener {
	private AbPullToRefreshView mRefreshView;
	private ListView mListView;
	private ClearEditText mEditText;
	private Button mSearchBtn;
	private FriendSearchAdapter mAdapter;
	private ArrayList<FriendsBean> list;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 10;
	private String condition;
	private String userId;
	private FinalDb db;

	private List<MemberUser> users;

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.friend_search_title);
		setTitleLayoutBg(R.color.chat_tab_join_friend_color);
		righTextLayout.setVisibility(View.INVISIBLE);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_friend_search);
		// 搜索条件
		condition = getIntent().getStringExtra("condition");
		list = new ArrayList<FriendsBean>();
		db = FinalDb.create(this);
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		mEditText = (ClearEditText) findViewById(R.id.searchContent);
		mSearchBtn = (Button) findViewById(R.id.searchBtn);
		mSearchBtn.setBackgroundResource(R.drawable.chat_add_friend_btn_bg);
		mSearchBtn.setOnClickListener(this);
		mListView = (ListView) findViewById(R.id.listview);
		mRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mRefreshView.setOnHeaderRefreshListener(this);
		mRefreshView.setOnFooterLoadListener(this);
		mListView.setFooterDividersEnabled(false);
		mListView.setHeaderDividersEnabled(false);
		mEditText.setText(condition);
		getData(condition);

	}

	private void getData(String condition) {
		params.put("userId", userId);
		// type:必填（0:查看好友；1：查看所有用户）
		params.put("type", "1");
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
							if (mRefreshView != null) {
								mRefreshView.onFooterLoadFinish();
								mRefreshView.onHeaderRefreshFinish();
							}
							Toast.makeText(FriendSearchActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();

						}

					}
				});
	}

	protected void dealResult(FriendResult result) {
		switch (listStatus) {
		case Constant.ListStatus.LOAD:
			list.addAll(result.getData().getFriend().getRows());
			mRefreshView.onFooterLoadFinish();
			break;
		case Constant.ListStatus.INIT:
			list.clear();
			list.addAll(result.getData().getFriend().getRows());
			break;
		case Constant.ListStatus.REFRESH:
			list.clear();
			list.addAll(result.getData().getFriend().getRows());
			mRefreshView.onHeaderRefreshFinish();
			break;
		}
		if (mAdapter == null) {
			mAdapter = new FriendSearchAdapter(FriendSearchActivity.this, list,
					result.getData().getUserInfo());
			mListView.setAdapter(mAdapter);
		} else {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.searchBtn:
			searchContent();
			break;
		default:
			break;
		}

	}

	private void searchContent() {
		if (list != null) {
			list.clear();
		}
		condition = mEditText.getText().toString();
		page = 1;
		getData(condition);
	}

}
