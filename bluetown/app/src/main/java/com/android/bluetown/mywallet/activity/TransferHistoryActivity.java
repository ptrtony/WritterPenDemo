package com.android.bluetown.mywallet.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.next.tagview.TagCloudView;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.BalanceDetailAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.BillBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.PromptDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.ParseJSONTools;

/**
 * @author hedi
 * @data:  2016年6月4日 下午2:01:58 
 * @Description:  TODO<转账记录> 
 */
public class TransferHistoryActivity extends TitleBarActivity implements
		OnClickListener, OnHeaderRefreshListener, OnFooterLoadListener,
		OnItemClickListener, TagCloudView.OnTagClickListener {
	private ListView mListView;
	private AbPullToRefreshView mPullToRefreshView;
	private BalanceDetailAdapter adapter;
	private List<BillBean> list;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 15;
	private String userId;
	private FinalDb db;
	private List<MemberUser> users;
	private SharePrefUtils prefUtils;
	private RelativeLayout empty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_company_show);
		BlueTownExitHelper.addActivity(this);
		initUIView();
		setData();
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
		setBackImageView();
		setTitleView("转账记录");
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
		// searchBtnLy.setOnClickListener(this);
	}

	/**
	 * 
	 * @Title: initUIView
	 * @Description: TODO(初始化界面組件)
	 * @throws
	 */
	private void initUIView() {
		prefUtils = new SharePrefUtils(this);
		db = FinalDb.create(this);
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		mPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView = (ListView) findViewById(R.id.companyInfoList);
		mListView.setOnItemClickListener(this);
		empty=(RelativeLayout)findViewById(R.id.empty);
		mListView.setEmptyView(empty);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterLoadListener(this);
	}

	private void setData() {
		// TODO Auto-generated method stub
		list = new ArrayList<BillBean>();
		adapter = new BalanceDetailAdapter(this, list);
		mListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private void getData() {
		// TODO Auto-generated method stub
		params.put("userId", userId);
		params.put("communicationToken",
				prefUtils.getString(SharePrefUtils.TOKEN, ""));
		params.put("customerId", userId);
		params.put("billStatus", "6");
		params.put("page", page + "");
		params.put("rows", size + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.BillAction_queryBillListOfZ, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						page++;
						JSONObject json;
						try {
							json = new JSONObject(s);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								JSONArray data = json.optJSONArray("data");
								for (int j = 0; j < data.length(); j++) {
									BillBean bb = new BillBean();
									JSONObject itemObj = data.optJSONObject(j);
									bb = (BillBean) ParseJSONTools
											.getInstance().fromJsonToJava(
													itemObj, BillBean.class);
									list.add(bb);
								}
								adapter.notifyDataSetChanged();
							} else {
								new PromptDialog.Builder(TransferHistoryActivity.this)
										.setViewStyle(
												PromptDialog.VIEW_STYLE_NORMAL)
										.setMessage(json.optString("repMsg"))
										.setButton1(
												"确定",
												new PromptDialog.OnClickListener() {

													@Override
													public void onClick(
															Dialog dialog,
															int which) {
														// TODO Auto-generated
														// method stub
														dialog.cancel();
													}
												}).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						mPullToRefreshView.onFooterLoadFinish();
						mPullToRefreshView.onHeaderRefreshFinish();

					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub

					}

				});

	}

	@Override
	public void onTagClick(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFooterLoad(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		getData();
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		list.clear();
		page = 1;
		getData();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		BillBean bb = new BillBean();
		bb = list.get(position);
		Intent intent = (new Intent(TransferHistoryActivity.this,
				BillDetailActivity.class));
		intent.putExtra("amount", bb.getAmount() + "");
		intent.putExtra("billTypeStr", bb.getBillTypeStr());
		intent.putExtra("tradeTypeStr", bb.getTradeTypeStr());
		intent.putExtra("billStatusStr", bb.getBillStatusStr());
		intent.putExtra("createTime", bb.getCreateTime());
		intent.putExtra("paymentNum", bb.getPaymentNum());
		intent.putExtra("payTypeStr", bb.getPayTypeStr());
		intent.putExtra("customerId", bb.getCustomerId());
		intent.putExtra("orderId", bb.getOrderId());
		intent.putExtra("bid", bb.getBid());
		intent.putExtra("transactionNumber", bb.getTransactionNumber());
		intent.putExtra("commodityInformation", bb.getCommodityInformation());
		intent.putExtra("customerName", bb.getCustomerName());
		intent.putExtra("merchantName", bb.getMerchantName());
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {

		// TODO Auto-generated method stub
		switch (v.getId()) {

		default:
			break;
		}

	}

}
