package com.android.bluetown.wrapper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.ab.http.AbRequestParams;
import com.ab.util.AbJsonUtil;
import com.android.bluetown.ChangeDeviceActivity;
import com.android.bluetown.MainActivity;
import com.android.bluetown.R;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.bean.FriendsBean;
import com.android.bluetown.bean.LocationInfo;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.TokenBean;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.network.AbHttpUtil;
import com.android.bluetown.popup.LoadingAlertDialog;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.receiver.RongCloudEvent;
import com.android.bluetown.result.RegisterResult;
import com.android.bluetown.result.Result;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.DeviceUtil;
import com.android.bluetown.utils.ToastManager;

import net.tsz.afinal.FinalDb;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

import static com.android.bluetown.InputPasswordActivity.ERROR;
import static com.android.bluetown.InputPasswordActivity.SUCCESS;
import static com.android.bluetown.InputPasswordActivity.TOKEN_ERROR;

/**
 * Created by Dafen on 2018/8/24.
 * 封装登录接口相关的逻辑操作
 */

public class LoginWrapper {
    private Context mContext;
    private SharePrefUtils prefUtils;
    protected AbRequestParams params;
    protected AbHttpUtil httpInstance;
    private FinalDb db;
    private LoadingAlertDialog mLoadingDialog;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TOKEN_ERROR:
                    break;
                case SUCCESS:
                    RongCloudEvent.getInstance().setOtherListener();
//                    FinalDb db = FinalDb.create(InputPasswordActivity.this);
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
                    Intent intent = new Intent();
                    intent.setClass(mContext, MainActivity.class);
                    mContext.startActivity(intent);
                    ((Activity)mContext).finish();
                    break;
                case ERROR:
                    break;
                default:
                    break;
            }
        }

    };
