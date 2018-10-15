package com.android.bluetown.home.hot.model.act;

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
import com.android.bluetown.adapter.HistoryComplaintListAdapter;
import com.android.bluetown.bean.HistoryComplaintSuggestBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.listener.AbsHttpStringResponseListener;

import com.android.bluetown.result.HistoryComplaintResult;
import com.android.bluetown.utils.Constant;

/**
 * 
 * @ClassName: HistoryComlaintSuggestActivity
 * @Description:TODO(历史投诉建议)
 * @author: shenyz
 * @date: 2015年8月18日 上午10:34:08
 * 
 */
public class HistoryComlaintSuggestActivity extends TitleBarActivity implements
		OnItemClickListener, OnHeaderRefreshListener, OnFooterLoadListener {
	private AbPullToRefreshView mPullRefreshView;
	private ListView complaintSuggestList;
	private List<HistoryComplaintSuggestBean> list;
	private HistoryComplaintListAdapter adapter;
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
		addContentView(R.layout.ac_history_complaint_suggest);
		initUIView();
		getHistoryComplaint();
	}

	private void initUIView() {
		list = new ArrayList<HistoryComplaintSuggestBean>();
		mPullRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		complaintSuggestList = (ListView) findViewById(R.id.complaintSuggestList);
		complaintSuggestList.setOnItemClickListener(this);
		mPullRefreshView.setOnHeaderRefreshListener(this);
		mPullRefreshView.setOnFooterLoadListener(this);
		db = FinalDb.create(this);
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.history_complaint_suggest);
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void getHistoryComplaint() {
		params.put("userId", userId);
		params.put("page", page + "");
		params.put("size", size + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.COMPLAINT_HISTORY_LIST, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						HistoryComplaintResult result = (HistoryComplaintResult) AbJsonUtil
								.fromJson(s, HistoryComplaintResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									mPullRefreshView.onFooterLoadFinish();
									mPullRefreshView.onHeaderRefreshFinish();
									Toast.makeText(
											HistoryComlaintSuggestActivity.this,
											R.string.no_data, Toast.LENGTH_LONG)
											.show();
								}
							});
						}

					}

				});
	}

	protected void dealResult(HistoryComplaintResult result) {
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
			adapter = new HistoryComplaintListAdapter(this, list);
			complaintSuggestList.setAdapter(adapter);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onFooterLoad(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		listStatus = Constant.ListStatus.LOAD;
		page++;
		getHistoryComplaint();

	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		list.clear();
		listStatus = Constant.ListStatus.REFRESH;
		page = 1;
		getHistoryComplaint();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("details", list.get(position));
		intent.setClass(HistoryComlaintSuggestActivity.this,
				HistoryComlaintSuggestDetailsActivity.class);
		startActivity(intent);

	}

}
