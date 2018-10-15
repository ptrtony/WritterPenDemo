package com.android.bluetown;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

import java.util.List;
import java.util.Set;

import net.tsz.afinal.FinalDb;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.ab.http.AbRequestParams;
import com.ab.util.AbJsonUtil;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.FriendsBean;
import com.android.bluetown.bean.LocationInfo;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.TokenBean;
import com.android.bluetown.custom.dialog.PromptDialog;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
//import com.android.bluetown.receiver.RongCloudEvent;
import com.android.bluetown.result.RegisterResult;
import com.android.bluetown.result.Result;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.DeviceUtil;
import com.android.bluetown.view.ClearEditText;
import com.android.bluetown.view.TimeButton;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author hedi
 * @data: 2016年7月8日 下午6:25:02
 * @Description: TODO<更换设备>
 */
public class ChangeDeviceActivity extends TitleBarActivity implements
        OnClickListener {
    public static final int TOKEN_ERROR = 0;
    public static final int SUCCESS = 1;
    public static final int ERROR = 2;
    private ClearEditText verifycode;
    private TimeButton get_verifycode;
    private Button register;
    private String verifycode_num;
    private String deviceId;
    private SharePrefUtils sharePrefUtils;
    private FinalDb db;
    private String userId;
    private double latitude = 0.0;
    private double longitude = 0.0;
    // 首页注册成功或企业用户注册正在审核跳转还是从其他界面跳转的标志
    private String flag;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TOKEN_ERROR:
                    break;
                case SUCCESS:
//				RongCloudEvent.getInstance().setOtherListener();
                    FinalDb db = FinalDb.create(ChangeDeviceActivity.this);
                    List<MemberUser> users = db.findAll(MemberUser.class);
                    if (users != null && users.size() != 0) {
                        MemberUser user = users.get(0);
                        if (user != null) {
                            String userId = user.getMemberId();
                            String nickname = user.getNickName();
                            String headImage = user.getHeadImg();
                            // 将该用户保存到数据库
                            List<FriendsBean> friendList = db
                                    .findAllByWhere(FriendsBean.class, " userId=\""
                                            + userId + "\"");
                            if (friendList.size() == 0) {
                                FriendsBean friend = new FriendsBean();
                                friend.setUserId(userId);
                                friend.setHeadImg(headImage);
                                friend.setNickName(nickname);
                                db.save(friend);
                            }
                        }
                    }
                    break;
                case ERROR:
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addContentView(R.layout.ac_change_decice);
        BlueTownExitHelper.addActivity(this);
        initView();
        sharePrefUtils = new SharePrefUtils(this);
        db = FinalDb.create(this);
        db.deleteAll(MemberUser.class);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.register:
                if (verifycode.getText().toString().equals(verifycode_num)) {
                    update();
                } else {
                    new PromptDialog.Builder(ChangeDeviceActivity.this)
                            .setViewStyle(PromptDialog.VIEW_STYLE_NORMAL)
                            .setMessage("请填写正确验证码")
                            .setButton1("确定", new PromptDialog.OnClickListener() {

                                @Override
                                public void onClick(Dialog dialog, int which) {
                                    // TODO Auto-generated method stub
                                    dialog.cancel();
                                }
                            }).show();
                }
                break;
            case R.id.reget_verifycode:
                get_verifycode.setIsOK(true);
                getverifycode(getIntent().getStringExtra("telePhone"));
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void initTitle() {
        // TODO Auto-generated method stub
//		setBackImageView();
//		setTitleView("安全校验");
        setTitleState();
        setTitleLayoutBg(R.color.title_bg_blue);
        rightImageLayout.setVisibility(View.INVISIBLE);
    }

    private void initView() {
        verifycode = findViewById(R.id.verifycode);
//		tel_num = (TextView) findViewById(R.id.tel_num);
        get_verifycode = findViewById(R.id.reget_verifycode);
        get_verifycode.setIsOK(false);
        get_verifycode.setTextAfter("s").setTextBefore("获取验证码")
                .setLenght(60 * 1000);
//		reget_verifycode = (TextView) findViewById(R.id.reget_verifycode);
        register = findViewById(R.id.register);
//		cant_register = (TextView)findViewById(R.id.cant_register);

//		tel_num.setText("验证码已发送至+86 " + getIntent().getStringExtra("telePhone"));
        findViewById(R.id.reget_verifycode).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        register.setOnClickListener(this);
        setSubmitButtonClick();
        verifycode.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                setSubmitButtonClick();
            }
        });
        deviceId = DeviceUtil.toMD5UniqueId(ChangeDeviceActivity.this);
    }

    private void getverifycode(String tel) {
        AbRequestParams params = new AbRequestParams();
        // httpUtil.setToken(SharePUtil.get(SharePUtil.KEY_TOKEN, ""));
        params.put("telphone", tel);
        params.put("pttId", deviceId);
        httpInstance.post(Constant.HOST_URL
                        + Constant.Interface.MobiUserAction_repeat, params,
                new AbsHttpStringResponseListener(this, null) {
                    @Override
                    public void onSuccess(int arg0, String arg1) {
                        // TODO Auto-generated method stub
                        JSONObject json;
                        try {
                            json = new JSONObject(arg1);
                            String repCode = json.optString("repCode");
                            if (repCode.equals(Constant.HTTP_SUCCESS)) {
                                verifycode_num = json.optString("checkCode");
                            } else {
                                new PromptDialog.Builder(ChangeDeviceActivity.this)
                                        .setViewStyle(
                                                PromptDialog.VIEW_STYLE_NORMAL)
                                        .setMessage(json.optString("repMsg"))
                                        .setButton1(
                                                "确定",
                                                new PromptDialog.OnClickListener() {

                                                    @Override
                                                    public void onClick(
                                                            Dialog dialog,
                                                            int which) {
                                                        // TODO Auto-generated
                                                        // method stub
                                                        dialog.cancel();
                                                    }
                                                }).show();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }


                });
    }

    private void update() {
        AbRequestParams params = new AbRequestParams();
        // httpUtil.setToken(SharePUtil.get(SharePUtil.KEY_TOKEN, ""));
        params.put("userAccount", getIntent().getStringExtra("telePhone"));
        params.put("pttId", deviceId);
        httpInstance.post(Constant.HOST_URL + Constant.Interface.MobiMemberAction_updateEquipment,
                params, new AbsHttpStringResponseListener(this, null) {
                    @Override
                    public void onSuccess(int arg0, String arg1) {
                        // TODO Auto-generated method stub
                        JSONObject json;
                        try {
                            json = new JSONObject(arg1);
                            String repCode = json.optString("repCode");
                            if (repCode.equals(Constant.HTTP_SUCCESS)) {
                                login(getIntent().getStringExtra("telePhone"), getIntent().getStringExtra("password"));
                            } else {
                                register.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }


                });
    }

    private void login(final String username, final String password) {
        params.put("telphone", username);
        params.put("password", password);
        params.put("pttId", DeviceUtil.toMD5UniqueId(ChangeDeviceActivity.this));
        httpInstance.post(Constant.HOST_URL + Constant.Interface.LOGIN, params,
                new AbsHttpStringResponseListener(this, null) {

                    @Override
                    public void onSuccess(int arg0, String arg1) {
                        // TODO Auto-generated method stub
                        RegisterResult result = (RegisterResult) AbJsonUtil
                                .fromJson(arg1, RegisterResult.class);
                        try {
                            if (result.getRepCode().contains(
                                    Constant.HTTP_SUCCESS)) {
                                JSONObject json = new JSONObject(arg1);
                                JSONObject data = new JSONObject(json
                                        .optString("data"));
                                JSONObject memberuser = new JSONObject(data
                                        .optString("memberUser"));
                                JSONObject USER = new JSONObject(memberuser
                                        .optString("user"));
                                List<FriendsBean> list_msg;
                                LocationInfo info = null;
                                List<LocationInfo> infos = null;
                                List<MemberUser> users = null;
                                MemberUser user = null;
                                MemberUser memberUser = null;
                                String userId;
                                TokenBean token = null;
                                final String rongtoken;
                                // statu=0，1，2
                                // （为企业用户，是0的时候，企业正在审核，1的时候审核拒绝，2的时候审核通过，3的时候为普通用户登陆）
                                int userStatus = Integer.parseInt(result
                                        .getData().getMemberUser().getStatu());
                                switch (userStatus) {
                                    case 0:
                                        // 游客身份进入主页（正在审核）
                                        TipDialog.showDialogClearTop(
                                                ChangeDeviceActivity.this, R.string.tip,
                                                R.string.confirm,
                                                R.string.commit_tip,
                                                MainActivity.class);
                                        break;
                                    case 1:
                                        // 游客身份进入主页（拒绝审核）
                                        TipDialog.showDialogClearTop(
                                                ChangeDeviceActivity.this, R.string.tip,
                                                R.string.confirm,
                                                R.string.commit_refuse_tip,
                                                MainActivity.class);
                                        break;
                                    case 2:
                                        // 企业用户登录成功
                                        memberUser = result.getData()
                                                .getMemberUser();
                                        userId = memberUser.getMemberId();
                                        rongtoken = result.getData().getRong()
                                                .getToken();
                                        token = memberUser.getTokenBean();
                                        user = new MemberUser();
                                        users = db.findAll(MemberUser.class);
                                        sharePrefUtils.setString(
                                                SharePrefUtils.TOKEN,
                                                token.getToken());
                                        sharePrefUtils.setString(
                                                SharePrefUtils.PAYPASSWORD,
                                                USER.optString("payPassword"));
                                        sharePrefUtils.setString(
                                                SharePrefUtils.HANDPASSWORD,
                                                USER.optString("handPassword"));
                                        sharePrefUtils.setString(
                                                SharePrefUtils.PPID,
                                                USER.optString("ppid"));
                                        sharePrefUtils.setString(
                                                SharePrefUtils.OOID,
                                                USER.optString("ooid"));
                                        sharePrefUtils.setString(
                                                SharePrefUtils.STAMPIMG,
                                                USER.optString("stampImg"));
                                        sharePrefUtils.setString(
                                                SharePrefUtils.CHECKSTATE,
                                                USER.optString("checkState"));
                                        sharePrefUtils.setString(
                                                SharePrefUtils.REALNAME,
                                                USER.optString("name"));
                                        sharePrefUtils.setString(
                                                SharePrefUtils.IDCARD,
                                                USER.optString("idCard"));
                                        sharePrefUtils.setString(
                                                SharePrefUtils.NOPASSWORDPAY,
                                                USER.optString("smallNoPayPassword"));
                                        sharePrefUtils.setString(
                                                SharePrefUtils.NOPASSWORDPAY_COUNT,
                                                USER.optString("smallMoney"));
                                        sharePrefUtils.setString(SharePrefUtils.COMPANYNAME,
                                                memberuser.optString("companyName"));
                                        if (users == null || users.size() == 0) {
                                            // 默认记住密码 保存用户名和密码
                                            user.setUsername(username);
                                            user.setPassword(password);
                                            user.setUserType(Constant.COMPANY_USER);
                                            user.setRongToken(rongtoken);
                                            user.setCToken(token.getToken());
                                            user.setHeadImg(memberUser.getHeadImg());
                                            user.setMemberId(userId);
                                            user.setAddress(memberUser.getAddress());
                                            user.setStatu(memberUser.getStatu());
                                            user.setNickName(memberUser
                                                    .getNickName());
                                            user.setName(memberUser.getName());
                                            user.setSex(memberUser.getSex());
                                            user.setCompanyName(memberUser
                                                    .getCompanyName());
                                            user.setHotRegion(memberUser
                                                    .getHotRegion());
                                            user.setGardenId(memberUser
                                                    .getGardenId());
                                            sharePrefUtils.setString(
                                                    SharePrefUtils.GARDEN,
                                                    memberUser.getHotRegion());
                                            sharePrefUtils.setString(
                                                    SharePrefUtils.GARDEN_ID,
                                                    memberUser.getGardenId());
                                            infos = db.findAll(LocationInfo.class);
                                            if (infos != null && infos.size() != 0) {
                                                info = infos.get(0);
                                                if (info != null) {
                                                    latitude = info.getLatitude();
                                                    longitude = info.getLongitude();
                                                }
                                            }
                                            // 更新当前用户的地理位置
                                            updateCoorde(userId);
                                            // 设置标签别名请注意处理call back结果。只有call back
                                            // 返回值为 0
                                            // 才设置成功，才可以向目标推送。否则服务器 API
                                            // 会返回1011错误,实现点到点的推送
                                            user.setAlias(userId);
                                            setDirectPush(user, userId);
                                            // 保存登录信息
                                            db.save(user);
                                        }

//									Toast.makeText(ChangeDeviceActivity.this, "登录成功！",
//											Toast.LENGTH_SHORT).show();

                                        list_msg = db.findAllByWhere(
                                                FriendsBean.class, " userId=\""
                                                        + userId + "\"");
                                        // 如果该用户不存在，保存该用户信息
                                        if (list_msg.size() == 0) {
                                            FriendsBean friend = new FriendsBean();
                                            friend.setUserId(memberUser
                                                    .getMemberId());
                                            friend.setHeadImg(memberUser
                                                    .getHeadImg());
                                            friend.setNickName(memberUser
                                                    .getNickName());
                                            db.save(friend);
                                        }

                                        loginRongCould(rongtoken);
                                        if (TextUtils.isEmpty(flag)) {
                                            startActivity(new Intent(
                                                    ChangeDeviceActivity.this,
                                                    MainActivity.class));
                                        }
                                        ImageLoader.getInstance()
                                                .clearMemoryCache();
                                        ImageLoader.getInstance().clearDiskCache();
                                        ImageLoader.getInstance().clearDiscCache();
                                        finish();
                                        break;
                                    case 3:
                                        // 普通用户登录成功
                                        memberUser = result.getData()
                                                .getMemberUser();
                                        userId = memberUser.getMemberId();
                                        rongtoken = result.getData().getRong()
                                                .getToken();
                                        token = memberUser.getTokenBean();
                                        user = new MemberUser();
                                        users = db.findAll(MemberUser.class);
                                        sharePrefUtils.setString(
                                                SharePrefUtils.TOKEN,
                                                token.getToken());
                                        sharePrefUtils.setString(
                                                SharePrefUtils.PAYPASSWORD,
                                                USER.optString("payPassword"));
                                        sharePrefUtils.setString(
                                                SharePrefUtils.HANDPASSWORD,
                                                USER.optString("handPassword"));
                                        sharePrefUtils.setString(SharePrefUtils.PPID,
                                                USER.optString("ppId"));
                                        sharePrefUtils.setString(SharePrefUtils.OOID,
                                                USER.optString("ooId"));
                                        sharePrefUtils.setString(
                                                SharePrefUtils.STAMPIMG,
                                                USER.optString("stampImg"));
                                        sharePrefUtils.setString(
                                                SharePrefUtils.CHECKSTATE,
                                                USER.optString("checkState"));
                                        sharePrefUtils.setString(
                                                SharePrefUtils.REALNAME,
                                                USER.optString("name"));
                                        sharePrefUtils.setString(
                                                SharePrefUtils.IDCARD,
                                                USER.optString("idCard"));
                                        sharePrefUtils.setString(
                                                SharePrefUtils.NOPASSWORDPAY,
                                                USER.optString("smallNoPayPassword"));
                                        sharePrefUtils.setString(
                                                SharePrefUtils.NOPASSWORDPAY_COUNT,
                                                USER.optString("smallMoney"));
                                        sharePrefUtils.setString(
                                                SharePrefUtils.PASSWORD,
                                                USER.optString("password"));
                                        sharePrefUtils.setString(
                                                SharePrefUtils.CODE,
                                                USER.optString("userCode"));
                                        sharePrefUtils.setString(SharePrefUtils.COMPANYNAME,
                                                memberuser.optString("companyName"));
                                        sharePrefUtils.setString(SharePrefUtils.HEAD_IMG,
                                                USER.optString("headImg"));
                                        sharePrefUtils.setString(SharePrefUtils.NICK_NAME,
                                                memberuser.optString("nickName"));
                                        if (users == null || users.size() == 0) {
                                            // 默认记住密码 保存用户名和密码
                                            user.setUsername(username);
                                            user.setPassword(password);
                                            user.setUserType(Constant.NORMAL_USER);
                                            user.setRongToken(rongtoken);
                                            user.setCToken(token.getToken());
                                            user.setHeadImg(memberUser.getHeadImg());
                                            user.setMemberId(userId);
                                            user.setAddress(memberUser.getAddress());
                                            user.setStatu(memberUser.getStatu());
                                            user.setNickName(memberUser
                                                    .getNickName());
                                            user.setName(memberUser.getName());
                                            user.setSex(memberUser.getSex());
                                            user.setCompanyName(memberUser
                                                    .getCompanyName());
                                            user.setHotRegion(memberUser
                                                    .getHotRegion());
                                            user.setGardenId(memberUser
                                                    .getGardenId());
                                            sharePrefUtils.setString(
                                                    SharePrefUtils.GARDEN,
                                                    memberUser.getHotRegion());
                                            sharePrefUtils.setString(
                                                    SharePrefUtils.GARDEN_ID,
                                                    memberUser.getGardenId());
                                            infos = db.findAll(LocationInfo.class);
                                            if (infos != null && infos.size() != 0) {
                                                info = infos.get(0);
                                                if (info != null) {
                                                    latitude = info.getLatitude();
                                                    longitude = info.getLongitude();
                                                }
                                            }
                                            // 更新当前用户的地理位置
                                            updateCoorde(userId);
                                            // 设置标签别名请注意处理call back结果。只有call back
                                            // 返回值为 0
                                            // 才设置成功，才可以向目标推送。否则服务器 API
                                            // 会返回1011错误,实现点到点的推送
                                            user.setAlias(userId);
                                            setDirectPush(user, userId);
                                            // 保存登录信息
                                            db.save(user);
                                        }

                                        list_msg = db.findAllByWhere(
                                                FriendsBean.class, " userId=\""
                                                        + userId + "\"");
                                        // 如果该用户不存在，保存该用户信息
                                        if (list_msg.size() == 0) {
                                            FriendsBean friend = new FriendsBean();
                                            friend.setUserId(memberUser
                                                    .getMemberId());
                                            friend.setHeadImg(memberUser
                                                    .getHeadImg());
                                            friend.setNickName(memberUser
                                                    .getNickName());
                                            db.save(friend);
                                        }
                                        loginRongCould(rongtoken);

                                        if (TextUtils.isEmpty(flag)) {
                                            startActivity(new Intent(
                                                    ChangeDeviceActivity.this,
                                                    MainActivity.class));
                                        }
                                        ImageLoader.getInstance()
                                                .clearMemoryCache();
                                        ImageLoader.getInstance().clearDiskCache();
                                        finish();
                                        break;
                                    default:
                                        break;
                                }

                            } else if (result.getRepCode().contains("666666")) {
                                Intent intent = new Intent(ChangeDeviceActivity.this,
                                        ChangeDeviceActivity.class);
                                intent.putExtra("telePhone", username);
                                startActivity(intent);
                            } else {
                                TipDialog.showDialog(ChangeDeviceActivity.this,
                                        R.string.tip, R.string.confirm,
                                        result.getRepMsg());
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }

                    private void setDirectPush(final MemberUser user,
                                               final String userId) {
                        // TODO Auto-generated method stub
                        JPushInterface.setAlias(ChangeDeviceActivity.this, userId,
                                new TagAliasCallback() {

                                    @Override
                                    public void gotResult(int arg0,
                                                          String arg1, Set<String> arg2) {
                                        // TODO Auto-generated method
                                        // stub
                                        // 写入标签名
                                        user.setAlias(userId);
                                    }
                                });
                    }

                }

        );
    }

    private void updateCoorde(String userId) {
        params.put("userId", userId);
        params.put("longitude", longitude + "");
        params.put("latitude", latitude + "");
        httpInstance.post(Constant.HOST_URL + Constant.Interface.UPDATE_COORD,
                params, new AbsHttpResponseListener() {
                    @Override
                    public void onSuccess(int i, String s) {
                        Result result = (Result) AbJsonUtil.fromJson(s,
                                Result.class);
                        if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
                        } else {
                            Toast.makeText(ChangeDeviceActivity.this,
                                    result.getRepMsg(), Toast.LENGTH_SHORT)
                                    .show();
                        }

                    }
                });
    }

    /**
     * login RongCloud
     */
    private void loginRongCould(String token) {
        if (!token.isEmpty()) {
            if (getApplicationInfo().packageName.equals(BlueTownApp
                    .getCurProcessName(getApplicationContext()))) {
                /**
                 * IMKit SDK调用第二步,建立与服务器的连接
                 */
                RongIM.connect(token, new RongIMClient.ConnectCallback() {

                    /**
                     * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的
                     * Token
                     */
                    @Override
                    public void onTokenIncorrect() {
                        handler.sendEmptyMessage(TOKEN_ERROR);
                    }

                    /**
                     * 连接融云成功
                     *
                     * @param userid
                     *            当前 token
                     */
                    @Override
                    public void onSuccess(String userid) {
                        Message message = new Message();
                        message.what = SUCCESS;
                        message.obj = userid;
                        handler.sendMessage(message);
                    }

                    /**
                     * 连接融云失败
                     *
                     * @param errorCode
                     *            错误码，可到官网 查看错误码对应的注释
                     */
                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        handler.sendEmptyMessage(ERROR);
                    }
                });
            }

        }
    }

    private void setSubmitButtonClick() {
        if (!TextUtils.isEmpty(verifycode.getText().toString().intern())) {
            register.setBackground(getDrawable(R.drawable.bg_click_button));
            register.setClickable(true);
        } else {
            register.setBackground(getDrawable(R.drawable.bg_unclick_button));
            register.setClickable(false);
        }
    }
}
