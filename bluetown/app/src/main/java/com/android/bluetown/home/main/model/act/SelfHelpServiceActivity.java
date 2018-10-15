package com.android.bluetown.home.main.model.act;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.tsz.afinal.FinalDb;
import android.annotation.SuppressLint;
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
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbJsonUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.ComplainAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.SelfServiceType;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.datewheel.SelfHelpDateDialog;
import com.android.bluetown.img.AlbumFilesInterface;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.popup.AlbumPopupWindow;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.SelfServiceTypeResult;
import com.android.bluetown.spinner.custom.AbstractSpinerAdapter;
import com.android.bluetown.spinner.custom.SpinerPopWindow;
import com.android.bluetown.utils.AlbumUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.DisplayUtils;
import com.android.bluetown.utils.ImageCompress.BitmapUtil;
import com.android.bluetown.utils.ImageUtils;
import com.android.bluetown.utils.InputSoftUtil;
import com.android.bluetown.utils.PictureItemDecoration;
import com.android.bluetown.utils.UpdateImageUtils;
import com.yanzhenjie.album.AlbumFile;

/**
 * 
 * @ClassName: SelfHelpServiceActivity
 * @Description:TODO(自助报修)
 * @author: shenyz
 * @date: 2015年8月13日 下午7:59:46
 * 
 */
