package com.android.bluetown;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.adapter.ProcedureApprovalHistoryListAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.ProcedureHistoryBean;
import com.android.bluetown.bean.ProcedureHistoryBean.DataBean;
import com.android.bluetown.bean.ProcedureHistoryBean.DetailBean;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.listview.XListView;
import com.android.bluetown.listview.XListView.IXListViewListener;
import com.android.bluetown.utils.Constant;

/**
 * 
 * @ClassName: ProcedureApprovalHistoryActivity
 * @Description:TODO(历史审批列表)
 * @author: shenyz
 * @date: 2015年8月21日 上午11:11:29
 * 
 */
public class ProcedureApprovalHistoryActivity extends TitleBarActivity
		implements IXListViewListener, OnItemClickListener, OnClickListener {
	private LayoutInflater mInflater;
	private TextView zoneTel, realEstateTel;
	private XListView mListView;
	private List<DetailBean> list;
	private ProcedureApprovalHistoryListAdapter adapter;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 10;
	
	private String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_procedure_approval_history);
		BlueTownExitHelper.addActivity(this);
		initViews();
		getData();
	}

	/**
	 * 
	 * @Title: initViews
	 * @Description: TODO(初始化界面组件)
	 * @throws
	 */
	private void initViews() {
	
		list = new ArrayList<DetailBean>();
		mInflater = LayoutInflater.from(this);
		View view = mInflater.inflate(R.layout.procedure_approval_history_top,
				null);
		zoneTel = (TextView) view.findViewById(R.id.zoneTel);
		realEstateTel = (TextView) view.findViewById(R.id.realEstateTel);
		mListView = (XListView) findViewById(R.id.approvalList);
		mListView.addHeaderView(view);
		mListView.setAdapter(null);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		mListView.setFooterDividersEnabled(false);
		mListView.setHeaderDividersEnabled(false);
		mListView.setXListViewListener(this);
		zoneTel.setOnClickListener(this);
		realEstateTel.setOnClickListener(this);
		FinalDb db = FinalDb.create(this);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.zoneTel:
			if (!TextUtils.isEmpty(zoneTel.getText().toString())) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_CALL);
				// url:统一资源定位符
				// uri:统一资源标示符（更广）
				intent.setData(Uri.parse("tel:" + zoneTel.getText().toString()));
				// 开启系统拨号器
				startActivity(intent);
			}
			break;
		case R.id.realEstateTel:
			if (!TextUtils.isEmpty(realEstateTel.getText().toString())) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_CALL);
				// url:统一资源定位符
				// uri:统一资源标示符（更广）
				intent.setData(Uri.parse("tel:"
						+ realEstateTel.getText().toString()));
				// 开启系统拨号器
				startActivity(intent);
			}
			break;
		}
	}

	private void getData() {
		// TODO Auto-generated method stub
		
		if (TextUtils.isEmpty(userId)) {
			TipDialog.showDialogNoClose(ProcedureApprovalHistoryActivity.this,
					R.string.tip, R.string.confirm, R.string.login_info_tip,
					LoginActivity.class);
		} else {
			params.put("userId", userId);
			params.put("page", page + "");
			params.put("rows", size + "");
			httpInstance.post(Constant.HOST_URL
					+ Constant.Interface.PROCEDURE_HISTORY_APPROVAL, params,
					new AbsHttpStringResponseListener(this, null) {
						@Override
						public void onSuccess(int statusCode, String content) {
							ProcedureHistoryBean result1 = (ProcedureHistoryBean) AbJsonUtil
									.fromJson(content,
											ProcedureHistoryBean.class);
							if (result1.getRepCode().contains(
									Constant.HTTP_SUCCESS)) {
								dealResult(result1.data);
							} else if (result1.getRepCode().contains(
									Constant.HTTP_NO_DATA)) {
							
							    mListView.stopLoadMore();
								mListView.stopRefresh();
								Toast.makeText(
										ProcedureApprovalHistoryActivity.this,
										R.string.no_data, Toast.LENGTH_LONG)
										.show();
								
							}

						}

					});
		}

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
		setTitleView(R.string.hostory_approval);
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	protected void dealResult(DataBean historyBean) {
		switch (listStatus) {
		case Constant.ListStatus.LOAD:
			list.addAll(historyBean.rows);
			
			mListView.stopLoadMore();
	
			break;
		case Constant.ListStatus.INIT:
			list.clear();
			list.addAll(historyBean.rows);
			break;
		case Constant.ListStatus.REFRESH:
			list.clear();
			list.addAll(historyBean.rows);
		
			mListView.stopRefresh();
			
			break;
		}
	if (adapter == null) {
		adapter = new ProcedureApprovalHistoryListAdapter(this, list);
		mListView.setAdapter(adapter);
	}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 
	 * @Title: onRefresh 上拉刷新
	 * @Description:
	 * @see IXListViewListener#onRefresh()
	 */
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		list.clear();
		listStatus = Constant.ListStatus.REFRESH;
		page = 1;
		getData();
	}

	/**
	 * 
	 * @Title: onLoadMore 下拉加载
	 * @Description:
	 * @see IXListViewListener#onLoadMore()
	 */
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		listStatus = Constant.ListStatus.LOAD;
		page++;
		getData();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}
}
