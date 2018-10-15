package com.android.bluetown.home.makefriends.activity;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.ActivityNotFoundException;
import android.content.Intent;
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
import com.ab.util.AbJsonUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.GroupsBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.home.main.model.act.MakeFriendsActivity;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.GroupDetailResult;
import com.android.bluetown.result.GroupDetailResult.GroupDetail;
import com.android.bluetown.result.ModifyGroupResult;
import com.android.bluetown.spinner.custom.AbstractSpinerAdapter;
import com.android.bluetown.spinner.custom.SpinerPopWindow;
import com.android.bluetown.utils.Constant;

public class ModifyGroupActivity extends TitleBarActivity implements
		OnClickListener {
	private TextView groupSize;
	private List<String> faultTypesList;
	private SpinerPopWindow mFaultTypePop;
	private ImageView groupImg;
	private View avatarView;
	/* 用来标识请求照相功能的activity */
	private static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	/* 用来标识请求裁剪图片后的activity */
	private static final int CAMERA_CROP_DATA = 3022;
	/* 拍照的照片存储位置 */
	private File PHOTO_DIR = null;
	// 照相机拍照得到的图片
	private File mCurrentPhotoFile;
	private String mFileName;
	private EditText mName;
	private String departure;
	private String userId;
	private FinalDb db;
	private List<MemberUser> users;
	private String mid;
	private boolean isModifyImg;
	private String headImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_modify_groupinfo);
		BlueTownExitHelper.addActivity(this);
		initView();
		initFaultType();
	}

	private void showSpinWindow(SpinerPopWindow mFaultTypePop,
			TextView faultType2) {
		// TODO Auto-generated method stub
		mFaultTypePop.setWidth(faultType2.getWidth());
		mFaultTypePop.showAsDropDown(faultType2);
	}

	private void initFaultType() {
		faultTypesList = new ArrayList<String>();
		String[] faultTypes = getResources().getStringArray(
				R.array.faultTypeArray2);
		for (int i = 0; i < faultTypes.length; i++) {
			faultTypesList.add(faultTypes[i]);
		}
		mFaultTypePop = new SpinerPopWindow(this, groupSize);
		mFaultTypePop.refreshData(faultTypesList, 0);
		mFaultTypePop.setItemListener(new onSipnnerItemClick(faultTypesList,
				groupSize));

	}

	private void initView() {
		db = FinalDb.create(this);
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		try {
			mid = getIntent().getStringExtra("mid");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			mid = "";
		}
		groupSize = (TextView) findViewById(R.id.groupSize);
		mName = (EditText) findViewById(R.id.et_name);
		groupImg = (ImageView) findViewById(R.id.groupImg);
		groupSize.setOnClickListener(this);
		groupImg.setOnClickListener(this);
		findViewById(R.id.btn_confirm).setOnClickListener(this);
		getGroupDetails(mid);
	}

	/**
	 * userId：邀请用户的id（必填） mid：加入的群id(必填) flockName：群名称（必填）
	 */
	/**
	 * @param mid
	 */
	private void getGroupDetails(final String mid) {
		// mid:群id（必填）
		// userId:当前登录用户(必填)
		params.put("userId", userId);
		params.put("mid", mid);
		httpInstance.post(Constant.HOST_URL + Constant.Interface.GROUP_DETAIL,
				params, new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						final GroupDetailResult result = (GroupDetailResult) AbJsonUtil
								.fromJson(s, GroupDetailResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							// TODO Auto-generated method stub
							final GroupDetail detail = result.getData();
							groupSize.setText(detail.getScale());
							mName.setText(detail.getFlockName());
							imageLoader.displayImage(detail.getFlockImg(),
									groupImg, defaultOptions);
						}

					}

				});
	}

	/**
	 * 
	 * @Title: initTitle
	 * @Description:初始化标题栏
	 * @see com.android.bluetown.activity.TitleBarActivity#initTitle()
	 */
	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("修改群资料");
		setTitleLayoutBg(R.color.chat_tab_create_group_color);
		rightImageLayout.setVisibility(View.INVISIBLE);

	}

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

		private void setItemData(List<String> data, TextView textView, int pos) {
			if (pos >= 0 && pos <= data.size()) {
				String value = data.get(pos);
				textView.setText(value);
			}
		}
	}

	/**
	 * flockName：群名称（必填） scale：群规模（必填）（0:500,1:200） userId：创建人（必填） flockImg:群图片
	 * mid:群主键（非必填）(创建群不填，修改群资料必须填写)
	 * 
	 * @param type
	 */
	private void modifyGroupInfo(final String flockName, String scale) {
		// TODO Auto-generated method stub
		params.put("flockName", flockName);
		params.put("scale", scale);
		params.put("userId", userId);
		if (isModifyImg) {
			params.put("flockImg", headImg + "");
		} else {
			params.put("flockImg", "[]");
		}
		params.put("mid", mid);
		httpInstance.post(Constant.HOST_URL + Constant.Interface.CREATE_GROUP,
				params, new AbsHttpStringResponseListener(
						ModifyGroupActivity.this, null) {
					@Override
					public void onSuccess(int i, String s) {
						ModifyGroupResult result = (ModifyGroupResult) AbJsonUtil
								.fromJson(s, ModifyGroupResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							Toast.makeText(context, "修改成功！", Toast.LENGTH_SHORT)
									.show();
							// 将该群信息保存到数据库
							List<GroupsBean> groupList = db.findAllByWhere(
									GroupsBean.class, " mid=\"" + mid + "\"");
							if (groupList.size() == 0) {
								GroupsBean groupBean = new GroupsBean();
								groupBean.setMid(mid);
								groupBean.setFlockImg(result.getObject());
								groupBean.setFlockName(flockName);
								db.save(groupBean);
							} else {
								db.deleteById(GroupsBean.class, " mid=\"" + mid
										+ "\"");
								GroupsBean groupBean = new GroupsBean();
								groupBean.setMid(mid);
								groupBean.setFlockImg(result.getObject());
								groupBean.setFlockName(flockName);
								db.save(groupBean);

							}
							Group group = new Group(mid, flockName, Uri
									.parse(result.getObject()));
							// 需要更新的群缓存数据（融云数据）
							RongIM.getInstance().refreshGroupInfoCache(group);
							Intent intent = new Intent();
							intent.setClass(ModifyGroupActivity.this,
									MakeFriendsActivity.class);
							intent.putExtra("group", "group");
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
							finish();
						} else {
							TipDialog.showDialog(ModifyGroupActivity.this,
									R.string.tip, R.string.confirm,
									result.getRepMsg());
						}

					}

				});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.groupSize:
			showSpinWindow(mFaultTypePop, groupSize);
			break;
		case R.id.groupImg:
			showPickDialog();
			break;
		case R.id.btn_confirm:
			if (userId != null && !userId.isEmpty()) {
				departure = mName.getText().toString().trim();
				String scale = groupSize.getText().toString();
				if (TextUtils.isEmpty(scale)) {
					scale = "500";
				}
				if (TextUtils.isEmpty(departure) || TextUtils.isEmpty(scale)) {
					TipDialog.showDialog(ModifyGroupActivity.this,
							R.string.tip, R.string.dialog_ok,
							R.string.publish_group_perfect_info1);
				} else {
					modifyGroupInfo(departure, scale);
				}
			} else {
				TipDialog.showDialogNoClose(ModifyGroupActivity.this,
						R.string.tip, R.string.confirm,
						R.string.login_info_tip, LoginActivity.class);
			}

			break;
		}

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
				AbDialogUtil.removeDialog(ModifyGroupActivity.this);
				// 从相册中去获取
				selectImageFromGallry();
			}

		});

		camButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AbDialogUtil.removeDialog(ModifyGroupActivity.this);
				doPickPhotoAction();
			}

		});

		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AbDialogUtil.removeDialog(ModifyGroupActivity.this);
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
			AbToastUtil.showToast(ModifyGroupActivity.this, "没有找到照片");
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
				Toast.makeText(ModifyGroupActivity.this, "未在存储卡中找到这个文件",
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
				groupImg.setImageBitmap(avatar);
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
	}
}