//    private SweetAlertDialog sweetAlertDialog1;

    public LoginWrapper(Context context){
        this.mContext = context;
        prefUtils = new SharePrefUtils(context);
        db = FinalDb.create(context);
        params = new AbRequestParams();
        httpInstance = AbHttpUtil.getInstance(context);
        httpInstance.setEasySSLEnabled(true);
        mLoadingDialog = new LoadingAlertDialog((Activity)context);
    }

    /**
     * @param username
     * @param password
     */
    public void login(final String username, final String password) {
        if (mLoadingDialog!=null&&!((Activity)mContext).isFinishing()){
            mLoadingDialog.show();
        }
        params.put("telphone", username);
        params.put("password", password);
        params.put("pttId", DeviceUtil.toMD5UniqueId(mContext));
        httpInstance.post(Constant.HOST_URL + Constant.Interface.LOGIN, params,
                new AbsHttpResponseListener() {
                    @Override
                    public void onSuccess(int arg0, String arg1) {
                        // TODO Auto-generated method stub
                        RegisterResult result = (RegisterResult) AbJsonUtil
                                .fromJson(arg1, RegisterResult.class);
                        JSONObject data;
                        try {
                            if (result.getRepCode().contains(
                                    Constant.HTTP_SUCCESS)) {
                                JSONObject json = new JSONObject(arg1);
                                data = new JSONObject(json.optString("data"));
                                JSONObject memberuser = new JSONObject(data
                                        .optString("memberUser"));
                                JSONObject USER = new JSONObject(memberuser
                                        .optString("user"));
                                prefUtils.setBoolean("isLoging",true);
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
                                                (Activity) mContext, R.string.tip,
                                                R.string.confirm,
                                                R.string.commit_tip,
                                                MainActivity.class);
                                        break;
                                    case 1:
                                        // 游客身份进入主页（拒绝审核）
                                        TipDialog.showDialogClearTop(
                                                (Activity) mContext, R.string.tip,
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
                                        prefUtils.setString(SharePrefUtils.TOKEN,
                                                token.getToken());
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
                                            prefUtils.setString(
                                                    SharePrefUtils.GARDEN,
                                                    memberUser.getHotRegion());
                                            prefUtils.setString(
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
//                                        if (!TextUtils.isEmpty(flag)) {
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
//                                        }
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
                                        prefUtils.setString(SharePrefUtils.TOKEN,
                                                token.getToken());
                                        prefUtils.setString(
                                                SharePrefUtils.PAYPASSWORD,
                                                USER.optString("payPassword"));
                                        prefUtils.setString(
                                                SharePrefUtils.HANDPASSWORD,
                                                USER.optString("handPassword"));
                                        prefUtils.setString(SharePrefUtils.PPID,
                                                USER.optString("ppId"));
                                        prefUtils.setString(SharePrefUtils. OOID,
                                                USER.optString("ooId"));
                                        prefUtils.setString(
                                                SharePrefUtils.STAMPIMG,
                                                USER.optString("stampImg"));
                                        prefUtils.setString(
                                                SharePrefUtils.CHECKSTATE,
                                                USER.optString("checkState"));
                                        prefUtils.setString(
                                                SharePrefUtils.REALNAME,
                                                USER.optString("name"));
                                        prefUtils.setString(SharePrefUtils.IDCARD,
                                                USER.optString("idCard"));
                                        prefUtils.setString(SharePrefUtils.CODE,
                                                USER.optString("userCode"));
                                        prefUtils.setString(
                                                SharePrefUtils.NOPASSWORDPAY,
                                                USER.optString("smallNoPayPassword"));
                                        prefUtils.setString(
                                                SharePrefUtils.NOPASSWORDPAY_COUNT,
                                                USER.optString("smallMoney"));
                                        prefUtils.setString(
                                                SharePrefUtils.PASSWORD,
                                                USER.optString("password"));
                                        prefUtils
                                                .setString(
                                                        SharePrefUtils.COMPANYNAME,
                                                        memberuser
                                                                .optString("companyName"));
                                        prefUtils.setString(
                                                SharePrefUtils.HEAD_IMG,
                                                USER.optString("headImg"));
                                        prefUtils.setString(
                                                SharePrefUtils.NICK_NAME,
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
                                            prefUtils.setString(
                                                    SharePrefUtils.GARDEN,
                                                    memberUser.getHotRegion());
                                            prefUtils.setString(
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
//                                        if (!TextUtils.isEmpty(flag)) {

                                            list_msg = db.findAllByWhere(
                                                    FriendsBean.class,
                                                    " userId=\""
                                                            + memberUser
                                                            .getMemberId()
                                                            + "\"");
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
                                            loginRongCould(result.getData()
                                                    .getRong().getToken());
//                                        }
                                        break;
                                    default:
                                        break;

                                }

                            }else if (result.getRepCode().contains("666666")) {
                                Intent intent = new Intent(mContext,
                                        ChangeDeviceActivity.class);
                                intent.putExtra("telePhone", username);
                                intent.putExtra("password", password);
                                mContext.startActivity(intent);
                            } else {
                                TipDialog.showDialog(mContext,
                                        R.string.tip, R.string.confirm,
                                        R.string.login_error);

                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                if (mLoadingDialog!=null &&!((Activity)mContext).isDestroyed()){
                                    mLoadingDialog.dismiss();
                                }
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int i, String s, Throwable throwable) {
                        super.onFailure(i, s, throwable);
                        ToastManager.getInstance(mContext).showText(s);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            if (mLoadingDialog!=null &&!((Activity)mContext).isDestroyed()){
                                mLoadingDialog.dismiss();
                            }
                        }
                    }

                    /**
                     * 设置定向推送的标签
                     *
                     * @param userId
                     */
                    private void setDirectPush(final MemberUser memberUser,
                                               final String userId) {
                        JPushInterface.setAlias(mContext, userId,
                                new TagAliasCallback() {

                                    @Override
                                    public void gotResult(int arg0,
                                                          String arg1, Set<String> arg2) {
                                        // TODO Auto-generated method
                                        // stub
                                        // 写入标签名
                                        memberUser.setAlias(userId);
                                    }
                                });
                    }
                });
    }

    /**
     * login RongCloud
     */
    private void loginRongCould(String token) {
        if (!token.isEmpty()) {
            if (mContext.getApplicationInfo().packageName.equals(BlueTownApp
                    .getCurProcessName(mContext.getApplicationContext()))) {
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
                            Toast.makeText(mContext,
                                    result.getRepMsg(), Toast.LENGTH_SHORT)
                                    .show();
                        }

                    }
                });
    }

}
