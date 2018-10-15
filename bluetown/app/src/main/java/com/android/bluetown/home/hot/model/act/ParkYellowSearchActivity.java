package com.android.bluetown.home.hot.model.act;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
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
import com.android.bluetown.adapter.ParkYellowPageListAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.ParkYellowPageItemBean;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.YellowPageResult;
import com.android.bluetown.utils.Constant;

/**
 * 
 * @ClassName: ParkYellowPageActivity
 * @Description:TODO(HomeActivity-园区黄页--黄页搜索)
 * @author: shenyz
 * @date: 2015年7月31日 上午10:38:28
 * 
 */
public class ParkYellowSearchActivity extends TitleBarActivity implements
		OnHeaderRefreshListener, OnFooterLoadListener, OnItemClickListener {
	private ListView mListView;
	private AbPullToRefreshView pullToRefreshView;
	private ParkYellowPageListAdapter adapter;
	private List<ParkYellowPageItemBean> list;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 10;
	private double latitude, longitude;
	private SharePrefUtils prefUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_park_yellow_page);
		BlueTownExitHelper.addActivity(this);
		initUIView();
	}

	private void initUIView() {
		prefUtils = new SharePrefUtils(this);
		list = new ArrayList<ParkYellowPageItemBean>();
		latitude = getIntent().getDoubleExtra("latitude", 0.0);
		longitude = getIntent().getDoubleExtra("longitude", 0.0);
		pullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView = (ListView) findViewById(R.id.parkYellowInfoList);
		pullToRefreshView.setOnHeaderRefreshListener(this);
		pullToRefreshView.setOnFooterLoadListener(this);
		mListView.setOnItemClickListener(this);

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
		setSearchView(R.string.lookfor_content_hint);
		setRighTextView(R.string.search);
		setTitleLayoutBg(R.color.title_bg_blue);
		righTextLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(searchView.getText().toString())) {
					TipDialog.showDialog(ParkYellowSearchActivity.this,
							R.string.tip, R.string.confirm,
							R.string.search_empty);
					return;
				}
				if (list != null) {
					list.clear();
				}
				page = 1;
				getData(searchView.getText().toString());
			}
		});

	}

	private void getData(String searchValue) {
		/**
		 * longitude(必填) latitude(必填) tid黄页类型id(可填)
		 */
		params.put("tid", "");
		params.put("gardenId",
				prefUtils.getString(SharePrefUtils.GARDEN_ID, ""));
		params.put("searchValue", searchValue);
		params.put("longitude", longitude + "");
		params.put("latitude", latitude + "");
		params.put("page", page + "");
		params.put("rows", size + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.YELLOW_PAGE_LIST, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						YellowPageResult result = (YellowPageResult) AbJsonUtil
								.fromJson(s, YellowPageResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							if (pullToRefreshView != null) {

								pullToRefreshView.onHeaderRefreshFinish();
								pullToRefreshView.onFooterLoadFinish();
							}
							Toast.makeText(ParkYellowSearchActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();

						}

					}
				});
	}

	protected void dealResult(YellowPageResult result) {
		switch (listStatus) {
		case Constant.ListStatus.LOAD:
			list.addAll(result.getData().getRows());
			pullToRefreshView.onFooterLoadFinish();

			break;
		case Constant.ListStatus.INIT:
			list.clear();
			list.addAll(result.getData().getRows());
			break;
		case Constant.ListStatus.REFRESH:
			list.clear();
			list.addAll(result.getData().getRows());
			pullToRefreshView.onHeaderRefreshFinish();

			break;
		}

		if (adapter == null) {
			adapter = new ParkYellowPageListAdapter(this, list);
			mListView.setAdapter(adapter);
		}
		adapter.notifyDataSetChanged();
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
		listStatus = Constant.ListStatus.REFRESH;
		if (list != null) {
			list.clear();
		}
		page = 1;
		getData(null);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("content", list.get(position).getScontent());
		intent.setClass(ParkYellowSearchActivity.this,
				ParkYellowDetailsActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
