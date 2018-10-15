package com.android.bluetown.home.main.model.act;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.tsz.afinal.FinalDb;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.ReplacementTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelChangedListener;
import antistatic.spinnerwheel.OnWheelScrollListener;
import antistatic.spinnerwheel.adapters.AbstractWheelTextAdapter;
import com.ab.util.AbJsonUtil;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.RemindCarport;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.datewheel.CanAppointTimePickerDialog;
import com.android.bluetown.datewheel.PersonPickerDialog;
import com.android.bluetown.jsbridge.BridgeWebView;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.RemindCarportResult;
import com.android.bluetown.result.Result;
import com.android.bluetown.spinner.custom.AbstractSpinerAdapter;
import com.android.bluetown.spinner.custom.SpinerPopWindow;
import com.android.bluetown.utils.AllCapTransformationMethod;
import com.android.bluetown.utils.CheckUtil;
import com.android.bluetown.utils.Constant;
/**
 * @author hedi
 * @data: 2016年4月28日 下午4:57:22
 * @Description: 访客预约
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class GuestAppointmentActivity extends TitleBarActivity implements OnClickListener {
	private LinearLayout ll_carnum;
	private LinearLayout dealDateLy;
	private TextView canApponitDate;
	private EditText tv_company, tv_company2, tv_company3, tv_company4,
			tv_company5, tv_company6, faultContent;
	private TextView tv_time, remindCarport;
	private Button appointCommit;
	private String carCount;
	private String userId;
	private FinalDb db;
	private String[] str;
	private List<MemberUser> users;
	private PersonPickerRecevier recevier;
	private List<String> mParkNoList;
	private AbstractWheel wheel;
	private SpinerPopWindow mParkNoPop;
	private List<RemindCarport> typesList;
	private String parkLotNotId;
	private String vdate, mid;
	private Resources resource;
	private RemindCarportResult result;
	private SharePrefUtils prefUtils;
	private boolean wheelScrolled = false;
	private DayArrayAdapter dayAdapter;
	private TextView appointinfo;


	private BridgeWebView guestWebView;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					TipDialog.showDialogStartNewActivity(
							GuestAppointmentActivity.this, R.string.tip,
							R.string.confirm, R.string.appoint_success,
							GuestAppointmentHistoryActivity.class);
					break;
				case 2:
					TipDialog.showDialog(GuestAppointmentActivity.this,
							R.string.tip, R.string.confirm,
							R.string.un_apponit_carport);
					break;
				case 3:

					TipDialog.showDialog(GuestAppointmentActivity.this,
							R.string.tip, R.string.confirm, (String) (msg.obj));
					break;
				default:
					break;
			}
		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_guest_appoint);
		BlueTownExitHelper.addActivity(this);
		registerPersonRecevier();
		initView();
	}

	/**
	 * 注册设置园区的广播
	 */
	private void registerPersonRecevier() {
		recevier = new PersonPickerRecevier();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.guestcount.choice.action");
		filter.addAction("android.guesttime.choice.action");
		registerReceiver(recevier, filter);
	}

	private void initView() {
		prefUtils = new SharePrefUtils(this);
		db = FinalDb.create(this);
		resource = this.getResources();
		mParkNoList = new ArrayList<String>();
		ll_carnum = (LinearLayout) findViewById(R.id.ll_carnum);
		dealDateLy = (LinearLayout) findViewById(R.id.dealDateLy);
		dealDateLy.setOnClickListener(this);
		tv_company = (EditText) findViewById(R.id.tv_company);
		tv_company.setTransformationMethod(new AllCapTransformationMethod());
		tv_company2 = (EditText) findViewById(R.id.tv_company2);
		tv_company3 = (EditText) findViewById(R.id.tv_company3);
		tv_company4 = (EditText) findViewById(R.id.tv_company4);
		tv_company5 = (EditText) findViewById(R.id.tv_company5);
		tv_company6 = (EditText) findViewById(R.id.tv_company6);
		tv_time = (TextView) findViewById(R.id.tv_time);
		faultContent = (EditText) findViewById(R.id.faultContent);
		canApponitDate = (TextView) findViewById(R.id.canApponitDate);
		remindCarport = (TextView) findViewById(R.id.remindCarport);
		appointinfo = (TextView) findViewById(R.id.appointinfo);
		appointCommit = (Button) findViewById(R.id.appointCommit);
		appointinfo.setText(prefUtils
				.getString(SharePrefUtils.APPOINT_INFO, ""));
		switch (prefUtils.getString(SharePrefUtils.APPOINT_CARNUM, "")) {
			case "1":

				break;
			case "2":
				tv_company4.setVisibility(View.VISIBLE);
				break;
			case "3":
				ll_carnum.setVisibility(View.VISIBLE);
				tv_company4.setVisibility(View.VISIBLE);
				tv_company5.setVisibility(View.VISIBLE);
				break;
			case "4":
				ll_carnum.setVisibility(View.VISIBLE);
				tv_company4.setVisibility(View.VISIBLE);
				tv_company5.setVisibility(View.VISIBLE);
				tv_company6.setVisibility(View.VISIBLE);
				break;

			default:
				break;
		}
		appointCommit.setOnClickListener(this);
		wheel = (AbstractWheel) findViewById(R.id.people);
		str = new String[] { "1", "2", "3", "4", "5", "6", "7" };
		dayAdapter = new DayArrayAdapter(this, str);
		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
		wheel.setViewAdapter(dayAdapter);
		tv_company3.setTransformationMethod(new InputLowerToUpper());
		canApponitDate.setText("提示：预约只能提前" + getIntent().getStringExtra("vdate") + "小时进行");
	}

	public class InputLowerToUpper extends ReplacementTransformationMethod{
		@Override
		protected char[] getOriginal() {
			char[] lower = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z' };
			return lower;
		}

		@Override
		protected char[] getReplacement() {
			char[] upper = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };
			return upper;
		}

	}

	// Wheel scrolled flag

	// Wheel scrolled listener
	OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
		public void onScrollingStarted(AbstractWheel wheel) {
			wheelScrolled = true;
		}

		public void onScrollingFinished(AbstractWheel wheel) {
			wheelScrolled = false;
			wheel.setViewAdapter(dayAdapter);
		}
	};

	// Wheel changed listener
	private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
		public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
			if (!wheelScrolled) {
				wheel.setViewAdapter(dayAdapter);
			}
		}
	};

	private AbstractWheel getWheel(int id) {
		return (AbstractWheel) findViewById(id);
	}

	private class DayArrayAdapter extends AbstractWheelTextAdapter {
		private String[] str;

		/**
		 * Constructor
		 */
		protected DayArrayAdapter(Context context, String[] data) {
			super(context, R.layout.wheel_text_centered, NO_RESOURCE);
			str = data;
			setItemTextResource(R.id.text);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			TextView text = (TextView) view.findViewById(R.id.text);
			text.setText(str[index]);
			if (str.length == 15) {
				if (index == getWheel(R.id.people).getCurrentItem()) {
					text.setTextColor(0xff4a90fb);
				}
			}

			return view;
		}

		@Override
		public int getItemsCount() {
			return str.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return "";
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getRemindCarport();
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		if (!TextUtils.isEmpty(userId)) {
			appointCommit.setClickable(true);
			appointCommit.setBackground(getResources().getDrawable(
					R.drawable.blue_darker_btn_bg));
		} else {
			appointCommit.setClickable(false);
			appointCommit.setBackground(getResources().getDrawable(
					R.drawable.gray_btn_bg1));
			TipDialog.showDialogNoClose(GuestAppointmentActivity.this,
					R.string.tip, R.string.confirm, R.string.login_info_tip,
					LoginActivity.class);
		}
	}

	private void getRemindCarport() {
		// TODO Auto-generated method stub

		params.put("gardenId",
				prefUtils.getString(SharePrefUtils.GARDEN_ID, ""));
		httpInstance.post(
				Constant.HOST_URL + Constant.Interface.REMIND_CARPORT, params,
				new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int arg0, String arg1) {
						result = (RemindCarportResult) AbJsonUtil.fromJson(
								arg1, RemindCarportResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							if (typesList != null) {
								typesList.clear();
							}
							if (mParkNoList != null) {
								mParkNoList.clear();
							}
							typesList = result.getData().getStallList();
							if (typesList != null && typesList.size() > 0) {
								for (int i = 0; i < typesList.size(); i++) {
									if (typesList.get(i) != null) {
										mParkNoList.add(typesList.get(i)
												.getRegion());
									}

								}
								// initParkNoType();
								// 默认显示园区第一个停车场的可预约车位
								carCount = typesList.get(0)
										.getRemainingSpaces();
								parkLotNotId = typesList.get(0)
										.getParkingLotNo();
								vdate = typesList.get(0).getVdate();
								mid = typesList.get(0).getMid();
								BlueTownApp.CANAPPOINT = vdate;
								showCurrentParkLotState();

							} else {
								TipDialog.showDialog(
										GuestAppointmentActivity.this,
										R.string.tip, R.string.confirm,
										result.getRepMsg());
							}
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							Toast.makeText(GuestAppointmentActivity.this,
									result.getRepMsg(), Toast.LENGTH_SHORT)
									.show();
						} else {
							TipDialog.showDialog(GuestAppointmentActivity.this,
									R.string.tip, R.string.confirm,
									result.getRepMsg());
						}
					}

					@Override
					public void onFailure(int i, String s, Throwable throwable) {
						// TODO Auto-generated method stub
						super.onFailure(i, s, throwable);
						TipDialog.showDialog(GuestAppointmentActivity.this,
								R.string.tip, R.string.confirm, s);
					}
				});
	}

	/**
	 * 判断当前停车场的
	 *
	 * @param result
	 */
	private void showCurrentParkLotState() {
		if (!TextUtils.isEmpty(carCount)) {
			int cars = Integer.parseInt(carCount);
			if (cars < 0) {
				TipDialog.showDialog(GuestAppointmentActivity.this,
						R.string.tip, R.string.confirm, result.getRepMsg());
			} else if (cars == 0) {
				TipDialog.showDialog(GuestAppointmentActivity.this,
						R.string.tip, R.string.confirm, result.getRepMsg());
			} else {
				canApponitDate.setText("提示：预约只能提前" + vdate + "小时进行");
				String tip = "可预约剩余车位为：" + carCount;
				SpannableString mSpannableString = new SpannableString(tip);
				int start = tip.indexOf("：");
				mSpannableString.setSpan(
						new ForegroundColorSpan(resource
								.getColor(R.color.red_btn_bg_color)),
						start + 1, tip.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				remindCarport.setText(mSpannableString);
			}
		}
	}

	/**
	 *
	 * @Title: initTitle
	 * @Description: 初始化标题栏
	 * @see com.android.bluetown.activity.TitleBarActivity#initTitle()
	 */
	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("访客预约");
		setMainLayoutBg(R.color.title_bg_blue);
		setRightImageView(R.drawable.ic_history);
		rightImageLayout.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rightImageLayout:
				if (!TextUtils.isEmpty(userId)) {
					startActivity(new Intent(GuestAppointmentActivity.this,
							GuestAppointmentHistoryActivity.class));
				} else {
					TipDialog.showDialogNoClose(GuestAppointmentActivity.this,
							R.string.tip, R.string.confirm,
							R.string.login_info_tip, LoginActivity.class);
				}
				break;
			case R.id.dealDateLy:
				// 可预约访客时间
				CanAppointTimePickerDialog appointDialog = new CanAppointTimePickerDialog(
						GuestAppointmentActivity.this,getIntent().getStringExtra("vdate"));
				appointDialog.show();
				break;
		/*
		 * case R.id.parkingLotNo: showSpinWindow(mParkNoPop, parkingLotNo);
		 * break;
		 */
			case R.id.tv_company1:
				// 获取访客人数
				PersonPickerDialog personDialog = new PersonPickerDialog(
						GuestAppointmentActivity.this);
				personDialog.show();
				break;
			case R.id.appointCommit:

				if (!TextUtils.isEmpty(userId)) {
					appointCommit.setClickable(true);
					appointCommit.setBackground(getResources().getDrawable(
							R.drawable.blue_darker_btn_bg));
					String busNumber = tv_company.getText().toString();
					String visitorCount = str[wheel.getCurrentItem()];
					String mname = tv_company2.getText().toString();
					String tell = tv_company3.getText().toString();
					String makingTime = tv_time.getText().toString();
					String faultContentStr = faultContent.getText().toString();
					String parkingLotNoStr = getIntent().getStringExtra(
							"gardenZone");
					// String ownerNameStr = ownerName.getText().toString();
					SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");// 设置日期格式
					String createTime = df.format(new Date());
					if (TextUtils.isEmpty(busNumber)
							|| TextUtils.isEmpty(visitorCount)
							|| TextUtils.isEmpty(mname) || TextUtils.isEmpty(tell)
							|| TextUtils.isEmpty(makingTime)
							|| TextUtils.isEmpty(createTime)
							|| TextUtils.isEmpty(parkingLotNoStr)) {
						TipDialog.showDialog(GuestAppointmentActivity.this,
								R.string.tip, R.string.confirm,
								R.string.appoint_into_tip);
						return;
					}

					if (tell.length() < 11) {
						TipDialog.showDialog(GuestAppointmentActivity.this,
								R.string.tip, R.string.confirm,
								R.string.phone_error);
						return;
					}

					if (!isTelPhoto(tell)) {
						TipDialog.showDialog(GuestAppointmentActivity.this,
								R.string.tip, R.string.confirm,
								R.string.error_phone);
						return;
					}

					if (!isCarNum(busNumber)) {
						TipDialog.showDialog(GuestAppointmentActivity.this,
								R.string.tip, R.string.confirm,
								R.string.car_num_tip);
						return;
					}

					if (!checkNum(visitorCount)) {
						TipDialog.showDialog(GuestAppointmentActivity.this,
								R.string.tip, R.string.confirm,
								R.string.visitor_count_tip);
						return;
					}
					getGuestAppoint(busNumber, visitorCount, mname, tell,
							makingTime, createTime, faultContentStr,
							parkingLotNoStr);
				} else {
					appointCommit.setClickable(false);
					appointCommit.setBackground(getResources().getDrawable(
							R.drawable.gray_btn_bg1));
					TipDialog.showDialogNoClose(GuestAppointmentActivity.this,
							R.string.tip, R.string.confirm,
							R.string.login_info_tip, LoginActivity.class);
				}
				break;
			default:
				break;
		}

	}

	private void getGuestAppoint(final String busNumber,
								 final String visitorCount, final String mname, final String tell,
								 final String makingTime, final String createTime,
								 final String faultContentStr, String parkingLotNoStr) {
		/*
		 * 车牌号码：busNumber(必填) 访客人数：visitorCount(必填) 访客姓名：mname(必填) 联系方式：tell(必填)
		 * 访客时间：makingTime(必填) 创建时间：createTime(必填) 用户Id：userId(必填)
		 * 停车场编号：parkingLotNo(必填) 车主姓名：ownerName(必填) 备注：remark
		 */
		params.put("busNumber", CheckUtil.exChange(busNumber));
		params.put("visitorCount", visitorCount);
		params.put("mname", mname);
		params.put("tell", tell);
		params.put("makingTime", makingTime);
		params.put("createTime", createTime);
		params.put("userId", userId);

		params.put("parkingLotNo", getIntent().getStringExtra("parkingLotNo"));

		// params.put("ownerName", ownerNameStr);
		params.put("remark", faultContentStr);
		params.put("mid", getIntent().getStringExtra("mid"));

		httpInstance.post(Constant.HOST_URL
						+ Constant.Interface.ADD_GUEST_APPOINT, params,
				new AbsHttpStringResponseListener(this, null) {

					@Override
					public void onSuccess(int arg0, String arg1) {
						Result result = (Result) AbJsonUtil.fromJson(arg1,
								Result.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							TipDialog.showDialogStartNewActivity(
									GuestAppointmentActivity.this,
									R.string.tip, R.string.confirm,
									R.string.appoint_success,
									GuestAppointmentHistoryActivity.class);
						} else if (result.getRepCode().contains(
								Constant.HTTP_ERROR)) {
							if (result.getRepMsg().contains("失败")) {
								TipDialog.showDialogStartNewActivity(
										GuestAppointmentActivity.this,
										R.string.tip, R.string.confirm,
										R.string.appoint_success,
										GuestAppointmentHistoryActivity.class);
							} else {
								TipDialog.showDialog(
										GuestAppointmentActivity.this,
										R.string.tip, R.string.confirm,
										result.getRepMsg());
							}
						}
					}
				});
	}

	private boolean isCarNum(String carNum) {
		Pattern pattern = Pattern
				.compile("^[\u4eac\u6d25\u5180\u664b\u8499\u8fbd\u5409\u9ed1\u6caa\u82cf\u6d59\u7696\u95fd\u8d63\u9c81\u8c6b\u9102\u6e58\u7ca4\u6842\u743c\u6e1d\u5ddd\u8d35\u4e91\u85cf\u9655\u7518\u9752\u5b81\u65b0\u5b66]{1}[A-Za-z]{1}[A-Za-z_0-9]{4}[A-Za-z_0-9_\u4e00-\u9fa5]$");
		Matcher matcher = pattern.matcher(carNum);
		if (!matcher.matches()) {
			System.out.println("车牌号格式不对！");
			return false;
		} else {
			System.out.println("车牌号格式正确！");
			return true;
		}
	}

	/**
	 *
	 * @Title: isTelPhoto
	 * @Description: TODO(判断电话号码是否有效)
	 * @param mobiles
	 *            电话号码
	 * @return
	 * @throws
	 */
	private boolean isTelPhoto(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 数字校验
	 *
	 * @param date
	 * @return
	 */
	private boolean checkNum(String visitorCount) {
		Pattern p = Pattern.compile("^[0-9]*$");
		Matcher m = p.matcher(visitorCount);
		return m.matches();

	}

	/*
	 * private void initParkNoType() { mParkNoPop = new SpinerPopWindow(this,
	 * parkingLotNo); mParkNoPop.refreshData(mParkNoList, 0);
	 * mParkNoPop.setItemListener(new onSipnnerItemClick(mParkNoList,
	 * parkingLotNo));
	 *
	 * }
	 *
	 * private void showSpinWindow(SpinerPopWindow mFaultTypePop, TextView
	 * faultType2) { // TODO Auto-generated method stub
	 * mFaultTypePop.setWidth(faultType2.getWidth());
	 * mFaultTypePop.showAsDropDown(faultType2); }
	 */

	/**
	 *
	 * @ClassName: onSipnnerItemClick
	 * @Description:TODO(这里用一句话描述这个类的作用)
	 * @author: shenyz
	 * @date: 2015年8月6日 下午2:59:33
	 *
	 */
	class onSipnnerItemClick implements
			AbstractSpinerAdapter.IOnItemSelectListener {
		private List<String> data;
		private TextView textView;

		public onSipnnerItemClick() {
			// TODO Auto-generated constructor stub
		}

		public onSipnnerItemClick(List<String> data, TextView textView) {
			// TODO Auto-generated constructor stub
			this.data = data;
			this.textView = textView;
		}

		@Override
		public void onItemClick(int pos) {
			// TODO Auto-generated method stub
			setItemData(data, textView, pos);
		}

	}

	private void setItemData(List<String> data, TextView textView, int pos) {
		if (pos >= 0 && pos <= data.size()) {
			String value = data.get(pos);
			textView.setText(value);
			parkLotNotId = typesList.get(pos).getParkingLotNo() + "";
			carCount = typesList.get(pos).getRemainingSpaces();
			vdate = typesList.get(pos).getVdate();
			mid = typesList.get(pos).getMid();
			BlueTownApp.CANAPPOINT = vdate;
			showCurrentParkLotState();
		}
	}

	private class PersonPickerRecevier extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals("android.guesttime.choice.action")) {
				String appointTime = intent.getStringExtra("appointTime");
				tv_time.setText(appointTime);
			}
		}
	}

}
