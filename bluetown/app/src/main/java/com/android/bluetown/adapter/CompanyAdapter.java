package com.android.bluetown.adapter;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.bean.CompanyShowItemBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.listener.OnCompanyChangeListener;

import com.android.bluetown.result.UserOperationResult;
import com.android.bluetown.utils.Constant;

/**
 * 
 * @ClassName: CompanyShowListAdapter
 * @Description:TODO(企业展示列表的Adapter)
 * @author: shenyz
 * @date: 2015年8月5日 上午11:16:37
 * 
 */
public class CompanyAdapter extends BaseContentAdapter {
	private FinalDb db;
	private String userId;
	private List<MemberUser> users;
	private OnCompanyChangeListener listener;

	public CompanyAdapter(Context mContext, List<?> data,
			OnCompanyChangeListener listener) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
		db = FinalDb.create(mContext);
		this.listener = listener;
		
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_company_show, null);
			mHolder = new ViewHolder();
			mHolder.companyImg = (ImageView) convertView
					.findViewById(R.id.companyImg);
			mHolder.companyName = (TextView) convertView
					.findViewById(R.id.companyName);
			mHolder.companyAddress = (TextView) convertView
					.findViewById(R.id.companyAddress);
			mHolder.companyDistance = (TextView) convertView
					.findViewById(R.id.companyDistance);
			mHolder.companyType = (TextView) convertView
					.findViewById(R.id.companyType);
			mHolder.like = (ImageView) convertView.findViewById(R.id.likeImg);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);

		return convertView;
	}

	private void setData(final ViewHolder mHolder, final int position) {
		// TODO Auto-generated method stub
		final CompanyShowItemBean item = (CompanyShowItemBean) getItem(position);
		imageLoader.displayImage(item.getLogoImg(), mHolder.companyImg,
		defaultOptions);
		mHolder.companyName.setText(item.getCompanyName());
		mHolder.companyAddress.setText(item.getAddress());
		mHolder.companyDistance.setText("距离" + item.getCompanyDistance());
		mHolder.companyType.setText(item.getTypeName());
		if (item.getIsCollect().contains("1")) {
			mHolder.like.setImageResource(R.drawable.like_p);
		} else {
			mHolder.like.setImageResource(R.drawable.like);
		}
		mHolder.like.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				users = db.findAll(MemberUser.class);
				if (users != null && users.size() != 0) {
					MemberUser user = users.get(0);
					if (user != null) {
						userId = user.getMemberId();
					}
				}
				String bid = item.getBid();
				if (!TextUtils.isEmpty(userId)) {
					if (!item.getIsCollect().equals("1")) {
						collectCompany(userId, position, bid, mHolder, item);
					} else {
						collectCompany(userId, position, bid, mHolder, item);
					}
				} else {
					TipDialog.showDialogNoClose(context, R.string.tip,
							R.string.confirm, R.string.login_info_tip,
							LoginActivity.class);
				}

			}
		});
	}

	private void collectCompany(String userId, final int position, String bid,
			final ViewHolder mHolder, final CompanyShowItemBean item) {
		/*
		 * userId 用户id actionID 收藏(关注或点赞)对象的id actionType 5：关注，6：收藏，7：点赞 8加入圈子
		 */
		params.put("userId", userId);
		params.put("actionId", bid);
		params.put("actionType", Constant.ATTENTION + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.CIRCLE_TYPEW_INFO, params,
				new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						UserOperationResult result = (UserOperationResult) AbJsonUtil
								.fromJson(s, UserOperationResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							if (result.getData().equals("关注成功")) {
								mHolder.like
										.setImageResource(R.drawable.like_p);
								item.setIsCollect("1");
								listener.onCompanyCollect(position, item);
							} else {
								mHolder.like.setImageResource(R.drawable.like);
								item.setIsCollect("0");
								listener.onCompanyCollect(position, item);

							}
						}

					}
				});

	}

	static class ViewHolder {
		private ImageView companyImg;
		private TextView companyName;
		private TextView companyAddress;
		private TextView companyDistance;
		private TextView companyType;
		private ImageView like;
	}

}
