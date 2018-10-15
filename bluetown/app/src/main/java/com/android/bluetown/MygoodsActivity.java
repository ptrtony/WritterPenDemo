package com.android.bluetown;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.adapter.ProductGridAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.ProductBean;
import com.android.bluetown.home.main.model.act.ProductDetailActivity;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.MyCollectProductResult;
import com.android.bluetown.utils.Constant;

//我发布的商品
public class MygoodsActivity extends TitleBarActivity implements
		OnItemClickListener, OnHeaderRefreshListener, OnFooterLoadListener {
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
	private String userId;
	private String gardenId;
	private RelativeLayout empty;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.activity_mygoods);
		BlueTownExitHelper.addActivity(this);
		initUIViews();
		getData();
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
		setTitleView("我发布的商品");
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	private void initUIViews() {
		
		mProductList = new ArrayList<ProductBean>();
		mAbPullToRefreshView = (AbPullToRefreshView) this
				.findViewById(R.id.mPullRefreshView);
		productGridView = (GridView) findViewById(R.id.productGridView);
		productGridView.setOnItemClickListener(this);
		// 设置监听器
		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);
		empty=(RelativeLayout)findViewById(R.id.empty);
		productGridView.setEmptyView(empty);
		FinalDb db=FinalDb.create(MygoodsActivity.this);
		List<MemberUser> users=db.findAll(MemberUser.class);
		if (users!=null&&users.size()!=0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
				gardenId=user.getGardenId();
			}
		}

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
		/**
		 * userId 用户id(必填) filterMine 过滤关键字(查询我发布的商品)(必填,值为0)
		 */
		params.put("userId", userId);
		params.put("gardenId",gardenId);
		params.put("filterMine", "0");
		params.put("page", page + "");
		params.put("rows", size + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.FLEA_MARKERT_LIST, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						MyCollectProductResult result = (MyCollectProductResult) AbJsonUtil
								.fromJson(s, MyCollectProductResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							if (mAbPullToRefreshView != null) {
								mAbPullToRefreshView.onFooterLoadFinish();
								mAbPullToRefreshView.onHeaderRefreshFinish();
							}
							Toast.makeText(MygoodsActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();

						}

					}
					@Override
					public void onFailure(int arg0, String arg1, Throwable arg2) {
						// TODO Auto-generated method stub
						mAbPullToRefreshView.onFooterLoadFinish();
						mAbPullToRefreshView.onHeaderRefreshFinish();
					}
				});
	}

	protected void dealResult(MyCollectProductResult result) {
		switch (listStatus) {
		case Constant.ListStatus.LOAD:
			mProductList.addAll(result.getData().getRows());
			mAbPullToRefreshView.onFooterLoadFinish();
			break;
		case Constant.ListStatus.INIT:
			mProductList.clear();
			mProductList.addAll(result.getData().getRows());
			break;
		case Constant.ListStatus.REFRESH:
			mProductList.clear();
			mProductList.addAll(result.getData().getRows());
			mAbPullToRefreshView.onHeaderRefreshFinish();
			break;
		}
		if (myGridViewAdapter == null) {
			myGridViewAdapter = new ProductGridAdapter(this, mProductList, true);
			productGridView.setAdapter(myGridViewAdapter);
		}
		myGridViewAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("cid", mProductList.get(position).getCid());
		intent.setClass(MygoodsActivity.this, ProductDetailActivity.class);
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
		mProductList.clear();
		listStatus = Constant.ListStatus.REFRESH;
		if (mProductList != null) {
			mProductList.clear();
		}
		page = 1;
		getData();
	}
}
