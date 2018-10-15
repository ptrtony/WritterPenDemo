package com.android.bluetown.home.main.model.act;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import com.android.bluetown.R;
import com.android.bluetown.base.BaseWebActivity;
import com.android.bluetown.base.WebTranslateInterface;
import com.android.bluetown.utils.Constant;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class SmartRestaurantActivity extends BaseWebActivity implements OnRefreshListener {
    private static final String SMART_RESTAURANT_URL =Constant.WEB_BASE_URL+Constant.Interface.MERCHANT_LISTS;
    private SmartRefreshLayout mRefreshView;
    @Override
    protected int contentViewResId() {
        return R.layout.include_base_jsbridge;
    }

    @Override
    protected void initView() {
        mRefreshView = findViewById(R.id.pull_to_refresh);
        mWVJBWebView = findViewById(R.id.smart_restaurant_webview);
        mRefreshView.setOnRefreshListener(this);
        mRefreshView.setRefreshHeader(new ClassicsHeader(this));
        mRefreshView.setEnableLoadMore(false);
        mRefreshView.setBackgroundColor(Color.WHITE);
        initWebView(mWVJBWebView);
        setCallbackVisible(false);
        setTitle("周边");
        webInterface = new WebTranslateInterface() {
            @Override
            public void mainToOtherTranslate(String url) {
                Intent intent = new Intent(SmartRestaurantActivity.this,SmartRestaurantDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(LOAD_URL,url);
                intent.putExtras(bundle);
                startActivity(intent,bundle);
            }
        };
    }

    @Override
    public boolean isMainActivity() {
        return true;
    }

    @Override
    protected String setLoadUrl() {
        setCallbackVisible(false);
        return SMART_RESTAURANT_URL;
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
//        clearListData();
        destroyWebView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void loadurlFinish() {
        mRefreshView.finishRefresh();
    }




    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        clearCustomData();
        refreshLoadUrl(SMART_RESTAURANT_URL);
    }
}
