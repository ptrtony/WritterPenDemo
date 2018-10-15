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
import android.widget.RelativeLayout;
import android.widget.Toast;




import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.GuestAppointmentHistoryListAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.GuestAppointmentHistoryItemBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.GuestAppointHistoryResult;
import com.android.bluetown.utils.Constant;

//访客预约历史
public class GuestAppointmentHistoryActivity extends TitleBarActivity implements
		OnItemClickListener,  OnHeaderRefreshListener, OnFooterLoadListener  {
	private AbPullToRefreshView mPullToRefreshView;
	private ListView mListView;
	private GuestAppointmentHistoryListAdapter adapter;
	private List<GuestAppointmentHistoryItemBean> list;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 10;

	private String userId;
	private FinalDb db;
	private List<MemberUser> users;
	private RelativeLayout empty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_parking_history);
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
		setTitleView(R.string.guest_apponit_history);
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
		db = FinalDb.create(this);
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		list = new ArrayList<GuestAppointmentHistoryItemBean>();
		mPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView = (ListView) findViewById(R.id.companyInfoList);
		empty=(RelativeLayout)findViewById(R.id.empty);
		mListView.setEmptyView(empty);
		mListView.setHeaderDividersEnabled(false);
		mListView.setFooterDividersEnabled(false);
		mListView.setOnItemClickListener(this);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		
		mPullToRefreshView.setOnFooterLoadListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void getData() {
		// TODO Auto-generated method stub
		params.put("userId", userId);
		params.put("page", page + "");
		params.put("size", size + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.GUEST_APPOINT_LIST, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						GuestAppointHistoryResult result = (GuestAppointHistoryResult) AbJsonUtil
								.fromJson(s, GuestAppointHistoryResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							if (mPullToRefreshView != null) {
								mPullToRefreshView.onFooterLoadFinish();
								mPullToRefreshView.onHeaderRefreshFinish();
							}
							
									Toast.makeText(
											GuestAppointmentHistoryActivity.this,
											R.string.no_data, Toast.LENGTH_LONG).show();
							
						}

					}
				});

	}

	protected void dealResult(GuestAppointHistoryResult result) {
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
		
		if (adapter==null) {
			adapter = new GuestAppointmentHistoryListAdapter(this, list);
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
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		// orderType1 预约成功，2：按时到达，0：预约失败是0，3：过期未到
		String orderType = list.get(position).getOrderType();
		Intent intent = new Intent();
		intent.putExtra("mid", list.get(position).getMid());
		if (orderType.equals("1") || orderType.equals("2")) {
			intent.setClass(GuestAppointmentHistoryActivity.this,
					GuestAppointmentSuccessActivity.class);
			startActivity(intent);
		} else {
			intent.setClass(GuestAppointmentHistoryActivity.this,
					GuestAppointmentFailActivity.class);
			startActivity(intent);
		}
	
	}

}
