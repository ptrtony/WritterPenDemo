package com.android.bluetown.home.hot.model.act;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.WebViewActivity;
import com.android.bluetown.adapter.CompanyGrowHelpListAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.CompanyGrowHelpBean;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.CompanyGroupHelpResult;
import com.android.bluetown.utils.Constant;

/**
 * 
 * @ClassName: CompanyGrowHelpActivity
 * @Description:TODO(HomeActivity-企业成长帮助)
 * @author: shenyz
 * @date: 2015年7月31日 上午10:33:44
 * 
 */
public class CompanyGrowHelpActivity extends TitleBarActivity implements
		OnClickListener, OnItemClickListener, OnHeaderRefreshListener,
		OnFooterLoadListener {
	private Button job51Btn, jobToJobBtn, ningboCityBtn;
	private ListView mListView;
	private AbPullToRefreshView mPullRefreshView;
	private LayoutInflater mInflater;
	private CompanyGrowHelpListAdapter adapter;
	private List<CompanyGrowHelpBean> list;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 10;
	private SharePrefUtils prefUtils;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_company_grow_help);
		BlueTownExitHelper.addActivity(this);
		initUIViews();
		getData();
	}

	private void initUIViews() {
		prefUtils = new SharePrefUtils(this);
		list = new ArrayList<CompanyGrowHelpBean>();
		mInflater = LayoutInflater.from(this);
		View view = mInflater.inflate(R.layout.company_grow_help_top, null);
		job51Btn = (Button) view.findViewById(R.id.jobBtn);
		jobToJobBtn = (Button) view.findViewById(R.id.jobToJob);
		ningboCityBtn = (Button) view.findViewById(R.id.ningbo);
		mPullRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView = (ListView) findViewById(R.id.companyGrowHelpInfoList);
	
		mListView.addHeaderView(view);
		job51Btn.setOnClickListener(this);
		jobToJobBtn.setOnClickListener(this);
		ningboCityBtn.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
		mPullRefreshView.setOnFooterLoadListener(this);
		mPullRefreshView.setOnHeaderRefreshListener(this);

	}

	/**
	 * 
	 * @Title: initTitle
	 * @Description: 初始化标题栏
	 * @see com.android.bluetown.activity.TitleBarActivity#initTitle()
	 */
	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.company_growup_help);
		rightImageLayout.setVisibility(View.INVISIBLE);
		setTitleLayoutBg(R.color.title_bg_blue);
	}

	/**
	 * 
	 * @Title: getData
	 * @Description: TODO(page 当前页，默认第1页 （可填项） rows 每页多少条数据，默认10条（可填项） tid:商品类型；
	 *               commodityName：商品名称)
	 * @throws
	 */
	private void getData() {
		// TODO Auto-generated method stub
		params.put("gardenId",
				prefUtils.getString(SharePrefUtils.GARDEN_ID, ""));
		params.put("page", page + "");
		params.put("rows", size + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.COMPANY_GROUP_INFO_LIST, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						CompanyGroupHelpResult result = (CompanyGroupHelpResult) AbJsonUtil
								.fromJson(s, CompanyGroupHelpResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
						    if (mPullRefreshView != null) {
								mPullRefreshView.onHeaderRefreshFinish();
								mPullRefreshView.onFooterLoadFinish();
							}
							Toast.makeText(CompanyGrowHelpActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();

						}

					}
				});
	}

	protected void dealResult(CompanyGroupHelpResult result) {
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
			adapter = new CompanyGrowHelpListAdapter(this, list);
			mListView.setAdapter(adapter);
		}
		adapter.notifyDataSetChanged();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.jobBtn:
			intent.putExtra("URL", "http://ehire.51job.com/");
			intent.putExtra("title", getString(R.string.job_51));
			intent.setClass(CompanyGrowHelpActivity.this, WebViewActivity.class);
			startActivity(intent);
			break;
		case R.id.jobToJob:
			intent.putExtra("URL", "http://zlzw.zhaopin.com/");
			intent.putExtra("title", getString(R.string.job_to_job));
			intent.setClass(CompanyGrowHelpActivity.this, WebViewActivity.class);
			startActivity(intent);
			break;
		case R.id.ningbo:
			intent.putExtra("URL", "http://gtog.ningbo.gov.cn/");
			intent.putExtra("title", getString(R.string.ninbo_city));
			intent.setClass(CompanyGrowHelpActivity.this, WebViewActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (position != 0) {
			Intent intent = new Intent();
			intent.putExtra("content", list.get(position - 1).getContent());
			intent.putExtra("title", list.get(position - 1).getTitle());
			intent.putExtra("date", list.get(position - 1).getCreateTime());
			intent.setClass(CompanyGrowHelpActivity.this,
					CompanyGrowHelpDetailActivity.class);
			startActivity(intent);
		}
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

}
