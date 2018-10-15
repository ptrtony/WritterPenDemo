//package com.android.bluetown;
//
//import io.rong.imkit.RongIM;
//import io.rong.imlib.RongIMClient;
//
//import java.lang.ref.WeakReference;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Set;
//
//import org.json.JSONObject;
//
//import net.tsz.afinal.FinalDb;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.app.FragmentActivity;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.Window;
//import android.view.animation.AlphaAnimation;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//import cn.jpush.android.api.JPushInterface;
//import cn.jpush.android.api.TagAliasCallback;
//
//import com.ab.http.AbHttpUtil;
//import com.ab.http.AbRequestParams;
//import com.ab.util.AbJsonUtil;
//import com.android.bluetown.app.BlueTownApp;
//import com.android.bluetown.app.BlueTownExitHelper;
//import com.android.bluetown.bean.FriendsBean;
//import com.android.bluetown.bean.LocationInfo;
//import com.android.bluetown.bean.MemberUser;
//import com.android.bluetown.bean.TokenBean;
//import com.android.bluetown.custom.dialog.TipDialog;
//import com.android.bluetown.listener.AbsHttpResponseListener;
//import com.android.bluetown.pref.SharePrefUtils;
//import com.android.bluetown.result.RegisterResult;
//import com.android.bluetown.result.Result;
//import com.android.bluetown.utils.Constant;
//import com.android.bluetown.utils.DeviceUtil;
//import com.android.bluetown.utils.LocationUtil;
//import com.android.bluetown.utils.StatusBarUtils;
//import com.hedi.update.UpdateManager;
//import com.nostra13.universalimageloader.core.ImageLoader;
//
//import static com.android.bluetown.app.BlueTownApp.DEBUG;
//
///**
// * @ClassName: WelcomeActivity
// * @Description:TODO(欢迎页面)
// * @author: shenyz
// * @date: 2015年7月17日 下午12:01:03
// */
//
///**
// * @author shenyz
// */
//public class WelcomeActivity extends FragmentActivity{
//    private static final String TAG = "UpdateManager";
//    private static final int REQUEST_CODE_INSTALL_PERMISSION = 107;
//    /**
//     * 跟布局
//     */
////    private LinearLayout rootLayout;
//    private SharePrefUtils prefUtils;
//    private FinalDb db;
//    private static final int GO_HOME = 1000;
//    private static final int GO_GUIDE = 1001;
////    // 延迟3秒
////    private static final long SPLASH_DELAY_MILLIS = 10;
//    // Http请求
//    protected AbHttpUtil httpInstance;
//    // Http请求参数
//    protected AbRequestParams params;
//    // 是否为第一次进入该app
//    private String first = "0";
//
//    private String userId;
//    private double latitude = 0.0;
//    private double longitude = 0.0;
//    private UpdateManager manager;
//    private ConnectivityManager mConnectivityManager;
//    private NetworkInfo netInfo;
//
//    /**
//     * Handler:跳转到不同界面
//     */
//    private MyHandler mHandler = new MyHandler(WelcomeActivity.this);
//
//
//    private class MyHandler extends Handler {
//        WeakReference<WelcomeActivity> weakReference;
//
//        MyHandler(WelcomeActivity activity) {
//            weakReference = new WeakReference<WelcomeActivity>(activity);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case GO_HOME:
//                    goHome();
//                    break;
//                case GO_GUIDE:
//                    goGuide();
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//    }
//
//    private void registerNetWorkRecevicer() {
//        // TODO Auto-generated method stub
//        IntentFilter mFilter = new IntentFilter();
//        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(myNetReceiver, mFilter);
//
//    }
//
//
//    private void unRegisterNetWorkRecevicer() {
//        if (myNetReceiver != null) {
//            unregisterReceiver(myNetReceiver);
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onCreate(savedInstanceState);
//        //解决oppo手机按home键退出应用程序的bug
//        Log.d(TAG, "加载数据前" + getSystemCurrentTime());
//        if (!isTaskRoot()) {
//            finish();
//            return;
//        }
//        Log.d(TAG, "加载数据前1" + getSystemCurrentTime());
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
////        setContentView(R.layout.ac_welcome);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            StatusBarUtils.getInstance().initStatusBar(true, this);
//        }
//        Log.d(TAG, "加载数据前2" + getSystemCurrentTime());
//        BlueTownExitHelper.addActivity(WelcomeActivity.this);
//        prefUtils = new SharePrefUtils(this);
//        db = FinalDb.create(this);
//        initHttpRequest();
//        Log.d(TAG, "加载数据前3" + getSystemCurrentTime());
//        initUI();
////        installProcess();
//        LocationUtil.getInstance(getApplicationContext()).startLoc();
//        registerNetWorkRecevicer();
//    }
//
//    private void initHttpRequest() {
//        // TODO Auto-generated method stub
//        if (httpInstance == null) {
//            httpInstance = AbHttpUtil.getInstance(this);
//            httpInstance.setEasySSLEnabled(true);
//        }
//
//        if (params == null) {
//            params = new AbRequestParams();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        // TODO Auto-generated method stub
//        super.onResume();
//    }
//
//    /**
//     * @throws
//     * @Title: initUI
//     * @Description: TODO(初始化界面)
//     */
//    private void initUI() {
////        rootLayout = (LinearLayout) findViewById(R.id.rootLayout);
////        setWelcomeAnim();
//        // 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
//        Log.d(TAG, "加载数据前4" + getSystemCurrentTime());
//        first = prefUtils.getString(SharePrefUtils.FIRST_USER, "0");
//        // 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
//        // 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
//        // 自动登录
//        if (!TextUtils.isEmpty(first) && "1".equals(first)) {
////            new Handler().post(new Runnable() {
////
////                public void run() {
//
//            List<MemberUser> users = db.findAll(MemberUser.class);
//            if (users != null && users.size() != 0) {
//                MemberUser user = users.get(0);
//                if (user != null) {
//                    userId = user.getMemberId();
//                }
//            }
//            // TODO: 2018/5/8 注释内容 更新改为跳转到主页面
////            if (!DEBUG) {
////                Log.d(TAG, "加载数据前5" + getSystemCurrentTime());
////                manager = new UpdateManager(WelcomeActivity.this);
////                manager.checkUpdate();
////            } else {
////                if (!TextUtils.isEmpty(userId)) {
////                    String user_phone = users.get(0).getUsername();
////                    String user_pswd = users.get(0).getPassword();
////                    login(user_phone, user_pswd);
//////                        UpdateManager manager = new
//////                                UpdateManager(WelcomeActivity.this);
//////                        manager.checkUpdate();
////                }else {
////                    Intent intent = new Intent(WelcomeActivity.this,
////                            LoginActivity.class);
////                    WelcomeActivity.this.startActivity(intent);
////                    WelcomeActivity.this.finish();
////                }
////            }
//
////                }
////            });
//        }else{
//            mHandler.sendEmptyMessage(GO_GUIDE);
//        }
//    }
//
//    /**
//     * @throws
//     * @Title: setWelcomeAnim
//     * @Description: TODO(设置欢迎页的动画)
//     */
//    private void goHome() {
//        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
//        WelcomeActivity.this.startActivity(intent);
//        WelcomeActivity.this.finish();
//
//    }
//
//    private void goGuide() {
//        prefUtils.setString(SharePrefUtils.FIRST_USER, "1");
//        Intent intent = new Intent(WelcomeActivity.this,
//                LoginActivity.class);
//        WelcomeActivity.this.startActivity(intent);
//        WelcomeActivity.this.finish();
//    }
//
//    private void login(final String username, final String password) {
//        params.put("telphone", username);
//        params.put("password", password);
//        params.put("pttId", DeviceUtil.toMD5UniqueId(WelcomeActivity.this));
//        httpInstance.post(Constant.HOST_URL + Constant.Interface.LOGIN, params,
//                new AbsHttpResponseListener() {
//                    @Override
//                    public void onSuccess(int arg0, String arg1) {
//                        // TODO Auto-generated method stub
//                        try {
//                            RegisterResult result = (RegisterResult) AbJsonUtil
//                                    .fromJson(arg1, RegisterResult.class);
//                            if (result.getRepCode().contains(
//                                    Constant.HTTP_SUCCESS)) {
//                                JSONObject json = new JSONObject(arg1);
//                                JSONObject data = new JSONObject(json
//                                        .optString("data"));
//                                JSONObject memberuser = new JSONObject(data
//                                        .optString("memberUser"));
//                                JSONObject USER = new JSONObject(memberuser
//                                        .optString("user"));
//                                MemberUser memberUser, user = null;
//                                List<FriendsBean> list_msg;
//                                List<MemberUser> users = null;
//                                LocationInfo info = null;
//                                List<LocationInfo> infos = null;
//                                TokenBean token = new TokenBean();
//                                final String rongtoken;
//                                // statu=0，1，2
//                                // （为企业用户，是0的时候，企业正在审核，1的时候审核拒绝，2的时候审核通过，3的时候为普通用户登陆）
//                                int userStatus = Integer.parseInt(result
//                                        .getData().getMemberUser().getStatu());
//                                switch (userStatus) {
//                                    case 0:
//                                        // 游客身份进入主页（正在审核）
//                                        TipDialog.showDialogClearTop(
//                                                WelcomeActivity.this, R.string.tip,
//                                                R.string.confirm,
//                                                R.string.commit_tip,
//                                                MainActivity.class);
//                                        break;
//                                    case 1:
//                                        // 游客身份进入主页（拒绝审核）
//                                        TipDialog.showDialogClearTop(
//                                                WelcomeActivity.this, R.string.tip,
//                                                R.string.confirm,
//                                                R.string.commit_refuse_tip,
//                                                MainActivity.class);
//                                        break;
//                                    case 2:
//                                        // 企业用户登录成功
//                                        memberUser = result.getData()
//                                                .getMemberUser();
//                                        userId = memberUser.getMemberId();
//                                        rongtoken = result.getData().getRong()
//                                                .getToken();
//                                        token = memberUser.getTokenBean();
//                                        prefUtils.setString(SharePrefUtils.TOKEN,
//                                                token.getToken());
//                                        user = new MemberUser();
//                                        users = db.findAll(MemberUser.class);
//                                        if (users == null || users.size() == 0) {
//                                            // 默认记住密码 保存用户名和密码
//                                            user.setMemberId(userId);
//                                            user.setAddress(memberUser.getAddress());
//                                            user.setStatu(memberUser.getStatu());
//                                            user.setNickName(memberUser
//                                                    .getNickName());
//                                            user.setHeadImg(memberUser.getHeadImg());
//                                            user.setName(memberUser.getName());
//                                            user.setSex(memberUser.getSex());
//                                            user.setCompanyName(memberUser
//                                                    .getCompanyName());
//                                            user.setUserType(Constant.COMPANY_USER);
//                                            user.setUsername(username);
//                                            user.setPassword(password);
//                                            user.setRongToken(rongtoken);
//                                            user.setCToken(token.getToken());
//                                            user.setGardenId(memberUser
//                                                    .getGardenId());
//                                            user.setHotRegion(memberUser
//                                                    .getHotRegion());
//
//                                            // 设置标签别名请注意处理call back结果。只有call back
//                                            // 返回值为 0
//                                            // 才设置成功，才可以向目标推送。否则服务器 API
//                                            // 会返回1011错误,实现点到点的推送
//                                            user.setAlias(userId);
//                                            setDirectPush(user, userId);
//                                            // 保存登录信息
//                                            db.save(user);
//                                        }
//                                        infos = db.findAll(LocationInfo.class);
//                                        if (infos != null && infos.size() != 0) {
//                                            info = infos.get(0);
//                                            if (info != null) {
//                                                latitude = info.getLatitude();
//                                                longitude = info.getLongitude();
//                                            }
//                                        }
//                                        // 更新当前用户的地理位置
//                                        updateCoorde(userId);
//                                        list_msg = db.findAllByWhere(
//                                                FriendsBean.class, " userId=\""
//                                                        + userId + "\"");
//                                        // 如果该用户不存在，保存该用户信息
//                                        if (list_msg.size() == 0) {
//                                            FriendsBean friend = new FriendsBean();
//                                            friend.setUserId(memberUser
//                                                    .getMemberId());
//                                            friend.setHeadImg(memberUser
//                                                    .getHeadImg());
//                                            friend.setNickName(memberUser
//                                                    .getNickName());
//                                            db.save(friend);
//                                        }
//                                        ImageLoader.getInstance()
//                                                .clearMemoryCache();
//                                        ImageLoader.getInstance().clearDiskCache();
//                                        ImageLoader.getInstance().clearDiscCache();
//                                        connect(rongtoken);
//                                        break;
//                                    case 3:
//                                        // 普通用户登录成功
//                                        memberUser = result.getData()
//                                                .getMemberUser();
//                                        userId = memberUser.getMemberId();
//                                        rongtoken = result.getData().getRong()
//                                                .getToken();
//                                        token = memberUser.getTokenBean();
//                                        user = new MemberUser();
//                                        users = db.findAll(MemberUser.class);
//                                        prefUtils.setString(SharePrefUtils.TOKEN,
//                                                token.getToken());
//                                        prefUtils.setString(SharePrefUtils.PPID,
//                                                USER.optString("ppId"));
//                                        prefUtils.setString(SharePrefUtils.OOID,
//                                                USER.optString("ooId"));
//                                        prefUtils.setString(
//                                                SharePrefUtils.STAMPIMG,
//                                                USER.optString("stampImg"));
//                                        prefUtils.setString(
//                                                SharePrefUtils.CHECKSTATE,
//                                                USER.optString("checkState"));
//                                        prefUtils.setString(
//                                                SharePrefUtils.REALNAME,
//                                                USER.optString("name"));
//                                        prefUtils.setString(SharePrefUtils.IDCARD,
//                                                USER.optString("idCard"));
//                                        prefUtils.setString(SharePrefUtils.CODE,
//                                                USER.optString("userCode"));
//                                        prefUtils
//                                                .setString(
//                                                        SharePrefUtils.COMPANYNAME,
//                                                        memberuser
//                                                                .optString("companyName"));
//                                        prefUtils.setString(
//                                                SharePrefUtils.HEAD_IMG,
//                                                USER.optString("headImg"));
//                                        prefUtils.setString(
//                                                SharePrefUtils.NICK_NAME,
//                                                memberuser.optString("nickName"));
//                                        if (users == null || users.size() == 0) {
//                                            // 默认记住密码 保存用户名和密码
//                                            user.setMemberId(userId);
//                                            user.setAddress(memberUser.getAddress());
//                                            user.setStatu(memberUser.getStatu());
//                                            user.setNickName(memberUser
//                                                    .getNickName());
//                                            user.setHeadImg(memberUser.getHeadImg());
//                                            user.setName(memberUser.getName());
//                                            user.setSex(memberUser.getSex());
//                                            user.setCompanyName(memberUser
//                                                    .getCompanyName());
//                                            user.setUserType(Constant.NORMAL_USER);
//                                            user.setUsername(username);
//                                            user.setPassword(password);
//                                            user.setRongToken(rongtoken);
//                                            user.setCToken(token.getToken());
//                                            user.setGardenId(memberUser
//                                                    .getGardenId());
//                                            user.setHotRegion(memberUser
//                                                    .getHotRegion());
//
//                                            // 设置标签别名请注意处理call back结果。只有call back
//                                            // 返回值为 0
//                                            // 才设置成功，才可以向目标推送。否则服务器 API
//                                            // 会返回1011错误,实现点到点的推送
//                                            user.setAlias(userId);
//                                            setDirectPush(user, userId);
//                                            // 保存登录信息
//                                            db.save(user);
//                                        }
//
//                                        infos = db.findAll(LocationInfo.class);
//                                        if (infos != null && infos.size() != 0) {
//                                            info = infos.get(0);
//                                            if (info != null) {
//                                                latitude = info.getLatitude();
//                                                longitude = info.getLongitude();
//                                            }
//                                        }
//                                        // 更新当前用户的地理位置
//                                        updateCoorde(userId);
//                                        list_msg = db.findAllByWhere(
//                                                FriendsBean.class, " userId=\""
//                                                        + userId + "\"");
//                                        // 如果该用户不存在，保存该用户信息
//                                        if (list_msg.size() == 0) {
//                                            FriendsBean friend = new FriendsBean();
//                                            friend.setUserId(memberUser
//                                                    .getMemberId());
//                                            friend.setHeadImg(memberUser
//                                                    .getHeadImg());
//                                            friend.setNickName(memberUser
//                                                    .getNickName());
//                                            db.save(friend);
//                                        }
//                                        ImageLoader.getInstance()
//                                                .clearMemoryCache();
//                                        ImageLoader.getInstance().clearDiskCache();
//                                        ImageLoader.getInstance().clearDiscCache();
//                                        connect(rongtoken);
//                                        break;
//                                    default:
//                                        break;
//                                }
//
//                            } else if (result.getRepCode().contains("666666")) {
//                                Intent intent = new Intent(
//                                        WelcomeActivity.this,
//                                        ChangeDeviceActivity.class);
//                                intent.putExtra("telePhone", username);
//                                intent.putExtra("password", password);
//                                startActivity(intent);
//                                // WelcomeActivity.this.finish();
//                            } else if (result.getRepCode().contains("333111")) {
//                                TipDialog.showDialogStartNewActivity(
//                                        WelcomeActivity.this, R.string.tip,
//                                        R.string.confirm,
//                                        R.string.server_error,
//                                        MainActivity.class);
//                            } else {
//                                List<MemberUser> users = db
//                                        .findAll(MemberUser.class);
//                                if (users != null && users.size() != 0) {
//                                    MemberUser user = users.get(0);
//                                    if (user != null) {
//                                        userId = user.getMemberId();
//                                    }
//                                }
//                                if (!TextUtils.isEmpty(userId)) {
//                                    // 删除登录保存的登录信息
//                                    db.deleteAll(MemberUser.class);
//                                    prefUtils.setString(SharePrefUtils.GARDEN,
//                                            "");
//                                    prefUtils.setString(
//                                            SharePrefUtils.GARDEN_ID, "");
//                                    prefUtils.setString(SharePrefUtils.TOKEN,
//                                            "");
//                                    prefUtils.setString(
//                                            SharePrefUtils.PAYPASSWORD, "");
//                                    prefUtils.setString(
//                                            SharePrefUtils.HANDPASSWORD, "");
//                                    prefUtils.setString(
//                                            SharePrefUtils.PASSWORD, "");
//                                    prefUtils.setString(
//                                            SharePrefUtils.NOPASSWORDPAY_COUNT,
//                                            "");
//                                    prefUtils.setString(
//                                            SharePrefUtils.NOPASSWORDPAY, "");
//                                    prefUtils.setString(
//                                            SharePrefUtils.COMPANYNAME, "");
//                                    prefUtils.setString(
//                                            SharePrefUtils.REALNAME, "");
//                                    prefUtils.setString(SharePrefUtils.IDCARD,
//                                            "");
//                                    prefUtils
//                                            .setString(SharePrefUtils.PPID, "");
//                                    prefUtils
//                                            .setString(SharePrefUtils.OOID, "");
//                                    prefUtils.setString(
//                                            SharePrefUtils.CHECKSTATE, "");
//                                    prefUtils.setString(
//                                            SharePrefUtils.STAMPIMG, "");
//                                    prefUtils.setString(
//                                            SharePrefUtils.HEAD_IMG, "");
//                                    prefUtils.setString(
//                                            SharePrefUtils.NICK_NAME, "");
//                                    prefUtils
//                                            .setString(SharePrefUtils.CODE, "");
//                                    setDirectPush2("");
//                                    // 好友验证消息未读消息的状态
//                                    BlueTownApp.isScanUnReadMsg = false;
//                                    // 好友验证消息未读消息数(交友)
//                                    BlueTownApp.unReadMsgCount = 0;
//                                    // 活动中心消息未读消息数
//                                    BlueTownApp.actionMsgCount = 0;
//                                    // 跳蚤市场消息未读消息数
//                                    BlueTownApp.fleaMarketMsgCount = 0;
//                                    // 设置用户退出了登录（退出状态）
//                                    prefUtils.setString(
//                                            SharePrefUtils.USER_STATE, "1");
//                                    TipDialog.showDialogStartNewActivity(
//                                            WelcomeActivity.this, R.string.tip,
//                                            R.string.confirm,
//                                            R.string.server_error,
//                                            MainActivity.class);
//
//                                }
//                            }
//                        } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(int i, String s, Throwable throwable) {
//                        List<MemberUser> users = db.findAll(MemberUser.class);
//                        if (users != null && users.size() != 0) {
//                            MemberUser user = users.get(0);
//                            if (user != null) {
//                                userId = user.getMemberId();
//                            }
//                        }
//                        if (!TextUtils.isEmpty(userId)) {
//                            // 删除登录保存的登录信息
//                            db.deleteAll(MemberUser.class);
//                            prefUtils.setString(SharePrefUtils.GARDEN, "");
//                            prefUtils.setString(SharePrefUtils.GARDEN_ID, "");
//                            prefUtils.setString(SharePrefUtils.TOKEN, "");
//                            prefUtils.setString(SharePrefUtils.PAYPASSWORD, "");
//                            prefUtils
//                                    .setString(SharePrefUtils.HANDPASSWORD, "");
//                            prefUtils.setString(SharePrefUtils.PASSWORD, "");
//                            prefUtils.setString(
//                                    SharePrefUtils.NOPASSWORDPAY_COUNT, "");
//                            prefUtils.setString(SharePrefUtils.NOPASSWORDPAY,
//                                    "");
//                            prefUtils.setString(SharePrefUtils.COMPANYNAME, "");
//                            prefUtils.setString(SharePrefUtils.REALNAME, "");
//                            prefUtils.setString(SharePrefUtils.IDCARD, "");
//                            prefUtils.setString(SharePrefUtils.PPID, "");
//                            prefUtils.setString(SharePrefUtils.OOID, "");
//                            prefUtils.setString(SharePrefUtils.CHECKSTATE, "");
//                            prefUtils.setString(SharePrefUtils.STAMPIMG, "");
//                            prefUtils.setString(SharePrefUtils.HEAD_IMG, "");
//                            prefUtils.setString(SharePrefUtils.NICK_NAME, "");
//                            prefUtils.setString(SharePrefUtils.USER_STATE, "1");
//                            prefUtils.setString(SharePrefUtils.LL_ID, "");
//                            prefUtils.setString(SharePrefUtils.KEY_LIST, "");
//                            prefUtils.setString(SharePrefUtils.RONG_TOKEN, "");
//                            prefUtils.setString(SharePrefUtils.KEY_LIST_TIME,
//                                    "");
//                            setDirectPush2("");
//                            // 好友验证消息未读消息的状态
//                            BlueTownApp.isScanUnReadMsg = false;
//                            // 好友验证消息未读消息数(交友)
//                            BlueTownApp.unReadMsgCount = 0;
//                            // 活动中心消息未读消息数
//                            BlueTownApp.actionMsgCount = 0;
//                            // 跳蚤市场消息未读消息数
//                            BlueTownApp.fleaMarketMsgCount = 0;
//                            // 设置用户退出了登录（退出状态）
//                            Log.d("TAG", "msg:" + s.toString() + "throw" + throwable.getMessage().toString());
//                            TipDialog.showDialogStartNewActivity(
//                                    WelcomeActivity.this, R.string.tip,
//                                    R.string.confirm, R.string.server_error,
//                                    LoginActivity.class);
//                        }
//                    }
//
//                });
//
//    }
//
//    private void setDirectPush(final MemberUser user, final String userId) {
//        // TODO Auto-generated method stub
//        JPushInterface.setAlias(WelcomeActivity.this, userId,
//                new TagAliasCallback() {
//
//                    @Override
//                    public void gotResult(int arg0, String arg1,
//                                          Set<String> arg2) {
//                        // TODO Auto-generated method
//                        // stub
//                        // 写入标签名
//                        user.setAlias(userId);
//                    }
//                });
//
//    }
//
//    private void setDirectPush2(final String userId) {
//        // TODO Auto-generated method stub
//        JPushInterface.setAlias(WelcomeActivity.this, userId,
//                new TagAliasCallback() {
//
//                    @Override
//                    public void gotResult(int arg0, String arg1,
//                                          Set<String> arg2) {
//                        // TODO Auto-generated method
//                        // stub
//                        // 写入标签名
//                    }
//                });
//
//    }
//
//    private void updateCoorde(String userId) {
//        params.put("userId", userId);
//        params.put("longitude", longitude + "");
//        params.put("latitude", latitude + "");
//        httpInstance.post(Constant.HOST_URL + Constant.Interface.UPDATE_COORD,
//                params, new AbsHttpResponseListener() {
//                    @Override
//                    public void onSuccess(int i, String s) {
//                        Result result = (Result) AbJsonUtil.fromJson(s,
//                                Result.class);
//                        if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
//                        } else {
//                            Toast.makeText(WelcomeActivity.this,
//                                    result.getRepMsg(), Toast.LENGTH_SHORT)
//                                    .show();
//                        }
//
//                    }
//                });
//    }
//
//    /**
//     * 建立与融云服务器的连�?
//     */
//    private void connect(final String token) {
//        if (token != null && getApplicationInfo().packageName.equals(BlueTownApp
//                .getCurProcessName(getApplicationContext()))) {
//
//            RongIM.connect(token, new RongIMClient.ConnectCallback() {
//
//                @Override
//                public void onTokenIncorrect() {
//
//                    Log.d("LoginActivity", "--onTokenIncorrect");
//                }
//
//                /**
//                 * 链接成功
//                 */
//                @Override
//                public void onSuccess(String userid) {
//                    prefUtils.setString(SharePrefUtils.RONG_TOKEN, token);
////                RongCloudEvent.getInstance().setOtherListener();
//                    Intent intent = new Intent();
//                    intent.setClass(WelcomeActivity.this, MainActivity.class);
//                    WelcomeActivity.this.startActivity(intent);
//                    WelcomeActivity.this.finish();
//                }
//
//                /**
//                 * 连接融云失败
//                 *
//                 * @param errorCode
//                 */
//                @Override
//                public void onError(RongIMClient.ErrorCode errorCode) {
//                    Log.d("LoginActivity", "--onError" + errorCode);
//                    UpdateManager manager = new UpdateManager(WelcomeActivity.this);
//                    manager.checkUpdate();
//                }
//            });
//        }
//    }
//
//    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            String action = intent.getAction();
//            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
//                mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//                netInfo = mConnectivityManager.getActiveNetworkInfo();
//                if (netInfo != null && netInfo.isAvailable()) {
//
//                } else {
//                    // 网络断开
//
//                }
//            }
//
//        }
//    };
//
//    @Override
//    protected void onDestroy() {
//        // TODO Auto-generated method stub
//        super.onDestroy();
//        unregisterReceiver(myNetReceiver);
////        manager.removeReceiver();
////        unbindService();
//    }
//
//
//    public String getSystemCurrentTime() {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
//        //获取当前时间
//        Date date = new Date(System.currentTimeMillis());
//        return simpleDateFormat.format(date);
//    }
//
//}
