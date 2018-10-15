package com.android.bluetown.home.makefriends.activity;

import java.util.List;
import net.tsz.afinal.FinalDb;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.SweetAlertDialog.OnSweetClickListener;
import com.android.bluetown.listener.AbsHttpStringResponseListener;

import com.android.bluetown.result.GroupDetailResult;
import com.android.bluetown.result.GroupDetailResult.GroupDetail;
import com.android.bluetown.result.Result;
import com.android.bluetown.utils.Constant;

/**
 * 群资料
 * 
 * @author shenyz
 * 
 */
public class GroupDataActivity extends TitleBarActivity implements
		OnClickListener {
	private String mid, flockName;
	private String userId,groupMasterId;
	private FinalDb db;
	private List<MemberUser> users;
	private RelativeLayout modifygroupInfoLy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_groupdata);
		BlueTownExitHelper.addActivity(this);
		initView();
		getGroupMasterDetails(mid);
	}

	private void initView() {
		db = FinalDb.create(this);
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		mid = getIntent().getStringExtra("mid");
		flockName = getIntent().getStringExtra("flcokName");
		// 邀请群成员
		findViewById(R.id.rl_group_members).setOnClickListener(this);
		// 群资料
		findViewById(R.id.groupInfoLy).setOnClickListener(this);
		// 群成员
		findViewById(R.id.groupMemberLayout).setOnClickListener(this);
		modifygroupInfoLy = (RelativeLayout) findViewById(R.id.modifygroupInfoLy);
		modifygroupInfoLy.setOnClickListener(GroupDataActivity.this);
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
		setTitleView("群资料");
		setTitleLayoutBg(R.color.chat_tab_message_color);
		rightImageLayout.setVisibility(View.INVISIBLE);

	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.rl_group_members:
			intent.setClass(GroupDataActivity.this,
					InviteGroupMembersActivity.class);
			intent.putExtra("mid", mid);
			intent.putExtra("flockName", flockName);
			startActivity(intent);
			break;
		case R.id.groupMemberLayout:
			intent.setClass(GroupDataActivity.this, GroupMembersActivity.class);
			intent.putExtra("mid", mid);
			startActivity(intent);
			break;
		case R.id.groupInfoLy:
			getGroupDetails(mid);
			break;
		case R.id.modifygroupInfoLy:
			intent.setClass(GroupDataActivity.this, ModifyGroupActivity.class);
			intent.putExtra("mid", mid);
			startActivity(intent);
			break;
		}

	}

	/**
	 * userId：邀请用户的id（必填） mid：加入的群id(必填) flockName：群名称（必填）
	 */
	/**
	 * @param mid
	 */
	private void getGroupDetails(String mid) {
		final View contentView = LayoutInflater.from(this).inflate(
				R.layout.groupinfo_pop_layout, null);
		final ImageView groupImg = (ImageView) contentView
				.findViewById(R.id.groupImg);
		final TextView groupName = (TextView) contentView
				.findViewById(R.id.groupName);
		final TextView groupNumber = (TextView) contentView
				.findViewById(R.id.groupNumber);
		final TextView groupPersonCount = (TextView) contentView
				.findViewById(R.id.groupPersonCount);
		final ImageView groupMasterImg = (ImageView) contentView
				.findViewById(R.id.groupMasterImg);
		final TextView groupNickName = (TextView) contentView
				.findViewById(R.id.groupNickName);
		final Button joinGroup = (Button) contentView
				.findViewById(R.id.joinGroup);
		// mid:群id（必填）
		// userId:当前登录用户(必填)
		params.put("userId", userId);
		params.put("mid", mid);
		httpInstance.post(Constant.HOST_URL + Constant.Interface.GROUP_DETAIL,
				params, new AbsHttpStringResponseListener(
						GroupDataActivity.this, null) {
					@Override
					public void onSuccess(int i, String s) {
						final GroupDetailResult result = (GroupDetailResult) AbJsonUtil
								.fromJson(s, GroupDetailResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							// TODO Auto-generated method stub
							final GroupDetail detail = result.getData();
							if (!TextUtils.isEmpty(detail.getFlockImg())) {
								finalBitmap.display(groupImg,
										detail.getFlockImg());
							} else {
								groupImg.setBackgroundResource(R.drawable.ic_msg_empty);
							}
							groupName.setText(detail.getFlockName());
							groupNumber.setText(detail.getMid());
							groupPersonCount.setText(detail.getFlockNumber()
									+ "人");
							if (!TextUtils.isEmpty(detail.getHeadImg())) {
								finalBitmap.display(groupMasterImg,
										detail.getHeadImg());
							} else {
								groupMasterImg
										.setBackgroundResource(R.drawable.ic_msg_empty);
							}
							groupNickName.setText(detail.getNickName());
							final Dialog dialog = showDialog(context,
									contentView);
							String loginUserId = userId;
							// 表示是群主
							if (loginUserId.equals(detail.getUserId())) {
								// 解散群
								joinGroup.setText(R.string.dimiss_group);
								joinGroup
										.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												showDismissGroupDialog(
														GroupDataActivity.this,
														R.string.tip,
														R.string.dialog_ok,
														R.string.cancel,
														R.string.confirm_dimiss_group_title,
														detail.getMid(), dialog);
											}
										});
							} else {
								joinGroup.setClickable(true);
								joinGroup
										.setBackgroundResource(R.drawable.orange_no_radius_btn_bg);
								disGroupMaster(joinGroup, result, detail,
										dialog);

							}


						}

					}

				});
	}
	
	private void getGroupMasterDetails(String mid) {
		// mid:群id（必填）
		// userId:当前登录用户(必填)
		params.put("userId", userId);
		params.put("mid", mid);
		httpInstance.post(Constant.HOST_URL + Constant.Interface.GROUP_DETAIL,
				params, new AbsHttpStringResponseListener(
						GroupDataActivity.this, null) {
					@Override
					public void onSuccess(int i, String s) {
						final GroupDetailResult result = (GroupDetailResult) AbJsonUtil
								.fromJson(s, GroupDetailResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							// TODO Auto-generated method stub
							final GroupDetail detail = result.getData();
							groupMasterId=detail.getUserId();
							if (groupMasterId.equals(userId)) {
								// 群资料
								modifygroupInfoLy.setVisibility(View.VISIBLE);
							}else {
								modifygroupInfoLy.setVisibility(View.GONE);
							}
						}

					}

				});
	}

	/**
	 * 非群主加入或退出群的操作
	 * 
	 * @param joinGroup
	 * @param result
	 * @param detail
	 * @param dialog
	 */
	private void disGroupMaster(final Button joinGroup,
			final GroupDetailResult result, final GroupDetail detail,
			final Dialog dialog) {
		if (!TextUtils.isEmpty(detail.getState())) {
			// 已加入群
			if (detail.getState().equals("0")) {
				// 已加入群，显示是否退群
				joinGroup.setText(R.string.delete_quit);
				joinGroup.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						showQuitGroupDialog(GroupDataActivity.this,
								R.string.tip, R.string.dialog_ok,
								R.string.cancel,
								R.string.confirm_delete_group_title,
								detail.getMid(), joinGroup,dialog);
					}
				});
			} else {
				// 未加入群，显示加入群
				joinGroup.setText(R.string.add);
				// 加群
				joinGroup.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated
						// method stub
						joinGroup(result.getData().getMid(), result.getData()
								.getFlockName(), dialog);
					}
				});
			}
		}
	}

	private Dialog showDialog(Context context, final View contentView) {
		final Dialog dialog = new Dialog(context, R.style.alert_dialog);
		dialog.show();
		dialog.setCanceledOnTouchOutside(true);
		Window window = dialog.getWindow();
		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
		p.width = (int) (d.getWidth()); // 宽度设置
		window.setAttributes(p); // 设置生效
		window.setGravity(Gravity.BOTTOM);
		dialog.setContentView(contentView);
		return dialog;
	}

	/**
	 * 加入群
	 */
	private void joinGroup(String mid, String flockName, final Dialog dialog) {
	
		params.put("userId", userId);
		params.put("mid", mid);
		httpInstance
				.post(Constant.HOST_URL + Constant.Interface.APPLY_FOR_IN_GROUP,
						params, new AbsHttpStringResponseListener(
								GroupDataActivity.this, null) {
							@Override
							public void onSuccess(int i, String s) {
								Result result = (Result) AbJsonUtil.fromJson(s,
										Result.class);
								if (result.getRepCode().contains(
										Constant.HTTP_SUCCESS)) {
									Toast.makeText(context,
											R.string.apply_for_success,
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
	 * 踢出成员用户/用户自己退群 cid：群id(必填) userId:用户id(必填)
	 */
	private void deleteGroup(final String mid, final Dialog dialog,
			final Button joinBtn) {
		params.put("userId", userId);
		params.put("cid", mid);
		httpInstance.post(Constant.HOST_URL + Constant.Interface.DELETE_MEMBER,
				params, new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						Result result = (Result) AbJsonUtil.fromJson(s,
								Result.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							Toast.makeText(context, R.string.quit_success,
									Toast.LENGTH_LONG).show();
							dialog.cancel();
							getGroupDetails(mid);
						} else {
							Toast.makeText(context, result.getRepMsg(),
									Toast.LENGTH_LONG).show();
							dialog.cancel();
						}

					}
				});
	}

	/**
	 * 解散群 userId:群主id（必填） groupId:群id（必填）
	 */
	private void dimissGroup(final String mid, final Dialog dialog) {
		params.put("userId", userId);
		params.put("groupId", mid);
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.GROUP_MASTER_DISMISS, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						Result result = (Result) AbJsonUtil.fromJson(s,
								Result.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							Toast.makeText(context, R.string.dimiss_success,
									Toast.LENGTH_LONG).show();
							Intent intent = new Intent();
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.setClass(context, MakeFriendsActivity1.class);
							startActivity(intent);
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
	 * 是否确认退出群的提示
	 * 
	 * @param context
	 * @param titleId
	 * @param confirmTextId
	 * @param cancelTextId
	 * @param contentStr
	 * @param mid
	 * @param joinBtn
	 */
	private void showQuitGroupDialog(final Context context, int titleId,
			int confirmTextId, int cancelTextId, int contentStr,
			final String mid, final Button joinBtn,final Dialog dialog1) {
		SweetAlertDialog dialog = new SweetAlertDialog(context)
				.setContentText(context.getString(contentStr));
		dialog.setTitleText(context.getString(titleId));
		dialog.setConfirmText(context.getString(confirmTextId));
		dialog.setCancelText(context.getString(cancelTextId));
		dialog.setContentText(context.getString(contentStr));
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				deleteGroup(mid, sweetAlertDialog, joinBtn);
				sweetAlertDialog.dismiss();
				dialog1.dismiss();
			}
		});
		dialog.setCancelClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				sweetAlertDialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * 是否确认解散群的提示
	 * 
	 * @param context
	 * @param titleId
	 * @param confirmTextId
	 * @param cancelTextId
	 * @param contentStr
	 * @param mid
	 * @param joinBtn
	 */
	private void showDismissGroupDialog(final Context context, int titleId,
			int confirmTextId, int cancelTextId, int contentStr,
			final String mid, final Dialog dialog1) {
		SweetAlertDialog dialog = new SweetAlertDialog(context)
				.setContentText(context.getString(contentStr));
		dialog.setTitleText(context.getString(titleId));
		dialog.setConfirmText(context.getString(confirmTextId));
		dialog.setCancelText(context.getString(cancelTextId));
		dialog.setContentText(context.getString(contentStr));
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				dimissGroup(mid, dialog1);
				sweetAlertDialog.dismiss();
				dialog1.dismiss();
			}
		});
		dialog.setCancelClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				sweetAlertDialog.dismiss();
			}
		});
		dialog.show();
	}
}
