package com.android.bluetown.home.main.model.act;

import java.util.ArrayList;
import java.util.List;

import me.next.tagview.TagCloudView;
import me.next.tagview.TagCloudView.OnTagClickListener;
import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.android.bluetown.bean.LocationInfo;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.QueryBusinessTypeListBean;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.listener.OnCompanyChangeListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.CompanyShowResult;
import com.android.bluetown.result.QueryBusinessTypeListData;
import com.android.bluetown.utils.Constant;


/**
 * 
 * @ClassName: CompanyShowActivity
 * @Description:TODO(HomeActivity-企业展示)
 * @author: shenyz
 * @date: 2015年7月31日 上午10:02:52
 * 
 */
public class CompanyShowActivity extends TitleBarActivity implements
		OnClickListener, OnHeaderRefreshListener, OnFooterLoadListener,
		OnItemClickListener, OnCompanyChangeListener {
	private ListView mListView;
	private AbPullToRefreshView mPullRefreshView;
	private CompanyShowListAdapter adapter;
	private List<CompanyShowItemBean> list;
	private List<QueryBusinessTypeListBean> typeList;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 10;
	public Handler handler = new Handler();
	// 企业名称
	private String enterpriseId;
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
		setRightImageView(R.drawable.classify_1);
		setTitleLayoutBg(R.color.title_bg_blue);
		searchBtnLy.setVisibility(View.VISIBLE);
		searchBtnLy.setOnClickListener(this);
		rightImageLayout.setOnClickListener(this);
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
		db = FinalDb.create(this);
		getUserId();
		list = new ArrayList<CompanyShowItemBean>();
		mListView = (ListView) findViewById(R.id.companyInfoList);
		mPullRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView.setOnItemClickListener(this);
		mPullRefreshView.setOnHeaderRefreshListener(this);
		mPullRefreshView.setOnFooterLoadListener(this);
	    List<LocationInfo> infos = db.findAll(LocationInfo.class);
		if (infos != null && infos.size() > 0) {
			LocationInfo info = infos.get(0);
			if (info != null) {
				latitude = info.getLatitude();
				longitude = info.getLongitude();
			}
		}

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

	/**
	 * 选择企业类型
	 * 
	 * @param zones
	 */
	private void selectCompanyType(List<String> zones) {
		LayoutInflater inflater = LayoutInflater.from(this);
		// 引入窗口配置文件
		View view = inflater.inflate(R.layout.home_select_zone, null);
		// 创建PopupWindow对象
		final PopupWindow pop = new PopupWindow(view,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, false);
		final TagCloudView selecTagCloudView = (TagCloudView) view
				.findViewById(R.id.tag_cloud_view);
		LinearLayout blankLy = (LinearLayout) view.findViewById(R.id.blankLy);
		selecTagCloudView.setTags(zones);
		// 需要设置一下此参数，点击外边可消失
		pop.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失
		pop.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击
		pop.setFocusable(true);
		pop.showAsDropDown(titleLayout);
		blankLy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
			}
		});
		selecTagCloudView.setOnTagClickListener(new OnTagClickListener() {

			@Override
			public void onTagClick(int arg0) {
				// TODO Auto-generated method stub
				enterpriseId = typeList.get(arg0).getSid();
				if (list != null && list.size() > 0) {
					list.clear();
				}
				page = 1;
				getCompanyShow();
				pop.dismiss();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.searchBtnLy:
			Intent intent = new Intent();
			intent.putExtra("userId", userId);
			intent.putExtra("latitude", latitude);
			intent.putExtra("longitude", longitude);
			intent.setClass(CompanyShowActivity.this,
					CompanyShowSearchActivity.class);
			startActivity(intent);
			break;
		case R.id.rightImageLayout:
			getQueryBusinessTypeList();
			break;

		default:
			break;
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

	private void getQueryBusinessTypeList() {
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.QUERYBUSINESS_TYPELIST, params,
				new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						QueryBusinessTypeListData result = (QueryBusinessTypeListData) AbJsonUtil
								.fromJson(s, QueryBusinessTypeListData.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							List<String> companyTypes = new ArrayList<String>();
							for (int j = 0; j < result.getData().size(); j++) {
								companyTypes.add(result.getData().get(j)
										.getTypeName());
							}

							typeList = result.getData();
							selectCompanyType(companyTypes);
						}

					}
					@Override
					public void onFailure(int arg0, String arg1, Throwable arg2) {
						// TODO Auto-generated method stub
						mPullRefreshView.onFooterLoadFinish();
						mPullRefreshView.onHeaderRefreshFinish();
					}
				});

	}

	private void getCompanyShow() {
		/** userid为非必填项 */
		params.put("userId", userId);
		params.put("gardenId",
				prefUtils.getString(SharePrefUtils.GARDEN_ID, ""));
		// 企业查询的时候使用
		params.put("companyName", "");
		// 企业战士列表的时候使用
		params.put("enterpriseId", enterpriseId);
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
							if (mPullRefreshView != null) {
								mPullRefreshView.onHeaderRefreshFinish();
								mPullRefreshView.onFooterLoadFinish();
								if (adapter != null) {
									adapter.notifyDataSetChanged();
								}
							}
							Toast.makeText(CompanyShowActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(CompanyShowActivity.this,
									result.getRepMsg(), Toast.LENGTH_LONG)
									.show();
						}

					}
					@Override
					public void onFailure(int arg0, String arg1, Throwable arg2) {
						// TODO Auto-generated method stub
						mPullRefreshView.onFooterLoadFinish();
						mPullRefreshView.onHeaderRefreshFinish();
					}
				});
	}

	protected void dealResult(CompanyShowResult result) {
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
			adapter = new CompanyShowListAdapter(CompanyShowActivity.this,
					list, this);
			mListView.setAdapter(adapter);
		}
		adapter.notifyDataSetChanged();

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
		Intent intent = new Intent(CompanyShowActivity.this,
				CompanyDetailsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("latitude",companyshowitembean.getLatitude());
		bundle.putString("longitude",companyshowitembean.getLongitude());
		bundle.putString("bid", companyshowitembean.getBid());
		bundle.putString("companyName",companyshowitembean.getCompanyName());
		bundle.putString("describe",companyshowitembean.getAddress());
		bundle.putInt("position", position);
		intent.putExtras(bundle);
		startActivity(intent,bundle);

	}

}
