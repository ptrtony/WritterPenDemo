package com.android.bluetown.home.hot.model.act;

import java.util.ArrayList;
import java.util.List;

import me.next.tagview.TagCloudView;
import me.next.tagview.TagCloudView.OnTagClickListener;
import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
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
import com.android.bluetown.adapter.CompanyInfoPublishAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.CompanyInfoPublishBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.CompanyInfoPublishResult;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.Utils;

/**
 * 
 * @ClassName: CompanyInfoPublicActivity
 * @Description:TODO(HomeActivity-企业需求发布)
 * @author: shenyz
 * @date: 2015年7月31日 上午10:34:18
 * 
 */
public class CompanyInfoPublicActivity extends TitleBarActivity implements
		OnClickListener, OnItemClickListener, OnHeaderRefreshListener,
		OnFooterLoadListener {
	private Button newPublish;
	private ListView publishInfoList;
	private AbPullToRefreshView mPullToRefreshView;
	private List<CompanyInfoPublishBean> list;
	private CompanyInfoPublishAdapter mAdapter;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 10;
	private String typeId = "";
	public Handler handler = new Handler();
	private SharePrefUtils prefUtils;
	private String userId;
	private FinalDb db;
	private List<MemberUser> users;
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
				demand.setIsCollect("0");
				list.set(position, demand);
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
		addContentView(R.layout.ac_company_info_public);
		BlueTownExitHelper.addActivity(this);
		initUIView();
		getData();

	}

	private void initUIView() {
		BlueTownApp.setHanler(hander);
		db = FinalDb.create(this);
		prefUtils = new SharePrefUtils(this);
		list = new ArrayList<CompanyInfoPublishBean>();
		newPublish = (Button) findViewById(R.id.newPublish);
		mPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		publishInfoList = (ListView) findViewById(R.id.publishInfoList);
		publishInfoList.setOnItemClickListener(this);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterLoadListener(this);
		newPublish.setOnClickListener(this);
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
		setRightImageView(R.drawable.ic_menu);
		setTitleLayoutBg(R.color.title_bg_blue);
		searchBtnLy.setVisibility(View.VISIBLE);
		searchBtnLy.setOnClickListener(this);
		rightImageLayout.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getUserId();
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
	 * 
	 * @Title: getData
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @throws
	 */
	private void getData() {
		// TODO Auto-generated method stub
		params.put("userId", userId);
		params.put("gardenId",
				prefUtils.getString(SharePrefUtils.GARDEN_ID, ""));
		params.put("typeId", typeId + "");
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
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {

							if (mPullToRefreshView != null) {
								mPullToRefreshView.onFooterLoadFinish();
								mPullToRefreshView.onHeaderRefreshFinish();
							}
							if (mAdapter != null) {
								mAdapter.notifyDataSetChanged();
							}
							Toast.makeText(CompanyInfoPublicActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();

						}

					}
				});
	}

	protected void dealResult(CompanyInfoPublishResult result) {
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

		if (mAdapter == null) {
			// 使用自定义的Adapter
			mAdapter = new CompanyInfoPublishAdapter(this, list);
			publishInfoList.setAdapter(mAdapter);
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.newPublish:
			startActivity(new Intent(CompanyInfoPublicActivity.this,
					PublishNewDemandActivity.class));
			break;
		case R.id.rightImageLayout:
			List<String> tagsList = Utils.getCompanyPublicInfoTypeList();
			selectTypePop(tagsList, titleLayout);
			break;
		case R.id.searchBtnLy:
			startActivity(new Intent(CompanyInfoPublicActivity.this,
					SearchDemandActivity.class));
			break;
		default:
			break;
		}
	}

	private void selectTypePop(List<String> tagsList, LinearLayout layout) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(this);
		// 引入窗口配置文件
		View view = inflater.inflate(R.layout.type_select_layout, null);
		// 创建PopupWindow对象
		final PopupWindow pop = new PopupWindow(view,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, false);
		final TagCloudView selecTagCloudView = (TagCloudView) view
				.findViewById(R.id.tag_cloud_view);
		final LinearLayout typeBlankLy = (LinearLayout) view
				.findViewById(R.id.typeBlankLy);
		selecTagCloudView.setTags(tagsList);
		// 需要设置一下此参数，点击外边可消失
		pop.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失
		pop.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击
		pop.setFocusable(true);
		if (Build.VERSION.SDK_INT >= 24) {
			Rect visibleFrame = new Rect();
			layout.getGlobalVisibleRect(visibleFrame);
			int height = layout.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
			pop.setHeight(height);
			pop.showAsDropDown(layout);
		} else {
			pop.showAsDropDown(layout);
		}
		typeBlankLy.setOnClickListener(new OnClickListener() {

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
				if (arg0 == 0) {
					typeId = "";
				} else {
					typeId = (arg0 - 1) + "";
				}
				if (list != null && list.size() > 0) {
					list.clear();
				}
				page = 1;
				getData();
				pop.dismiss();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(CompanyInfoPublicActivity.this,
				DemandDetailsActivity.class);
		intent.putExtra("infodetails", list.get(position));
		intent.putExtra("position", position);
		startActivity(intent);

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
