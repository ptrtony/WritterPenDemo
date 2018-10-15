package com.android.bluetown.fragment;
import java.util.ArrayList;
import java.util.List;
import net.tsz.afinal.FinalDb;
import org.json.JSONException;
import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbJsonUtil;
import com.android.bluetown.R;
import com.android.bluetown.bean.FriendsBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.PromptDialog;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.home.main.model.act.OthersInfoActivity;
import com.android.bluetown.my.AuthenticationActivity;
import com.android.bluetown.my.AuthenticationIngActivity;
import com.android.bluetown.mywallet.activity.PayActivity;
import com.android.bluetown.mywallet.activity.PayMoneyActivity;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.FriendResult;
import com.android.bluetown.utils.Constant;
import android.support.v4.app.Fragment;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import static android.content.Context.VIBRATOR_SERVICE;

public class CoreFragment extends Fragment implements OnClickListener , QRCodeView.Delegate{
	private static final String TAG = CoreFragment.class.getSimpleName();
	private QRCodeView mQRCodeView;
	private View view;
	private FinalDb db;
	private String userId;
	private SharePrefUtils prefUtils;
	private AbHttpUtil httpUtil = null;
	private String merchantId;
	private String merchantName;
	private String ttoken;
	private String money;
	private ArrayList<FriendsBean> list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_core, container, false);
		httpUtil = AbHttpUtil.getInstance(getActivity());
		httpUtil.setTimeout(10000);
		httpUtil.setEasySSLEnabled(true);
		initView();
		getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		return view;
	}

	private void initView() {
		mQRCodeView = view.findViewById(R.id.zxingview);
		mQRCodeView.setDelegate(this);
		mQRCodeView.startSpot();
		view.findViewById(R.id.titleLeft).setOnClickListener(this);
		prefUtils = new SharePrefUtils(getActivity());
		list = new ArrayList<FriendsBean>();
		db = FinalDb.create(getActivity());
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		mQRCodeView.startCamera();
		mQRCodeView.showScanRect();
	}

	@Override
	public void onStop() {
		mQRCodeView.stopCamera();
		super.onStop();
	}


	@SuppressWarnings("deprecation")
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		mQRCodeView.onDestroy();
		super.onDestroy();
	}

	private void vibrate() {
		Vibrator vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(200);
	}

	private void coderesult(String code) {
		AbRequestParams params = new AbRequestParams();
		// httpUtil.setToken(SharePUtil.get(SharePUtil.KEY_TOKEN, ""));
		params.put("userId", userId);
		params.put("communicationToken",
				prefUtils.getString(SharePrefUtils.TOKEN, ""));
		params.put("type", "用户");
		params.put("code", code);
		httpUtil.post(Constant.HOST_URL
				+ Constant.Interface.PaymentCodeAction_selectByCode, params,
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int arg0, String arg1) {
						// TODO Auto-generated method stub
						JSONObject json;
						try {
							json = new JSONObject(arg1);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								JSONObject data1 = new JSONObject(json
										.optString("data1"));
								JSONObject data2 = new JSONObject(json
										.optString("data2"));
								JSONObject mer = new JSONObject(data1
										.optString("mer"));
								money = json.optString("data3");
								merchantId = mer.optString("userId");
								merchantName = mer.optString("merchantName");
								ttoken = data2.optString("token");
								if(prefUtils.getString(SharePrefUtils.CHECKSTATE, "").equals("1")){
									if (json.optDouble("data3") == 0) {
										Intent intent = new Intent();
										intent.setClass(getActivity(),
												PayActivity.class);
										intent.putExtra("merchantId", merchantId);
										intent.putExtra("ttoken", ttoken);
										intent.putExtra("merchantName",
												merchantName);
										startActivity(intent);
									} else {
										Intent intent = new Intent();
										intent.setClass(getActivity(),
												PayMoneyActivity.class);
										intent.putExtra("money", money);
										intent.putExtra("merchantId", merchantId);
										intent.putExtra("ttoken", ttoken);
										intent.putExtra("merchantName",
												merchantName);
										startActivity(intent);
									}
								}else if(prefUtils.getString(SharePrefUtils.CHECKSTATE, "").equals("3")){
									TipDialog.showDialogNoClose(getActivity(),
											R.string.tip, R.string.AuthenticationIng,
											R.string.Authenticationinfo,
											AuthenticationIngActivity.class);
								}else{
									TipDialog.showDialogNoClose(getActivity(),
											R.string.tip, R.string.gotoAuthentication,
											R.string.Authenticationinfo, AuthenticationActivity.class);
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
														getActivity().finish();
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

	private void DistinguishCode(String distinguishCode) {
		AbRequestParams params = new AbRequestParams();
		// httpUtil.setToken(SharePUtil.get(SharePUtil.KEY_TOKEN, ""));
		params.put("userId", userId);
		params.put("communicationToken",
				prefUtils.getString(SharePrefUtils.TOKEN, ""));
		params.put("type", "用户");
		params.put("distinguishCode", distinguishCode);
		httpUtil.post(Constant.HOST_URL
				+ Constant.Interface.PaymentCodeAction_selectByDistinguishCode,
				params, new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int arg0, String arg1) {
						// TODO Auto-generated method stub
						JSONObject json;
						try {
							json = new JSONObject(arg1);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								JSONObject data1 = new JSONObject(json
										.optString("data1"));
								JSONObject data2 = new JSONObject(json
										.optString("data2"));
								JSONObject mer = new JSONObject(data1
										.optString("mer"));
								money = json.optString("data3");
								merchantId = mer.optString("userId");
								merchantName = mer.optString("merchantName");
								ttoken = data2.optString("token");
								if(prefUtils.getString(SharePrefUtils.CHECKSTATE, "").equals("1")){
									Intent intent = new Intent();
									intent.setClass(getActivity(),
											PayActivity.class);
									intent.putExtra("merchantId", merchantId);
									intent.putExtra("ttoken", ttoken);
									intent.putExtra("merchantName", merchantName);
									startActivity(intent);
								}else if(prefUtils.getString(SharePrefUtils.CHECKSTATE, "").equals("3")){
									TipDialog.showDialogNoClose(getActivity(),
											R.string.tip, R.string.AuthenticationIng,
											R.string.Authenticationinfo,
											AuthenticationIngActivity.class);
								}else{
									TipDialog.showDialogNoClose(getActivity(),
											R.string.tip, R.string.gotoAuthentication,
											R.string.Authenticationinfo, AuthenticationActivity.class);
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
														getActivity().finish();
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

	private void add(String code) {
		AbRequestParams params = new AbRequestParams();
		params.put("userId", userId);
		// type:必填（0:查看好友；1：查看所有用户）
		params.put("type", "1");
		params.put("userCode", code);
		params.put("page", 1 + "");
		params.put("rows", 10 + "");
		httpUtil.post(Constant.HOST_URL
				+ Constant.Interface.FRIEND_LIST, params,
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int arg0, String arg1) {
						// TODO Auto-generated method stub
						JSONObject json;
						try {
							json = new JSONObject(arg1);
							FriendResult result = (FriendResult) AbJsonUtil
									.fromJson(arg1, FriendResult.class);
							if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
								list.addAll(result.getData().getFriend().getRows());
								Intent intent = new Intent();
								intent.putExtra("otherUserId", list.get(0).getUserId());
								intent.setClass(getActivity(), OthersInfoActivity.class);
								startActivity(intent);
															
							} else if (result.getRepCode().contains(Constant.HTTP_NO_DATA)){
								new PromptDialog.Builder(getActivity())
								.setViewStyle(
										PromptDialog.VIEW_STYLE_NORMAL)
								.setMessage("你们已是好友")
								.setButton1(
										"确定",
										new PromptDialog.OnClickListener() {

											@Override
											public void onClick(
													Dialog dialog,
													int which) {
												// TODO Auto-generated
												// method stub
												getActivity().finish();
												dialog.cancel();
											}
										}).show();
								
							}else {
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
														getActivity().finish();
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.titleLeft:
			getActivity().finish();
			break;
		}
	}

	@Override
	public void onScanQRCodeSuccess(String result) {
		if (result.equals("")) {
			Toast.makeText(getActivity(), "Scan failed!", Toast.LENGTH_SHORT)
					.show();
		} else {
			vibrate();
			mQRCodeView.startSpot();
			if (result.indexOf("distinguishCode") != -1) {
				String str[] = result.split("\\=");
				DistinguishCode(str[1]);
			} else if(result.indexOf("userCode") != -1){
				String str[] = result.split("\\=");
				add(str[1]);
			}else{
				coderesult(result);
			}

		}
		mQRCodeView.stopSpot();
	}

	@Override
	public void onScanQRCodeOpenCameraError() {
		Log.e(TAG, "打开相机出错");
	}

}
