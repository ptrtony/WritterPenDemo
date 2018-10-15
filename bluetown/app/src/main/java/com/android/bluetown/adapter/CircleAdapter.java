package com.android.bluetown.adapter;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.bean.CircleBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.listener.OnCircleChangeCallBackListener;

import com.android.bluetown.result.UserOperationResult;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.view.RoundedImageView;

/**
 * 
 * @ClassName: CircleAdapter
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: shenyz
 * @date: 2015年8月10日 下午4:33:03
 * 
 */
public class CircleAdapter extends BaseContentAdapter {
	private FinalDb db;
	private String userId;
	private List<MemberUser> users;

	private OnCircleChangeCallBackListener listener;

	public CircleAdapter(Context mContext, List<?> data,
			OnCircleChangeCallBackListener listener) {
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
			convertView = mInflater.inflate(R.layout.item_circle, null);
			mHolder = new ViewHolder();
			mHolder.circleLogo = (RoundedImageView) convertView
					.findViewById(R.id.circleLogo);
			mHolder.circlePersons = (TextView) convertView
					.findViewById(R.id.circlePersons);
			mHolder.circleTitle = (TextView) convertView
					.findViewById(R.id.circleTitle);
			mHolder.circleContent = (TextView) convertView
					.findViewById(R.id.circleContent);
			mHolder.joinInBtn = (Button) convertView.findViewById(R.id.joinIn);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);

		return convertView;
	}

	private void setData(final ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final CircleBean item = (CircleBean) getItem(position);
		if (!TextUtils.isEmpty(item.getGroupImg())) {
			imageLoader.displayImage(item.getGroupImg(), mHolder.circleLogo,
					defaultOptions);
		} else {
			mHolder.circleLogo.setImageResource(R.drawable.ic_msg_empty);
		}
		mHolder.circleTitle.setText(item.getGroupName());
		mHolder.circlePersons.setText(item.getCount());
		mHolder.circleContent.setText(item.getState());
		if (!TextUtils.isEmpty(item.getAid())) {
			mHolder.joinInBtn.setText(R.string.joined_in);
		} else {
			mHolder.joinInBtn.setText(R.string.add_info);
		}

		mHolder.joinInBtn.setOnClickListener(new ClickListener(position,
				mHolder.joinInBtn, item));
	}

	static class ViewHolder {
		private TextView circleTitle, circlePersons, circleContent;
		private RoundedImageView circleLogo;
		private Button joinInBtn;
	}

	private class ClickListener implements OnClickListener {
		private Button joinBtn;
		private CircleBean item;
		private int position;

		@SuppressWarnings("unused")
		public ClickListener() {
			// TODO Auto-generated constructor stub
		}

		public ClickListener(int position, Button joinBtn, CircleBean item) {
			// TODO Auto-generated constructor stub
			this.position = position;
			this.joinBtn = joinBtn;
			this.item = item;

		}

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
			if (!TextUtils.isEmpty(userId)) {
				joinIn(userId, item, position, joinBtn);
			} else {
				TipDialog.showDialogNoClose(context, R.string.tip,
						R.string.confirm, R.string.login_info_tip,
						LoginActivity.class);
			}
		}
	}
	
	

	private void joinIn(final String userId, final CircleBean circle,
			final int position, final Button joinBtn) {
		/*
		 * userId 用户id actionID 收藏(关注或点赞)对象的id actionType 5：关注，6：收藏，7：点赞 8加入圈子
		 */
		params.put("userId", userId);
		params.put("actionId", circle.getGid());
		params.put("actionType", Constant.ADD_CIRCLE + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.CIRCLE_TYPEW_INFO, params,
				new AbsHttpStringResponseListener(context, null) {
					@Override
					public void onSuccess(int i, String s) {
						UserOperationResult result = (UserOperationResult) AbJsonUtil
								.fromJson(s, UserOperationResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							if (result.getData().equals("关注成功")) {
								joinBtn.setText(R.string.joined_in);
								circle.setAid(userId);
								listener.onCircleChange(position, circle);
								Toast.makeText(context,
										R.string.add_circle_success,
										Toast.LENGTH_SHORT).show();
							} else {
								joinBtn.setText(R.string.add_info);
								circle.setAid(null);
								listener.onCircleChange(position, circle);
								Toast.makeText(context,
										R.string.quit_circle_success,
										Toast.LENGTH_SHORT).show();
							}
						}

					}
				});

	}

}
