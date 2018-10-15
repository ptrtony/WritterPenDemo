//package com.android.bluetown.fragment;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import com.ab.util.AbJsonUtil;
//import com.ab.view.pullview.AbPullToRefreshView;
//import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
//import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
//import com.android.bluetown.R;
//import com.android.bluetown.adapter.ActionCenterListAdapter;
//import com.android.bluetown.app.BlueTownApp;
//import com.android.bluetown.bean.ActionCenterItemBean;
//import com.android.bluetown.home.main.model.act.ActionCenterDetailsActivity;
//import com.android.bluetown.listener.AbsHttpStringResponseListener;
//import com.android.bluetown.result.ActionCenterResult;
//import com.android.bluetown.utils.Constant;
//
///**
// *
// * @ClassName: ActionCenterFragment
// * @Description:TODO(HomeActivity-下面活动列表)
// * @author: shenyz
// * @date: 2015年7月31日 上午10:29:59
// *
// */
//public class ActionCenterFragment extends AbsFragment implements
//		OnHeaderRefreshListener, OnFooterLoadListener, OnItemClickListener {
//	private AbPullToRefreshView mAbPullToRefreshView;
//	private ListView mListView;
//	private ActionCenterListAdapter adapter;
//	private List<ActionCenterItemBean> list;
//	/** 列表初始化状态 */
//	protected int listStatus = Constant.ListStatus.INIT;
//	/** 默认第一页 */
//	protected int page = 1;
//	/** 默认每页10条数据 */
//	private int size = 10;
//	private String tid;
//	private View view;
//
//	public ActionCenterFragment() {
//		// TODO Auto-generated constructor stub
//	}
//
//	public ActionCenterFragment(String tid) {
//		// TODO Auto-generated constructor stub
//		this.tid = tid;
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		if (view == null) {
//			view = inflater
//					.inflate(R.layout.ac_action_center, container, false);
//		}
//		ViewGroup parent = (ViewGroup) view.getParent();
//		if (parent != null) {
//			parent.removeView(view);
//		}
//		initUIView(view);
//		getData(tid);
//		return view;
//	}
//
//	/**
//	 *
//	 * @Title: initUIView
//	 * @Description: TODO(初始化界面組件)
//	 * @throws
//	 */
//	private void initUIView(View view) {
//		try {
//			String push = getActivity().getIntent().getStringExtra("push");
//			if (!TextUtils.isEmpty(push)) {
//				// 点击查看清空活动中心的推送的消息数
//				if (BlueTownApp.actionMsgCount != 0) {
//					BlueTownApp.actionMsgCount = 0;
//					Intent refreshintent = new Intent(
//							"com.android.bm.refresh.new.msg.action");
//					getActivity().sendBroadcast(refreshintent);
//				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		mListView = (ListView) view.findViewById(R.id.companyInfoList);
//		mAbPullToRefreshView = (AbPullToRefreshView) view
//				.findViewById(R.id.mPullRefreshView);
//		list = new ArrayList<ActionCenterItemBean>();
//		mListView.setHeaderDividersEnabled(false);
//		mListView.setFooterDividersEnabled(false);
//		mListView.setOnItemClickListener(this);
//		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
//		mAbPullToRefreshView.setOnFooterLoadListener(this);
//
//	}
//
//	private void getData(String tid) {
//		params.put("tid", tid);
//		params.put("page", page + "");
//		params.put("rows", size + "");
//		httpInstance.post(Constant.HOST_URL
//				+ Constant.Interface.ACTION_CENTER_LIST, params,
//				new AbsHttpStringResponseListener(getActivity(), null) {
//					@Override
//					public void onSuccess(int i, String s) {
//						ActionCenterResult result = (ActionCenterResult) AbJsonUtil
//								.fromJson(s, ActionCenterResult.class);
//						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
//							dealResult(result);
//						} else if (result.getRepCode().contains(
//								Constant.HTTP_NO_DATA)) {
//							if (list != null && list.size() > 0) {
//								mAbPullToRefreshView.onHeaderRefreshFinish();
//								mAbPullToRefreshView.onFooterLoadFinish();
//								Toast.makeText(getActivity(), R.string.no_data,
//										Toast.LENGTH_LONG).show();
//							}
//
//						}
//
//					}
//				});
//	}
//
//	protected void dealResult(ActionCenterResult result) {
//		switch (listStatus) {
//		case Constant.ListStatus.LOAD:
//			list.addAll(result.getData().getAction().getRows());
//			mAbPullToRefreshView.onFooterLoadFinish();
//			break;
//		case Constant.ListStatus.INIT:
//			list.clear();
//			list.addAll(result.getData().getAction().getRows());
//			break;
//		case Constant.ListStatus.REFRESH:
//			list.clear();
//			list.addAll(result.getData().getAction().getRows());
//			mAbPullToRefreshView.onHeaderRefreshFinish();
//			break;
//		}
//		if (adapter==null) {
//			adapter = new ActionCenterListAdapter(getActivity(), list, "index");
//			mListView.setAdapter(adapter);
//		}else {
//			adapter.notifyDataSetChanged();
//		}
//
//
//	}
//
//	@Override
//	public void onFooterLoad(AbPullToRefreshView arg0) {
//		// TODO Auto-generated method stub
//		listStatus = Constant.ListStatus.LOAD;
//		page++;
//		getData(tid);
//	}
//
//	@Override
//	public void onHeaderRefresh(AbPullToRefreshView arg0) {
//		// TODO Auto-generated method stub
//		list.clear();
//		listStatus = Constant.ListStatus.REFRESH;
//		page = 1;
//		getData(tid);
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		// TODO Auto-generated method stub
//		if (list != null && list.size() > 0) {
//			Intent intent = new Intent();
//			intent.putExtra("aid", list.get(arg2).getAid());
//			intent.setClass(getActivity(), ActionCenterDetailsActivity.class);
//			startActivity(intent);
//		}
//
//	}
//
//}
