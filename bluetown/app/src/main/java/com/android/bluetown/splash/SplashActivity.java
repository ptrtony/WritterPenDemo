package com.android.bluetown.splash;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.MainActivity;
import com.android.bluetown.R;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.receiver.RongCloudEvent;
import com.android.bluetown.rong.ChatroomKit;
import com.android.bluetown.utils.StatusBarUtils;
import com.hedi.update.UpdateManager;
import java.lang.ref.WeakReference;
import io.rong.imlib.RongIMClient;
/**
 * Created by Dafen on 2018/7/12.
 */

public class SplashActivity extends AppCompatActivity implements UpdateManager.ToTranslateActivityListener {
    private static final int TO_MAIN = 1;
    private static final int TO_LOGIN = 2;
    private SharePrefUtils sharePrefUtils;
    private UpdateManager updateManager;

    private MyHandler mHandler = new MyHandler(this);
    private static class MyHandler extends Handler{
        WeakReference<SplashActivity> weakReference;
        MyHandler(SplashActivity splashActivity){
            this.weakReference = new WeakReference<SplashActivity>(splashActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent();
            SplashActivity context = weakReference.get();
            switch (msg.what){
                case TO_MAIN:
                    intent.setClass(context, MainActivity.class);
                    break;
                case TO_LOGIN:
                    intent.setClass(context, LoginActivity.class);
                    break;
            }
            context.startActivity(intent);
            context.finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!this.isTaskRoot()) {
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        setContentView(R.layout.ac_welcome);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //设置半透明导航栏适配华为手机
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtils.getInstance().initStatusBar(true, this);
        }
        updateManager = new UpdateManager(this);
        updateManager.checkUpdate(this);
    }

    @Override
    protected void onDestroy() {
        if (updateManager!=null)
            updateManager.removeReceiver();
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onTranslateListener() {
        sharePrefUtils = new SharePrefUtils(this);
        String token = sharePrefUtils.getString(SharePrefUtils.TOKEN,"");
        boolean isLoging =sharePrefUtils.getBoolean("isLoging",false);
        if (token!=null&& !TextUtils.isEmpty(token.intern())&&isLoging){
            if (RongIMClient.getInstance().getCurrentConnectionStatus()!= RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED){
                String rongToken = sharePrefUtils.getString(SharePrefUtils.RONG_TOKEN,"");
                if (rongToken !=null &&!TextUtils.isEmpty(rongToken)){
                    ChatroomKit.connect(this,rongToken,new RongIMClient.ConnectCallback(){
                        @Override
                        public void onSuccess(String s) {
                            sharePrefUtils.setString(SharePrefUtils.RONG_TOKEN,rongToken);
                            RongCloudEvent.getInstance().setOtherListener();
                            mHandler.sendEmptyMessageDelayed(TO_MAIN,1500);
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            mHandler.sendEmptyMessageDelayed(TO_LOGIN,1500);
                        }
                        @Override
                        public void onTokenIncorrect() {
                            mHandler.sendEmptyMessageDelayed(TO_LOGIN,1500);
                        }
                    });
                }else{
                    mHandler.sendEmptyMessageDelayed(TO_LOGIN,1500);
                }
            }else{
                mHandler.sendEmptyMessageDelayed(TO_MAIN,1500);
            }
        }else{
            mHandler.sendEmptyMessageDelayed(TO_LOGIN,1500);
        }
    }
}
