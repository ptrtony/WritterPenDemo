package com.android.bluetown.home.main.model.act;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalDb;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.GuestAppointmentHistoryListAdapter;
import com.android.bluetown.adapter.ParkingHistoryAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.ParkingBean;
import com.android.bluetown.custom.dialog.PromptDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.ParseJSONTools;

/**
 * @author hedi
 * @data: 2016年5月27日 下午5:17:47
 * @Description: TODO<停车包月历史>
 */
public class ParkingHistoryActivity extends TitleBarActivity implements
		OnItemClickListener, OnHeaderRefreshListener, OnFooterLoadListener {
	private AbPullToRefreshView mPullToRefreshView;
	private ListView mListView;
	private ParkingHistoryAdapter adapter;
	private List<ParkingBean> list;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 15;
	private RelativeLayout empty;
	private String userId;
	private FinalDb db;
	private List<MemberUser> users;
	private SharePrefUtils prefUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_parking_history);
		BlueTownExitHelper.addActivity(this);
		initUIView();
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
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("停车包月历史");
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
		db = FinalDb.create(this);
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		list = new ArrayList<ParkingBean>();
		prefUtils = new SharePrefUtils(this);
		mPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView = (ListView) findViewById(R.id.companyInfoList);
		empty=(RelativeLayout)findViewById(R.id.empty);
		mListView.setEmptyView(empty);
		mListView.setHeaderDividersEnabled(false);
		mListView.setFooterDividersEnabled(false);
		mListView.setOnItemClickListener(this);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterLoadListener(this);
		if (adapter==null) {
			adapter = new ParkingHistoryAdapter(this, list);
			mListView.setAdapter(adapter);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void getData() {
		// TODO Auto-generated method stub
		params.put("userId", userId);
		params.put("communicationToken",
				prefUtils.getString(SharePrefUtils.TOKEN, ""));
		params.put("page", page + "");
		params.put("rows", size + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.ParkingOrderAction_queryOrdrList, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {

						JSONObject json;
						try {
							json = new JSONObject(s);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								JSONArray data = json.optJSONArray("data");
								for (int j = 0; j < data.length(); j++) {
									JSONObject itemObj = data.optJSONObject(j);
									ParkingBean pb = (ParkingBean) ParseJSONTools
											.getInstance().fromJsonToJava(
													itemObj, ParkingBean.class);
									list.add(pb);
								}
								adapter.notifyDataSetChanged();
							} else {
								new PromptDialog.Builder(
										ParkingHistoryActivity.this)
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
	public void onFooterLoad(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		page++;
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
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		// orderType1 预约成功，2：按时到达，0：预约失败是0，3：过期未到
		ParkingBean pb=new ParkingBean();
		pb=list.get(position);
		Intent intent = new Intent();		
		intent.setClass(ParkingHistoryActivity.this,
				ParkingMonthlySuccessActivity.class);
		intent.putExtra("userName", pb.getUserName());
		intent.putExtra("phoneNumber", pb.getPhoneNumber());
		intent.putExtra("carNumber", pb.getCarNumber());
		intent.putExtra("parkingName", pb.getParkingName());
		intent.putExtra("parkingSpace", pb.getParkingSpace());
		intent.putExtra("parkingLotNo", pb.getParkingNo());
		intent.putExtra("date", pb.getEndTime());
		intent.putExtra("monthnum", pb.getMouthNumber());
		intent.putExtra("parkingType", pb.getParkingType());
		intent.putExtra("region", pb.getRegion());
		intent.putExtra("mid", pb.getParkingId());
		intent.putExtra("amount", pb.getAmount()+"");
		intent.putExtra("orderStatus", pb.getOrderStatus());
		intent.putExtra("oldOrderId", pb.getPoid());
		intent.putExtra("orderNum", pb.getOrderNum());
		if(pb.getIsXubao()!=null){
			intent.putExtra("isXubao", pb.getIsXubao());
		}		
		startActivity(intent);

	}

}
