package com.android.bluetown;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.home.hot.model.act.SelectAreaActivity;
import com.android.bluetown.my.AuthenticationActivity;
import com.android.bluetown.utils.DisplayUtils;
import com.android.bluetown.utils.StatusBarUtils;
import com.android.bluetown.wrapper.LoginWrapper;
import com.android.bluetown.pref.SharePrefUtils;

import static com.android.bluetown.pref.SharePrefUtils.SELECT_USER_TYPE;

/**
 * Created by Dafen on 2018/8/24.
 * 选择身份
 */

public class SelectIdentityActivity extends Activity implements View.OnClickListener {
    private String userName;
    private String password;
    private boolean isBlueIdentity;
    private ImageView mBackIv;
    private SharePrefUtils sharePrefUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_identity);
        BlueTownExitHelper.addActivity(this);
        sharePrefUtils = new SharePrefUtils(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtils.getInstance().initStatusBar(true, this);
        }
        initView();
    }

    private void initView() {
        TextView mIdentityTitle = findViewById(R.id.tv_identity_title);
        mBackIv = findViewById(R.id.iv_back);
        mBackIv.setOnClickListener(this);
        Button mBtnCompany = findViewById(R.id.btn_company_user);
        Button mBtnCustom = findViewById(R.id.btn_custom_user);
        Bundle bundle = getIntent().getExtras();
        userName = bundle.getString("username");
        password = bundle.getString("password");
        isBlueIdentity = bundle.getBoolean("isBlueIdentity");
        if (isBlueIdentity){
            mIdentityTitle.setTextSize(DisplayUtils.px2sp(this,48));
            mIdentityTitle.setText(getString(R.string.blue_identity_user));
            mBtnCompany.setText(getString(R.string.line_into));
            mBtnCustom.setText(getString(R.string.submit_company_massage));
            mBtnCompany.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginWrapper loginWrapper = new LoginWrapper(SelectIdentityActivity.this);
                    loginWrapper.login(userName, password);
                }
            });
            mBtnCustom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectCompany();
                }
            });
        }else{
            mBtnCompany.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectCompany();
                }
            });
            mBtnCustom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectCustom();
                }
            });

        }

    }

    public void selectCompany() {
        sharePrefUtils.setBoolean(SELECT_USER_TYPE,true);
        Intent intent = new Intent(SelectIdentityActivity.this, AuthenticationActivity.class);
        Bundle bundle = new Bundle();
//        bundle.putString("username",userName);
//        bundle.putString("password",password);
        intent.putExtras(bundle);
        startActivity(intent,bundle);
    }

    public void selectCustom() {
        sharePrefUtils.setBoolean(SELECT_USER_TYPE,false);
        Intent intent = new Intent(SelectIdentityActivity.this, AuthenticationActivity.class);
        Bundle bundle = new Bundle();
//        bundle.putString("username",userName);
//        bundle.putString("password",password);
        intent.putExtras(bundle);
        startActivity(intent,bundle);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
