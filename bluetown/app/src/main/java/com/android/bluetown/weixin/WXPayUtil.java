package com.android.bluetown.weixin;

import android.app.Activity;
import android.content.Intent;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.android.bluetown.listener.AliPayListener;
import com.android.bluetown.result.PreWXPayResult;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by Dafen on 2018/5/14.
 */

public class WXPayUtil{
    //微信appid
    public static String APPID = "";
    //微信支付商户号
//    public static final String PARTNER = "1460723102";
//    //AppSecret
//    public static final String APP_SECRET = "22fdf4f18cf406defac05a6db7ba6c81";
//    //API密钥
//    public static final String APP_KEY = "oDSxdsew399289sdsldsi8LKJPLM8dPK";
//
//
//    //基本接口
//    private static final String BASE_URL ="https://api.mch.weixin.qq.com";
//    //生成预交易单的接口
//    private static final String PRE_GRADE_URL = BASE_URL+"/pay/unifiedorder";
//    //支付结果通知的url是由生成订单返回的参数中notify_url作为接口
//    private static final String NOTIFY_URL = BASE_URL+"/wxpay/pay.php";
//    //查询订单
//    private static final String CHECK_ORDER_URL = BASE_URL+"/pay/orderquery";
//    //关闭订单
//    private static final String CLOSE_ORDER_URL = BASE_URL+"/pay/closeorder";
//    //申请退款
//    private static final String REFUND_URL = BASE_URL+"/secapi/pay/refund";
//    //查询退款
//    private static final String CHECK_REFUND_URL = BASE_URL+"/pay/refundquery";
//    //下载对账单
//    private static final String LOADING_ORDER_BILL_URL =BASE_URL+"/pay/downloadbill";
//    //下载资金账单
//    private static final String LOADING_MONEY_BILL_URL = BASE_URL+"/pay/downloadbill";
//    //交易保障
//    private static final String GRADE_PROTECTION_URL =BASE_URL+"/payitil/report";
//    //退款结果通知
//    private static final String REFUND_RESULT_NITIFY_URL = "";
    // Http请求参数
    protected AbRequestParams params;
    // Http请求
    protected AbHttpUtil httpInstance;
    //上下文
    private Activity mContext;
    private IWXAPI msgApi;
    private PreWXPayResult mResult;
    private static AliPayListener listener;
    public WXPayUtil(Activity context, PreWXPayResult result) {
        mContext = context;
        mResult = result;
        msgApi = WXAPIFactory.createWXAPI(mContext,null);
        //将app注册到微信
        msgApi.registerApp(mResult.getmAppId());
        listener = (AliPayListener) mContext;
        APPID = mResult.getmAppId();
        if (params == null) {
            params = new AbRequestParams();
        }

        if (httpInstance == null) {
            httpInstance = AbHttpUtil.getInstance(mContext);
            httpInstance.setEasySSLEnabled(true);
        }

    }
    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

//    private Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//        }
//    };
    /**
     * 调用wx sdk pay前先预支付获取prepay_id 在调用支付api
     */

    public void pay(){
        if (null!=msgApi){
            PayReq request = new PayReq();
            request.appId = mResult.getmAppId();
            request.partnerId = mResult.getmPartnerid();
            request.prepayId = mResult.getmPrepayid();
            request.packageValue = mResult.getmPackageStr();
            request.nonceStr = mResult.getmNoncestr();
            request.timeStamp = mResult.getmTimestamp();
            request.sign = mResult.getmSign();
            msgApi.sendReq(request);
        }
    }
    public static AliPayListener getPaylistener(){
        return listener;
    }
}
