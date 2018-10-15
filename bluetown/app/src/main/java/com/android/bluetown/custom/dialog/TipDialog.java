package com.android.bluetown.custom.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.android.bluetown.LoginActivity;
import com.android.bluetown.utils.Log;

/**
 * 
 * @ClassName: TipDialog
 * @Description:TODO(提示类对话框)
 * @author: shenyz
 * @date: 2015年8月4日 上午10:28:49
 * 
 */
public class TipDialog extends SweetAlertDialog {

	public TipDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public TipDialog(Context context, int alertType) {
		super(context, alertType);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @Title: showDialog
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param context
	 *            上下文对象
	 * @param alterType
	 *            对画框类型
	 * @param contentStr
	 *            对话框提示内容
	 * @throws
	 */
	public static void showDialog(Activity context, int alterType, int contentStr) {
		SweetAlertDialog dialog = new SweetAlertDialog(context, alterType,
				false).setContentText(context.getString(contentStr));
		if (!context.isFinishing()){
			dialog.show();
		}
	}

	/**
	 * 
	 * @Title: showDialog
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param context
	 *            上下文对象
	 * @param alterType
	 *            对画框类型
	 * @param contentStr
	 *            对话框提示内容
	 * @throws
	 */
	public static void showDialog(Activity context, int alterType,
			String contentStr) {
		SweetAlertDialog dialog = new SweetAlertDialog(context, alterType,
				false).setContentText(contentStr);
		if (!context.isFinishing()){
			dialog.show();
		}
	}

	/**
	 * 
	 * @Title: showDialogFinishSelf
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param context
	 * @param titleId
	 * @param confirmTextId
	 * @param contentStr
	 * @throws
	 */
	public static void showDialogFinishSelf(final Activity context,
			int titleId, int confirmTextId, int contentStr) {
		SweetAlertDialog dialog = new SweetAlertDialog(context)
				.setContentText(context.getString(contentStr));
		dialog.setTitleText(context.getString(titleId));
		dialog.setConfirmText(context.getString(confirmTextId));
		dialog.isShowCancelButton();
		dialog.setContentText(context.getString(contentStr));
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				context.finish();
				sweetAlertDialog.dismiss();
			}
		});
		if (!context.isFinishing()){
			dialog.show();
		}
	}

	public static void showDialog(Context context, int titleId,
			int confirmTextId, int contentStr) {
		SweetAlertDialog dialog = new SweetAlertDialog(context)
				.setContentText(context.getString(contentStr));
		dialog.setTitleText(context.getString(titleId));
		dialog.setConfirmText(context.getString(confirmTextId));
		dialog.isShowCancelButton();
		dialog.setContentText(context.getString(contentStr));
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				sweetAlertDialog.dismiss();
			}
		});
		dialog.show();
	}

	public static void showDialog(final Context context, int titleId,
			int confirmTextId, String contentStr) {
		SweetAlertDialog dialog = new SweetAlertDialog(context)
				.setContentText(contentStr);
		dialog.setTitleText(context.getString(titleId));
		dialog.setConfirmText(context.getString(confirmTextId));
		dialog.isShowCancelButton();
		dialog.setContentText(contentStr);
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				sweetAlertDialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * 
	 * @Title: showDialog
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param context
	 * @param titleId
	 * @param confirmTextId
	 * @param contentStr
	 * @throws
	 */
	public static void showDialog(final Activity context, int titleId,
			int confirmTextId, int contentStr) {
		SweetAlertDialog dialog = new SweetAlertDialog(context)
				.setContentText(context.getString(contentStr));
		dialog.setTitleText(context.getString(titleId));
		dialog.setConfirmText(context.getString(confirmTextId));
		dialog.isShowCancelButton();
		dialog.setContentText(context.getString(contentStr));
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				sweetAlertDialog.dismiss();
			}
		});
		if (!context.isFinishing()){
			dialog.show();
		}

	}

	/**
	 * showDialog
	 * 
	 * @param context
	 * @param titleId
	 * @param confirmTextId
	 * @param contentStr
	 */
	public static void showDialog(final Activity context, int titleId,
			int confirmTextId, String contentStr) {
		SweetAlertDialog dialog = new SweetAlertDialog(context)
				.setContentText(contentStr);
		dialog.setTitleText(context.getString(titleId));
		dialog.setConfirmText(context.getString(confirmTextId));
		dialog.isShowCancelButton();
		dialog.setContentText(contentStr);
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				sweetAlertDialog.dismiss();
			}
		});
		if (!context.isFinishing()){
			dialog.show();
		}
	}

	/**
	 * 
	 * @Title: showDialog
	 * @Description: TODO(确定按钮有实际作用的对话框)
	 * @param context
	 *            上下文对象
	 * @param titleId
	 *            标题String的Id
	 * @param confirmTextId
	 *            确定按钮String的Id
	 * @param contentStr
	 *            提示内容String的Id
	 * @param cls
	 *            跳转的Activity的Class对象 并清除栈里面的其他Activity
	 * @throws
	 */
	public static void showDialogClearTop(final Activity context, int titleId,
			int confirmTextId, int contentStr, final Class<?> cls) {
		SweetAlertDialog dialog = new SweetAlertDialog(context)
				.setContentText(context.getString(contentStr));
		dialog.setTitleText(context.getString(titleId));
		dialog.setConfirmText(context.getString(confirmTextId));
		dialog.isShowCancelButton();
		dialog.setContentText(context.getString(contentStr));
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, cls);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
				context.finish();
				sweetAlertDialog.dismiss();
			
			}
		});
		if (!context.isFinishing()){
			dialog.show();
		}
	}

	/**
	 * 
	 * @Title: showDialog
	 * @Description: TODO(确定按钮有实际作用的对话框)
	 * @param context
	 *            上下文对象
	 * @param titleId
	 *            标题String的Id
	 * @param confirmTextId
	 *            确定按钮String的Id
	 * @param contentStr
	 *            提示内容String的Id
	 * @param cls
	 *            跳转的Activity的Class对象 并Close自己
	 * @throws
	 */
	public static void showDialogStartNewActivity(final Activity context,
			int titleId, int confirmTextId, int contentStr, final Class<?> cls) {
		SweetAlertDialog dialog = new SweetAlertDialog(context)
				.setContentText(context.getString(contentStr));
		dialog.setTitleText(context.getString(titleId));
		dialog.setConfirmText(context.getString(confirmTextId));
		dialog.isShowCancelButton();
		dialog.setContentText(context.getString(contentStr));
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, cls);
				context.startActivity(new Intent(context, cls));
				context.finish();
				sweetAlertDialog.dismiss();
			}
		});
		if (!context.isFinishing()){
			dialog.show();
		}
	}
	public static void showDialogStartNewActivity(final Activity context,
												  int titleId, int confirmTextId, String contentStr, final Class<?> cls) {
		SweetAlertDialog dialog = new SweetAlertDialog(context)
				.setContentText(contentStr);
		dialog.setTitleText(context.getString(titleId));
		dialog.setConfirmText(context.getString(confirmTextId));
		dialog.isShowCancelButton();
		dialog.setContentText(contentStr);
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, cls);
				context.startActivity(new Intent(context, cls));
				if (!context.getClass().getSimpleName().equals("SelectAreaActivity")){
					context.finish();
				}
				sweetAlertDialog.dismiss();
			}
		});
		if (!context.isFinishing()){
			dialog.show();
		}
	}
	/**
	 * 
	 * @Title: showDialogNoClose
	 * @Description: TODO(确定按钮有实际作用的对话框)
	 * @param context
	 *            上下文对象
	 * @param titleId
	 *            标题String的Id
	 * @param confirmTextId
	 *            确定按钮String的Id
	 * @param contentStr
	 *            提示内容String的Id
	 * @param cls
	 *            跳转的Activity的Class对象(与用户登录交互用)
	 * @throws
	 */
	public static void showDialogNoClose(final Context context, int titleId,
			int confirmTextId, int contentStr, final Class<?> cls) {
		SweetAlertDialog dialog = new SweetAlertDialog(context)
				.setContentText(context.getString(contentStr));
		dialog.setTitleText(context.getString(titleId));
		dialog.setConfirmText(context.getString(confirmTextId));
		dialog.isShowCancelButton();
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentText(context.getString(contentStr));
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				if (cls == LoginActivity.class) {
					// 提示用户登录完，返回之前的页面
					Intent intent = new Intent();
					intent.putExtra("flag", "close");
					intent.setClass(context, cls);
					context.startActivity(intent);
					((Activity)context).finish();
				} else {
					context.startActivity(new Intent(context, cls));
				}
				sweetAlertDialog.dismiss();
			}
		});
		dialog.show();
	}
}
