/**
 * 
 */
package com.android.bluetown.surround;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbJsonUtil;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.UMSOrder;
import com.android.bluetown.result.UMSResult;
import com.android.bluetown.utils.Constant;

/**
 * 银联支付
 * 
 * @author lig
 * 
 */
public class UMSActivity extends TitleBarActivity {
	private WebView mWebView;
	private Handler mHandler;
	private UMSOrder order;
	private int tag;

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBg(R.color.title_bg_blue);
		setTitleView(R.string.ums_title);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_news);
		BlueTownExitHelper.addActivity(this);
		initUIView();
	}

	public void initUIView() {
		mWebView = (WebView) findViewById(R.id.webview);
		// 载入具体的web地址
		WebSettings webSettings = mWebView.getSettings();
		// 不保存密码
		webSettings.setSavePassword(false);
		// 不保存表单数据
		webSettings.setSaveFormData(false);
		webSettings.setDefaultTextEncodingName("UTF-8");
		// 不支持页面放大功能
		webSettings.setSupportZoom(true);
		webSettings.setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new MyAndroidWebViewClient());
		mWebView.setVisibility(View.VISIBLE);
		getData();

	}

	private void initValue(UMSOrder order) {
		String parString = "?merSign=" + order.Sign + "&chrCode="
				+ order.ChrCode + "&tranId=" + order.TransId + "&url="
				+ Constant.Interface.URL_UMS_CALLBACK + "&mchantUserCode="
				+ order.mchantUserCode;
		mWebView.loadUrl(Constant.Interface.URL_UMS + parString);
	}

	private void getData() {
		params.put("orderNum", "");
		params.put("orderAmount", "");
		params.put("custUuid", "");
		httpInstance.post(Constant.Interface.URL_UMS_ORDER, params,
				new AbStringHttpResponseListener() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFailure(int arg0, String arg1, Throwable arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(int arg0, String arg1) {
						// TODO Auto-generated method stub
						if (arg1 != null && arg1.length() > 0) {
							UMSResult result = (UMSResult) AbJsonUtil.fromJson(
									arg1, UMSResult.class);
							if (result.getRepCode().contains(
									Constant.HTTP_SUCCESS)) {
								order = result.getData();
								initValue(order);
							} else {
								Toast.makeText(UMSActivity.this, "银联下单失败",
										Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(UMSActivity.this, "银联下单失败",
									Toast.LENGTH_LONG).show();
							finish();
						}
					}
				});
	}

	/**
	 * 获取订单状态
	 */
	private void getOrderStatus() {
		params.put("orderNum", "");
		params.put("transId", "");
		httpInstance.post(Constant.Interface.URL_UMS_QUERY, params,
				new AbStringHttpResponseListener() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFailure(int arg0, String arg1, Throwable arg2) {
						// TODO Auto-generated method stub
						Toast.makeText(UMSActivity.this, "网络异常，请稍后在试",
								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onSuccess(int arg0, String arg1) {
						// TODO Auto-generated method stub
						if (arg1 != null && arg1.length() > 0) {
							UMSResult result = (UMSResult) AbJsonUtil.fromJson(
									arg1, UMSResult.class);
							if (result.getRepCode().contains(
									Constant.HTTP_SUCCESS)) {
								order = result.getData();
								orderStatusDispose(order);
								Toast.makeText(UMSActivity.this, "支付失败",
										Toast.LENGTH_LONG).show();
							} else {
								Toast.makeText(UMSActivity.this, "支付失败",
										Toast.LENGTH_LONG).show();
								finish();
							}
						}
					}
				});
	}

	private void orderStatusDispose(UMSOrder order) {
		if (Integer.parseInt(order.TransState) == UMSOrder.UMS_STATUS_SUCCESS) {
			Intent intent = new Intent();
			intent.setClass(UMSActivity.this, OnlinePaySuccessActivity.class);
			startActivity(intent);
		} else {
			finish();
		}
	}

	class MyAndroidWebViewClient extends WebViewClient {

		@Override
		public void onPageFinished(WebView view, String url) {
			if (url.contains(Constant.Interface.URL_UMS)) {
				titleLayout.setVisibility(View.GONE);
				return;
			}
			if (url.contains(Constant.Interface.URL_UMS_CALLBACK)) {
				if (url.contains(Constant.Interface.URL_UMS_SUCCESS)) {
					getOrderStatus();
				} else {
					finish();
				}
			}
		}
	}
}
