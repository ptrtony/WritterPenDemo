package com.android.bluetown.home.makefriends.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
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
import com.android.bluetown.adapter.JoinGroupAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.GroupsBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.listener.AbsHttpStringResponseListener;

import com.android.bluetown.result.GroupResult;
import com.android.bluetown.sortlistview.ClearEditText;
import com.android.bluetown.utils.Constant;

/**
 * 加入群组
 * 
 * @author shenyz
 * 
 */
public class JoinGroupActivity extends TitleBarActivity implements
		OnClickListener, OnHeaderRefreshListener, OnFooterLoadListener {
	private AbPullToRefreshView mRefreshView;
	private ListView mListView;
	private JoinGroupAdapter adapter;
	private List<GroupsBean> list;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 10;
	private ClearEditText mEditText;
	private Button mSearchBtn;
	private View divider;
	private String condition;
	private String userId;
	private FinalDb db;
	
	private List<MemberUser> users;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_friend_search);
		BlueTownExitHelper.addActivity(this);
		initUIView();
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
		setTitleView(R.string.add_group_title);
		setTitleLayoutBg(R.color.chat_tab_join_group_color);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	/**
	 * 
	 * @Title: initUIView
	 * @Description: TODO(初始化界面組件)
	 * @throws
	 */
	private void initUIView() {
		db = FinalDb.create(this);
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		list = new ArrayList<GroupsBean>();
		mEditText = (ClearEditText) findViewById(R.id.searchContent);
		mEditText.setHint(R.string.search_group_hint);
		mSearchBtn = (Button) findViewById(R.id.searchBtn);
		divider = findViewById(R.id.divider);
		divider.setVisibility(View.GONE);
		mSearchBtn.setBackgroundResource(R.drawable.green_darker_btn_bg);
		mSearchBtn.setOnClickListener(this);
		mListView = (ListView) findViewById(R.id.listview);
		mRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mRefreshView.setOnHeaderRefreshListener(this);
		mRefreshView.setOnFooterLoadListener(this);
		mListView.setFooterDividersEnabled(false);
		mListView.setHeaderDividersEnabled(false);

	}

	private void getData(String searchKeyword) {
		params.put("userId", userId);
		params.put("type", "1");
		params.put("condition", searchKeyword);
		params.put("page", page + "");
		params.put("size", size + "");
		httpInstance.post(Constant.HOST_URL + Constant.Interface.GROUP_LIST,
				params, new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						GroupResult result = (GroupResult) AbJsonUtil.fromJson(
								s, GroupResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							if (mRefreshView != null) {
								mRefreshView.onHeaderRefreshFinish();
								mRefreshView.onFooterLoadFinish();
							}
							Toast.makeText(JoinGroupActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();
						}

					}
				});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.searchBtn:
			if (list != null) {
				list.clear();
			}
			condition = mEditText.getText().toString();
			page = 1;
			getData(condition);
			break;
		default:
			break;
		}

	}

	protected void dealResult(GroupResult result) {
		switch (listStatus) {
		case Constant.ListStatus.LOAD:
			list.addAll(result.getData().getRows());
			mRefreshView.onFooterLoadFinish();
			break;
		case Constant.ListStatus.INIT:
			list.clear();
			list.addAll(result.getData().getRows());
			break;
		case Constant.ListStatus.REFRESH:
			list.clear();
			list.addAll(result.getData().getRows());
			mRefreshView.onHeaderRefreshFinish();
			break;
		}
		if (adapter == null) {
			adapter = new JoinGroupAdapter(JoinGroupActivity.this, list);
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

}
