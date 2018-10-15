package com.android.bluetown.jsbridge;
import android.util.Log;
import com.google.gson.Gson;

/**
 * Created by Dafen on 2018/6/27.
 */

public class JsBridgeHelper {
    private static final String TAG = "JsBridgeHelper";
    private BridgeWebView mBridgeWebView;
    private JsBridgeInterface jsBridgeInterface;
    public JsBridgeHelper(BridgeWebView bridgeWebView,JsBridgeInterface jsBridgeInterface){
        this.mBridgeWebView = bridgeWebView;
        this.jsBridgeInterface = jsBridgeInterface;
    }

    /**
     * js调原生
     * @param T
     */
    public void getJsBridgeCallBackData(String T){
        if (mBridgeWebView!=null){
            mBridgeWebView.registerHandler(T, new BridgeHandler() {
                @Override
                public void handler(String data, CallBackFunction function) {
                    if (jsBridgeInterface!=null){
                        jsBridgeInterface.setCallbackValue(data);
                    }
                    Log.d(TAG,T);
                    function.onCallBack("T exe,sfsdfsdf");
                }
            });
        }

    }




    /**
     * 原生调js
     * @param methodFunction
     * @param object
     */
    public void jsCallBackJavaFunction(String methodFunction,Object object){
        mBridgeWebView.callHandler(methodFunction, new Gson().toJson(object), new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
            }
        });
    }

}
