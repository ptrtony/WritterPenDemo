package com.android.bluetown.home.makefriends.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.ActivityNotFoundException;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ab.util.AbDialogUtil;
import com.ab.util.AbFileUtil;
import com.ab.util.AbJsonUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.ModifyPicGridViewAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.SweetAlertDialog.OnSweetClickListener;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;

import com.android.bluetown.result.Result;
import com.android.bluetown.spinner.custom.AbstractSpinerAdapter;
import com.android.bluetown.spinner.custom.SpinerPopWindow;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.UpdateImageUtils;
import com.android.bluetown.view.NoScrollGridView;

public class CreateGroupActivity extends TitleBarActivity implements
		OnClickListener, OnItemClickListener {
	private TextView groupSize;
	private List<String> faultTypesList;
	private SpinerPopWindow mFaultTypePop;
	private NoScrollGridView imgGridView;
	private ModifyPicGridViewAdapter adapter;
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
	private List<String> imgsList;
	private String userId;
	private FinalDb db;
	
	private List<MemberUser> users;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_creategroup);
		BlueTownExitHelper.addActivity(this);
		initView();
		initFaultType();
		initData();
	}

	private void initData() {
		// 初始化图片保存路径
		String photo_dir = AbFileUtil.getImageDownloadDir(this);
		if (AbStrUtil.isEmpty(photo_dir)) {
			AbToastUtil.showToast(this, "存储卡不存在");
		} else {
			PHOTO_DIR = new File(photo_dir);
		}
		adapter = new ModifyPicGridViewAdapter(this);
		adapter.update();
		imgGridView.setAdapter(adapter);
		imgGridView.setOnItemClickListener(this);

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

	private void initView() {
		db = FinalDb.create(this);
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		groupSize = (TextView) findViewById(R.id.groupSize);
		mName = (EditText) findViewById(R.id.et_name);
		imgGridView = (NoScrollGridView) findViewById(R.id.imgGridView);
		groupSize.setOnClickListener(this);
		findViewById(R.id.btn_confirm).setOnClickListener(this);
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
		setTitleView("创建群");
		setTitleLayoutBg(R.color.chat_tab_create_group_color);
		rightImageLayout.setVisibility(View.INVISIBLE);

	}

	/**
	 * flockName：群名称（必填） scale：群规模（必填）（0:500,1:200） userId：创建人（必填） flockImg:群图片
	 * mid:群主键（非必填）(创建群不填，修改群资料必须填写)
	 * 
	 * @param type
	 */
	private void createGroup(String flockName, String scale) {
		// TODO Auto-generated method stub
		params.put("flockName", flockName);
		params.put("scale", scale);
		params.put("userId", userId);
		if (imgsList != null && imgsList.size() > 0) {
			params.put("flockImg", AbJsonUtil.toJson(imgsList));
		}
		params.put("mid", "");
		httpInstance.post(Constant.HOST_URL + Constant.Interface.CREATE_GROUP,
				params, new AbsHttpStringResponseListener(
						CreateGroupActivity.this, null) {
					@Override
					public void onSuccess(int i, String s) {
						Result result = (Result) AbJsonUtil.fromJson(s,
								Result.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							showDialog(CreateGroupActivity.this, R.string.tip,
									R.string.dialog_ok,
									R.string.publish_success1);
						} else {
							TipDialog.showDialog(CreateGroupActivity.this,
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
		case R.id.btn_confirm:
			if (userId!=null&&!userId.isEmpty()) {
				departure = mName.getText().toString().trim();
				String scale = groupSize.getText().toString();
				if (TextUtils.isEmpty(scale)) {
					scale = "500";
				}
				if (TextUtils.isEmpty(departure) || TextUtils.isEmpty(scale)) {
					TipDialog.showDialog(CreateGroupActivity.this,
							R.string.tip, R.string.dialog_ok,
							R.string.publish_group_perfect_info1);
				} else {
					createGroup(departure, scale);
				}
			} else {
				TipDialog.showDialogNoClose(CreateGroupActivity.this,
						R.string.tip, R.string.confirm,
						R.string.login_info_tip, LoginActivity.class);
			}

			break;
		}

	}

	private void showDialog(final Context context, int titleId,
			int confirmTextId, int contentStr) {
		SweetAlertDialog dialog = new SweetAlertDialog(context)
				.setContentText(context.getString(contentStr));
		dialog.setTitleText(context.getString(titleId));
		dialog.setConfirmText(context.getString(confirmTextId));
		dialog.showCancelButton(false);
		dialog.setContentText(context.getString(contentStr));
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				// 清空组件的数据
				mName.setText("");
				groupSize.setText("");
				if (UpdateImageUtils.bmp.size() != 0) {
					UpdateImageUtils.bmp.clear();
				}
				adapter.notifyDataSetChanged();
				sweetAlertDialog.dismiss();
			}
		});
		dialog.show();
	}

	private void openAddImg() {
		// TODO Auto-generated method stub
		showPickDialog();
	}

	/**
	 * show pick dialog
	 */
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
				AbDialogUtil.removeDialog(CreateGroupActivity.this);
				// 从相册中去获取
				try {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
					intent.setType("image/*");
					startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
				} catch (ActivityNotFoundException e) {
					AbToastUtil.showToast(CreateGroupActivity.this, "没有找到照片");
				}
			}

		});

		camButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AbDialogUtil.removeDialog(CreateGroupActivity.this);
				doPickPhotoAction();
			}

		});

		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AbDialogUtil.removeDialog(CreateGroupActivity.this);
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
			AbToastUtil.showToast(CreateGroupActivity.this, "没有找到照片");
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (position == UpdateImageUtils.bmp.size()) {
			openAddImg();
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
			AbToastUtil.showToast(CreateGroupActivity.this, "没有可用的存储卡");
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
			AbToastUtil.showToast(CreateGroupActivity.this, "未找到系统相机程序");
		}
	}

	public String getPath(Uri uri) {
		if (AbStrUtil.isEmpty(uri.getAuthority())) {
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		return path;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA:
			try {
				Uri uri = data.getData();
				String currentFilePath = getPath(uri);
				if (!AbStrUtil.isEmpty(currentFilePath)) {
					if (null == uri) {
						return;
					}

					Intent intent = new Intent();

					intent.setAction("com.android.camera.action.CROP");
					intent.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
					intent.putExtra("crop", "true");
					intent.putExtra("aspectX", 1);// 裁剪框比例
					intent.putExtra("aspectY", 1);
					intent.putExtra("outputX", 190);// 输出图片大小
					intent.putExtra("outputY", 190);
					intent.putExtra("return-data", true);

					startActivityForResult(intent, CAMERA_CROP_DATA);
				} else {
					AbToastUtil.showToast(CreateGroupActivity.this,
							"未在存储卡中找到这个文件");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;
		case CAMERA_WITH_DATA:
			try {
				Uri uri = Uri.fromFile(mCurrentPhotoFile);
				if (null == uri) {
					return;
				}

				Intent intent = new Intent();

				intent.setAction("com.android.camera.action.CROP");
				intent.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
				intent.putExtra("crop", "true");
				intent.putExtra("aspectX", 1);// 裁剪框比例
				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", 190);// 输出图片大小
				intent.putExtra("outputY", 190);
				intent.putExtra("return-data", true);
				startActivityForResult(intent, CAMERA_CROP_DATA);
			} catch (Exception e) {
				// TODO: handle exception
			}

			break;
		case CAMERA_CROP_DATA:
			try {

				Bitmap bmap = data.getParcelableExtra("data");
				UpdateImageUtils.bmp.add(bmap);
				adapter.notifyDataSetChanged();
				imgsList = getImageBase64(UpdateImageUtils.bmp);
			} catch (Exception e) {
				// TODO: handle exception
			}

			break;
		}

	}

	/**
	 * get image by base64
	 * 
	 * @return
	 */
	protected List<String> getImageBase64(List<Bitmap> imgs) {
		List<String> t = new ArrayList<String>();
		for (Bitmap b : imgs) {
			String t1 = bitmapToBase64(b);
			if (t1 != null) {
				t.add(t1);
			}
		}
		return t;
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
		if (UpdateImageUtils.bmp.size() != 0) {
			UpdateImageUtils.bmp.clear();
		}
	}
}
