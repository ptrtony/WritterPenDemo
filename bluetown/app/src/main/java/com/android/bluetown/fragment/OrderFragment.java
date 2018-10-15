package com.android.bluetown.fragment;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.R;
import com.android.bluetown.adapter.OrderAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.OrderParams;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.listener.OrderCallBackListener;
import com.android.bluetown.result.OrderListResult;
import com.android.bluetown.result.OrderListResult.Order;
import com.android.bluetown.surround.OrderDetailActivity;
import com.android.bluetown.utils.Constant;

/**
 * 我的订单——交易中
 * 
 * @author wangxuan
 * 
 */

public class OrderFragment extends Fragment implements OnItemClickListener,
		OnHeaderRefreshListener, OnFooterLoadListener, OrderCallBackListener {
	protected AbHttpUtil httpInstance;
	protected AbRequestParams params;
	private View view;
	private AbPullToRefreshView mPullRefreshView;
	private ListView mListView;
	private OrderAdapter mAdapter;
	private ArrayList<Order> list;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 10;

	private String orderType, status;
	private String userId;
	private FinalDb db;
	private List<MemberUser> users;
	Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			// 取消订单
			case OrderParams.CANCEL_ORDER:
				int position = msg.arg1;
				Order order = (Order) msg.obj;
				onOrderStatusChange(position, order);
				break;
			default:
				break;
			}

		}

	};

	public OrderFragment() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param orderType
	 *            订单类型
	 * @param status
	 *            订单状态
	 */
	public OrderFragment(String orderType, String status) {
		// TODO Auto-generated constructor stub
		this.orderType = orderType;
		this.status = status;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.view_orderlist, container, false);
		findId();
		initHttp();
		getData();
		return view;
	}

	private void findId() {
		BlueTownApp.setHanler(hander);

		list = new ArrayList<Order>();
		mListView = (ListView) view.findViewById(R.id.order_jiaoyizhong);
		mPullRefreshView = (AbPullToRefreshView) view
				.findViewById(R.id.mPullRefreshView);
		mListView.setOnItemClickListener(this);
		mPullRefreshView.setOnFooterLoadListener(this);
		mPullRefreshView.setOnHeaderRefreshListener(this);
		db = FinalDb.create(getActivity());
		getUserId();
	}

	/**
	 * 
	 * @Title: initHttp
	 * @Description: TODO(初始化http请求)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void initHttp() {
		if (params == null) {
			params = new AbRequestParams();
		}
		if (httpInstance == null) {
			httpInstance = AbHttpUtil.getInstance(getActivity());
		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/**
	 * 获取童虎id
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

	private void getData() {
		params.put("userId", userId);
		params.put("status", status);
		params.put("orderType", orderType);
		params.put("page", page + "");
		params.put("rows", size + "");
		httpInstance.post(Constant.HOST_URL + Constant.Interface.ORDER_LIST,
				params, new AbsHttpStringResponseListener(getActivity(), null) {
					@Override
					public void onSuccess(int i, String s) {
						OrderListResult result = (OrderListResult) AbJsonUtil
								.fromJson(s, OrderListResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							Toast.makeText(getActivity(), R.string.no_data,
									Toast.LENGTH_LONG).show();
							if (mPullRefreshView != null) {
								mPullRefreshView.onFooterLoadFinish();
								mPullRefreshView.onHeaderRefreshFinish();
							}
						}

					}
				});
	}

	protected void dealResult(OrderListResult result) {
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
		if (mAdapter == null) {
			mAdapter = new OrderAdapter(getActivity(), list, this);
			mListView.setAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
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

	@Override
	public void onOrderStatusChange(int position, Order order) {
		// TODO Auto-generated method stub
		if (mAdapter != null) {
			list.set(position, order);
			mAdapter.notifyDataSetChanged();
		}
		list.clear();
		listStatus = Constant.ListStatus.REFRESH;
		page = 1;
		getData();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (Activity.RESULT_OK == resultCode) {
			if (!TextUtils.isEmpty(data.getExtras().getString("code"))) {
				String code = data.getExtras().getString("code");
				if (code.equals("1")) {
					list.clear();
					listStatus = Constant.ListStatus.REFRESH;
					page = 1;
					getData();
				}
			}
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Order order = (Order) arg0.getItemAtPosition(arg2);
		Intent intent = new Intent();
		intent.setClass(getActivity(), OrderDetailActivity.class);
		intent.putExtra("cid", order.getCid());
		intent.putExtra("position", arg2);
		startActivityForResult(intent, 0); 
	}
}
