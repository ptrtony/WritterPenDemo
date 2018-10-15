package com.android.bluetown.home.main.model.act;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.UserBean;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.popup.LoadingAlertDialog;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;
import com.google.gson.Gson;
import net.tsz.afinal.FinalDb;
import org.json.JSONException;
import java.util.List;

import wendu.webviewjavascriptbridge.WVJBWebView;

import static com.android.bluetown.utils.Constant.HOME_REFRESH;

public class GuestAppointmentCarSeatActivity extends TitleBarActivity implements View.OnClickListener {
    private FinalDb db;
    private List<MemberUser> memberUsers;
    private String userId;
    private WVJBWebView mWebView;
    private LoadingAlertDialog mLoadDialog;
    private UserBean mUserBean;
    private String titleName = "";
    private String parkingId = "";
    private String parkingNo = "";
    private SharePrefUtils sharePrefUtils;
    @Override
    public void initTitle() {
        addContentView(R.layout.activity_guest_appointment_car_seat);
        mWebView = findViewById(R.id.guest_appointment_web);
        mLoadDialog = new LoadingAlertDialog(this);
        mUserBean = new UserBean();
        db = FinalDb.create(this);
        memberUsers = db.findAll(MemberUser.class);
        if (memberUsers != null && memberUsers.size() > 0) {
            userId = memberUsers.get(0).memberId;
        }
        sharePrefUtils = new SharePrefUtils(this);
        mUserBean.userId = userId;
        mUserBean.communicationToken = sharePrefUtils.getString(SharePrefUtils.TOKEN, null);
        Bundle bundle = getIntent().getExtras();
        titleName = bundle.getString("paringName");
        parkingId = bundle.getString("parkingId");
        parkingNo = bundle.getString("parkingNo");
        initWebView();
        setBackImageView();
        setRightImageView(R.drawable.ic_history);
        setMainLayoutBg(R.color.app_blue);
        setTitleView(titleName);
        rightImageLayout.setOnClickListener(this);
    }


    private void initWebView() {
        StringBuilder sb = new StringBuilder();
        sb.append("https://bluetown-h5.era3.net/#/temporaryPark/reservationold");
        sb.append("/id/" + parkingId);
        sb.append("/no/"+parkingNo);
        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        mWebView.setWebViewClient(mWebViewClient);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSupportMultipleWindows(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        mWebView.loadUrl(sb.toString());
        mWebView.registerHandler("toast", new WVJBWebView.WVJBHandler() {
            @Override
            public void handler(Object o, WVJBWebView.WVJBResponseCallback wvjbResponseCallback) {
                org.json.JSONObject obj = null;
                try {
                    obj = new org.json.JSONObject(o.toString());
                    String data1 = obj.getString("data");
                    Toast.makeText(GuestAppointmentCarSeatActivity.this,data1, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        mWebView.registerHandler("getUserInfo", new WVJBWebView.WVJBHandler() {
            @Override
            public void handler(Object o, WVJBWebView.WVJBResponseCallback wvjbResponseCallback) {
                wvjbResponseCallback.onResult(new Gson().toJson(mUserBean));
            }
        });

        mWebView.registerHandler("successBack", new WVJBWebView.WVJBHandler() {
            @Override
            public void handler(Object o, WVJBWebView.WVJBResponseCallback wvjbResponseCallback) {
//                Intent intent = new Intent();
//                intent.setAction("android.parking.reservation");
//                sendBroadcast(intent);
                HOME_REFRESH = 1;
                GuestAppointmentCarSeatActivity.this.finish();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rightImageLayout:
                if (!TextUtils.isEmpty(userId)) {
                    startActivity(new Intent(GuestAppointmentCarSeatActivity.this,
                            GuestAppointmentHistoryActivity.class));
                } else {
                    TipDialog.showDialogNoClose(GuestAppointmentCarSeatActivity.this,
                            R.string.tip, R.string.confirm,
                            R.string.login_info_tip, LoginActivity.class);
                }
                break;
        }
    }

    private WebViewClient mWebViewClient = new WebViewClient(){
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mLoadDialog.show();

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            mLoadDialog.dismiss();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
        mWebView = null;
    }
}
