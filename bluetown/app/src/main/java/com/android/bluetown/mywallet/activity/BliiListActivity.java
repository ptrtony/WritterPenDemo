package com.android.bluetown.mywallet.activity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.next.tagview.TagCloudView;
import me.next.tagview.TagCloudView.OnTagClickListener;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.BillListAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.BillBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.PromptDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.ParseJSONTools;
import com.android.bluetown.utils.Utils;
import com.android.bluetown.view.LoadListView;

/**
 * @author hedi
 * @data: 2016年4月28日 下午4:56:30
 * @Description: 账单
 */
public class BliiListActivity extends TitleBarActivity implements OnClickListener,
		OnItemClickListener, OnTagClickListener, LoadListView.IloadListener {
	private LoadListView mListView;
//	private AbPullToRefreshView mPullToRefreshView;
	private BillListAdapter adapter;
	private ArrayList<BillBean> list;
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
	SimpleDateFormat s = new SimpleDateFormat("yyyy-MM");
	private String time;
	private Calendar calendar;
	private String lastmonth = "";
	private String billStatus="";
	private RelativeLayout empty;
	private int billSize;
	private int totalSize;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_bill_list);
		BlueTownExitHelper.addActivity(this);
		initView();
		setData();
		getData(billStatus);
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("账单");
		setTitleLayoutBg(R.color.title_bg_blue);
		setRighTextView("分类");
		righTextLayout.setOnClickListener(this);

	}

	private void initView() {
		prefUtils = new SharePrefUtils(this);
		db = FinalDb.create(this);
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
//		mPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView = findViewById(R.id.companyInfoList);
		empty=(RelativeLayout)findViewById(R.id.empty);
		mListView.setOnItemClickListener(this);
//		mPullToRefreshView.setOnHeaderRefreshListener(this);
//		mPullToRefreshView.setOnFooterLoadListener(this);
		mListView.setInterface(this);
		Date currentTime = new Date();
		calendar = Calendar.getInstance();
		time = s.format(currentTime);
	}

	private void setData() {
		// TODO Auto-generated method stub		
		list = new ArrayList<BillBean>();		
		adapter = new BillListAdapter(this);
		adapter.setData(list);
		mListView.setAdapter(adapter);
	}

	private void getData(String billStatus) {
		// TODO Auto-generated method stub
		billSize = 0;
		params.put("userId", userId);
		params.put("communicationToken",
				prefUtils.getString(SharePrefUtils.TOKEN, ""));
		params.put("customerId", userId);
		params.put("billStatus", billStatus);		
		params.put("page", page + "");
		params.put("rows", size + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.BillAction_queryBillList, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						page++;
						Log.d("page","page=============="+page);
						JSONObject json;
						try {
							json = new JSONObject(s);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								JSONArray data = json.optJSONArray("data");
								for (int j = 0; j < data.length(); j++) {
									BillBean bb = new BillBean();
									JSONObject itemObj = data.optJSONObject(j);
									JSONArray data1 = itemObj
											.optJSONArray("data");
									Log.d("itemObject","newData:"+itemObj.optString("month").toString()+"~~~"+lastmonth);
									if (!itemObj.optString("month").toString().equals(
											lastmonth)) {
										bb.setMonth(itemObj.optString("month"));
										list.add(bb);
										Log.d("topFlowView","month+~~~~~~~~~~~~~~~~~~~"+1);
									}
									if (j == data.length() - 1) {
										lastmonth = itemObj.optString("month");
									}
									for (int k = 0; k < data1.length(); k++) {
										JSONObject data2 = data1
												.optJSONObject(k);
										bb = (BillBean) ParseJSONTools
												.getInstance().fromJsonToJava(
														data2, BillBean.class);
										bb.setMonth("0");
										Log.d("topFlowView","month+~~~~~~~~~~~~~~~~~~~"+2);
										list.add(bb);
										billSize++;
									}
								}
								Log.d("billSize","billSize:"+billSize);
								mListView.setTotal_size(billSize);
								adapter.setData(list);
								mListView.setEmptyView(empty);
							} else {
								new PromptDialog.Builder(BliiListActivity.this)
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
						mListView.loadComplete();
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
//						mPullToRefreshView.onFooterLoadFinish();
//						mPullToRefreshView.onHeaderRefreshFinish();
						mListView.loadComplete();
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
//						mListView.loadComplete();
					}
					@Override
					public void onFailure(int arg0, String arg1, Throwable arg2) {
						// TODO Auto-generated method stub
//						mPullToRefreshView.onHeaderRefreshFinish();
//						mPullToRefreshView.onFooterLoadFinish();
						mListView.loadComplete();
					}

				});

	}

	private void selectTypePop(final List<String> tagsList, LinearLayout typeLy) {
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
			typeLy.getGlobalVisibleRect(visibleFrame);
			int height = typeLy.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom*2;
			pop.setHeight(height);
			pop.showAsDropDown(typeLy);
		} else {
			pop.showAsDropDown(typeLy);
		}
		selecTagCloudView.setOnTagClickListener(new OnTagClickListener() {

			@Override
			public void onTagClick(int arg0) {
				// TODO Auto-generated method stub
				list.clear();
				lastmonth = "";
				page = 1;
				switch (arg0) {
				case 0:
					billStatus="";
					break;
				case 1:
					billStatus="0";
					break;
				case 2:
					billStatus="2";
					break;
				case 3:
					billStatus="4";
					break;
				case 4:
					billStatus="1";
					break;
				case 5:
					billStatus="7";
					break;
					
				default:
					break;
				}
				getData(billStatus);
				pop.dismiss();
			}
		});
		typeBlankLy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pop.dismiss();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rightTextLayout:
			List<String> tagList = Utils.getBillTypeList();
			selectTypePop(tagList, titleLayout);
			break;
		}
	}

	@Override
	public void onTagClick(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		BillBean bb = new BillBean();
		bb = list.get(position);
		if (bb.getMonth().equals("0")) {
			Intent intent = (new Intent(BliiListActivity.this,
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
		} else {
			Intent intent =(new Intent(BliiListActivity.this,
					BillMonthActivity.class));
			intent.putExtra("time", bb.getMonth());
			startActivity(intent);
		}

	}

//	@Override
//	public void onFooterLoad(AbPullToRefreshView arg0) {
//		// TODO Auto-generated method stub
//		getData(billStatus);
//	}
//
//	@Override
//	public void onHeaderRefresh(AbPullToRefreshView arg0) {
//		// TODO Auto-generated method stub
//		list.clear();
//		lastmonth = "";
//		page = 1;
//		getData(billStatus);
//	}

	@Override
	public void onLoad() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				getData(billStatus);
			}
		},500);
	}

}
