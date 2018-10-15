package com.android.bluetown.home.main.model.act;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.ProductGridAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.ProductBean;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.FleaMarketListResult;
import com.android.bluetown.utils.Constant;

/**
 * 
 * @ClassName: FleaMarketActivity
 * @Description:TODO(HomeActivity-跳蚤市场-搜索)
 * @author: shenyz
 * @date: 2015年7月31日 上午10:26:55
 * 
 */
public class FleaMarketSearchActivity extends TitleBarActivity implements
		OnClickListener, OnItemClickListener, OnHeaderRefreshListener,
		OnFooterLoadListener {
	private GridView productGridView;
	private AbPullToRefreshView mAbPullToRefreshView;
	private ProductGridAdapter myGridViewAdapter = null;
	private ArrayList<ProductBean> mProductList = new ArrayList<ProductBean>();
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
		addContentView(R.layout.ac_flea_market_search);
		BlueTownExitHelper.addActivity(this);
		initUIViews();
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
		setSearchView(R.string.search_content_hint);
		setRighTextView(R.string.search);
		setTitleLayoutBg(R.color.title_bg_blue);
		righTextLayout.setOnClickListener(this);
	}

	private void initUIViews() {
		prefUtils = new SharePrefUtils(this);
		mAbPullToRefreshView = (AbPullToRefreshView) this
				.findViewById(R.id.mPullRefreshView);
		productGridView = (GridView) findViewById(R.id.productGridView);
		productGridView.setOnItemClickListener(this);
		// 设置监听器
		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);

	}

	/**
	 * 
	 * @Title: getData
	 * @Description: TODO(page 当前页，默认第1页 （可填项） rows 每页多少条数据，默认10条（可填项） tid:商品类型；
	 *               commodityName：商品名称)
	 * @throws
	 */
	private void getData(String commodityName) {
		// TODO Auto-generated method stub
		params.put("tid", "");
		params.put("commodityName", commodityName);
		params.put("gardenId",
				prefUtils.getString(SharePrefUtils.GARDEN_ID, ""));
		params.put("page", page + "");
		params.put("rows", size + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.FLEA_MARKERT_LIST, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						FleaMarketListResult result = (FleaMarketListResult) AbJsonUtil
								.fromJson(s, FleaMarketListResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							if (mAbPullToRefreshView != null) {
								mAbPullToRefreshView.onFooterLoadFinish();
								mAbPullToRefreshView.onHeaderRefreshFinish();
							}
							if (myGridViewAdapter != null) {
								Toast.makeText(FleaMarketSearchActivity.this,
										R.string.no_data, Toast.LENGTH_LONG)
										.show();
							}
						}

					}
				});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rightTextLayout:
		if (TextUtils.isEmpty(searchView.getText().toString())) {
				TipDialog.showDialog(FleaMarketSearchActivity.this,
						R.string.tip, R.string.confirm, R.string.search_empty);
				return;
			}
			if (mProductList != null) {
				mProductList.clear();
			}
			page = 1;
			getData(searchView.getText().toString());
			searchView.setText("");
			break;
		default:
			break;
		}
	}

	protected void dealResult(FleaMarketListResult result) {
		if (result.getData().getCommodity().getRows().size() == 0) {
			mAbPullToRefreshView.onFooterLoadFinish();
			mAbPullToRefreshView.onHeaderRefreshFinish();
			Toast.makeText(FleaMarketSearchActivity.this, R.string.no_data,
					Toast.LENGTH_LONG).show();

		} else {
			switch (listStatus) {
			case Constant.ListStatus.LOAD:
				mProductList.addAll(result.getData().getCommodity().getRows());
				mAbPullToRefreshView.onFooterLoadFinish();
				break;
			case Constant.ListStatus.INIT:
				mProductList.clear();
				mProductList.addAll(result.getData().getCommodity().getRows());
				break;
			case Constant.ListStatus.REFRESH:
				mProductList.clear();
				mProductList.addAll(result.getData().getCommodity().getRows());
				mAbPullToRefreshView.onHeaderRefreshFinish();
				break;
			}
			if (myGridViewAdapter == null) {
				// 使用自定义的Adapter
				myGridViewAdapter = new ProductGridAdapter(this, mProductList,
						false);
				productGridView.setAdapter(myGridViewAdapter);
			}
			myGridViewAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("cid", mProductList.get(position).getCid());
		intent.setClass(FleaMarketSearchActivity.this,
				ProductDetailActivity.class);
		startActivity(intent);
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
		page = 1;
		getData(null);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
