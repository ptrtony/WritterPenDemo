package com.android.bluetown.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import com.ab.util.AbToastUtil;
import com.android.bluetown.app.BlueTownApp;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class FileUtils {
	private static final String OBJECT_PERSISTANCE_DIR = "obj";
	public static String SDPATH = Environment.getExternalStorageDirectory()
			+ "/bluecity/";

	public static boolean saveObjectToFile(Context ctx, String fileName,
			Object obj) {
		try {
			ByteArrayOutputStream mem_out = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(mem_out);
			out.writeObject(obj);
			out.close();
			String absolutePath = ctx.getFilesDir().getAbsolutePath()
					+ OBJECT_PERSISTANCE_DIR;
			File path = new File(absolutePath);
			if (!path.exists()) {
				path.mkdirs();
			}
			FileOutputStream fs = new FileOutputStream(new File(
					path.getAbsolutePath() + File.separator + fileName));
			fs.write(mem_out.toByteArray());
			mem_out.close();
			fs.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void saveBitmap(Bitmap bm, String picName) {
		try {
			if (!isFileExist("")) {
				File tempf = createSDDir("");
			}
			File f = new File(SDPATH, picName + ".JPEG");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File createSDDir(String dirName) throws IOException {
		File dir = new File(SDPATH + dirName);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			System.out.println("createSDDir:" + dir.getAbsolutePath());
			System.out.println("createSDDir:" + dir.mkdir());
		}
		return dir;
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		file.isFile();
		return file.exists();
	}

	public static void delFile(String fileName) {
		File file = new File(SDPATH + fileName);
		if (file.isFile()) {
			file.delete();
		}
		file.exists();
	}

	public static void deleteDir() {
		File dir = new File(SDPATH);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;

		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete();
			else if (file.isDirectory())
				deleteDir();
		}
		dir.delete();
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}

	public static Object readObjectFromFile(Context ctx, String fileName) {
		try {
			String absolutePath = ctx.getFilesDir().getAbsolutePath()
					+ OBJECT_PERSISTANCE_DIR;
			File f = new File(absolutePath);
			if (!f.exists()) {
				f.mkdirs();
				return null;
			}
			FileInputStream is = new FileInputStream(new File(
					f.getAbsolutePath() + File.separator + fileName));
			ObjectInputStream in = new ObjectInputStream(is);
			Object object = null;
			try {
				object = in.readObject();
			} catch (ClassNotFoundException e) {
				// not found;
			}
			in.close();
			is.close();
			return object;
		} catch (Exception e) {
			return null;
		}
	}

	public static void loadImg(final Context context, final String imgName,
			String url) {
		ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
//				Toast.makeText(context, "开始下载...", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				// TODO Auto-generated method stub
				saveMyBitmap(context,imgName, arg2);
			}

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	public static void saveMyBitmap(Context context, String imgName,
			Bitmap mBitmap) {
		File file = getFilePath(context,imgName);

		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(file);

			if (imgName.contains("jpg")) {
				mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			} else {
				mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			}
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fOut != null) {
					fOut.close();
				}
				if (mBitmap != null) {
					mBitmap.recycle();
				}
				scanFileAsync(context, file.getAbsolutePath());
//				Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 扫描指定文件
	public static void scanFileAsync(Context ctx, String filePath) {
		Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		scanIntent.setData(Uri.fromFile(new File(filePath)));
		ctx.sendBroadcast(scanIntent);
	}

	public static File getFilePath(Context context,String fileName) {
		File file = null;
		String filePath = getSDPath();
		makeRootDirectory(context,filePath);
		try {
			file = new File(filePath + fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return file;
	}
	
	public static String getSDPath() {

		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir.toString() + BlueTownApp.IMAGE_PATH;

	}
	
	public static void makeRootDirectory(Context context,String filePath) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {
			AbToastUtil.showToast(context, e.toString());
		}
	}
}
