package com.android.bluetown.home.main.model.act;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;

import com.android.bluetown.R;
import com.android.bluetown.base.BaseWebActivity;

public class SmartRestaurantDetailsActivity extends BaseWebActivity {

    @Override
    protected int contentViewResId() {
        return R.layout.include_jsbridge_view;
    }

    @Override
    protected void initView() {
        mWVJBWebView = findViewById(R.id.smart_restaurant_webview);
        initWebView(mWVJBWebView);
        setCallbackVisible(true);
    }

    @Override
    public boolean isMainActivity() {
        return false;
    }

    @Override
    protected String setLoadUrl() {
        return getIntent().getExtras().getString(LOAD_URL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyWebView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (mWVJBWebView.canGoBack()){
                mWVJBWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                mWVJBWebView.goBack();
                return true;
            }else{
                mllCallback.setVisibility(View.INVISIBLE);
                finish();
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void loadurlFinish() {
    }
}
