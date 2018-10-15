package com.android.bluetown.wrapper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.android.bluetown.utils.Constant;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Dafen on 2018/8/27.
 */

public class UploadPictureWrapper {
    //    private String uploadFile ="/sdcard/image.JPG";
    private String newName = "image.jpg";
    private static final int SUCCESS = 0X000001;
    private static final int FAILED = 0X000002;
    private static final String RESPONSE_RESULT = "response_result";
    private OnResponseResult responseResult;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    String result = msg.getData().getString(RESPONSE_RESULT);
                    if (responseResult != null) {
                        try {
                            JSONObject json = new JSONObject(result);
                            String resCode = json.getString("rep_code");
                            if (resCode.equals(Constant.HTTP_SUCCESS)){
                                responseResult.success(json.getString("result"));
                            }else{
                                responseResult.failed(json.getString("rep_msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
                case FAILED:
                    String result1 = msg.getData().getString(RESPONSE_RESULT);
                    if (responseResult != null) {
                        responseResult.failed(result1);
                    }
                    break;
            }
        }
    };

    /* 上传文件至Server的方法 */
    public void uploadFile(String uploadFile, OnResponseResult responseResult) {
        this.responseResult = responseResult;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String end = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                try {
                    URL url = new URL(Constant.HOST_URL1+ Constant.Interface.FILE_UPLOAD_PICTURE);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    /* 允许Input、Output，不使用Cache */
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    /* 设置传送的method=POST */
                    con.setRequestMethod("POST");
                    /* setRequestProperty */
                    con.setRequestProperty("Connection", "Keep-Alive");
                    con.setRequestProperty("Charset", "UTF-8");
                    con.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);
                    /* 设置DataOutputStream */
                    DataOutputStream ds =
                            new DataOutputStream(con.getOutputStream());
                    ds.writeBytes(twoHyphens + boundary + end);
                    ds.writeBytes("Content-Disposition: form-data; " +
                            "name=\"file1\";filename=\"" +
                            newName + "\"" + end);
                    ds.writeBytes(end);
                    /* 取得文件的FileInputStream */
                    FileInputStream fStream = new FileInputStream(uploadFile);
                    /* 设置每次写入1024bytes */
                    int bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];
                    int length = -1;
                    /* 从文件读取数据至缓冲区 */
                    while ((length = fStream.read(buffer)) != -1) {
                    /* 将资料写入DataOutputStream中 */
                        ds.write(buffer, 0, length);
                    }
                    ds.writeBytes(end);
                    ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
                    /* close streams */
                    fStream.close();
                    ds.flush();
                    /* 取得Response内容 */
                    InputStream is = con.getInputStream();
                    int ch;
                    StringBuffer b = new StringBuffer();
                    while ((ch = is.read()) != -1) {
                        b.append((char) ch);
                    }
                    Message msg = mHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString(RESPONSE_RESULT, b.toString());
                    msg.what = SUCCESS;
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                    Log.d("UploadPictureWrapper", b.toString());
                    /* 将Response显示于Dialog */
//                  TipDialog.showDialog(mContext, R.string.tip,R.string.sure,"上传成功"+b.toString().trim());
//                  responseResult.success(b.toString().trim());
                    /* 关闭DataOutputStream */
                    ds.close();
                } catch (Exception e) {
                    Message msg = mHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString(RESPONSE_RESULT, e.toString());
                    msg.what = FAILED;
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                    responseResult.failed(e.getMessage().toString());
//              TipDialog.showDialog(mContext, R.string.tip,R.string.sure,"上传失败"+e);
                }
            }
        }).start();
    }

    public interface OnResponseResult {
        void success(String pathUrl);
        void failed(String e);
    }
}
