package com.android.bluetown.my;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.R.color;
import com.android.bluetown.R.id;
import com.android.bluetown.R.layout;
import com.android.bluetown.R.string;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.EncodingHandler;
import com.google.zxing.WriterException;

public class MyQRActivity extends TitleBarActivity  {
	private ImageView my_QR;
	private FinalDb db;
	private String telnum = "";
	private SharePrefUtils prefUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(layout.ac_myqr);
		BlueTownExitHelper.addActivity(this);
		prefUtils = new SharePrefUtils(this);
		db = FinalDb.create(this);
		initView();
	}

	

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(string.my_QR);
		setTitleLayoutBg(color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}
	private void initView(){
		my_QR=(ImageView)findViewById(id.my_QR);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				telnum = user.getUsername();
			}
		}
		final Resources r = getResources();
		try {
			Bitmap qrCodeBitmap = EncodingHandler.createQRCode("userCode="+prefUtils.getString(SharePrefUtils.CODE, "0"), getW()/5*4);
			
			//------------------添加logo部分------------------//
//			if(getIntent().getParcelableExtra("bitmap")!=null){
//				Bitmap logoBmp = getIntent().getParcelableExtra("bitmap");
//				
//				//二维码和logo合并
//				Bitmap bitmap = Bitmap.createBitmap(qrCodeBitmap.getWidth(), qrCodeBitmap
//		                .getHeight(), qrCodeBitmap.getConfig());
//		        Canvas canvas = new Canvas(bitmap);
//		        //二维码
//		        canvas.drawBitmap(qrCodeBitmap, 0,0, null);
//		        //logo绘制在二维码中央
//				canvas.drawBitmap(logoBmp, qrCodeBitmap.getWidth() / 2
//						- logoBmp.getWidth() / 2, qrCodeBitmap.getHeight()
//						/ 2 - logoBmp.getHeight() / 2, null);
//				//------------------添加logo部分------------------//			
//				my_QR.setImageBitmap(bitmap);
//			}else{
				my_QR.setImageBitmap(qrCodeBitmap);
//			}
			
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	/**
	 * @return 获取屏幕宽度
	 */
	public int getW() {
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		int i = outMetrics.widthPixels;
		return i;
	}
}