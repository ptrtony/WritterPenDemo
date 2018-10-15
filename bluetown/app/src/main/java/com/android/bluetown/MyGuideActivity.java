package com.android.bluetown;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;

import com.android.bluetown.base.BaseWebActivity;
import com.android.bluetown.base.WebTranslateInterface;
import com.android.bluetown.home.main.model.act.SmartRestaurantDetailsActivity;
import com.android.bluetown.utils.Constant;

/**
 * Created by Dafen on 2018/8/28.
 */

public class MyGuideActivity extends BaseWebActivity{

    @Override
    protected int contentViewResId() {
        return R.layout.include_jsbridge_view;
    }

    @Override
    protected void initView() {
        mWVJBWebView = findViewById(R.id.smart_restaurant_webview);
        initWebView(mWVJBWebView);
        setCallbackVisible(true);
        webInterface = new WebTranslateInterface() {
            @Override
            public void mainToOtherTranslate(String url) {
                Intent intent = new Intent(MyGuideActivity.this,SmartRestaurantDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(LOAD_URL,url);
                intent.putExtras(bundle);
                startActivity(intent,bundle);
            }
        };
    }

    @Override
    public boolean isMainActivity() {
        return false;
    }

    @Override
    protected String setLoadUrl() {
        return getIntent().getStringExtra(Constant.LOAD_URL);
    }

    @Override
    public void loadurlFinish() {

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
}
