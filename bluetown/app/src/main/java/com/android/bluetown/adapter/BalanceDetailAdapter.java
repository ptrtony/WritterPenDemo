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

/**
 * 
 * @ClassName: CompanyShowListAdapter
 * @Description:TODO(我的历史账单)
 * @author: shenyz
 * @date: 2015年8月5日 上午11:16:37
 * 
 */
public class BalanceDetailAdapter extends BaseContentAdapter {
	private FinalDb db;
	private String userId = "";
	public BalanceDetailAdapter(Context mContext, List<?> data) {
		super(mContext,data);
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
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_balance_detail, null);
				mHolder = new ViewHolder();
				mHolder.balance = (TextView) convertView.findViewById(R.id.balance);
				mHolder.amounts = (TextView) convertView.findViewById(R.id.amounts);
				mHolder.contents = (TextView) convertView.findViewById(R.id.content);
				mHolder.time = (TextView) convertView.findViewById(R.id.date);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}
			setData(mHolder, position);
		
		

		return convertView;
	}

	private void setData(ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final BillBean item = (BillBean) getItem(position);
		mHolder.balance.setText("余额："+item.getBalance()+"元");
		mHolder.time.setText(item.getCreateTime());
		mHolder.contents.setText(item.getBillStatusStr());
		if (item.getCustomerId().equals(userId)) {
			mHolder.amounts.setTextColor(0xffFFAB53);
			mHolder.amounts.setText("-" + item.getAmount());
		} else {
			mHolder.amounts.setTextColor(0xff64dd17);
			mHolder.amounts.setText("+" + item.getAmount());
		}
	}

	static class ViewHolder {
		private TextView amounts;
		private TextView balance;
		private TextView time;
		private TextView contents;

	}

}