@SuppressLint("NewApi")
public class SelfHelpServiceActivity extends TitleBarActivity implements
		OnClickListener, ComplainAdapter.OnItemClickListener, AlbumPopupWindow.OnClickListener {
	private TextView faultType, serviceYear, serviceMonth, serviceDay;
	private EditText address, telphone, faultIntroContent;
	private RadioButton afternoon, noon, all;
	private Button commit;
	private LinearLayout dealDateLy;
	private RecyclerView imgGridView;
	private ComplainAdapter adapter;
	private SpinerPopWindow mFaultTypePop;
	private List<String> faultTypesList;
	private List<SelfServiceType> typesList;
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
	private String repairTypeId;
	private String resulttime;
	private String selection = "1";
	private List<String> imgsList;
	private String createTime;
	private DateRecevier receiver;
	private String userId, username, companyAddress;
	private int userType;
	private FinalDb db;
	private List<MemberUser> users;
	private SharePrefUtils mPrefUtils;
	private ArrayList<Bitmap> bitmaps;
	private String gardenId;
	private AlbumPopupWindow albumPopupWindow;
	private PictureItemDecoration itemDecoration;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				TipDialog.showDialogStartNewActivity(
						SelfHelpServiceActivity.this, R.string.tip,
						R.string.confirm, R.string.info_perfect,
						HistoryServiceActivity.class);
				break;
			case 2:
				SelfServiceTypeResult result = (SelfServiceTypeResult) msg.obj;
				TipDialog.showDialog(SelfHelpServiceActivity.this,
						R.string.tip, R.string.confirm, result.getRepMsg());
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
		addContentView(R.layout.ac_selfhelp_service);
		BlueTownExitHelper.addActivity(this);
		initViews();
		initData();
		getCurrentDate();
		IntentFilter filter = new IntentFilter();
		filter.addAction("self_date_choice");
		receiver = new DateRecevier();
		registerReceiver(receiver, filter);
	}

	private void initViews() {
		db = FinalDb.create(this);
		mPrefUtils = new SharePrefUtils(this);
		albumPopupWindow = new AlbumPopupWindow(this,this);
		faultType = (TextView) findViewById(R.id.faultType);
		serviceYear = (TextView) findViewById(R.id.year);
		serviceMonth = (TextView) findViewById(R.id.month);
		serviceDay = (TextView) findViewById(R.id.day);
		address = (EditText) findViewById(R.id.address);
		telphone = (EditText) findViewById(R.id.phone);
		faultIntroContent = (EditText) findViewById(R.id.faultContent);
		afternoon = (RadioButton) findViewById(R.id.afternoon);
		noon = (RadioButton) findViewById(R.id.noon);
		all = (RadioButton) findViewById(R.id.all);
		commit = (Button) findViewById(R.id.serviceCommit);
		imgGridView = findViewById(R.id.imgGridView);
		dealDateLy = (LinearLayout) findViewById(R.id.dealDateLy);
		faultType.setOnClickListener(this);
		commit.setOnClickListener(this);
		dealDateLy.setOnClickListener(this);
//		imgGridView.setOnItemClickListener(this);
		int padding = DisplayUtils.dip2px(this, 20);
		afternoon.setPadding(padding, 0, 0, 0);
		noon.setPadding(padding, 0, 0, 0);
		all.setPadding(padding, 0, 0, 0);
		afternoon.setOnClickListener(this);
		noon.setOnClickListener(this);
		all.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
				username = user.getUsername();
				companyAddress = user.getAddress();
				userType = user.getUserType();
				gardenId = user.getGardenId();
			}
			telphone.setText(username);
			address.setText(companyAddress);

		}
		if (TextUtils.isEmpty(gardenId)) {
			gardenId = mPrefUtils.getString(SharePrefUtils.GARDEN_ID, "");
		}
		getRepairTypeList();
		if (!TextUtils.isEmpty(userId)) {
			commit.setClickable(true);
			commit.setBackgroundResource(R.drawable.blue_darker_btn_bg);
		} else {
			commit.setClickable(false);
			commit.setBackgroundResource(R.drawable.gray_btn_bg1);
			TipDialog.showDialogNoClose(SelfHelpServiceActivity.this,
					R.string.tip, R.string.confirm, R.string.login_info_tip,
					LoginActivity.class);
		}

	}

	/**
	 * 
	 * @Title: initFaultType
	 * @Description: TODO(初始化故障类型)
	 * @throws
	 */
	private void initFaultType() {
		mFaultTypePop = new SpinerPopWindow(this, faultType);
		mFaultTypePop.refreshData(faultTypesList, 0);
		mFaultTypePop.setItemListener(new onSipnnerItemClick(faultTypesList,
				faultType));

	}

	/**
	 * 自助报修类型
	 */
	private void getRepairTypeList() {
		faultTypesList = new ArrayList<String>();
		params.put("gardenId", gardenId);
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.SELF_SERVICE_TYPE, params,
				new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						SelfServiceTypeResult result = (SelfServiceTypeResult) AbJsonUtil
								.fromJson(s, SelfServiceTypeResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							typesList = result.getData();
							for (int j = 0; j < result.getData().size(); j++) {
								faultTypesList.add(result.getData().get(j)
										.getTypeName());
							}
							initFaultType();
						} else {
							TipDialog.showDialog(SelfHelpServiceActivity.this,
									R.string.tip, R.string.confirm,
									result.getRepMsg());
						}

					}

				});
	}

	private void initData() {
		// 初始化图片保存路径
		adapter = new ComplainAdapter(SelfHelpServiceActivity.this,this);
		bitmaps = new ArrayList<>();
		bitmaps.add(ImageUtils.decodeSampledBitmapFromResource(getResources()
				, R.drawable.add_pic, DisplayUtils.dip2px(this, 80)
				, DisplayUtils.dip2px(this, 80)));
		adapter.setData(bitmaps);
		GridLayoutManager manager = new GridLayoutManager(this, 4);
		imgGridView.setLayoutManager(manager);
		imgGridView.setItemAnimator(new DefaultItemAnimator());
		if (imgGridView.getRecycledViewPool() != null) {
			imgGridView.getRecycledViewPool().setMaxRecycledViews(0, 10);
		}
		albumPopupWindow = new AlbumPopupWindow(this,this);
		itemDecoration = new PictureItemDecoration(DisplayUtils.dip2px(this, 15)
				, DisplayUtils.dip2px(this, 5));
		itemDecoration.getPictureLists(bitmaps);
		imgGridView.setAdapter(adapter);
	}

	/**
	 * 
	 * @Title: initTitle
	 * @Description: 初始化自助报修的标题栏
	 * @see com.android.bluetown.activity.TitleBarActivity#initTitle()
	 */
	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.self_help_service);
		setRightImageView(R.drawable.ic_history);
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setOnClickListener(this);
	}

	private void getCurrentDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		createTime = df.format(new Date());// new Date()为获取当前系统时间
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rightImageLayout:
			if (userType != 1) {
				TipDialog
						.showDialog(SelfHelpServiceActivity.this, R.string.tip,
								R.string.confirm, R.string.company_limite);
				return;
			}
			startActivity(new Intent(SelfHelpServiceActivity.this,
					HistoryServiceActivity.class));
			break;
		case R.id.faultType:

			showSpinWindow(mFaultTypePop, faultType);

			break;
		case R.id.noon:
			noon.setChecked(true);
			afternoon.setChecked(false);
			all.setChecked(false);
			selection = "1";
			break;
		case R.id.afternoon:
			noon.setChecked(false);
			afternoon.setChecked(true);
			all.setChecked(false);
			selection = "2";
			break;
		case R.id.all:
			noon.setChecked(false);
			afternoon.setChecked(false);
			all.setChecked(true);
			selection = "0";
			break;
		case R.id.serviceCommit:

			if (!TextUtils.isEmpty(userId)) {
				commit.setClickable(true);
				commit.setBackground(getResources().getDrawable(
						R.drawable.blue_darker_btn_bg));
				// 企业用户才有权限
				String statu = mPrefUtils.getString(SharePrefUtils.CHECKSTATE,"");
				if (statu.equals("1")) {

					/*
					 * type：报修类型id time：上门日期 selection：上门时间(1上午 2下午 0均可)
					 * address：报修地址 tell：联系电话 remark：备注 userId：用户id
					 * createTime：创建报修的日期
					 */
					String faultTypeStr = faultType.getText().toString();
					String serviceYearStr = serviceYear.getText().toString();
					String serviceMonthStr = serviceMonth.getText().toString();
					String serviceDayStr = serviceDay.getText().toString();
					String addressStr = address.getText().toString();
					String telphoneStr = telphone.getText().toString();
					String faultIntroContentStr = faultIntroContent.getText()
							.toString();
					if (TextUtils.isEmpty(faultTypeStr)
							|| faultTypeStr.contains("请选择")) {
						TipDialog.showDialog(SelfHelpServiceActivity.this,
								R.string.tip, R.string.confirm,
								R.string.fault_type_empty);
						return;
					}
					if ("年".equals(serviceYearStr)
							|| "月".equals(serviceMonthStr)
							|| "日".equals(serviceDayStr)) {
						TipDialog.showDialog(SelfHelpServiceActivity.this,
								R.string.tip, R.string.confirm,
								R.string.commit_date_empty);
						return;
					}
					if (TextUtils.isEmpty(addressStr)) {
						TipDialog.showDialog(SelfHelpServiceActivity.this,
								R.string.tip, R.string.confirm,
								R.string.address_empty);
						return;
					}
					if (TextUtils.isEmpty(telphoneStr)) {
						TipDialog.showDialog(SelfHelpServiceActivity.this,
								R.string.tip, R.string.confirm,
								R.string.tel_empty);
						return;
					}
					if (telphoneStr.length() < 11) {
						TipDialog.showDialog(SelfHelpServiceActivity.this,
								R.string.tip, R.string.confirm,
								R.string.phone_error);
						return;

					}
					if (!isTelPhoto(telphoneStr)) {
						TipDialog.showDialog(SelfHelpServiceActivity.this,
								R.string.tip, R.string.confirm,
								R.string.account_format_error);
						return;

					}
					if (TextUtils.isEmpty(faultIntroContentStr)) {
						TipDialog.showDialog(SelfHelpServiceActivity.this,
								R.string.tip, R.string.confirm,
								R.string.fault_explain_empty);
						return;
					}
					if (TextUtils.isEmpty(repairTypeId)
							&& (typesList != null && typesList.size() > 0)) {
						repairTypeId = typesList.get(0).getTid();
					}
					bitmaps.remove(bitmaps.size() - 1);
					imgsList = getImageBase64(bitmaps);
					// 访问接口交互
					params.put("type", repairTypeId);
					params.put("time", resulttime);
					params.put("selection", selection);
					params.put("address", addressStr);
					params.put("tell", telphoneStr);
					params.put("userId", userId);
					params.put("remark", faultIntroContentStr);
					params.put("createTime", createTime);
					params.put("gardenId", gardenId);
					if (imgsList != null && imgsList.size() > 0) {
						params.put("pictures", AbJsonUtil.toJson(imgsList));
					}
					httpInstance.post(Constant.HOST_URL
							+ Constant.Interface.ADD_SELF_SERVICE, params,
							new AbsHttpStringResponseListener(this, null) {
								@Override
								public void onSuccess(int i, String s) {
									SelfServiceTypeResult result = (SelfServiceTypeResult) AbJsonUtil
											.fromJson(s,
													SelfServiceTypeResult.class);
									if (result.getRepCode().contains(
											Constant.HTTP_SUCCESS)) {
										handler.sendEmptyMessage(1);
									} else {
										Message message = new Message();
										message.obj = result;
										message.what = 2;
										handler.sendMessage(message);
									}

								}

							});
				} else {
					TipDialog.showDialog(SelfHelpServiceActivity.this,
							R.string.tip, R.string.confirm,
							R.string.authentic_limit);
				}

			} else {
				commit.setClickable(false);
				commit.setBackground(getResources().getDrawable(
						R.drawable.gray_btn_bg1));
				TipDialog.showDialogNoClose(SelfHelpServiceActivity.this,
						R.string.tip, R.string.confirm,
						R.string.login_info_tip, LoginActivity.class);
			}

			break;
		case R.id.dealDateLy:
			SelfHelpDateDialog timeChoiceDialog = new SelfHelpDateDialog(
					SelfHelpServiceActivity.this);
			timeChoiceDialog.Show();
			break;
		default:
			break;
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

	private void openAddImg() {
		// TODO Auto-generated method stub
		InputSoftUtil.hideSoftInput(this);
		albumPopupWindow.show(findViewById(R.id.scrollview));
//		showPickDialog();
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
				AbDialogUtil.removeDialog(SelfHelpServiceActivity.this);
				// 从相册中去获取
				try {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
					intent.setType("image/*");
					startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
				} catch (ActivityNotFoundException e) {
					AbToastUtil.showToast(SelfHelpServiceActivity.this,
							"没有找到照片");
				}
			}

		});

		camButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AbDialogUtil.removeDialog(SelfHelpServiceActivity.this);
				doPickPhotoAction();
			}

		});

		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AbDialogUtil.removeDialog(SelfHelpServiceActivity.this);
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
			AbToastUtil.showToast(SelfHelpServiceActivity.this, "没有找到照片");
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
			AbToastUtil.showToast(SelfHelpServiceActivity.this, "没有可用的存储卡");
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
			AbToastUtil.showToast(SelfHelpServiceActivity.this, "未找到系统相机程序");
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
					AbToastUtil.showToast(SelfHelpServiceActivity.this,
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

	/**
	 * 
	 * @Title: showSpinWindow
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param mFaultTypePop
	 * @param faultType2
	 * @throws
	 */
	private void showSpinWindow(SpinerPopWindow mFaultTypePop,
			TextView faultType2) {
		// TODO Auto-generated method stub
		mFaultTypePop.setWidth(faultType2.getWidth());
		mFaultTypePop.showAsDropDown(faultType2);
	}

	@Override
	public void onItemDelListener(int position, ImageView picture) {
		adapter.deleteItem(position);
		bitmaps.remove(position);
		itemDecoration.getPictureLists(bitmaps);
	}

	@Override
	public void onItemClickListener(View view, int position) {
		if (bitmaps.size() - 1 == position) {
			openAddImg();
		}
	}

	@Override
	public void onCamere() {
		AlbumUtils.cameraPicture(SelfHelpServiceActivity.this, new AlbumFilesInterface() {
			@Override
			public void setAlbumFiles(ArrayList<AlbumFile> result) {

			}

			@Override
			public void setFilePath(String path) {
				bitmaps.add(bitmaps.size() - 1, ImageUtils.decodeSampledBitmapFromFilePath(BitmapUtil.amendRotatePhoto(path,SelfHelpServiceActivity.this), DisplayUtils.dip2px(SelfHelpServiceActivity.this, 80),
						DisplayUtils.dip2px(SelfHelpServiceActivity.this, 80)));
				itemDecoration.getPictureLists(bitmaps);
				adapter.setData(bitmaps);
			}
		});
	}

	@Override
	public void onAlbum() {
		ArrayList<AlbumFile> albumFiles = new ArrayList<>();
		AlbumUtils.selectAlbume(SelfHelpServiceActivity.this, albumFiles, new AlbumFilesInterface() {
			@Override
			public void setAlbumFiles(ArrayList<AlbumFile> result) {

				for (AlbumFile albumFile : result) {
					if (bitmaps.size() < 7) {
						bitmaps.add(bitmaps.size() - 1, ImageUtils.decodeSampledBitmapFromFilePath(albumFile.getPath(), DisplayUtils.dip2px(SelfHelpServiceActivity.this, 80),
								DisplayUtils.dip2px(SelfHelpServiceActivity.this, 80)));
					}
				}
				itemDecoration.getPictureLists(bitmaps);
				adapter.setData(bitmaps);
			}

			@Override
			public void setFilePath(String path) {

			}
		});
	}

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
			repairTypeId = typesList.get(pos).getTid() + "";
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (UpdateImageUtils.bmp.size() != 0) {
			UpdateImageUtils.bmp.clear();
		}
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
	}

	public class DateRecevier extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("self_date_choice")) {
				resulttime = intent.getStringExtra("date_string");
				serviceYear.setText(resulttime.substring(0,
						resulttime.indexOf("年") + 1));
				serviceMonth.setText(resulttime.substring(
						resulttime.indexOf("年") + 1,
						resulttime.indexOf("月") + 1));
				serviceDay.setText(resulttime.substring(
						resulttime.indexOf("月") + 1,
						resulttime.indexOf("日") + 1));
			}
		}
	}
}
