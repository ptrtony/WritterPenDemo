package com.android.bluetown;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.adapter.CompanyInfoPublishAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.CompanyInfoPublishBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.home.hot.model.act.DemandDetailsActivity;
import com.android.bluetown.leftslide.listview.SwipeMenu;
import com.android.bluetown.leftslide.listview.SwipeMenuCreator;
import com.android.bluetown.leftslide.listview.SwipeMenuItem;
import com.android.bluetown.leftslide.listview.SwipeMenuListView;
import com.android.bluetown.leftslide.listview.SwipeMenuListView.OnMenuItemClickListener;
import com.android.bluetown.leftslide.listview.SwipeMenuListView.OnSwipeListener;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.CompanyInfoPublishResult;
import com.android.bluetown.result.UserOperationResult;
import com.android.bluetown.utils.Constant;

/**
 * 
 * @ClassName: CompanyInformationActivity
 * @Description:TODO(我收藏的企业需求)
 * @author: shenyz
 * @date: 2015年8月25日 上午9:47:54
 * 
 */
public class CompanyInformationActivity extends TitleBarActivity implements
		OnHeaderRefreshListener, OnFooterLoadListener, OnItemClickListener,
		SwipeMenuCreator, OnMenuItemClickListener, OnSwipeListener {
	private SwipeMenuListView mListView;
	private AbPullToRefreshView mPullRefreshView;
	private CompanyInfoPublishAdapter mAdapter;
	private List<CompanyInfoPublishBean> list;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 10;
	private String userId, gardenId;
	Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int position = msg.arg1;
			CompanyInfoPublishBean demand = list.get(position);
			switch (msg.what) {
			case CompanyInfoPublishBean.DEMAND_COLLECT:
				demand.setIsCollect("1");
				list.set(position, demand);
				mAdapter.notifyDataSetChanged();
				break;
			case CompanyInfoPublishBean.CANCEL_DEMAND_COLLECT:
				list.remove(demand);
				mAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.activity_companyinformation);
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
	public void initTitle() {// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("我收藏的企业需求");
		setTitleLayoutBg(R.color.title_bg_blue);
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/**
	 * 
	 * @Title: initUIView
	 * @Description: TODO(初始化界面組件)
	 * @throws
	 */
	private void initUIView() {
		BlueTownApp.setHanler(hander);
		list = new ArrayList<CompanyInfoPublishBean>();
		mListView = (SwipeMenuListView) findViewById(R.id.companyInfoList);
		mPullRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView.setOnItemClickListener(this);
		// step 1. create a MenuCreator and set creator
		mListView.setMenuCreator(this);
		// step 2. listener item click event
		mListView.setOnMenuItemClickListener(this);
		// set SwipeListener
		mListView.setOnSwipeListener(this);
		mListView.setHeaderDividersEnabled(false);
		mListView.setFooterDividersEnabled(false);
		mPullRefreshView.setOnHeaderRefreshListener(this);
		mPullRefreshView.setOnFooterLoadListener(this);
		FinalDb db = FinalDb.create(this);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
				gardenId = user.getGardenId();
			}
		}
	}

	/**
	 * 
	 * @Title: getData
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @throws
	 */
	private void getData() {
		// TODO Auto-generated method stub
		/**
		 * userId 用户id filterMine 查询我收藏的企业需求关键字(值为1)
		 */
		params.put("searchValue", "");
		params.put("userId", userId);
		params.put("gardenId", gardenId);
		params.put("filterMine", "1");
		params.put("page", page + "");
		params.put("rows", size + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.COMPANY_DEMAND_PUBLISH_LIST, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						CompanyInfoPublishResult result = (CompanyInfoPublishResult) AbJsonUtil
								.fromJson(s, CompanyInfoPublishResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							mListView.setVisibility(View.VISIBLE);
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							runOnUiThread(new Runnable() {
								public void run() {
									if (list != null && list.size() == 0) {
										mListView.setVisibility(View.GONE);
									}
									mPullRefreshView.onFooterLoadFinish();
									mPullRefreshView.onHeaderRefreshFinish();
									if (mAdapter != null) {
										mAdapter.notifyDataSetChanged();
									}
									Toast.makeText(
											CompanyInformationActivity.this,
											R.string.no_data, Toast.LENGTH_LONG)
											.show();
								}
							});
						}

					}
					@Override
					public void onFailure(int arg0, String arg1, Throwable arg2) {
						// TODO Auto-generated method stub
						mPullRefreshView.onHeaderRefreshFinish();
						mPullRefreshView.onFooterLoadFinish();
					}
				});
	}

	protected void dealResult(CompanyInfoPublishResult result) {
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
		if (mAdapter == null) {
			mAdapter = new CompanyInfoPublishAdapter(this, list);
			mListView.setAdapter(mAdapter);
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (list != null && list.size() > 0) {
			Intent intent = new Intent();
			intent.setClass(CompanyInformationActivity.this,
					DemandDetailsActivity.class);
			intent.putExtra("infodetails", list.get(position));
			intent.putExtra("position", position);
			startActivity(intent);
		}

	}

	@Override
	public void create(SwipeMenu menu) {
		// TODO Auto-generated method stub
		// create "cancel collect" item
		SwipeMenuItem collectItem = new SwipeMenuItem(this);
		// set item background
		collectItem.setBackground(R.drawable.cancel_collect_bg);
		// set item width
		collectItem.setWidth(LayoutParams.WRAP_CONTENT);
		// set item title
		collectItem.setTitle(getString(R.string.cancel_collect_success));
		// set item title fontsize
		collectItem.setTitleSize(16);
		// set item title font color
		collectItem.setTitleColor(Color.WHITE);
		// add to menu
		menu.addMenuItem(collectItem);
	}

	@Override
	public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
		// TODO Auto-generated method stub
		cancelCollectCompany(userId, list.get(position));

		return false;
	}

	private void cancelCollectCompany(String userId,
			final CompanyInfoPublishBean item) {
		/*
		 * userId 用户id actionID 收藏(关注或点赞)对象的id actionType 5：关注，6：收藏，7：点赞 8加入圈子
		 */
		params.put("userId", userId);
		params.put("actionId", item.getNid());
		params.put("actionType", Constant.COLLOCT + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.CIRCLE_TYPEW_INFO, params,
				new AbsHttpStringResponseListener(
						CompanyInformationActivity.this, null) {
					@Override
					public void onSuccess(int i, String s) {
						UserOperationResult result = (UserOperationResult) AbJsonUtil
								.fromJson(s, UserOperationResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							if (!result.getData().equals("关注成功")) {
								list.remove(item);
								mAdapter.notifyDataSetChanged();
								if (list != null && list.size() == 0) {
									mListView.setVisibility(View.GONE);
								}
								Toast.makeText(CompanyInformationActivity.this,
										R.string.cancel_success,
										Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(CompanyInformationActivity.this,
									result.getRepMsg(), Toast.LENGTH_SHORT)
									.show();
						}

					}
					@Override
					public void onFailure(int arg0, String arg1, Throwable arg2) {
						// TODO Auto-generated method stub
						mPullRefreshView.onHeaderRefreshFinish();
						mPullRefreshView.onFooterLoadFinish();
					}
				});

	}

	@Override
	public void onSwipeEnd(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSwipeStart(int position) {
		// TODO Auto-generated method stub

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
		list.clear();
		// TODO Auto-generated method stub
		listStatus = Constant.ListStatus.REFRESH;
		page = 1;
		getData();
	}

}
