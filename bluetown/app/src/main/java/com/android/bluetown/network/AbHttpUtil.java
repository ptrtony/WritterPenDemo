package com.android.bluetown.network;

import android.content.Context;
import com.ab.http.AbBinaryHttpResponseListener;
import com.ab.http.AbFileHttpResponseListener;
import com.ab.http.AbHttpResponseListener;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
public class AbHttpUtil {
    private BlueTownClient mClient = null;
    private static AbHttpUtil mAbHttpUtil = null;

    public static AbHttpUtil getInstance(Context context) {
        if(mAbHttpUtil == null) {
            mAbHttpUtil = new AbHttpUtil(context);
        }

        return mAbHttpUtil;
    }

    private AbHttpUtil(Context context) {
        this.mClient = new BlueTownClient(context);
    }

    public void get(String url, AbHttpResponseListener responseListener) {
        this.mClient.get(url, (AbRequestParams)null, responseListener);
    }

    public void get(String url, AbRequestParams params, AbHttpResponseListener responseListener) {
        this.mClient.get(url, params, responseListener);
    }

    public void get(String url, AbBinaryHttpResponseListener responseListener) {
        this.mClient.get(url, (AbRequestParams)null, responseListener);
    }

    public void get(String url, AbRequestParams params, AbFileHttpResponseListener responseListener) {
        this.mClient.get(url, params, responseListener);
    }

    public void post(String url, AbHttpResponseListener responseListener) {
        this.mClient.post(url, (AbRequestParams)null, responseListener);
    }

    public void post(String url, AbRequestParams params, AbHttpResponseListener responseListener) {
        this.mClient.post(url, params, responseListener);
    }

    public void post(String url, AbRequestParams params, AbFileHttpResponseListener responseListener) {
        this.mClient.post(url, params, responseListener);
    }

    public void request(String url, AbStringHttpResponseListener responseListener) {
        this.request(url, (AbRequestParams)null, responseListener);
    }

    public void request(String url, AbRequestParams params, AbStringHttpResponseListener responseListener) {
        this.mClient.doRequest(url, params, responseListener);
    }

    public void setTimeout(int timeout) {
        this.mClient.setTimeout(timeout);
    }

    public void setEasySSLEnabled(boolean enabled) {
        this.mClient.setOpenEasySSL(enabled);
    }

    public void setEncode(String encode) {
        this.mClient.setEncode(encode);
    }

    public void setUserAgent(String userAgent) {
        this.mClient.setUserAgent(userAgent);
    }

    public void shutdownHttpClient() {
        if(this.mClient != null) {
            this.mClient.shutdown();
        }

    }
}
