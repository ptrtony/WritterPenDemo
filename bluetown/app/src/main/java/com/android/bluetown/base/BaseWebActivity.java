package com.android.bluetown.base;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.bluetown.R;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.UserBean;
import com.android.bluetown.mywallet.activity.RechargeActivity;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.NetworkUtils;
import com.android.bluetown.utils.StatusBarUtils;
import com.android.bluetown.popup.PasswordPop;
import com.google.gson.Gson;
import net.tsz.afinal.FinalDb;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import wendu.webviewjavascriptbridge.WVJBWebView;

/**
 * Created by Dafen on 2018/7/26.
 */

public abstract class BaseWebActivity extends AppCompatActivity implements View.OnClickListener, PasswordPop.InputPasswordSucceed {
    private static final String TAG="BaseWebActivity";
    public static final String LOAD_URL="details_load_url";
    public WVJBWebView mWVJBWebView;
    public RelativeLayout mllCallback;
    private FinalDb db;
    private SharePrefUtils sharePrefUtils;
    private UserBean userBean;
    private String userId;
    public ProgressBar mLoadingProgressBar;
    private List<String> loadHistoryUrls = new ArrayList<>();
    private WebSettings webSettings;
    TextView mTitle;
    public WebTranslateInterface webInterface;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(contentViewResId());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtils.getInstance().initStatusBar(true, this);
        }
        db = FinalDb.create(this);
        sharePrefUtils = new SharePrefUtils(this);
        userBean  = new UserBean();
        List<MemberUser> memberUsers = db.findAll(MemberUser.class);
        if (memberUsers!=null&&memberUsers.size()>0){
            userId = memberUsers.get(0).memberId;
        }
        userBean.userId = userId;
        userBean.communicationToken = sharePrefUtils.getString(SharePrefUtils.TOKEN,"");
        initView();
    }

    protected abstract int contentViewResId();

    public void initWebView(WVJBWebView mWVJBWebView){
        this.mWVJBWebView = mWVJBWebView;
        mllCallback = findViewById(R.id.ll_web_back);
        mTitle = findViewById(R.id.tv_web_title);
        mLoadingProgressBar = findViewById(R.id.actAgreementPb);
        mllCallback.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWVJBWebView.setWebContentsDebuggingEnabled(true);
        }
        mWVJBWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webSettings = mWVJBWebView.getSettings();
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setBlockNetworkImage(false);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWVJBWebView.setWebViewClient(mWebViewClient);
        mWVJBWebView.setWebChromeClient(mWebChromeClient);
        mWVJBWebView.loadUrl(setLoadUrl());
        PasswordPop pop = new PasswordPop(BaseWebActivity.this,
                mWVJBWebView, userId, "0", "0", null,
                null, null, 0);
        pop.setInputPassword(this);

        /**
         * 跳转到新页面
         */
        mWVJBWebView.registerHandler("newWebView", new WVJBWebView.WVJBHandler() {
            @Override
            public void handler(Object o, WVJBWebView.WVJBResponseCallback wvjbResponseCallback) {
                JSONObject object = null;
                try {
                    object = new JSONObject(o.toString());
                    StringBuilder sb =new StringBuilder();
                    sb.append(Constant.WEB_BASE_URL+object.getString("data"));
                    String url = sb.toString();
                    if (isMainActivity()){
                        if (webInterface!=null) {
                            webInterface.mainToOtherTranslate(url);
                        }
                    }else{
                        clearWebView();
                        mWVJBWebView.loadUrl(url);
                    }
                    Log.d(TAG,"webViewLoadUrl===="+url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        /**
         * 设置用户信息
         */
        mWVJBWebView.registerHandler("getUserInfo",new WVJBWebView.WVJBHandler() {
            @Override
            public void handler(Object o, WVJBWebView.WVJBResponseCallback wvjbResponseCallback) {
                wvjbResponseCallback.onResult(new Gson().toJson(userBean));
            }
        });


        /**
         * 跳转到上一个页面
         */
        mWVJBWebView.registerHandler("popView", new WVJBWebView.WVJBHandler() {
            @Override
            public void handler(Object o, WVJBWebView.WVJBResponseCallback wvjbResponseCallback) {
                actionKey(KeyEvent.KEYCODE_BACK);
            }
        });


        /**
         * 跳转到充值页面
         */
        mWVJBWebView.registerHandler("recharge", new WVJBWebView.WVJBHandler() {
            @Override
            public void handler(Object o, WVJBWebView.WVJBResponseCallback wvjbResponseCallback) {
                if (!isDestroyed()){
                    pop.dismiss();
                }
                toRechargeActivity();
            }
        });


        /**
         * 显示支付密码输入窗口
         */
        mWVJBWebView.registerHandler("payPassword", new WVJBWebView.WVJBHandler() {
            @Override
            public void handler(Object o, WVJBWebView.WVJBResponseCallback wvjbResponseCallback) {
                pop.showPopupWindow(mWVJBWebView);
            }
        });
    }


    /**
     * 跳转到充值页面
     */
    public void toRechargeActivity(){
        Intent intent = new Intent(BaseWebActivity.this, RechargeActivity.class);
        intent.putExtra("type",3);
        startActivity(intent);
    }


    protected abstract void initView();

    public abstract boolean isMainActivity();
    /**
     * 设置loadurl
     * @return
     */
    protected abstract String setLoadUrl();

    private WebViewClient mWebViewClient = new WebViewClient(){
        @SuppressLint("NewApi")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG,"shouldOverrideUrlLoading"+url);
            if (NetworkUtils.isConnectInternet(getApplicationContext())
                    ||NetworkUtils.isConnectWifi(getApplicationContext())) {
                webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
            } else {
                webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
            }
            if (Build.VERSION.SDK_INT <Build.VERSION_CODES.N){
                mWVJBWebView.loadUrl(url);
                return true;
            }
            return false;
        }

        @SuppressLint("NewApi")
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.d(TAG,"shouldOverrideUrl"+request.getUrl().toString());
            if (request.isRedirect()){
                mWVJBWebView.loadUrl(request.getUrl().toString());
                return true;
            }
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            loadurlFinish();
            mWVJBWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
            Log.d(TAG,"SslError:"+error.toString());
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient(){
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (isMainActivity()){
                setTitle("周边");
            }else{
                if (title!=null&&!TextUtils.isEmpty(title)) setTitle(title);
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress>=100){
                mLoadingProgressBar.setVisibility(View.GONE);
            }else{
                if (mLoadingProgressBar.getVisibility() == View.GONE) {
                    mLoadingProgressBar.setVisibility(View.GONE);
                }else
                {
                    mLoadingProgressBar.setVisibility(View.VISIBLE);
                }
                mLoadingProgressBar.setProgress(newProgress);
            }
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            mUploadCallbackAboveL=filePathCallback;
            take();
            return true;
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage=uploadMsg;
            take();
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg,String acceptType) {
            mUploadMessage=uploadMsg;
            take();
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg,String acceptType, String capture) {
            mUploadMessage=uploadMsg;
            take();
        }

    };


    @Override
    public void onClick(View v) {
        actionKey(KeyEvent.KEYCODE_BACK);
    }


    //自定义设置的返回键
    public void actionKey(final int keyCode) {
        new Thread () {
            public void run () {
                try {
                    Instrumentation inst=new Instrumentation();
                    inst.sendKeyDownUpSync(keyCode);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void setCallbackVisible(boolean isVisible){
        if (isVisible){
            mllCallback.setVisibility(View.VISIBLE);
        }else{
            mllCallback.setVisibility(View.GONE);
        }
    }

    public void setTitle(String title){
        mTitle.setText(title);
    }


    public void destroyWebView(){
        if (mWVJBWebView!=null){
            mWVJBWebView.destroy();
            mWVJBWebView = null;
        }
    }


    //清空webview数据和缓存
    public void clearWebView(){
        mWVJBWebView.clearCache(true);
        mWVJBWebView.clearHistory();
        mWVJBWebView.clearFormData();
    }

    public void refreshLoadUrl(String url){
        clearWebView();
        mWVJBWebView.loadUrl(url);
    }

    public abstract void loadurlFinish();


    //清空加载历史List
    public void clearCustomData(){
        loadHistoryUrls.clear();
    }

    @Override
    public void inputPasswordSucceed() {
        /**
         * 支付密码输入成功
         */
        mWVJBWebView.callHandler("payPasswordTrust");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FILECHOOSER_RESULTCODE)
        {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            }
            else  if (mUploadMessage != null) {

                if (result != null) {
                    String path = getPath(getApplicationContext(),
                            result);
                    Uri uri = Uri.fromFile(new File(path));
                    mUploadMessage
                            .onReceiveValue(uri);
                } else {
                    mUploadMessage.onReceiveValue(imageUri);
                }
                mUploadMessage = null;

            }
        }
    }

    @SuppressWarnings("null")
    @TargetApi(Build.VERSION_CODES.BASE)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE
                || mUploadCallbackAboveL == null) {
            return;
        }

        Uri[] results = null;

        if (resultCode == Activity.RESULT_OK) {

            if (data == null) {

                results = new Uri[]{imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();

                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }

                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        if(results!=null){
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        }else{
            results = new Uri[]{imageUri};
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        }

        return;
    }



    private void take(){
        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp");
        // Create the storage directory if it does not exist
        if (! imageStorageDir.exists()){
            imageStorageDir.mkdirs();
        }
        File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        imageUri = Uri.fromFile(file);

        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent i = new Intent(captureIntent);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            i.setPackage(packageName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntents.add(i);

        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        Intent chooserIntent = Intent.createChooser(i,"Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        startActivityForResult(chooserIntent,  FILECHOOSER_RESULTCODE);
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
