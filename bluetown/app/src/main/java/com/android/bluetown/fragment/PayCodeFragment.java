package com.android.bluetown.fragment;

import java.lang.ref.WeakReference;
import java.util.List;

import net.tsz.afinal.FinalDb;

import org.feng.sockettest.server.BackService;
import org.feng.sockettest.server.IBackService;
import org.json.JSONException;
import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.android.bluetown.R;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.PromptDialog;
import com.android.bluetown.mywallet.activity.RechargeActivity;
import com.android.bluetown.mywallet.activity.TransferSuccessActivity;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.EncodingHandler;
import com.google.zxing.WriterException;
//import com.mining.app.zxing.camera.CameraManager;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class PayCodeFragment extends Fragment implements OnClickListener {
	private ImageView barcode;
	private ImageView QR;
	private TextView barcode_num;
	private View view;
	private AbHttpUtil httpUtil = null;
	private FinalDb db;
	private String userId;
	private String code;
	private SharePrefUtils prefUtils;
	private MessageBackReciver mReciver;
	public IBackService iBackService;
	private IntentFilter mIntentFilter;
	public Intent mServiceIntent;

	private LocalBroadcastManager mLocalBroadcastManager;
	public ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			iBackService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			iBackService = IBackService.Stub.asInterface(service);
			try {
				List<MemberUser> users = db.findAll(MemberUser.class);
				if (users != null && users.size() != 0) {
					MemberUser user = users.get(0);
					if (user != null) {
						iBackService.sendMessage(user.getMemberId());
					}
				}				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	};

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_paycode, container, false);
//		CameraManager.init(getActivity().getApplication());
		httpUtil = AbHttpUtil.getInstance(getActivity());
		httpUtil.setTimeout(10000);
		httpUtil.setEasySSLEnabled(true);
		getActivity().getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		initView();
		return view;
	}

	private void initView() {
		QR = (ImageView) view.findViewById(R.id.QR);
		barcode = (ImageView) view.findViewById(R.id.barcode);
		barcode_num = (TextView) view.findViewById(R.id.barcode_num);
		view.findViewById(R.id.refresh).setOnClickListener(this);
		prefUtils = new SharePrefUtils(getActivity());
		db = FinalDb.create(getActivity());
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		view.findViewById(R.id.titleLeft).setOnClickListener(this);
		mLocalBroadcastManager = LocalBroadcastManager
				.getInstance(getActivity());
		mReciver = new MessageBackReciver(barcode_num);
		mServiceIntent = new Intent(getActivity(), BackService.class);
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(BackService.HEART_BEAT_ACTION);
		mIntentFilter.addAction(BackService.MESSAGE_ACTION);
		

	}

	class MessageBackReciver extends BroadcastReceiver {
		private WeakReference<TextView> textView;

		public MessageBackReciver(TextView tv) {
			textView = new WeakReference<TextView>(tv);
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			TextView tv = textView.get();
			if (action.equals(BackService.HEART_BEAT_ACTION)) {
				if (null != tv) {
					tv.setText("Get a heart heat");
				}
			} else {
				String message = intent.getStringExtra("message");
				try {
					JSONObject json = new JSONObject(message);
					if (json.optString("code").equals(Constant.HTTP_SUCCESS)) {
						Intent jushIntent = new Intent();
						jushIntent.setClass(getActivity(),
								TransferSuccessActivity.class);
						jushIntent.putExtra("title", "支付");
						jushIntent.putExtra("money",
								"¥" + json.optString("money"));
						jushIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
								| Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(jushIntent);
					} else if(json.optString("code").equals("666666")){
						new PromptDialog.Builder(getActivity())
						.setViewStyle(PromptDialog.VIEW_STYLE_NORMAL)
						.setCancelable(false)
						.setMessage(json.optString("message"))
						.setButton1("确认前往",
								new PromptDialog.OnClickListener() {

									@Override
									public void onClick(Dialog dialog,
											int which) {
										// TODO Auto-generated
										// method stub
										Intent intent = new Intent(getActivity(),
												RechargeActivity.class);
										intent.putExtra("type", 0);
										startActivity(intent);
										dialog.cancel();
									}
								}).show();
					}else {
						new PromptDialog.Builder(getActivity())
								.setViewStyle(PromptDialog.VIEW_STYLE_NORMAL)
								.setCancelable(false)
								.setMessage(json.optString("message"))
								.setButton1("确定",
										new PromptDialog.OnClickListener() {

											@Override
											public void onClick(Dialog dialog,
													int which) {
												// TODO Auto-generated
												// method stub
												creatQR();
												dialog.cancel();
											}
										}).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}

	private void creatQR() {
		AbRequestParams params = new AbRequestParams();
		// httpUtil.setToken(SharePUtil.get(SharePUtil.KEY_TOKEN, ""));
		params.put("userId", userId);
		params.put("code", code);
		params.put("communicationToken",
				prefUtils.getString(SharePrefUtils.TOKEN, ""));
		httpUtil.post(Constant.HOST_URL
				+ Constant.Interface.PaymentCodeAction_createCode, params,
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int arg0, String arg1) {
						// TODO Auto-generated method stub
						JSONObject json;
						try {
							json = new JSONObject(arg1);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								JSONObject data = new JSONObject(json
										.optString("data"));
								try {
									code = data.optString("code");
									Bitmap qrCodeBitmap = EncodingHandler
											.createQRCode(
													data.optString("code"),
													getW() / 4 * 3);
									barcode_num.setText(data.optString("code"));
									QR.setImageBitmap(qrCodeBitmap);
									LayoutParams layoutParams = barcode
											.getLayoutParams();
									Bitmap qrCodeBitmap2 = EncodingHandler
											.creatBarcode(getActivity(),
													data.optString("code"),
													getW() / 4 * 3, 160, false);
									barcode.setImageBitmap(qrCodeBitmap2);
								} catch (WriterException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								new PromptDialog.Builder(getActivity())
										.setViewStyle(
												PromptDialog.VIEW_STYLE_NORMAL)
										.setMessage(json.optString("repMsg"))
										.setButton1(
												"确定",
												new PromptDialog.OnClickListener() {

													@Override
													public void onClick(
															Dialog dialog,
															int which) {
														// TODO Auto-generated
														// method stub
														dialog.cancel();
													}
												}).show();
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int arg0, String arg1, Throwable arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub

					}

				});

	}

	/**
	 * @return 获取屏幕宽度
	 */
	public int getW() {
		WindowManager wm = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		int i = outMetrics.widthPixels;
		return i;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.titleLeft:
			getActivity().finish();
			break;
		case R.id.refresh:
			creatQR();
			// test();
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		creatQR();
		mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);
		getActivity().bindService(mServiceIntent, conn,
				Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onStop() {
		if (conn != null && mReciver != null) {
			getActivity().unbindService(conn);
			mLocalBroadcastManager.unregisterReceiver(mReciver);
			try {
				iBackService.close();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		super.onStop();

	}
}
