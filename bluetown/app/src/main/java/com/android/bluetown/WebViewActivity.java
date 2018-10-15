package com.android.bluetown;
import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import com.ab.fragment.AbLoadDialogFragment;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.custom.dialog.MyAbLoadDialogFragment;
import com.android.bluetown.popup.LoadingAlertDialog;

/**
 * Created by shaonuk on 14-8-5.
 */
@SuppressLint("ResourceAsColor")
@SuppressWarnings("unused")
public class WebViewActivity extends TitleBarActivity {
	private WebView webView;
	private FrameLayout customViewContainer;
	private WebChromeClient.CustomViewCallback customViewCallback;
	private View mCustomView;
	private myWebChromeClient mWebChromeClient;
	private myWebViewClient mWebViewClient;
	private String url;
	private String title;
	private int fontSize;
	private LoadingAlertDialog loadingDialog;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentView(R.layout.webview_layout);
		BlueTownExitHelper.addActivity(this);
		Intent intent = getIntent();
		url = intent.getStringExtra("URL");
		title = intent.getStringExtra("title");
		setTitleView(title);
		customViewContainer = (FrameLayout) findViewById(R.id.customViewContainer);
		webView =findViewById(R.id.webView);
		mWebViewClient = new myWebViewClient();
		mWebChromeClient = new myWebChromeClient();
		webView.setWebChromeClient(mWebChromeClient);
		WebSettings settings = webView.getSettings();
		settings.setAppCacheEnabled(true);
		settings.setSaveFormData(true);
		settings.setUseWideViewPort(true);//设置webview推荐使用的窗口
		settings.setLoadWithOverviewMode(true);//设置webview加载的页面的模式
		settings.setDisplayZoomControls(false);//隐藏webview缩放按钮
		// 支持javascript
		settings.setJavaScriptEnabled(true); // 设置支持javascript脚本
		settings.setAllowFileAccess(true); // 允许访问文件
		settings.setBuiltInZoomControls(true); // 设置显示缩放按钮
		settings.setSupportZoom(true); // 支持缩放
		settings.setDatabaseEnabled(true);
		settings.setGeolocationEnabled(true);//允许地理位置可用
		settings.setDomStorageEnabled(true);

		loadingDialog = new LoadingAlertDialog(this);
//		//启用数据库
//		settings.setDatabaseEnabled(true);
//
//		//设置定位的数据库路径
//		String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
//		settings.setGeolocationDatabasePath(dir);
//
//		//启用地理定位
//		settings.setGeolocationEnabled(true);
		// 为图片添加放大缩小功能
		webView.getSettings().setUseWideViewPort(true);
		webView.setInitialScale(70);   //100代表不缩放
		webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
		// 设置可以支持缩放
		//settings.setSupportZoom(true);
		// 设置出现缩放工具
		//settings.setBuiltInZoomControls(true);
		// 扩大比例的缩放
		//settings.setUseWideViewPort(true);
		// 自适应屏幕
		//settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		/**
		 * 用WebView显示图片，可使用这个参数 设置网页布局类型：
		 * 1、LayoutAlgorithm.NARROW_COLUMNS ：适应内容大小
		 * 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
		 */
		settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		webView.loadUrl(url);
		webView.setWebViewClient(mWebViewClient);
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	public boolean inCustomView() {
		return (mCustomView != null);
	}

	public void hideCustomView() {
		mWebChromeClient.onHideCustomView();
	}

	@Override
	protected void onPause() {
		super.onPause(); // To change body of overridden methods use File |
							// Settings | File Templates.
		webView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume(); // To change body of overridden methods use File |
							// Settings | File Templates.
		webView.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop(); // To change body of overridden methods use File |
						// Settings | File Templates.
		if (inCustomView()) {
			hideCustomView();
		}
	}

	// protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	// invalidate();
	// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	// }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (inCustomView()) {
				hideCustomView();
				return true;
			}

			if ((mCustomView == null) && webView.canGoBack()) {
				webView.goBack();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	class myWebChromeClient extends WebChromeClient {
		private Bitmap mDefaultVideoPoster;
		private View mVideoProgressView;


//		@Override
//		public void onShowCustomView(View view, int requestedOrientation,
//				CustomViewCallback callback) {
//			onShowCustomView(view, callback); // To change body of overridden
//												// methods use File | Settings |
//												// File Templates.
//		}

		@Override
		public void onShowCustomView(View view, int i, CustomViewCallback customViewCallback) {
//			super.onShowCustomView(view, i, customViewCallback);
			onShowCustomView(view, customViewCallback);
		}


		public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
			callback.invoke(origin, true, false);
			onGeolocationPermissionsShowPrompt(origin, callback);
		}


//		@Override
//		public void onShowCustomView(View view, CustomViewCallback callback) {
//
//			// if a view already exists then immediately terminate the new one
//			if (mCustomView != null) {
//				callback.onCustomViewHidden();
//				return;
//			}
//			mCustomView = view;
//			webView.setVisibility(View.GONE);
//			customViewContainer.setVisibility(View.VISIBLE);
//			customViewContainer.addView(view);
//			customViewCallback = callback;
//		}

		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			if (mCustomView != null) {
				callback.onCustomViewHidden();
				return;
			}
			mCustomView = view;
			webView.setVisibility(View.GONE);
			customViewContainer.setVisibility(View.VISIBLE);
			customViewContainer.addView(view);
			customViewCallback = callback;
		}

		@Override
		public View getVideoLoadingProgressView() {

			if (mVideoProgressView == null) {
				LayoutInflater inflater = LayoutInflater
						.from(WebViewActivity.this);
				mVideoProgressView = inflater.inflate(R.layout.loding_progress,
						null);
			}
			return mVideoProgressView;
		}

		@Override
		public void onHideCustomView() {
			super.onHideCustomView(); // To change body of overridden methods
										// use File | Settings | File Templates.
			if (mCustomView == null)
				return;

			webView.setVisibility(View.VISIBLE);
			customViewContainer.setVisibility(View.GONE);

			// Hide the custom view.
			mCustomView.setVisibility(View.GONE);

			// Remove the custom view from its container.
			customViewContainer.removeView(mCustomView);
			customViewCallback.onCustomViewHidden();

			mCustomView = null;
		}

//		@Override
//		public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
//			callback.invoke(origin,true,false);
//			super.onGeolocationPermissionsShowPrompt(origin, callback);
//		}
	}

	class myWebViewClient extends WebViewClient {

//		@Override
//		public boolean shouldOverrideUrlLoading(WebView view, String url) {
//			view.loadUrl(url);
//			return true;
//		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView webView, String s) {
			webView.loadUrl(s);
			return true;
		}

//		@Override
//		public void onPageStarted(WebView view, String url, Bitmap favicon) {
//			// TODO Auto-generated method stub
//			showLoadDialog(WebViewActivity.this, R.drawable.progress_circular,
//					"数据加载中");
//			super.onPageStarted(view, url, favicon);
//		}

		@Override
		public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
			loadingDialog.show();
			super.onPageStarted(webView, s, bitmap);
		}

//		@Override
//		public void onPageFinished(WebView view, String url) {
//			// TODO Auto-generated method stub
//			removeDialog();
//			view.loadUrl("javascript: hide()");
//
//		}


		@Override
		public void onPageFinished(WebView webView, String s) {
			loadingDialog.dismiss();
			webView.loadUrl("javascript:hide()");
//			super.onPageFinished(webView, s);
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
			newFragment.dismiss();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (newFragment!=null&&!isDestroyed()){
			newFragment.dismiss();
		}
	}

//	private void crashErrorLog(){
//		UserStrategy strategy = new UserStrategy(getApplicationContext());
//
//		strategy.setCrashHandleCallback(new CrashReport.CrashHandleCallback() {
//
//			public Map<String, String> onCrashHandleStart(int crashType, String errorType, String errorMessage, String errorStack) {
//
//				LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
//
//				String x5CrashInfo = com.tencent.smtt.sdk.WebView.getCrashExtraMessage(getApplicationContext());
//
//				map.put("x5crashInfo", x5CrashInfo);
//
//				return map;
//
//				}
//
//			@Override
//
//			public byte[] onCrashHandleStart2GetExtraDatas(int crashType, String errorType, String errorMessage, String errorStack) {
//
//				try {
//
//					return "Extra data.".getBytes("UTF-8");
//
//					} catch (Exception e) {
//
//					return null;
//
//					}
//
//				}
//
//			});
//
//		CrashReport.initCrashReport(getApplicationContext(), APPID, true, strategy);
//	}
}