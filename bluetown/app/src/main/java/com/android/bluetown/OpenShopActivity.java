package com.android.bluetown;

import java.io.File;
import java.util.ArrayList;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.ab.util.AbDialogUtil;
import com.ab.util.AbFileUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.android.bluetown.adapter.GridViewCheckAdapter;
import com.android.bluetown.adapter.ModifyPicGridViewAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.DemandTypeBean;
import com.android.bluetown.utils.UpdateImageUtils;
import com.android.bluetown.view.NoScrollGridView;

public class OpenShopActivity extends TitleBarActivity {
	private ArrayList<DemandTypeBean> modelsList = null;
	private NoScrollGridView typesGridView,shopImgGridView;
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
	private static final int TAKE_PICTURE = 0x000000;
	private String path = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_open_shop);
		BlueTownExitHelper.addActivity(this);
		typesGridView = (NoScrollGridView) findViewById(R.id.shopTypeGridView);
		shopImgGridView = (NoScrollGridView) findViewById(R.id.shopImgGridView);
		initData();
		setData(this, typesGridView);
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
		setTitleView(R.string.open_shop);
		setTitleLayoutBg(R.color.title_bg_blue);
	}

	public void setData(Context context, GridView gridView) {
		if (modelsList == null) {
			modelsList = new ArrayList<DemandTypeBean>();
		}
		modelsList.clear();
		modelsList.add(new DemandTypeBean(R.drawable.ic_shop_type1, R.string.household_appliances, true));
		modelsList.add(new DemandTypeBean(R.drawable.ic_shop_type2, R.string.appliances, false));
		modelsList.add(new DemandTypeBean(R.drawable.ic_shop_type3, R.string.mother_baby_products, false));
		modelsList.add(new DemandTypeBean(R.drawable.ic_shop_type4, R.string.outdoor_activities, false));
		modelsList.add(new DemandTypeBean(R.drawable.ic_shop_type5, R.string.ambrosia, false));
		modelsList.add(new DemandTypeBean(R.drawable.ic_shop_type6, R.string.skin_beauty, false));
		modelsList.add(new DemandTypeBean(R.drawable.ic_shop_type7, R.string.fashion_girl, false));
		modelsList.add(new DemandTypeBean(R.drawable.ic_shop_type8, R.string.digital_burning_guest, false));
		GridViewCheckAdapter adapter = new GridViewCheckAdapter(context, modelsList);
		gridView.setAdapter(adapter);
		// 设置item点击事件
		typesGridView.setOnItemClickListener(demandTypeClickListener);
	}

	private OnItemClickListener demandTypeClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub

		}
	};
	
	private OnItemClickListener demandImgsClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			if (position == UpdateImageUtils.bmp.size()) {
				openAddImg();
			}

		}
	};
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
		shopImgGridView.setAdapter(adapter);
		shopImgGridView.setOnItemClickListener(demandImgsClickListener);

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
		Button albumButton = (Button) avatarView.findViewById(R.id.choose_album);
		Button camButton = (Button) avatarView.findViewById(R.id.choose_cam);
		Button cancelButton = (Button) avatarView.findViewById(R.id.choose_cancel);
		albumButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AbDialogUtil.removeDialog(OpenShopActivity.this);
				// 从相册中去获取
				try {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
					intent.setType("image/*");
					startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
				} catch (ActivityNotFoundException e) {
					AbToastUtil.showToast(OpenShopActivity.this, "没有找到照片");
				}
			}

		});

		camButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AbDialogUtil.removeDialog(OpenShopActivity.this);
				doPickPhotoAction();
			}

		});

		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AbDialogUtil.removeDialog(OpenShopActivity.this);
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
			AbToastUtil.showToast(OpenShopActivity.this, "没有找到照片");
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
			AbToastUtil.showToast(OpenShopActivity.this, "没有可用的存储卡");
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
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (Exception e) {
			AbToastUtil.showToast(OpenShopActivity.this, "未找到系统相机程序");
		}
	}

	public String getPath(Uri uri) {
		if (AbStrUtil.isEmpty(uri.getAuthority())) {
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		return path;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case TAKE_PICTURE:
			if (UpdateImageUtils.drr.size() < 9 && resultCode == -1) {
				UpdateImageUtils.drr.add(path);
			}
			break;
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
					AbToastUtil.showToast(OpenShopActivity.this, "未在存储卡中找到这个文件");
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

			} catch (Exception e) {
				// TODO: handle exception
			}

			break;
		}

	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (UpdateImageUtils.bmp.size()!=0) {
			UpdateImageUtils.bmp.clear();
		}
	}

}
