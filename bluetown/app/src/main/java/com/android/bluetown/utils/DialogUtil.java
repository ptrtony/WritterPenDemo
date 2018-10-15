package com.android.bluetown.utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

/**
 * @author <a href="mailto:sunyi4j@gmail.com">Roy</a> on Sep 2, 2011
 */
public class DialogUtil {
	private static boolean isShowing = false;
	private static final Lock lock = new ReentrantLock();
	
	/**
	 * Interface of dialog callback.
	 */
	public interface DialogCallback {
		public void callbackOk();

		public void callbackCancel();
	}

	/**
	 * creat a alertDialog have ok button.
	 * 
	 * @param Context
	 *            : Activity's context
	 * @param title
	 *            : the dialog title, null is promise.
	 * @param message
	 *            : the dialog message, null is promise.
	 * @return void
	 */
	public static void showInfo(Context context, String title, String message) {
		showInfo(context, title, message, null);
	}
	
	/**
	 * creat a alertDialog have ok button.
	 * @param context
	 * @param title
	 * @param message
	 * @param callback
	 */
	public static void showInfo(Context context, String title, String message, DialogCallback callback) {
		creatDialog(context, title, message, callback, false, false);
	}

	/**
	 * creat a alertDialog have ok&cancel button.
	 * 
	 * @param Context
	 *            : Activity's context
	 * @param title
	 *            : the dialog title, null is promise.
	 * @param message
	 *            : the dialog message, null is promise.
	 * @param DialogCallback
	 *            : the callback.
	 * @return void
	 */
	public static void showConfirm(Context context, String title, String message, DialogCallback callback) {
		creatDialog(context, title, message, callback, false, true);
	}

	/**
	 * creat a alertDialog have ok&cancel button.
	 * 
	 * @param Context
	 *            : Activity's context
	 * @param title
	 *            : the dialog title, null is promise.
	 * @param message
	 *            : the dialog message, null is promise.
	 * @param DialogCallback
	 *            : the callback.
	 * @param DialogCallback
	 *            : the button show yes/no or ok/cancel.
	 * @return void
	 */
	public static void showConfirmOrRefuse(Context context, String title, String message, DialogCallback callback) {
		creatDialog(context, title, message, callback, true, true);
	}

	/**
	 * creat a alertDialog
	 * 
	 * @param Context
	 *            : Activity's context
	 * @param title
	 *            : the dialog title, null is promise.
	 * @param message
	 *            : the dialog message, null is promise.
	 * @param DialogCallback
	 *            : the callback.
	 * @return void
	 */
	private static void creatDialog(Context context, String title, String Message, final DialogCallback dialogCallback, boolean type, boolean isConfirm) {
		// Single dialog handling
		lock.lock();
		if (!isShowing) {
			isShowing = true;
			lock.unlock();

			try {
				Builder builder = new Builder(context);
				builder.setMessage(Message);
				builder.setTitle(title);
				String okStr, cancelStr;
				if (type) {
					okStr = context.getResources().getString(android.R.string.yes);
					cancelStr = context.getResources().getString(android.R.string.no);
				} else {
					okStr = context.getResources().getString(android.R.string.ok);
					cancelStr = context.getResources().getString(android.R.string.cancel);
				}
				builder.setPositiveButton(okStr, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						try {
							if (dialogCallback != null) {
								dialogCallback.callbackOk();
							}
						} catch (Exception e) {
						} finally {
							isShowing = false;
						}
					}
				});
				if (dialogCallback != null && isConfirm) {
					builder.setNegativeButton(cancelStr, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								dialogCallback.callbackCancel();
							} catch (Exception e) {
							} finally {
								isShowing = false;
							}
						}
					});
				}
				AlertDialog dialog = builder.create();
				dialog.setOnCancelListener(new OnCancelListener() {
					  
					@Override
					public void onCancel(DialogInterface dialog) {
						try {
							dialogCallback.callbackCancel();
						} catch (Exception e) {
						} finally {
							isShowing = false;
						}
					}
				});
				
				Activity activity = (Activity)context;
				
				if(!activity.isFinishing()) {
					dialog.show();
				} else {
					isShowing = false;
				}
			} catch (RuntimeException ex) {
				isShowing = false;
				throw ex;
			}
		} else {
			lock.unlock();
		}
	}

	/**
	 * creat a toast show short time
	 * 
	 * @param Context
	 *            : Activity's context
	 * @param message
	 *            : the dialog message.
	 * @return void
	 */
	public static void showToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * creat a toast show long time
	 * 
	 * @param Context
	 *            : Activity's context
	 * @param message
	 *            : the dialog message.
	 * @return void
	 */
	public static void showToastForLong(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_LONG).show();
	}

}
