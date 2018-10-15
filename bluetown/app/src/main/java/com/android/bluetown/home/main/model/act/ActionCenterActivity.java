package com.android.bluetown.home.main.model.act;

import java.util.ArrayList;
import java.util.List;

import me.next.tagview.TagCloudView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.ActionCenterListAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.ActionCenterItemBean;
import com.android.bluetown.bean.TypeBean;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.ActionCenterResult;
import com.android.bluetown.utils.Constant;

/**
 * 
 * @ClassName: ActionCenterActivity
 * @Description:TODO(HomeActivity-活动中心)
 * @author: shenyz
 * @date: 2015年7月31日 上午10:29:59
 * 
 */
public class ActionCenterActivity extends TitleBarActivity implements
		OnHeaderRefreshListener, OnFooterLoadListener, OnItemClickListener,
		OnClickListener, TagCloudView.OnTagClickListener {
	private AbPullToRefreshView mAbPullToRefreshView;
	private ListView mListView;
	private ActionCenterListAdapter adapter;
	private ArrayList<ActionCenterItemBean> list;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 10;
	private List<TypeBean> typeList;
	private List<String> companyTypes;
	private String tid;
	private TagCloudView tagCloudView;
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
		setTitleLayoutBg(R.color.title_bg_blue);
		setTitleView(R.string.action_center);
		righTextLayout.setVisibility(View.INVISIBLE);

	}

	/**
	 * 
	 * @Title: initUIView
	 * @Description: TODO(初始化界面組件)
	 * @throws
	 */
	private void initUIView() {
		try {
			String push = getIntent().getStringExtra("push");
			if (!TextUtils.isEmpty(push)) {
				// 点击查看清空活动中心的推送的消息数
				if (BlueTownApp.actionMsgCount != 0) {
					BlueTownApp.actionMsgCount = 0;
					Intent refreshintent = new Intent(
							"com.android.bm.refresh.new.msg.action");
					sendBroadcast(refreshintent);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		prefUtils = new SharePrefUtils(this);
		list = new ArrayList<ActionCenterItemBean>();
		companyTypes = new ArrayList<String>();
		LayoutInflater inflater = LayoutInflater.from(this);
		mListView = (ListView) findViewById(R.id.companyInfoList);
		mAbPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		View view = inflater.inflate(R.layout.action_center_top, null);
		view.setBackgroundColor(getResources().getColor(R.color.app_bg_color));
		TextView searchView = (TextView) view.findViewById(R.id.searchView);
		tagCloudView = (TagCloudView) view.findViewById(R.id.tag_cloud_view);
		tagCloudView.setBackgroundColor(getResources().getColor(R.color.white));
		tagCloudView.setOnTagClickListener(this);
		mListView.addHeaderView(view);
		mListView.setAdapter(null);
		mListView.setHeaderDividersEnabled(false);
		mListView.setFooterDividersEnabled(false);
		mListView.setOnItemClickListener(this);
		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);
		searchView.setOnClickListener(this);
		adapter = new ActionCenterListAdapter(this, "main");
		adapter.setData(list);
		mListView.setAdapter(adapter);
	}

	private void getData() {
		params.put("tid", tid);
		params.put("gardenId",
				prefUtils.getString(SharePrefUtils.GARDEN_ID, ""));
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
							if (typeList == null) {
								typeList = result.getData().getTypeList();
							}
							if (companyTypes != null
									&& companyTypes.size() == 0) {
								for (int j = 0; j < typeList.size(); j++) {
									companyTypes.add(typeList.get(j)
											.getTypeName());
								}
								tagCloudView.setTags(companyTypes);
							}
							Message message = mHandler.obtainMessage();
							message.obj = result;
							mHandler.sendMessage(message);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							runOnUiThread(new Runnable() {
								public void run() {
									if (list != null && list.size() > 0) {
										mAbPullToRefreshView
												.onHeaderRefreshFinish();
										mAbPullToRefreshView
												.onFooterLoadFinish();
									}

									Toast.makeText(ActionCenterActivity.this,
											R.string.no_data, Toast.LENGTH_LONG)
											.show();
								}
							});
						}
						mAbPullToRefreshView.onFooterLoadFinish();
						mAbPullToRefreshView.onHeaderRefreshFinish();
					}

					@Override
					public void onFailure(int i, String s, Throwable throwable) {
						super.onFailure(i, s, throwable);
						mAbPullToRefreshView.onFooterLoadFinish();
						mAbPullToRefreshView.onHeaderRefreshFinish();
					}
				});

	}

	protected void dealResult(ActionCenterResult result) {
		switch (listStatus) {
		case Constant.ListStatus.LOAD:
			list.addAll(result.getData().getAction().getRows());
//			mAbPullToRefreshView.onFooterLoadFinish();
			break;
		case Constant.ListStatus.INIT:
			list.clear();
			list.addAll(result.getData().getAction().getRows());
			break;
		case Constant.ListStatus.REFRESH:
			list.clear();
			list.addAll(result.getData().getAction().getRows());
//			mAbPullToRefreshView.onHeaderRefreshFinish();
			break;
		}
		adapter.setData(list);
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
	public void onTagClick(int position) {
		// TODO Auto-generated method stub
		if (position == -1) {
			Toast.makeText(getApplicationContext(), "沒有这个标签",
					Toast.LENGTH_SHORT).show();
		} else {
			tid = typeList.get(position).getTid();
			if (list != null && list.size() > 0) {
				list.clear();
			}
			page = 1;
			getData();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (arg2 != 0) {
			if (list != null && list.size() > 0) {
				Intent intent = new Intent();
				intent.putExtra("aid", list.get(arg2 - 1).getAid());
				intent.setClass(ActionCenterActivity.this,
						ActionCenterDetailsActivity.class);
				startActivity(intent);
			}

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.searchView:
			startActivity(new Intent(ActionCenterActivity.this,
					ActionCenterSearchActivity.class));
			break;

		default:
			break;
		}
	}

}
