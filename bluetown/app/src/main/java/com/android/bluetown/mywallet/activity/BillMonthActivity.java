package com.android.bluetown.mywallet.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.BillMonthAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.MonthBillBean;
import com.android.bluetown.bean.SettingMessageItemBean;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.ParseJSONTools;
import com.android.bluetown.view.HorizontalListView;

/**
 * @author hedi
 * @data:  2016年4月28日 下午4:59:27 
 * @Description:  月账单
 */
public class BillMonthActivity extends TitleBarActivity implements
		OnClickListener ,OnItemClickListener{
	private TextView in,out;
	private TextView shitang_num,textView2;
	private TextView shop_num,textView4;
	private TextView parking_num,textView6;
	private TextView transfer_out_num,textView8;
	private TextView charge_num,charge_amount;
	private TextView refund_num,refund_amount;
	private TextView transfer_in_num,transfer_in_amount;
	private HorizontalListView mListView;
	private List<SettingMessageItemBean> list;
	private List<MonthBillBean> list2;
	private BillMonthAdapter adapter;
	private FinalDb db;
	private String userId;
	private SharePrefUtils prefUtils;
	private ProgressBar pb_progressbar,pb_progressbar2,pb_progressbar3,pb_progressbar4;
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_bill_month);
		BlueTownExitHelper.addActivity(this);
		initView();
		setData();
		getdata(getIntent().getStringExtra("time"));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("月账单");
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}
	private void initView(){
		prefUtils = new SharePrefUtils(this);
		db = FinalDb.create(this);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		mListView=(HorizontalListView)findViewById(R.id.listview);
		pb_progressbar=(ProgressBar)findViewById(R.id.pb_progressbar);
		pb_progressbar2=(ProgressBar)findViewById(R.id.pb_progressbar2);
		pb_progressbar3=(ProgressBar)findViewById(R.id.pb_progressbar3);
		pb_progressbar4=(ProgressBar)findViewById(R.id.pb_progressbar4);
		shitang_num=(TextView)findViewById(R.id.shitang_num);
		textView2=(TextView)findViewById(R.id.textView2);
		shop_num=(TextView)findViewById(R.id.shop_num);
		textView4=(TextView)findViewById(R.id.textView4);
		parking_num=(TextView)findViewById(R.id.parking_num);
		textView6=(TextView)findViewById(R.id.textView6);
		transfer_out_num=(TextView)findViewById(R.id.transfer_out_num);
		textView8=(TextView)findViewById(R.id.textView8);
		charge_num=(TextView)findViewById(R.id.charge_num);
		charge_amount=(TextView)findViewById(R.id.charge_amount);
		refund_num=(TextView)findViewById(R.id.refund_num);
		refund_amount=(TextView)findViewById(R.id.refund_amount);
		transfer_in_num=(TextView)findViewById(R.id.transfer_in_num);
		transfer_in_amount=(TextView)findViewById(R.id.transfer_in_amount);
		in=(TextView)findViewById(R.id.in);
		out=(TextView)findViewById(R.id.out);
		mListView.setOnItemClickListener(this);
	}
	private void setData(){
		list = new ArrayList<SettingMessageItemBean>();
		list2 = new ArrayList<MonthBillBean>();
		Calendar c = Calendar.getInstance();			
		for (int i = 0; i < 5; i++) {
			Date time = c.getTime();
			c.add(Calendar.MONTH,-1);			
			list.add(new SettingMessageItemBean((c.get(Calendar.MONTH)+2)+"月", sf.format(time), "", "",
					""));

		}
		adapter=new BillMonthAdapter(this,list);
		mListView.setAdapter(adapter);
		for(int j= 0;j<5;j++){
			if(getIntent().getStringExtra("time").equals(list.get(j).getUserNick())){
				adapter.setSeclection(j);
			}
		}
		adapter.notifyDataSetChanged();
	}
	private void getdata(String time){		
		in.setText("");
		out.setText("");
		shitang_num.setText("");
		textView2.setText("");
		shop_num.setText("");
		textView4.setText("");
		parking_num.setText("");
		textView6.setText("");
		transfer_out_num.setText("");
		textView8.setText("");
		charge_num.setText("");
		charge_amount.setText("");
		refund_num.setText("");
		refund_amount.setText("");
		transfer_in_num.setText("");
		transfer_in_amount.setText("");
		pb_progressbar.setProgress(0);
		pb_progressbar2.setProgress(0);
		pb_progressbar3.setProgress(0);
		pb_progressbar4.setProgress(0);
		params.put("userId", userId);
		params.put("communicationToken",
				prefUtils.getString(SharePrefUtils.TOKEN, ""));
		params.put("time", time);
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.BillAction_queryBillListByCustomerOfMonth, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						JSONObject json;
						try {
							json = new JSONObject(s);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								String data3 =json.optString("data3");
								String data2 =json.optString("data2");
								JSONArray data1 = json.optJSONArray("data1");
								in.setText("+"+data3);
								out.setText("-"+data2);
								for (int j = 0; j < data1.length(); j++) {
									JSONObject itemObj = data1.optJSONObject(j);
									MonthBillBean mbb=(MonthBillBean) ParseJSONTools.getInstance().fromJsonToJava(itemObj, MonthBillBean.class);
									list2.add(mbb);
								}
								for(int k =0 ; k<list2.size();k++){
									list2.get(k);
									switch (list2.get(k).billStatusStr) {
									case "食堂消费":
										shitang_num.setText(list2.get(k).num+"笔");
										textView2.setText(list2.get(k).amount+"元");
										pb_progressbar.setProgress((int)(Double.parseDouble(list2.get(k).amount)/Double.parseDouble(data2)*100));
										break;
									case "商户消费":
										shop_num.setText(list2.get(k).num+"笔");
										textView4.setText(list2.get(k).amount+"元");
										pb_progressbar2.setProgress((int)(Double.parseDouble(list2.get(k).amount)/Double.parseDouble(data2)*100));
										break;
									case "停车费":
										parking_num.setText(list2.get(k).num+"笔");
										textView6.setText(list2.get(k).amount+"元");
										pb_progressbar3.setProgress((int)(Double.parseDouble(list2.get(k).amount)/Double.parseDouble(data2)*100));
										break;
									case "转账":
										if(list2.get(k).tranTypeStr.equals("支出")){
											transfer_out_num.setText(list2.get(k).num+"笔");
											textView8.setText(list2.get(k).amount+"元");
											pb_progressbar4.setProgress((int)(Double.parseDouble(list2.get(k).amount)/Double.parseDouble(data2)*100));
										}else{
											transfer_in_num.setText(list2.get(k).num+"笔");
											transfer_in_amount.setText(list2.get(k).amount+"元");
										}										
										break;
									case "充值":
										charge_num.setText(list2.get(k).num+"笔");
										charge_amount.setText(list2.get(k).amount+"元");
										break;
									case "退款":
										refund_num.setText(list2.get(k).num+"笔");
										refund_amount.setText(list2.get(k).amount+"元");
										break;

									default:
										break;
									}
								}
								
							} else {
								Toast.makeText(
										BillMonthActivity.this,
										json.optString("repMsg"),
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		list2.clear();
		adapter.setSeclection(position);
		getdata(adapter.gettime(position));
		adapter.notifyDataSetChanged();
	}

}
