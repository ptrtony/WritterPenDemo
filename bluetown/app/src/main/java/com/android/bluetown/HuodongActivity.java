package com.android.bluetown;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.adapter.ActionCenterListAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.ActionCenterItemBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.home.main.model.act.ActionCenterDetailsActivity;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.ActionCenterResult;
import com.android.bluetown.result.MyAttentionActionResult;
import com.android.bluetown.utils.Constant;

//我报名的活动
public class HuodongActivity extends TitleBarActivity implements
		OnHeaderRefreshListener, OnFooterLoadListener, OnItemClickListener {
	private ListView mListView;
	private AbPullToRefreshView mPullToRefreshView;
	private ActionCenterListAdapter adapter;
	private ArrayList<ActionCenterItemBean> list;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 10;
	private String userId = "";
	private String gardenId = "";
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			MyAttentionActionResult result = (MyAttentionActionResult) msg.obj;
			if (result!=null){
				dealResult(result);
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.activity_huodong);
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
		setTitleView("我报名的活动");
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
		
		list = new ArrayList<ActionCenterItemBean>();
		mListView = (ListView) findViewById(R.id.companyInfoList);
		mPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView.setHeaderDividersEnabled(false);
		mListView.setFooterDividersEnabled(false);
		mListView.setOnItemClickListener(this);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterLoadListener(this);
		FinalDb db = FinalDb.create(this);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
				gardenId=user.getGardenId();
			}
		}
		if (adapter == null) {
			adapter = new ActionCenterListAdapter(this, "main");
			adapter.setData(list);
			mListView.setAdapter(adapter);
		}
	}

	private void getData() {
		/**
		 * userId 用户id filterMine 查询我加入的活动关键字(值为1)
		 */
		params.put("userId", userId);
		params.put("gardenId",gardenId);
		params.put("filterMine", "1");
		params.put("page", page + "");
		params.put("rows", size + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.ACTION_CENTER_LIST, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						MyAttentionActionResult result = (MyAttentionActionResult) AbJsonUtil
								.fromJson(s, MyAttentionActionResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							Message message  = mHandler.obtainMessage();
							message.obj = result;
							mHandler.sendMessage(message);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							if (mPullToRefreshView != null) {
								mPullToRefreshView.onHeaderRefreshFinish();
								mPullToRefreshView.onFooterLoadFinish();
							}
							if (adapter != null) {
								adapter.notifyDataSetChanged();
							}
							Toast.makeText(HuodongActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();

						}

					}
					@Override
					public void onFailure(int arg0, String arg1, Throwable arg2) {
						// TODO Auto-generated method stub
						mPullToRefreshView.onFooterLoadFinish();
						mPullToRefreshView.onHeaderRefreshFinish();
					}
				});
	}

	protected void dealResult(MyAttentionActionResult result) {
		switch (listStatus) {
		case Constant.ListStatus.LOAD:
			list.addAll(result.getData().getRows());
			mPullToRefreshView.onFooterLoadFinish();
			break;
		case Constant.ListStatus.INIT:
			list.clear();
			list.addAll(result.getData().getRows());
			break;
		case Constant.ListStatus.REFRESH:
			list.clear();
			list.addAll(result.getData().getRows());
			mPullToRefreshView.onHeaderRefreshFinish();
			break;
		}
		adapter.setData(list);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
	public void onFooterLoad(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		listStatus = Constant.ListStatus.LOAD;
		page++;
		getData();

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("aid", list.get(arg2).getAid());
		intent.setClass(HuodongActivity.this, ActionCenterDetailsActivity.class);
		startActivity(intent);
	}

}
