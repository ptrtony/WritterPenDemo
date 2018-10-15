package com.android.bluetown;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalDb;

import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.my.AuthenticationActivity;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.Utils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lingyun.qr.exception.ParamFormatException;
import com.lingyun.qr.handler.QRUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author hedi
 * @data: 2016年4月29日 下午4:16:03
 * @Description: 通行码
 */
public class PassCodeActivity extends TitleBarActivity implements
		OnClickListener {
	private ImageView my_QR;
	private FinalDb db;
	private String userId;
	private SharePrefUtils prefUtils;
	private TextView confirm, refresh;
	private LinearLayout rl_qr;
	private LinearLayout ll;
	private Date d2, d1;
	private ConnectivityManager mConnectivityManager;
	private NetworkInfo netInfo;
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private boolean isReceiverTag = false;
	/**
	 * register network state
	 */
	private void registerNetWorkRecevicer() {
		// TODO Auto-generated method stub
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(myNetReceiver, mFilter);

	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("通行码");
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_passcode);
		BlueTownExitHelper.addActivity(this);
		prefUtils = new SharePrefUtils(this);
		if (prefUtils.getString(SharePrefUtils.CHECKSTATE, "").equals("1")) {
			initView();
		} else {
			initView2();
		}

	}

	private void initView() {
		db = FinalDb.create(this);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		my_QR = (ImageView) findViewById(R.id.my_QR);
		refresh = (TextView) findViewById(R.id.refresh);
		rl_qr = (LinearLayout) findViewById(R.id.rl_qr);
		refresh.setOnClickListener(this);
		rl_qr.setVisibility(View.VISIBLE);
		d1 = new Date(System.currentTimeMillis());
		try {
			d2 = sDateFormat.parse(prefUtils.getString(
					SharePrefUtils.KEY_LIST_TIME, ""));
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		// if (prefUtils.getString(SharePrefUtils.LL_ID, "") == null
		// || prefUtils.getString(SharePrefUtils.LL_ID, "").equals("")
		// || prefUtils.getString(SharePrefUtils.KEY_LIST, "") == null
		// || prefUtils.getString(SharePrefUtils.KEY_LIST, "").equals("")
		// || d2.getTime() - d1.getTime() < 0) {
		// getdata();
		// } else {
		//
		//
		// }
		resetBitmap("132456484656321");
		if (!isReceiverTag){
			registerNetWorkRecevicer();
			isReceiverTag = true;
		}

	}

	private void initView2() {
		ll = (LinearLayout) findViewById(R.id.ll);
		ll.setVisibility(View.VISIBLE);
		confirm = (TextView) findViewById(R.id.confirm);
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(PassCodeActivity.this,
						AuthenticationActivity.class));
			}
		});

	}

	private void getdata() {
		// TODO Auto-generated method stub
		params.put("userId", userId);
		params.put("communicationToken",
				prefUtils.getString(SharePrefUtils.TOKEN, ""));
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.MenJinAction_getSdkKeyAndLLid, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						JSONObject json;
						try {
							json = new JSONObject(s);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								try {
									List<String> keyList = new ArrayList<String>();
									JSONArray data = json.optJSONArray("data1");
									for (int j = 0; j < data.length(); j++) {
										String itemObj = data.optString(j);
										keyList.add(itemObj);
									}
									prefUtils.setString(SharePrefUtils.LL_ID,
											json.optString("data2"));
									prefUtils.setString(
											SharePrefUtils.KEY_LIST_TIME,
											json.optString("data3"));
									prefUtils.setString(
											SharePrefUtils.KEY_LIST,
											Utils.SceneList2String(keyList));
									List<Integer> storeAuthList = new ArrayList<Integer>();
									storeAuthList.add(1);
									storeAuthList.add(7);
									storeAuthList.add(8);
									String a = null;
									a = QRUtils.createElevatorControlQR(
											json.optString("data2"), keyList,
											storeAuthList, 5, 1, 1, "84324343",
											-3);
									resetBitmap(a);
								} catch (ParamFormatException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								resetBitmap("132456484656321");
//								new PromptDialog.Builder(PassCodeActivity.this)
//										.setViewStyle(
//												PromptDialog.VIEW_STYLE_NORMAL)
//										.setMessage(json.optString("repMsg"))
//										.setButton1(
//												"确定",
//												new PromptDialog.OnClickListener() {
//
//													@Override
//													public void onClick(
//															Dialog dialog,
//															int which) {
//														// TODO Auto-generated
//														// method stub
//														dialog.dismiss();
//													}
//												}).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

	}

	private void resetBitmap(final String content) {
		runOnUiThread(new Runnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				try {
					my_QR.setImageDrawable(new BitmapDrawable(encodeToQR(
							content, 600, null)));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 将字符串按照指定大小生成二维码图片
	 */
	public static Bitmap encodeToQR(String contentsToEncode, int dimension,
			Bitmap logoBm) throws Exception {
		try {
			if (TextUtils.isEmpty(contentsToEncode))
				return null;
			// 配置参数
			Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 容错级别
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			// 设置空白边距的宽度
			// hints.put(EncodeHintType.MARGIN, 2); //default is 4

			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(contentsToEncode,
					BarcodeFormat.QR_CODE, dimension, dimension, hints);
			int width = bitMatrix.getWidth();
			int height = bitMatrix.getHeight();
			int[] pixels = new int[width * height];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * width + x] = 0xff000000;
					} else {
						pixels[y * width + x] = 0xffffffff;
					}
				}
			}

			// 生成二维码图片的格式，使用ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

			if (logoBm != null) {
				// bitmap = addLogo(bitmap, logoBm);
			}

			// 必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
			return bitmap;
		} catch (WriterException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @return 获取屏幕宽度
	 */
	public int getW() {
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		int i = outMetrics.widthPixels;
		return i;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.refresh:
			if (prefUtils.getString(SharePrefUtils.LL_ID, "") == null
					|| prefUtils.getString(SharePrefUtils.LL_ID, "").equals("")
					|| prefUtils.getString(SharePrefUtils.KEY_LIST, "") == null
					|| prefUtils.getString(SharePrefUtils.KEY_LIST, "").equals(
							"") || d2.getTime() - d1.getTime() < 0) {
				getdata();
			} else {
				try {
					List<String> keyList = new ArrayList<String>();
					List<Integer> storeAuthList = new ArrayList<Integer>();
					storeAuthList.add(1);
					storeAuthList.add(7);
					storeAuthList.add(8);
					keyList = Utils.String2SceneList(prefUtils.getString(
							SharePrefUtils.KEY_LIST, ""));
					String a = null;
					a = QRUtils.createElevatorControlQR(
							prefUtils.getString(SharePrefUtils.LL_ID, ""),
							keyList, storeAuthList, 5, 1, 1, "84324343", -3);
					resetBitmap(a);
				} catch (ParamFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (StreamCorruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			break;

		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (myNetReceiver != null&&isReceiverTag) {
			unregisterReceiver(myNetReceiver);
			isReceiverTag = false;
		}
	}

	// 监听网络状态变化的广播接收器
	private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				netInfo = mConnectivityManager.getActiveNetworkInfo();
				getdata();
			} else {
				// 网络断开
				try {
					List<String> keyList = new ArrayList<String>();
					List<Integer> storeAuthList = new ArrayList<Integer>();
					storeAuthList.add(1);
					storeAuthList.add(7);
					storeAuthList.add(8);
					keyList = Utils.String2SceneList(prefUtils.getString(
							SharePrefUtils.KEY_LIST, ""));
					String a = null;
					a = QRUtils.createElevatorControlQR(
							prefUtils.getString(SharePrefUtils.LL_ID, ""),
							keyList, storeAuthList, 5, 1, 1, "84324343", -3);
					resetBitmap(a);
				} catch (ParamFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (StreamCorruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//

			}
		}

	};

}
