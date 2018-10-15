package com.android.bluetown;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.adapter.MessageListAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.MeaageResult;
import com.android.bluetown.result.MeaageResult.MessageBean;
import com.android.bluetown.utils.Constant;

public class SettingMessageActivity extends TitleBarActivity implements
		OnHeaderRefreshListener, OnFooterLoadListener, OnItemClickListener {
	private AbPullToRefreshView mAbPullToRefreshView;
	private ListView mListView;
	private MessageListAdapter adapter;
	private List<MessageBean> list;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 10;
	private String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_action_center);
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
		setTitleView("消息");
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	/**
	 * 
	 * @Title: initUIView
	 * @Description: TODO(初始化界面組件)
	 * @throws
	 */
	private void initUIView() {
		
		mListView = (ListView) findViewById(R.id.companyInfoList);
		mAbPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView.setHeaderDividersEnabled(false);
		mListView.setFooterDividersEnabled(false);
		mListView.setOnItemClickListener(this);
		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);
		list = new ArrayList<MessageBean>();
		FinalDb db = FinalDb.create(this);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
	}

	private void getData() {
	
		if (!TextUtils.isEmpty(userId)) {
			params.put("userId", userId);
			params.put("page", page + "");
			params.put("rows", size + "");
			httpInstance.post(Constant.HOST_URL
					+ Constant.Interface.PUSH_MESSAGE_LIST, params,
					new AbsHttpStringResponseListener(this, null) {
						@Override
						public void onSuccess(int i, String s) {
							MeaageResult result = (MeaageResult) AbJsonUtil
									.fromJson(s, MeaageResult.class);
							if (result.getRepCode().contains(
									Constant.HTTP_SUCCESS)) {
								dealResult(result);
							} else if (result.getRepCode().contains(
									Constant.HTTP_NO_DATA)) {
								if (mAbPullToRefreshView!=null) {
								mAbPullToRefreshView.onHeaderRefreshFinish();
								mAbPullToRefreshView.onFooterLoadFinish();
								}
								Toast.makeText(SettingMessageActivity.this,
										R.string.no_data, Toast.LENGTH_LONG)
										.show();

							}

						}
						@Override
						public void onFailure(int arg0, String arg1, Throwable arg2) {
							// TODO Auto-generated method stub
							mAbPullToRefreshView.onFooterLoadFinish();
							mAbPullToRefreshView.onHeaderRefreshFinish();
						}
					});
		}

	}

	protected void dealResult(MeaageResult result) {
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
	if (adapter==null) {
		adapter = new MessageListAdapter(this, list);
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
		list.clear();
		listStatus = Constant.ListStatus.REFRESH;
		page = 1;
		getData();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		// startActivity(new Intent(SettingMessageActivity.this,
		// CompanyDetailsActivity.class));
	}

}
