package com.android.bluetown.mywallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.android.bluetown.MainActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.my.SettingActivity;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.weixin.AddressIPUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author hedi
 * @data:  2016年4月27日 下午4:44:20 
 * @Description:  转账成功
 */
public class TransferSuccessActivity extends TitleBarActivity implements OnClickListener {
	private TextView content;
	private TextView tvmoney;
	private String token;
	private String outGradeNo;
	private String money;
	private String tellphone;
	private String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_transfer_success);
		BlueTownExitHelper.addActivity(this);
		BlueTownExitHelper.addActivity2(this);
		initView();
		initIntent();
	}

	private void initIntent() {
		Intent intent = getIntent();
		int payType = intent.getIntExtra("payType",0);
		userId = intent.getStringExtra("userId");
		tellphone = intent.getStringExtra("tellphone");
		money = intent.getStringExtra("money");
		outGradeNo = intent.getStringExtra("out_trade_no");
		token = intent.getStringExtra("communicationToken");
		if (payType==2){
			checkWXOrderQuery();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.finish:
			if(getIntent().getIntExtra("type", 0)==1){
				TransferSuccessActivity.this.finish();
			}else{
				if (getIntent().getIntExtra("order",0)==3){
					BlueTownExitHelper.exitActivity();
				}else{
					Intent intent = new Intent();
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setClass(TransferSuccessActivity.this, MainActivity.class);
					startActivity(intent);
				}

			}
			
			break;
		}
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setTitleView(getIntent().getStringExtra("title"));
		setTitleLayoutBg(R.color.title_bg_blue);
		leftTextLayout.setVisibility(View.VISIBLE);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}
	private void initView(){
		findViewById(R.id.finish).setOnClickListener(this);
		content=(TextView)findViewById(R.id.content);
		tvmoney=(TextView)findViewById(R.id.money);
		content.setText(getIntent().getStringExtra("title")+"成功");
		if (getIntent().getStringExtra("money").contains("¥")){
			tvmoney.setText(getIntent().getStringExtra("money"));
		}else{
			tvmoney.setText("¥"+getIntent().getStringExtra("money"));
		}
	}

	//微信支付成功调用
	public void checkWXOrderQuery(){
		params.put("ip", AddressIPUtil.getIPAddress(TransferSuccessActivity.this));
		params.put("tradeTypeStr","充值");
		params.put("userId",userId);
		params.put("paymentType","2");
		params.put("amount",money);
		params.put("billStatusStr","充值");
		params.put("phoneNumber", tellphone);
		params.put("out_trade_no",outGradeNo);
		params.put("commodityInformation","微信转入" +money + "元");
		params.put("billStatus","3");
		params.put("communicationToken",token);
		params.put("paymentTypeStr","微信");
		params.put("tradeType","0");
		httpUtil.post(Constant.HOST_URL + Constant.Interface.WX_CHECK_ORDER,params, new AbsHttpStringResponseListener(TransferSuccessActivity.this,null) {
			@Override
			public void onSuccess(int i, String s) {
				JSONObject json ;
				try {
					json = new JSONObject(s);
					String repCode = json.optString("repCode");
					if (repCode.equals(Constant.HTTP_SUCCESS)){
					}else {
						closeBill();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	//充值失败关闭账单
	public void closeBill(){
		params.put("tradeTypeStr","充值");
		params.put("userId",userId);
		params.put("paymentType","2");
		params.put("amount",money);
		params.put("billStatusStr","充值");
		params.put("phoneNumber", tellphone);
		params.put("out_trade_no",outGradeNo);
		params.put("commodityInformation","微信转入" +money + "元");
		params.put("billStatus","3");
		params.put("communicationToken",token);
		params.put("paymentTypeStr","微信");
		params.put("tradeType","0");
		params.put("ip", AddressIPUtil.getIPAddress(TransferSuccessActivity.this));
		httpUtil.post(Constant.HOST_URL + Constant.Interface.CLOSE_BILL,params, new AbsHttpStringResponseListener(TransferSuccessActivity.this,null) {
			@Override
			public void onSuccess(int i, String s) {

			}
		});

	}

}
