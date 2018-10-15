package com.android.bluetown.adapter;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.R;
import com.android.bluetown.bean.GroupsBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.listener.AbsHttpStringResponseListener;

import com.android.bluetown.result.GroupDetailResult;
import com.android.bluetown.result.GroupDetailResult.GroupDetail;
import com.android.bluetown.result.Result;
import com.android.bluetown.utils.Constant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 
 * @ClassName: JoinGroupAdapter
 * @Description:TODO(加入群)
 * @author: shenyz
 * @date: 2015年8月24日 上午10:28:17
 * 
 */
public class JoinGroupAdapter extends BaseContentAdapter {
	private Activity mActivity;
	private String userId;
	private List<MemberUser> users;
	private FinalDb db;


	public JoinGroupAdapter(Context mContext, List<?> data) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
		this.mActivity = (Activity) mContext;
		db = FinalDb.create(mContext);
	}
	DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_msg_empty) // 设置图片下载期间显示的图片
			.showImageForEmptyUri(R.drawable.ic_msg_empty) // 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.ic_msg_empty) // 设置图片加载或解码过程中发生错误显示的图片
			.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
			.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
			.displayer(new RoundedBitmapDisplayer(3)) // 设置成圆角图片
			.build(); // 创建配置过得DisplayImageOption对象

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_join_group, null);
			mHolder = new ViewHolder();
			mHolder.groupImg = (ImageView) convertView
					.findViewById(R.id.groupImg);
			mHolder.groupNick = (TextView) convertView
					.findViewById(R.id.groupName);
			mHolder.joinGroup = (TextView) convertView
					.findViewById(R.id.groupBtn);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);

		return convertView;
	}

	private void setData(final ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final GroupsBean item = (GroupsBean) getItem(position);
		if (!TextUtils.isEmpty(item.getFlockImg())) {
			imageLoader.displayImage(item.getFlockImg(), mHolder.groupImg,
					options);
			} else {
			imageLoader.displayImage("drawable://" + R.drawable.ic_msg_empty,
					mHolder.groupImg, options);
		}
		mHolder.groupNick.setText(item.getFlockName());
		mHolder.joinGroup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getGroupDetails(mHolder, item.getMid());
			}
		});
	}

	/**
	 * userId:申请人id（必填） mid：群id（必填）
	 */
	private void joinGroup(String mid, String flockName,String userId, final Dialog dialog) {
		params.put("userId", userId);
		params.put("mid", mid);
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.APPLY_FOR_IN_GROUP, params,
				new AbsHttpStringResponseListener(context, null) {
					@Override
					public void onSuccess(int i, String s) {
						Result result = (Result) AbJsonUtil.fromJson(s,
								Result.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							Toast.makeText(context, R.string.apply_for_success,
									Toast.LENGTH_LONG).show();
							dialog.cancel();
						} else {
							dialog.cancel();
							Toast.makeText(context, result.getRepMsg(),
									Toast.LENGTH_LONG).show();
						}

					}
				});
	}

	/**
	 * userId：邀请用户的id（必填） mid：加入的群id(必填) flockName：群名称（必填）
	 */
	private void getGroupDetails(final ViewHolder mHolder, String mid) {
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}		
		params.put("userId", userId);
		params.put("mid", mid);
		httpInstance.post(Constant.HOST_URL + Constant.Interface.GROUP_DETAIL,
				params, new AbsHttpStringResponseListener(context, null) {
					@Override
					public void onSuccess(int i, String s) {
						final GroupDetailResult result = (GroupDetailResult) AbJsonUtil
								.fromJson(s, GroupDetailResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							// TODO Auto-generated method stub
							View contentView = LayoutInflater.from(context)
									.inflate(R.layout.groupinfo_pop_layout,
											null);
							ImageView groupImg = (ImageView) contentView
									.findViewById(R.id.groupImg);
							TextView groupName = (TextView) contentView
									.findViewById(R.id.groupName);
							TextView groupNumber = (TextView) contentView
									.findViewById(R.id.groupNumber);
							TextView groupPersonCount = (TextView) contentView
									.findViewById(R.id.groupPersonCount);
							ImageView groupMasterImg = (ImageView) contentView
									.findViewById(R.id.groupMasterImg);
							TextView groupNickName = (TextView) contentView
									.findViewById(R.id.groupNickName);
							Button joinGroup = (Button) contentView
									.findViewById(R.id.joinGroup);

							final GroupDetail item = result.getData();
							if (!TextUtils.isEmpty(item.getFlockImg())) {
								imageLoader.displayImage(item.getFlockImg(),
										groupImg, options);
							} else {
								imageLoader
										.displayImage("drawable://"
												+ R.drawable.ic_msg_empty, groupImg,
												options);
							}
							groupName.setText(result.getData().getFlockName());
							groupNumber.setText(item.getMid());
							groupPersonCount.setText(item.getFlockNumber()
									+ "人");
							if (!TextUtils.isEmpty(item.getHeadImg())) {
								imageLoader.displayImage(item.getHeadImg(),
										groupMasterImg, options);
							} else {
									imageLoader.displayImage("drawable://"
										+ R.drawable.ic_msg_empty, groupMasterImg,
										options);
							}
							groupNickName.setText(item.getNickName());
							final Dialog dialog = showDialog(mActivity,
									contentView);
							if (!TextUtils.isEmpty(item.getState())) {
								// state:是否加入该群（0已加入1未加入,这里只显示未加入的群）
								joinGroup
										.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												// TODO
												// Auto-generated
												// method
												// stub
												joinGroup(item.getMid(),
														item.getFlockName(),userId,
														dialog);
											}
										});
							}

							// }
							// });
						} else {
							Toast.makeText(context, result.getRepMsg(),
									Toast.LENGTH_LONG).show();
						}

					}
				});
	}

	private Dialog showDialog(Context context, final View contentView) {
		final Dialog dialog = new Dialog(context, R.style.alert_dialog);
		dialog.show();
		Window window = dialog.getWindow();
		WindowManager m = mActivity.getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
		p.width = (int) (d.getWidth()); // 宽度设置
		window.setAttributes(p); // 设置生效
		window.setGravity(Gravity.BOTTOM);
		dialog.setContentView(contentView);
		return dialog;
	}

	static class ViewHolder {
		private ImageView groupImg;
		private TextView groupNick;
		private TextView joinGroup;

	}

}
