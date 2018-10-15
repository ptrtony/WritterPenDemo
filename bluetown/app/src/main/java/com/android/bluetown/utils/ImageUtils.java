package com.android.bluetown.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.Base64;

import com.android.bluetown.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ImageUtils {
	/**
	 * 
	 * 
	 * @param imagePath
	 * @param width
	 * @param height
	 * @return
	 * @return Bitmap
	 */
	public static Bitmap getImageThumbnail(String imagePath, int width,
			int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; //

		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	/**
	 * 
	 * @param videoPath
	 * @param width
	 * @param height
	 * @param kind
	 * @return
	 * @return Bitmap
	 */
	public static Bitmap getVideoThumbnail(String videoPath, int width,
			int height, int kind) {
		Bitmap bitmap = null;
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		System.out.println("w" + bitmap.getWidth());
		System.out.println("h" + bitmap.getHeight());
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	/**
	 * 
	 * @Title: compressBmpToFile
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param bmp
	 * @param file
	 * @throws
	 */
	public static void compressBmpToFile(Bitmap bmp, File file) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 80;
		bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		while (baos.toByteArray().length / 1024 > 100) {
			baos.reset();
			options -= 10;
			bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baos.toByteArray());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title: compressImage
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param image
	 * @return
	 * @throws
	 */
	private static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 质量压缩方法，这里80表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 80) { // 循环判断如果压缩后图片是否大于80kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public static Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是1280*800分辨率，所以高和宽我们设置为
		float hh = 1280f;// 这里设置高度为1280f
		float ww = 800f;// 这里设置宽度为800f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		// return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
		return bitmap;
	}

	// ��ͼƬ��������Сѹ���ļ�
	public static Bitmap comp(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// �ж����ͼƬ����1M,����ѹ������������ͼƬ��BitmapFactory.decodeStream��ʱ���
			baos.reset();// ����baos�����baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// ����ѹ��50%����ѹ��������ݴ�ŵ�baos��
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// ���������ֻ��Ƚ϶���800*480�ֱ��ʣ����ԸߺͿ���������Ϊ
		float hh = 800f;// �������ø߶�Ϊ800f
		float ww = 480f;// �������ÿ��Ϊ480f
		int be = 1;// be=1��ʾ������
		if (w > h && w > ww) {// �����ȴ�Ļ����ݿ�ȹ̶���С����
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// ����߶ȸߵĻ����ݿ�ȹ̶���С����
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// �������ű���
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// ѹ���ñ�����С���ٽ�������ѹ��
	}

	public static DisplayImageOptions getErrorBig() {
		DisplayImageOptions option = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_msg_empty)
				.showImageForEmptyUri(R.drawable.ic_msg_empty)
				.showImageOnFail(R.drawable.ic_msg_empty).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return option;
	}

	public static DisplayImageOptions getErrorSmall() {
		DisplayImageOptions option = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_msg_empty)
				.showImageForEmptyUri(R.drawable.ic_msg_empty)
				.showImageOnFail(R.drawable.ic_msg_empty).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return option;
	}

	public static DisplayImageOptions getCirleGreyError() {
		DisplayImageOptions option = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_msg_empty)
				.showImageOnFail(R.drawable.ic_msg_empty).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(360)).build();
		return option;
	}

	public static DisplayImageOptions getCirleError() {
		DisplayImageOptions option = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_msg_empty)
				.showImageOnFail(R.drawable.ic_msg_empty).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(360)).build();
		return option;
	}

	public static DisplayImageOptions getCirleMin() {
		DisplayImageOptions option = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_msg_empty)
				.showImageOnFail(R.drawable.ic_msg_empty).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(360)).build();
		return option;
	}

	public static DisplayImageOptions getFangLogo() {
		DisplayImageOptions option = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_msg_empty)
				.showImageOnFail(R.drawable.ic_msg_empty).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return option;
	}

	public static DisplayImageOptions getGreyCicleFang() {
		DisplayImageOptions circle = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_msg_empty)
				.showImageOnFail(R.drawable.ic_msg_empty).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(360)).build();
		return circle;
	}

	//请求网络图片
	public static Bitmap returnBitMap(String url){
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection)myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}



	public static int calculateInSampleSize(BitmapFactory.Options options,

											int reqWidth, int reqHeight) {

		// 源图片的高度和宽度
		final int height = options.outHeight;

		final int width = options.outWidth;


		int inSampleSize = 1;


		if (height > reqHeight || width > reqWidth) {


			// 计算出实际宽高和目标宽高的比率


			final int heightRatio = Math.round((float) height / (float) reqHeight);


			final int widthRatio = Math.round((float) width / (float) reqWidth);


			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高


			// 一定都会大于等于目标的宽和高。


			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;


		}


		return inSampleSize;


	}
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
														 int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小


		final BitmapFactory.Options options = new BitmapFactory.Options();


		options.inJustDecodeBounds = true;


		BitmapFactory.decodeResource(res, resId, options);


		// 调用上面定义的方法计算inSampleSize值


		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);


		// 使用获取到的inSampleSize值再次解析图片


		options.inJustDecodeBounds = false;


		return BitmapFactory.decodeResource(res, resId, options);


	}


	public static Bitmap decodeSampledBitmapFromFilePath(String imagePath,

														 int reqWidth, int reqHeight) {

		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小


		final BitmapFactory.Options options = new BitmapFactory.Options();


		options.inJustDecodeBounds = true;


		BitmapFactory.decodeFile(imagePath, options);


		// 调用上面定义的方法计算inSampleSize值


		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);


		// 使用获取到的inSampleSize值再次解析图片


		options.inJustDecodeBounds = false;


		return BitmapFactory.decodeFile(imagePath, options);


	}

	/**

	 * 图片转成string

	 *

	 * @param bitmap

	 * @return

	 */

	public static String convertIconToString(Bitmap bitmap)

	{

		ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream

		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

		byte[] appicon = baos.toByteArray();// 转为byte数组

		return Base64.encodeToString(appicon, Base64.DEFAULT);



	}

	public static Bitmap stringtoBitmap(String string){
		//将字符串转换成Bitmap类型
		Bitmap bitmap=null;
		try {
			byte[] bitmapArray;
			bitmapArray=Base64.decode(string, Base64.DEFAULT);
			bitmap=BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
