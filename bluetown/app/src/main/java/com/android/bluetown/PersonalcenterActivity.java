package com.android.bluetown;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.tsz.afinal.FinalDb;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbDialogUtil;
import com.ab.util.AbFileUtil;
import com.ab.util.AbJsonUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.datewheel.TimeChoiceDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.Result;
import com.android.bluetown.result.UserInfoResult;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.view.RoundedImageView;

public class PersonalcenterActivity extends TitleBarActivity implements
		OnClickListener {
	private TextView mUserBirthday, companyName, gardenName;
	private EditText mail, userName, userNick;
	private TextView tel;
	private RoundedImageView userImg;
	private ImageView userSex;
	private View avatarView;
	/* 用来标识请求照相功能的activity */
	private static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	/* 用来标识请求裁剪图片后的activity */
	private static final int CAMERA_CROP_DATA = 3022;

	private File mCurrentPhotoFile;
	private String mFileName;
	private File PHOTO_DIR = null;
	private String birthday;
	private String headImg;
	private DateRecevier receiver;
	private boolean isModifyImg;
	private int gender = 0;
	private SharePrefUtils prefUtils;
	private String userId, username, companyNameStr,gardenNameStr;
	private int userType;
	private FinalDb db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.activity_personalcenter);
		BlueTownExitHelper.addActivity(this);
		initView();
		initFolderDir();
		IntentFilter filter = new IntentFilter();
		filter.addAction("date_choice");
		receiver = new DateRecevier();
		registerReceiver(receiver, filter);
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBgRes(R.drawable.other_top_bg1);
		setTitleView(R.string.person_infos);
		rightImageLayout.setVisibility(View.INVISIBLE);

	}

	private void initView() {
	
		mUserBirthday = (TextView) findViewById(R.id.tv_userBirthday);
		tel = (TextView) findViewById(R.id.tel);
		gardenName = (TextView) findViewById(R.id.gardenName);
		companyName = (TextView) findViewById(R.id.companyName);
	
		mail = (EditText) findViewById(R.id.mail);
		userName = (EditText) findViewById(R.id.userName);
		userNick = (EditText) findViewById(R.id.userNick);
		userSex = (ImageView) findViewById(R.id.userSex);
		userSex.setOnClickListener(this);
		findViewById(R.id.confirm).setOnClickListener(this);
		mUserBirthday.setOnClickListener(this);
		userImg = (RoundedImageView) findViewById(R.id.userImg);
		userImg.setOnClickListener(this);
	
		db = FinalDb.create(PersonalcenterActivity.this);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() > 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
				userType = user.getUserType();
				companyNameStr = user.getCompanyName();
				username = user.getUsername();
				gardenNameStr=user.getHotRegion();
			}

		}
		initData();

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		gardenName.setText(gardenNameStr);
		if (userType == Constant.COMPANY_USER) {
			companyName.setVisibility(View.VISIBLE);
			companyName.setText(companyNameStr);
		} else if (userType == Constant.NORMAL_USER) {
			companyName.setVisibility(View.GONE);
		}
		com.android.bluetown.bean.UserInfo userInfo = (com.android.bluetown.bean.UserInfo) getIntent()
				.getSerializableExtra("userInfo");

		if (userInfo != null) {
			mail.setText(userInfo.getEmail());
			userName.setText(userInfo.getName());
			userNick.setText(userInfo.getNickName());
			mUserBirthday.setText(userInfo.getBirthday());
			gender = Integer.parseInt(userInfo.getSex());
			if (userInfo.getHeadImg() != null
					&& !"".equals(userInfo.getHeadImg())) {
				finalBitmap.display(userImg, userInfo.getHeadImg());
			} else {
				userImg.setImageResource(R.drawable.ic_msg_empty);
			}
			headImg = userInfo.getHeadImg();

			if (userInfo.getSex().equals("1")) {
				userSex.setImageResource(R.drawable.ic_sex);
			} else {
				userSex.setImageResource(R.drawable.ic_sex_man);
			}
			String account = userInfo.getTelphone();
			if (!TextUtils.isEmpty(account)) {
				tel.setText(account);
			} else {
				tel.setText(userId);
			}

		}
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	/**
	 * 
	 * @Title: isMail
	 * @Description: TODO(判断邮箱地址是否有效)
	 * @param mails
	 *            邮箱地址校验
	 * @return
	 * @throws
	 */
	private boolean isMail(String mails) {
		String rex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern p = Pattern.compile(rex);
		Matcher m = p.matcher(mails);
		return m.matches();
	}

	public void changeSubmit() {
		String mailStr = mail.getText().toString();
		String userNameStr = userName.getText().toString();
		String userNickStr = userNick.getText().toString();
		birthday = mUserBirthday.getText().toString();
		
		// 企业用户对邮箱地址进行验证
		if (userType == Constant.COMPANY_USER) {
			if (TextUtils.isEmpty(mailStr)) {
					TipDialog.showDialog(PersonalcenterActivity.this, R.string.tip,
						R.string.confirm, R.string.eamil_empty);
				return;
			}
			if (!isMail(mailStr)) {
			
				TipDialog.showDialog(PersonalcenterActivity.this, R.string.tip,
						R.string.confirm, R.string.mail_format_error);
				return;
			}
		}
		if (TextUtils.isEmpty(userNameStr)) {
			TipDialog.showDialog(PersonalcenterActivity.this, R.string.tip,
					R.string.confirm, R.string.user_name_empty);
			return;
		}
		if (userNameStr.length() > 10) {
			TipDialog.showDialog(PersonalcenterActivity.this, R.string.tip,
					R.string.confirm, R.string.user_name_length);
			return;
		}
		if (TextUtils.isEmpty(userNickStr)) {
		TipDialog.showDialog(PersonalcenterActivity.this, R.string.tip,
					R.string.confirm, R.string.user_nick_empty);
			return;
		}
		if (userNickStr.length() > 10) {
			TipDialog.showDialog(PersonalcenterActivity.this, R.string.tip,
					R.string.confirm, R.string.user_nick_length);
			return;
		}
		if (TextUtils.isEmpty(birthday)) {
		 TipDialog.showDialog(PersonalcenterActivity.this, R.string.tip,
					R.string.confirm, R.string.birthday_empty);
			return;
		}
		if (TextUtils.isEmpty(headImg)) {
			TipDialog.showDialog(PersonalcenterActivity.this, R.string.tip,
					R.string.confirm, R.string.headImg_empty);
			return;
		}
		modifyUserInfo(mailStr, userNameStr, userNickStr, birthday, headImg);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirm:
			changeSubmit();
			break;
		case R.id.tv_userBirthday:
			TimeChoiceDialog timeChoiceDialog = new TimeChoiceDialog(
					PersonalcenterActivity.this);
			timeChoiceDialog.Show();
			break;
		case R.id.userImg:
			showPickDialog();
			break;
		case R.id.userSex:
			showGenderAmend();
			break;
		default:
			break;
		}

	}

	private void modifyUserInfo(String mailStr, String userNameStr,
			String userNickStr, String birthday2, String headImg2) {
		// TODO Auto-generated method stub
		/**
		 * nickName：用户昵称， headImg：用户头像, name：用户真实姓名， email：Email, birthday:用户生日，
		 * userId：用户id. (都是必填项)
		 */
		params.put("userId", userId);
		params.put("nickName", userNickStr);
		if (isModifyImg) {
			params.put("headImg", headImg + "");
		} else {
			params.put("headImg", "[]");
		}
		params.put("name", userNameStr);
		params.put("email", mailStr);
		params.put("birthday", birthday2);
		params.put("sex", gender + "");

		System.out.println("Hcl0000++++:" + gender + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.MODIFY_USER_INFO, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						Result result = (Result) AbJsonUtil.fromJson(s,
								Result.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							Toast.makeText(PersonalcenterActivity.this,
									"修改成功！", Toast.LENGTH_LONG).show();
							// 重新设置用户信息并刷新融云数据库改用户的信息
							getUserInfo();
						} else {
							Toast.makeText(PersonalcenterActivity.this,
									result.getRepMsg(), Toast.LENGTH_LONG)
									.show();

						}

					}
				});
	}

	/**
	 * 获取用户信息
	 */
	private void getUserInfo() {
		// TODO Auto-generated method stub
		params.put("userId", userId);
		httpInstance.post(Constant.HOST_URL + Constant.Interface.USER_INFO,
				params, new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						UserInfoResult result = (UserInfoResult) AbJsonUtil
								.fromJson(s, UserInfoResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							com.android.bluetown.bean.UserInfo userInfo = result
									.getData();
							if (userInfo != null) {
								String newHeadImg = userInfo.getHeadImg();
								String newNickName = userInfo.getNickName();
								String newUserId = userInfo.getUserId();
								if (TextUtils.isEmpty(newNickName)) {
								
									newNickName = username;
								}

								if (TextUtils.isEmpty(newHeadImg)) {
									newHeadImg = "";
								}
							
								List<MemberUser> list_user = db.findAllByWhere(
										MemberUser.class, " memberId=\""
												+ userId + "\"");
								if (list_user.size() != 0) {
									MemberUser user = list_user.get(0);
									MemberUser user1 = new MemberUser();
									user1.setNickName(newNickName);
									user1.setHeadImg(newHeadImg);
									user1.setSex(userInfo.getSex());
									user1.setName(userInfo.getName());
									user1.setUsername(userInfo.getTelphone());
									user1.setAddress(user.getAddress());
									user1.setStatu(user.getStatu());
							//		user1.setToken(user.getToken());
									user1.setCompanyName(user.getCompanyName());
									user1.setUsername(user.getUsername());
									user1.setPassword(user.getPassword());
									user1.setUserType(user.getUserType());
									user1.setAlias(user.getAlias());
									user1.setMemberId(user.getMemberId());
									user1.setGardenId(user.getGardenId());
									user1.setHotRegion(user.getHotRegion());
									db.deleteAll(MemberUser.class);
									db.save(user1);
								}
								UserInfo in = new UserInfo(newUserId,
										newNickName, Uri.parse(newHeadImg));
								// 需要更新的用户缓存数据
								RongIM.getInstance().refreshUserInfoCache(in);
								// AB互相聊天时，通知对方自己的昵称或图像变化了
								// RongIM.getInstance().setCurrentUserInfo(in);
								// RongIM.getInstance()
								// .setMessageAttachedUserInfo(true);
								finish();
							}

						}
					}
				});

	}

	private void setDate(String date) {
		mUserBirthday.setText(date);
	}

	private void initFolderDir() {
		String photo_dir = AbFileUtil.getImageDownloadDir(this);
		if (AbStrUtil.isEmpty(photo_dir)) {
			AbToastUtil.showToast(this, "存储卡不存在");
		} else {
			PHOTO_DIR = new File(photo_dir);
		}
	}

	private void showGenderAmend() {
		View genderView = getLayoutInflater().inflate(R.layout.gender_amend,
				null);
		final Button gender_boy = (Button) genderView
				.findViewById(R.id.gender_boy);
		final Button gender_girl = (Button) genderView
				.findViewById(R.id.gender_girl);
		Button cancel = (Button) genderView.findViewById(R.id.choose_cancel);
		gender_boy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				gender = 0;
				// changeSubmit();
				userSex.setImageResource(R.drawable.ic_sex_man);
				AbDialogUtil.removeDialog(PersonalcenterActivity.this);

			}
		});
		gender_girl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				gender = 1;
				// changeSubmit();
				userSex.setImageResource(R.drawable.ic_sex);
				AbDialogUtil.removeDialog(PersonalcenterActivity.this);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AbDialogUtil.removeDialog(PersonalcenterActivity.this);

			}
		});

		AbDialogUtil.showDialog(genderView, Gravity.BOTTOM);

	}

	private void showPickDialog() {

		avatarView = getLayoutInflater().inflate(R.layout.choose_avatar, null);
		Button albumButton = (Button) avatarView
				.findViewById(R.id.choose_album);
		Button camButton = (Button) avatarView.findViewById(R.id.choose_cam);
		Button cancelButton = (Button) avatarView
				.findViewById(R.id.choose_cancel);

		albumButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AbDialogUtil.removeDialog(PersonalcenterActivity.this);
				// 从相册中去获取
				selectImageFromGallry();
			}

		});

		camButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AbDialogUtil.removeDialog(PersonalcenterActivity.this);
				doPickPhotoAction();
			}

		});

		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AbDialogUtil.removeDialog(PersonalcenterActivity.this);
			}

		});
		AbDialogUtil.showDialog(avatarView, Gravity.BOTTOM);
	}

	protected void selectImageFromGallry() {
		try {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent.setType("image/*");
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			AbToastUtil.showToast(PersonalcenterActivity.this, "没有找到照片");
		}
	}

	/**
	 * 从照相机获取
	 */
	private void doPickPhotoAction() {
		String status = Environment.getExternalStorageState();
		// 判断是否有SD卡,如果有sd卡存入sd卡在说，没有sd卡直接转换为图片
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			doTakePhoto();
		} else {
			AbToastUtil.showToast(this, "没有可用的存储卡");
		}
	}

	/**
	 * 拍照获取图片
	 */

	protected void doTakePhoto() {
		try {
			mFileName = System.currentTimeMillis() + ".jpg";
			mCurrentPhotoFile = new File(PHOTO_DIR, mFileName);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(mCurrentPhotoFile));
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (Exception e) {
			AbToastUtil.showToast(this, "未找到系统相机程序");
		}
	}

	/**
	 * 描述：因为调用了Camera和Gally所以要判断他们各自的返回情况, 他们启动时是这样的startActivityForResult
	 */
	protected void onActivityResult(int requestCode, int resultCode,
			Intent mIntent) {
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA:
			Uri uri = mIntent.getData();
			String currentFilePath = getPath(uri);
			if (currentFilePath != null && !currentFilePath.equals("")) {
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(uri, "image/*");
				intent.putExtra("crop", "true");
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", 190);
				intent.putExtra("outputY", 190);
				intent.putExtra("return-data", true);
				startActivityForResult(intent, CAMERA_CROP_DATA);
			} else {
				AbToastUtil.showToast(this, "未在存储卡中找到这个文件");
				Toast.makeText(PersonalcenterActivity.this, "未在存储卡中找到这个文件",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case CAMERA_WITH_DATA:
			Uri uris = Uri.fromFile(mCurrentPhotoFile);
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uris, "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 190);
			intent.putExtra("outputY", 190);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, CAMERA_CROP_DATA);
			break;
		case CAMERA_CROP_DATA:
			Bitmap avatar = null;
			Bundle bundle = mIntent.getExtras();
			if (bundle != null) {
				avatar = bundle.getParcelable("data");
				userImg.setImageBitmap(avatar);
				// 将图片转化为64位子字符串
				headImg = bitmapToBase64(avatar) + ".png";
				isModifyImg = true;
			}

			break;
		}
	}

	/**
	 * 从相册得到的url转换为SD卡中图片路径
	 */
	public String getPath(Uri uri) {
		String path = null;
		if (AbStrUtil.isEmpty(uri.getAuthority())) {
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);
		if (cursor.moveToFirst()) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			path = cursor.getString(column_index);
		}
		cursor.close();
		return path;
	}

	/**
	 * 
	 * @Title: imgToBase64
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param bitmap
	 * @return
	 * @throws
	 */
	public static String bitmapToBase64(Bitmap bitmap) {

		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (receiver != null) {
			unregisterReceiver(receiver);
		}

	}

	public class DateRecevier extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("date_choice")) {
				String date = intent.getStringExtra("date_string");
				if (!TextUtils.isEmpty(date)) {
					date = date.replaceAll("年", ".").replaceAll("月", ".")
							.replaceAll("日", "");
					setDate(date);
				}

			}
		}
	}
}
