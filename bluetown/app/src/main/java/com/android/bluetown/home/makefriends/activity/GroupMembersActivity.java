package com.android.bluetown.home.makefriends.activity;

import java.util.ArrayList;
import java.util.List;

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
import com.android.bluetown.adapter.GroupMemberAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.GroupMemberResult;
import com.android.bluetown.result.GroupMemberResult.GroupMember;
import com.android.bluetown.utils.Constant;

/**
 * 群成员
 * 
 * @author shenyz
 * 
 */
public class GroupMembersActivity extends TitleBarActivity implements
		OnItemClickListener, OnHeaderRefreshListener, OnFooterLoadListener {
	private AbPullToRefreshView mRefreshView;
	private ListView mListView;
	private List<GroupMember> list;
	private GroupMemberAdapter adapter;
	private String mid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_group_members);
		BlueTownExitHelper.addActivity(this);
		initViews();
		getGroupMember(mid);
	}

	private void initViews() {
		mid = getIntent().getStringExtra("mid");
		mRefreshView = (AbPullToRefreshView) findViewById(R.id.memberPullRefresh);
		mListView = (ListView) findViewById(R.id.memberListView);
		mRefreshView.setOnHeaderRefreshListener(this);
		mRefreshView.setOnFooterLoadListener(this);
		mListView.setOnItemClickListener(this);
		list = new ArrayList<GroupMember>();
		adapter = new GroupMemberAdapter(GroupMembersActivity.this, list);
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
		setTitleLayoutBg(R.color.chat_tab_message_color);
		setTitleView("群成员");
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	private void getGroupMember(String mid) {
		// TODO Auto-generated method stub
		params.put("mid", mid);
		httpInstance.post(Constant.HOST_URL + Constant.Interface.GROUP_MEMBER,
				params, new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						GroupMemberResult result = (GroupMemberResult) AbJsonUtil
								.fromJson(s, GroupMemberResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							Toast.makeText(GroupMembersActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(GroupMembersActivity.this,
									result.getRepMsg(), Toast.LENGTH_LONG)
									.show();
						}

					}
				});

	}

	protected void dealResult(GroupMemberResult result) {
		list.clear();
		list.addAll(result.getData().getRows());
		mRefreshView.onHeaderRefreshFinish();
		mRefreshView.onFooterLoadFinish();
		adapter = new GroupMemberAdapter(this, list);
		mListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onFooterLoad(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		getGroupMember(mid);
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		getGroupMember(mid);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {

	}

}
