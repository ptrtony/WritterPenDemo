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
import com.android.bluetown.adapter.CompanyShowListAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.CompanyShowItemBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.home.main.model.act.CompanyDetailsActivity;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.listener.OnCompanyChangeListener;
import com.android.bluetown.result.CompanyShowResult;
import com.android.bluetown.utils.Constant;

/**
 * 
 * @ClassName: CompanyActivity
 * @Description:TODO(我关注的企业)
 * @author: shenyz
 * @date: 2015年9月11日 下午3:19:10
 * 
 */
public class CompanyActivity extends TitleBarActivity implements
		OnItemClickListener, OnHeaderRefreshListener, OnFooterLoadListener,
		OnCompanyChangeListener {
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
	private String userId,gardenId;
	public Handler handler = new Handler();
	
	Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int position = msg.arg1;
			switch (msg.what) {
			case CompanyShowItemBean.COLLECT_COMPANY:
				CompanyShowItemBean company = list.get(position);
				company.setIsCollect("1");
				onCompanyCollect(position, company);
				break;
			case CompanyShowItemBean.CANCEL_COLLECT_COMPANY:
				list.remove(position);
				adapter.notifyDataSetChanged();
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
		getCompanyShow();
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
		setTitleView("我关注的企业");
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
		BlueTownApp.setHanler(hander);
		list = new ArrayList<CompanyShowItemBean>();
		
		mListView = (ListView) findViewById(R.id.companyInfoList);
		mRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView.setOnItemClickListener(this);
		mRefreshView.setOnHeaderRefreshListener(this);
		mRefreshView.setOnFooterLoadListener(this);
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void getCompanyShow() {
		/**
		 * longitude 经度(必填) latitude 维度(必填) userId 用户id(必填) filterMine
		 * 代表关注我的企业(必填1)
		 */
		params.put("userId", userId);
		params.put("gardenId",gardenId);
		params.put("filterMine", "1");
		params.put("longitude", longitude + "");
		params.put("latitude", latitude + "");
		params.put("page", page + "");
		params.put("rows", size + "");
		httpInstance.post(Constant.HOST_URL + Constant.Interface.COMPANY_SHOW,
				params, new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						CompanyShowResult result = (CompanyShowResult) AbJsonUtil
								.fromJson(s, CompanyShowResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							mListView.setVisibility(View.VISIBLE);
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							if (mRefreshView != null) {
								mRefreshView.onFooterLoadFinish();
								mRefreshView.onHeaderRefreshFinish();
							}
							if (adapter != null) {
								adapter.notifyDataSetChanged();
							}
							Toast.makeText(CompanyActivity.this,
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
		adapter = new CompanyShowListAdapter(this, list, this);
		mListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onCompanyCollect(int position, CompanyShowItemBean company) {
		// TODO Auto-generated method stub
		String isCollect = company.getIsCollect();
		if (isCollect.equals("1")) {
			list.set(position, company);
			adapter.notifyDataSetChanged();
		} else {
			list.remove(position);
			adapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onFooterLoad(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		listStatus = Constant.ListStatus.LOAD;
		page++;
		getCompanyShow();
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		list.clear();
		listStatus = Constant.ListStatus.REFRESH;
		page = 1;
		getCompanyShow();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		CompanyShowItemBean companyshowitembean = (CompanyShowItemBean) parent
				.getItemAtPosition(position);
		Intent intent = new Intent(CompanyActivity.this,
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
