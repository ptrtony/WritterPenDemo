package com.android.bluetown.mywallet.activity;

import java.util.List;

import net.tsz.afinal.FinalDb;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bluetown.HomeActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.home.main.model.act.ParkingConfirmOrderActivity;
import com.android.bluetown.home.main.model.act.ParkingPayOrderActivity;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;

/**
 * @author hedi
 * @data:  2016年4月27日 下午1:03:39 
 * @Description:  转账
 */
public class TransferActivity extends TitleBarActivity implements OnClickListener{
	private EditText account;
	private TextView next;
	private TextView cant_next;
	private FinalDb db;
	private String userId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_transfer);
		BlueTownExitHelper.addActivity(this);
		initView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.next:
			getaccount();
			break;
		case R.id.rightTextLayout:
			startActivity(new Intent(TransferActivity.this,
					TransferHistoryActivity.class));
			break;
		}
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("转账");
		setTitleLayoutBg(R.color.title_bg_blue);
		setRighTextView("转账记录");
		righTextLayout.setOnClickListener(this);
	}
	private void initView(){
		account=(EditText)findViewById(R.id.account);
		next=(TextView)findViewById(R.id.next);
		cant_next=(TextView)findViewById(R.id.cant_next);
		next.setOnClickListener(this);
		db = FinalDb.create(this);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		account.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					next.setVisibility(View.GONE);
					cant_next.setVisibility(View.VISIBLE);
				} else {
					next.setVisibility(View.VISIBLE);
					cant_next.setVisibility(View.GONE);
				}
			}
		});
	}
	private void getaccount() {
		// TODO Auto-generated method stub
		params.put("userId", userId);
		params.put("telphone", account.getText().toString());
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.MobiMemberAction_getMemberUserByAccount, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						JSONObject json;
						try {
							json = new JSONObject(s);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								JSONObject data=json.optJSONObject("data");
								Intent intent = (new Intent(
										TransferActivity.this,
										TransferCountActivity.class));
								intent.putExtra("userId", data.optString("userId"));
								intent.putExtra("headImg", data.optString("headImg"));
								intent.putExtra("telphone", data.optString("telphone"));
								intent.putExtra("nickName", data.optString("nickName"));
								intent.putExtra("balance", getIntent().getStringExtra("balance"));
								startActivity(intent);
							} else {
								Toast.makeText(TransferActivity.this,
										json.optString("repMsg"),
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

	}
}
