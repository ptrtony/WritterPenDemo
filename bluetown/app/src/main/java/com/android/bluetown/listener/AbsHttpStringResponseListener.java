package com.android.bluetown.listener;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.ab.fragment.AbLoadDialogFragment;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbToastUtil;
import com.android.bluetown.R;
import com.android.bluetown.custom.dialog.MyAbLoadDialogFragment;

/**
 * Created by gouh on 2/27/2015.
 */
@SuppressLint("ResourceAsColor")
public abstract class AbsHttpStringResponseListener extends
		AbStringHttpResponseListener {
	protected Context context;
	protected String start = null;
	protected String failure = null;

	public AbsHttpStringResponseListener(Context c) {
		this(c, null, null);
	}

	public AbsHttpStringResponseListener(Context c, String s) {
		this(c, s, null);
	}

	public AbsHttpStringResponseListener(Context c, int s) {
		this(c, c.getString(s));
	}

	public AbsHttpStringResponseListener(Context c, String s, String f) {
		context = c;
		if (s == null) {
			s = c.getString(R.string.load_waitting);
		}
		if (f == null) {
			f = c.getString(R.string.server_error);
		}
		start = s;
		failure = f;
	}

	public AbsHttpStringResponseListener(Context c, int s, int f) {
		this(c, c.getString(s), c.getString(f));
	}

	@Override
	public void onStart() {
		showLoadDialog(context, R.drawable.progress_circular, start);
	}

	@Override
	public void onFinish() {
		removeDialog();
	}

	@Override
	public void onFailure(int i, String s, Throwable throwable) {
		if (failure != null) {
			AbToastUtil.showToast(context, R.string.server_error);
		}
	}

	private static String mDialogTag = "dialog";
	private AbLoadDialogFragment newFragment;

	public AbLoadDialogFragment showLoadDialog(Context context,
			int indeterminateDrawable, String message) {
		FragmentActivity activity = (FragmentActivity) context;
		newFragment = MyAbLoadDialogFragment.newInstance(1, 16973939);
		newFragment.setIndeterminateDrawable(indeterminateDrawable);
		newFragment.setMessage(message);
		newFragment.setTextColor(R.color.font_black);
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		ft.setTransition(4097);
		newFragment.show(ft, mDialogTag);

		return newFragment;
	}

	public void removeDialog() {
		if (newFragment != null) {
			newFragment.dismissAllowingStateLoss();
		}

	}
}
