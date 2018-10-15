package com.android.bluetown.result;

/**
 * Created by Dafen on 2018/5/14.
 */

public class PreWXPayResult {
    private String mAppId;
    private String mNoncestr;
    private String mOutTradeNo;
    private String mPackageStr;
    private String mSign;
    private String mPartnerid;
    private String mPrepayid;

    private String mTimestamp;

    public String getmAppId() {
        return mAppId;
    }

    public void setmAppId(String mAppId) {
        this.mAppId = mAppId;
    }

    public String getmNoncestr() {
        return mNoncestr;
    }

    public void setmNoncestr(String mNoncestr) {
        this.mNoncestr = mNoncestr;
    }

    public String getmOutTradeNo() {
        return mOutTradeNo;
    }

    public void setmOutTradeNo(String mOutTradeNo) {
        this.mOutTradeNo = mOutTradeNo;
    }

    public String getmPackageStr() {
        return mPackageStr;
    }

    public void setmPackageStr(String mPackageStr) {
        this.mPackageStr = mPackageStr;
    }

    public String getmSign() {
        return mSign;
    }

    public void setmSign(String mSign) {
        this.mSign = mSign;
    }

    public String getmPartnerid() {
        return mPartnerid;
    }

    public void setmPartnerid(String mPartnerid) {
        this.mPartnerid = mPartnerid;
    }

    public String getmPrepayid() {
        return mPrepayid;
    }

    public void setmPrepayid(String mPrepayid) {
        this.mPrepayid = mPrepayid;
    }


    public String getmTimestamp() {
        return mTimestamp;
    }

    public void setmTimestamp(String mTimestamp) {
        this.mTimestamp = mTimestamp;
    }
}
