package com.android.bluetown.home.main.model.act;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import com.android.bluetown.adapter.ActionCenterListAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.ActionCenterItemBean;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.ActionCenterResult;
import com.android.bluetown.utils.Constant;

/**
 * 
 * @ClassName: ActionCenterSearchActivity
 * @Description:TODO(HomeActivity-活动中心-搜索)
 * @author: shenyz
 * @date: 2015年7月31日 上午10:29:59
 * 
 */
public class ActionCenterSearchActivity extends TitleBarActivity implements
		OnHeaderRefreshListener, OnFooterLoadListener, OnItemClickListener,
		OnClickListener {
	private ListView mListView;
	private AbPullToRefreshView mAbPullToRefreshView;
	private ActionCenterListAdapter adapter;
	private ArrayList<ActionCenterItemBean> list;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 10;
	private SharePrefUtils prefUtils;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			ActionCenterResult result = (ActionCenterResult) msg.obj;
			if (result!=null){
				dealResult(result);
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_action_center_list);
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
		setSearchView(R.string.input_action_hint);
		setTitleLayoutBg(R.color.title_bg_blue);
		setRighTextView(R.string.search);
		righTextLayout.setOnClickListener(this);
	}

	/**
	 * 
	 * @Title: initUIView
	 * @Description: TODO(初始化界面組件)
	 * @throws
	 */
	private void initUIView() {
		prefUtils = new SharePrefUtils(this);
		list = new ArrayList<ActionCenterItemBean>();
		mListView = (ListView) findViewById(R.id.companyInfoList);
		mAbPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView.setHeaderDividersEnabled(false);
		mListView.setFooterDividersEnabled(false);
		mListView.setOnItemClickListener(this);
		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);
		adapter = new ActionCenterListAdapter(this, "main");
		adapter.setData(list);
		mListView.setAdapter(adapter);
	}

	private void getData(String searchValue) {
		params.put("tid", "");
		params.put("gardenId",
				prefUtils.getString(SharePrefUtils.GARDEN_ID, ""));
		params.put("searchValue", searchValue);
		params.put("page", page + "");
		params.put("rows", size + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.ACTION_CENTER_LIST, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						ActionCenterResult result = (ActionCenterResult) AbJsonUtil
								.fromJson(s, ActionCenterResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							Message message = mHandler.obtainMessage();
							message.obj = result;
							mHandler.sendMessage(message);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							if (mAbPullToRefreshView != null) {
								mAbPullToRefreshView.onHeaderRefreshFinish();
								mAbPullToRefreshView.onFooterLoadFinish();
							}
							Toast.makeText(ActionCenterSearchActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();
						}

					}
				});
	}

	protected void dealResult(ActionCenterResult result) {
		switch (listStatus) {
		case Constant.ListStatus.LOAD:
			list.addAll(result.getData().getAction().getRows());
			mAbPullToRefreshView.onFooterLoadFinish();
			break;
		case Constant.ListStatus.INIT:
			list.clear();
			list.addAll(result.getData().getAction().getRows());
			break;
		case Constant.ListStatus.REFRESH:
			list.clear();
			list.addAll(result.getData().getAction().getRows());
			mAbPullToRefreshView.onHeaderRefreshFinish();
			break;
		}
		adapter.setData(list);
	}

	@Override
	public void onFooterLoad(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		listStatus = Constant.ListStatus.LOAD;
		page++;
		getData(null);
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		list.clear();
		listStatus = Constant.ListStatus.REFRESH;
		page = 1;
		getData(null);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("aid", list.get(arg2).getAid());
		intent.setClass(ActionCenterSearchActivity.this,
				ActionCenterDetailsActivity.class);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rightTextLayout:
			if (TextUtils.isEmpty(searchView.getText().toString())) {
				TipDialog.showDialog(ActionCenterSearchActivity.this,
						R.string.tip, R.string.confirm, R.string.search_empty);
				return;
			}
			getData(searchView.getText().toString());
			searchView.setText("");
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
