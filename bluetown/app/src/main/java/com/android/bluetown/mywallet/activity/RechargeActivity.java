package com.android.bluetown.mywallet.activity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalDb;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.alipay.AlipayUtil;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.listener.AliPayListener;
import com.android.bluetown.listener.WXPayListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.PreWXPayResult;
import com.android.bluetown.result.UserInfoResult;
import com.android.bluetown.surround.OnlinePayActivity;
import com.android.bluetown.surround.OnlinePaySuccessActivity;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.EditInputFilter;
import com.android.bluetown.weixin.AddressIPUtil;
import com.android.bluetown.weixin.WXPayUtil;
import com.android.bluetown.wxapi.WXPayEntryActivity;

/**
 * @author hedi
 * @data: 2016年4月29日 下午1:57:02
 * @Description: 充值
 */
public class RechargeActivity extends TitleBarActivity implements
         AliPayListener, RadioGroup.OnCheckedChangeListener, OnClickListener {
    private EditText recharge;
    private TextView confirm, cant_confirm;
    private RadioGroup mRgRecharge;
    private RadioButton mAlipay, mWeChartPay;
    private FinalDb db;
    private String userId;
    private String token;
    private String telephone;
    private SharePrefUtils prefUtils;
    private int mRechargeType = 1;//1.支付宝 2.微信
    private PreWXPayResult result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addContentView(R.layout.ac_recharge);
        BlueTownExitHelper.addActivity(this);
        BlueTownExitHelper.addActivity2(this);
        initView();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.confirm:
                confirm.setClickable(false);
                if (mRechargeType == 1) {
                    alipayCharge();
                } else if (mRechargeType == 2) {
                    wxpayCharge();
                }

                break;
        }
    }

    @Override
    public void initTitle() {
        // TODO Auto-generated method stub
        setBackImageView();
        setTitleView("充值");
        setTitleLayoutBg(R.color.title_bg_blue);
        rightImageLayout.setVisibility(View.INVISIBLE);
    }

    private void initView() {
        recharge = (EditText) findViewById(R.id.recharge_count);
        confirm = (TextView) findViewById(R.id.confirm);
        cant_confirm = (TextView) findViewById(R.id.cant_confirm);
        mRgRecharge = findViewById(R.id.rb_recharge);
        mAlipay = findViewById(R.id.alipay);
        mWeChartPay = findViewById(R.id.weixin_pay);
        confirm.setOnClickListener(this);
        mRgRecharge.setOnCheckedChangeListener(this);
        prefUtils = new SharePrefUtils(this);
        db = FinalDb.create(RechargeActivity.this);
        List<MemberUser> users = db.findAll(MemberUser.class);
        if (users != null && users.size() != 0) {
            MemberUser user = users.get(0);
            if (user != null) {
                token = prefUtils.getString(SharePrefUtils.TOKEN, "");
                userId = user.getMemberId();
                telephone = user.getUsername();
            }
        }
        InputFilter[] filters = {new EditInputFilter(1000000000)};
        recharge.setFilters(filters);
        recharge.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        recharge.setText(s);
                        recharge.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    recharge.setText(s);
                    recharge.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        recharge.setText(s.subSequence(0, 1));
                        recharge.setSelection(1);
                        return;
                    }
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    confirm.setVisibility(View.GONE);
                    cant_confirm.setVisibility(View.VISIBLE);
                } else {
                    confirm.setVisibility(View.VISIBLE);
                    cant_confirm.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 充值支付宝下订单
     */
    private void alipayCharge() {
        // TODO Auto-generated method stub
        params.put("userId", userId);
        params.put("communicationToken", token);
        params.put("tradeType", "0");
        params.put("tradeTypeStr", "充值");
        params.put("paymentType", "1");
        params.put("paymentTypeStr", "支付宝");
        params.put("amonut", (recharge.getText().toString()));
        params.put("phoneNumber", telephone);
        params.put("billStatus", "3");
        params.put("billStatusStr", "充值");
        params.put("commodityInformation", "支付宝转入" + recharge.getText().toString() + "元");
        httpInstance.post(Constant.HOST_URL + Constant.Interface.BillAction_add,
                params, new AbsHttpStringResponseListener(this, null) {
                    @Override
                    public void onSuccess(int i, String s) {
                        JSONObject json;
                        try {
                            json = new JSONObject(s);
                            String repCode = json.optString("repCode");
                            if (repCode.equals(Constant.HTTP_SUCCESS)) {
                                JSONObject data = new JSONObject(json
                                        .optString("data"));

                                AlipayUtil alipayUtil = new AlipayUtil(RechargeActivity.this,
                                        data.optString("transactionNumber"), "0", data.optString("bid"));
                                alipayUtil.pay(getResources().getString(R.string.app_name),
                                        data.optString("transactionNumber"), Double.parseDouble(recharge.getText().toString()));


                            } else {
                                Toast.makeText(RechargeActivity.this,
                                        json.optString("repMsg"),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        confirm.setClickable(true);
                    }
                });

    }

    /**
     * 充值微信下订单
     */
    private void wxpayCharge() {
        params.put("userId", userId);
        params.put("communicationToken", token);
        params.put("tradeType", "0");
        params.put("tradeTypeStr", "充值");
        params.put("paymentType", "2");
        params.put("paymentTypeStr", "微信");
        params.put("amonut", (recharge.getText().toString()));
        params.put("phoneNumber", telephone);
        params.put("billStatus", "3");
        params.put("billStatusStr", "充值");
        params.put("commodityInformation", "微信转入" + recharge.getText().toString() + "元");
        httpInstance.post(Constant.HOST_URL + Constant.Interface.BILLACTION_ADD_WEIXIN,
                params, new AbsHttpStringResponseListener(this, null) {
                    @Override
                    public void onSuccess(int i, String s) {
                        JSONObject json;
                        try {
                            json = new JSONObject(s);
                            String repCode = json.optString("repCode");
                            if (repCode.equals(Constant.HTTP_SUCCESS)) {
                                JSONObject data = new JSONObject(json
                                        .optString("data"));
                                result = new PreWXPayResult();
                                result.setmAppId(data.optString("appid"));
                                result.setmNoncestr(data.optString("noncestr"));
                                result.setmOutTradeNo(data.optString("outTradeNo"));
                                result.setmPackageStr(data.optString("packageStr"));
                                result.setmPartnerid(data.optString("partnerid"));
                                result.setmPrepayid(data.optString("prepayid"));
                                result.setmSign(data.optString("sign"));
                                result.setmTimestamp(data.optString("timestamp"));
                                SharePrefUtils sharePrefUtils = new SharePrefUtils(RechargeActivity.this);
                                sharePrefUtils.setString("appid",data.optString("appid"));
                                WXPayUtil wxPayUtil = new WXPayUtil(RechargeActivity.this, result);
                                wxPayUtil.pay();
                            } else {
                                Toast.makeText(RechargeActivity.this,
                                        json.optString("repMsg"),
                                        Toast.LENGTH_SHORT).show();
                            }
                            confirm.setClickable(true);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int i, String s, Throwable throwable) {
                        super.onFailure(i, s, throwable);
                        Toast.makeText(RechargeActivity.this, s, Toast.LENGTH_SHORT).show();
                        confirm.setClickable(true);
                    }
                });

    }

    @Override
    public void paySuccess() {
        if (mRechargeType==1){
            setIntent(1);
        }else{
           setIntent(2);
        }
        confirm.setClickable(true);
    }

    @Override
    public void payFaild() {
        // TODO Auto-generated method stub
        if (mRechargeType==2){
            closeBill();
        }
        Toast.makeText(this, "支付失败！", Toast.LENGTH_SHORT).show();
        confirm.setClickable(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.alipay:
                mRechargeType = 1;
                mAlipay.setChecked(true);
                mWeChartPay.setChecked(false);
                break;
            case R.id.weixin_pay:
                mRechargeType = 2;
                mAlipay.setChecked(false);
                mWeChartPay.setChecked(true);
                break;
        }
    }

    //充值失败关闭账单
    public void closeBill(){
        params.put("tradeTypeStr","充值");
        params.put("userId",userId);
        params.put("paymentType","2");
        params.put("amount",(recharge.getText().toString()));
        params.put("billStatusStr","充值");
        params.put("phoneNumber", telephone);
        params.put("out_trade_no",result.getmOutTradeNo());
        params.put("commodityInformation","微信转入" + recharge.getText().toString() + "元");
        params.put("billStatus","3");
        params.put("communicationToken",token);
        params.put("paymentTypeStr","微信");
        params.put("tradeType","0");
        params.put("ip", AddressIPUtil.getIPAddress(RechargeActivity.this));
        httpUtil.post(Constant.HOST_URL + Constant.Interface.CLOSE_BILL,params, new AbsHttpStringResponseListener(RechargeActivity.this,null) {
            @Override
            public void onSuccess(int i, String s) {
            }
        });

    }


    public void setIntent(int payType){
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        if (getIntent().getIntExtra("type", 1) == 0) {
        } else {
            if (getIntent().getIntExtra("type",0)==3){
                intent.putExtra("order",3);
            }
            intent.setClass(RechargeActivity.this, TransferSuccessActivity.class);
            intent.putExtra("title", "充值");
            intent.putExtra("type", 1);
            intent.putExtra("payType",payType);
            intent.putExtra("userId",userId);
            intent.putExtra("communicationToken",token);
            intent.putExtra("tellphone",telephone);
            if (payType==2){
                intent.putExtra("out_trade_no",result.getmOutTradeNo());
            }
            intent.putExtra("money",recharge.getText().toString());
            startActivity(intent);
        }
        finish();
        Toast.makeText(RechargeActivity.this, "支付成功！", Toast.LENGTH_SHORT).show();
    }


}
