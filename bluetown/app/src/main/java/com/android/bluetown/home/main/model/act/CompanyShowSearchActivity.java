package com.android.bluetown.home.main.model.act;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
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
import com.android.bluetown.adapter.CompanyShowListAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.CompanyShowItemBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.listener.OnCompanyChangeListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.CompanyShowResult;
import com.android.bluetown.utils.Constant;

/**
 * 
 * @ClassName: CompanyShowSearchActivity
 * @Description:TODO(HomeActivity-企业展示-搜索)
 * @author: shenyz
 * @date: 2015年7月31日 上午10:02:52
 * 
 */
public class CompanyShowSearchActivity extends TitleBarActivity implements
		OnClickListener, OnHeaderRefreshListener, OnFooterLoadListener,
		OnItemClickListener, OnCompanyChangeListener {
	/** 企业信息展示列表的ListView */
	private ListView mListView;
	private AbPullToRefreshView mRefreshView;
	private CompanyShowListAdapter adapter;
	private List<CompanyShowItemBean> list;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 10;
	private double latitude = 0.0;
	private double longitude = 0.0;
	private String userId;
	private FinalDb db;
	private List<MemberUser> users;
	private SharePrefUtils prefUtils;
	Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int position = msg.arg1;
			CompanyShowItemBean company = list.get(position);
			switch (msg.what) {
			case CompanyShowItemBean.COLLECT_COMPANY:
				company.setIsCollect("1");
				onCompanyCollect(position, company);
				break;
			case CompanyShowItemBean.CANCEL_COLLECT_COMPANY:
				company.setIsCollect("0");
				onCompanyCollect(position, company);
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
		addContentView(R.layout.ac_company_show);
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
		setSearchView(R.string.company_name_search_hint);
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
		BlueTownApp.setHanler(hander);
		prefUtils = new SharePrefUtils(this);

		userId = getIntent().getStringExtra("userId");
		latitude = getIntent().getDoubleExtra("latitude", 0.0);
		longitude = getIntent().getDoubleExtra("longitude", 0.0);
		db = FinalDb.create(this);
		getUserId();
		list = new ArrayList<CompanyShowItemBean>();
		mRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView = (ListView) findViewById(R.id.companyInfoList);
		mListView.setOnItemClickListener(this);
		mRefreshView.setOnHeaderRefreshListener(this);
		mRefreshView.setOnFooterLoadListener(this);
	}

	/**
	 * 获取用户id
	 */
	private void getUserId() {
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}

		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (TextUtils.isEmpty(userId)) {
			getUserId();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rightTextLayout:
			if (TextUtils.isEmpty(searchView.getText().toString())) {
				TipDialog.showDialog(CompanyShowSearchActivity.this,
						R.string.tip, R.string.confirm, R.string.search_empty);
				return;
			}
			if (list != null && list.size() > 0) {
				page = 1;
				list.clear();
			}
			getCompanyShow(searchView.getText().toString());
			break;

		default:
			break;
		}
	}

	private void getCompanyShow(String search) {
		params.put("userId", userId);
		params.put("gardenId",
				prefUtils.getString(SharePrefUtils.GARDEN_ID, ""));
		// 企业查询的时候使用
		params.put("companyName", search);
		// 企业战士列表的时候使用
		params.put("enterpriseId", "");
		params.put("longitude", longitude + "");
		params.put("latitude", latitude + "");
		params.put("page", page + "");
		params.put("size", size + "");
		httpInstance.post(Constant.HOST_URL + Constant.Interface.COMPANY_SHOW,
				params, new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						CompanyShowResult result = (CompanyShowResult) AbJsonUtil
								.fromJson(s, CompanyShowResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							if (mRefreshView != null) {
								mRefreshView.onHeaderRefreshFinish();
								mRefreshView.onFooterLoadFinish();
								if (adapter != null) {
									adapter.notifyDataSetChanged();
								}
							}
							Toast.makeText(CompanyShowSearchActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();
						}

					}
				});
	}

	protected void dealResult(CompanyShowResult result) {
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
			adapter = new CompanyShowListAdapter(this, list, this);
			mListView.setAdapter(adapter);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onFooterLoad(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		listStatus = Constant.ListStatus.LOAD;
		page++;
		getCompanyShow(searchView.getText().toString());
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		list.clear();
		listStatus = Constant.ListStatus.REFRESH;
		page = 1;
		getCompanyShow(searchView.getText().toString());
	}

	@Override
	public void onCompanyCollect(int position, CompanyShowItemBean company) {
		// TODO Auto-generated method stub
		list.set(position, company);
		adapter.notifyDataSetChanged();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		CompanyShowItemBean companyshowitembean = (CompanyShowItemBean) parent
				.getItemAtPosition(position);
		Intent intent = new Intent(CompanyShowSearchActivity.this,
				CompanyDetailsActivity.class);
		intent.putExtra("bid", companyshowitembean.getBid());
		intent.putExtra("position", position);
		startActivity(intent);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
