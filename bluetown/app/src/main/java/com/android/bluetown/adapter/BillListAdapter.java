package com.android.bluetown.adapter;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.BillBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.view.PinnedSectionListView;

/**
 * 
 * @ClassName: CompanyShowListAdapter
 * @Description:TODO(我的历史账单)
 * @author: shenyz
 * @date: 2015年8月5日 上午11:16:37
 * 
 */
public class BillListAdapter extends BaseContentAdapter implements PinnedSectionListView.PinnedSectionListAdapter{
	private FinalDb db;
	private String userId = "";
	private static final int NO_PIN_VIEW = 1;
	private static final int PIN_VIEW = 0;
	private boolean isPin = false;
	public BillListAdapter(Context mContext) {
		super(mContext);
		// TODO Auto-generated constructor stub
		db = FinalDb.create(context);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
	}

	@Override
	public int getItemViewType(int position) {
		final BillBean item = (BillBean) getItem(position);
		isPin = item.getMonth().equals("0");
		if (isPin){
			return NO_PIN_VIEW;
		}else{
			return PIN_VIEW;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view=null;
		int type = getItemViewType(position);
		if (type==NO_PIN_VIEW){
			view = bindNoPinedView(position,convertView);
		}else if(type == PIN_VIEW){
			view = bindPinedView(position,convertView);
		}
		return view;
	}

	private void setData1(ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final BillBean item = (BillBean) getItem(position);
		mHolder.date.setText(item.getMonth());
	}

	private void setData(ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final BillBean item = (BillBean) getItem(position);
		mHolder.time.setText(item.getCreateTime().split("-")[1]+"-"+item.getCreateTime().split("-")[2].replaceAll(" ", "\n"));
		mHolder.contents.setText(item.getCommodityInformation());
		mHolder.paytype.setText(item.getBillTypeStr());
		if (item.getBillTypeStr().equals("已完成")
				|| item.getBillTypeStr().equals("已退款")
				|| item.getBillTypeStr().equals("已付款")) {
			mHolder.paytype.setTextColor(context.getResources().getColor(
					R.color.font_green));
		} else {
			mHolder.paytype.setTextColor(context.getResources().getColor(
					R.color.orange));
		}
		if (item.getTradeTypeStr().equals("充值")
				|| item.getTradeTypeStr().equals("收款")) {
			mHolder.amounts.setText("+" + item.getAmount());
		} else if (item.getTradeTypeStr().equals("支付")) {
			mHolder.amounts.setText("-" + item.getAmount());
		} else if (item.getTradeTypeStr().equals("转账")) {
			if (item.getCustomerId().equals(userId)) {
				mHolder.amounts.setText("-" + item.getAmount());
			} else {
				mHolder.amounts.setText("+" + item.getAmount());
			}
		}
	}

	@Override
	public boolean isItemViewTypePinned(int viewType) {
		return viewType == PIN_VIEW;
	}

	static class ViewHolder {
		private TextView amounts;
		private TextView paytype;
		private TextView time;
		private TextView contents;
		private TextView date;

	}
	private View bindNoPinedView(int position, View convertView){
		ViewHolder mHolder =null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_bill, null);
			mHolder = new ViewHolder();
			mHolder.amounts = (TextView) convertView
					.findViewById(R.id.amounts);
			mHolder.contents = (TextView) convertView
					.findViewById(R.id.content);
			mHolder.time = (TextView) convertView.findViewById(R.id.date);
			mHolder.paytype = (TextView) convertView
					.findViewById(R.id.pay_type);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);
		return convertView;
	}

	private View bindPinedView(int position, View convertView){
		ViewHolder mHolder=null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_bill_list_title,
					null);
			mHolder = new ViewHolder();
			mHolder.date = (TextView) convertView.findViewById(R.id.date);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData1(mHolder, position);
		return convertView;
	}
}
