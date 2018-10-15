package com.android.bluetown.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class Utils {
	public static String[] companyTypes = new String[] { "全部", "房地产", "建筑业",
			"高科技产业", "IT企业", "餐饮业" };
	public static String[] zones = new String[] { "宁波智慧园", "宁波软件园", "凌云产业园",
			"检测认证园" };
	public static String[] productTypes = new String[] { "分类一", "分类二", "分类三",
			"分类四" };
	public static String[] orderNormalTypes = new String[] { "评价", "销量",
			"价格", "上架时间" };
	public static String[] parkYellowPage = new String[] { "全部分类", "餐饮", "健身",
			"运动", "餐厅" };
	public static String[] actionCenter = new String[] { "全部", "展览", "培训",
			"考察", "聚会", "会议" };
	public static String[] publishInfoType = new String[] { "全部分类", "招聘", "融资",
			"租赁", "转让", "产品发布", "活动", "购买", "其他" };
	public static String[] publishInfoType1 = new String[] { "电影", "美食", "KTV",
			"摄影写真", "酒店", "休闲娱乐", "生活服务", "丽人" };
	public static String[] billType = new String[] { "全部", "食堂", "商铺", "社区服务",
			"泊车", "其他" };

	public static List<String> getCompanyType() {
		// TODO Auto-generated method stub
		List<String> typeList = new ArrayList<String>();
		for (int i = 0; i < companyTypes.length; i++) {
			typeList.add(companyTypes[i]);
		}
		return typeList;
	}

	public static List<String> getActionCenter() {
		// TODO Auto-generated method stub
		List<String> typeList = new ArrayList<String>();
		for (int i = 0; i < actionCenter.length; i++) {
			typeList.add(actionCenter[i]);
		}
		return typeList;
	}

	public static List<String> getZones() {
		// TODO Auto-generated method stub
		List<String> zoneList = new ArrayList<String>();
		for (int i = 0; i < zones.length; i++) {
			zoneList.add(zones[i]);
		}
		return zoneList;
	}

	public static List<String> getProductTypes() {
		// TODO Auto-generated method stub
		List<String> productTypeList = new ArrayList<String>();
		for (int i = 0; i < productTypes.length; i++) {
			productTypeList.add(productTypes[i]);
		}
		return productTypeList;
	}

	public static List<String> getparkYellowPage() {
		// TODO Auto-generated method stub
		List<String> productTypeList = new ArrayList<String>();
		for (int i = 0; i < parkYellowPage.length; i++) {
			productTypeList.add(parkYellowPage[i]);
		}
		return productTypeList;
	}

	public static List<String> getOrderNormalList() {
		// TODO Auto-generated method stub
		List<String> orderNormalList = new ArrayList<String>();
		for (int i = 0; i < orderNormalTypes.length; i++) {
			orderNormalList.add(orderNormalTypes[i]);
		}
		return orderNormalList;
	}

	public static List<String> getCompanyPublicInfoTypeList() {
		// TODO Auto-generated method stub
		List<String> orderNormalList = new ArrayList<String>();
		for (int i = 0; i < publishInfoType.length; i++) {
			orderNormalList.add(publishInfoType[i]);
		}
		return orderNormalList;
	}

	public static List<String> getCompanyPublicInfoTypeList1() {
		// TODO Auto-generated method stub
		List<String> orderNormalList = new ArrayList<String>();
		for (int i = 0; i < publishInfoType1.length; i++) {
			orderNormalList.add(publishInfoType1[i]);
		}
		return orderNormalList;
	}

	public static List<String> getBillTypeList() {
		// TODO Auto-generated method stub
		List<String> orderNormalList = new ArrayList<String>();
		for (int i = 0; i < billType.length; i++) {
			orderNormalList.add(billType[i]);
		}
		return orderNormalList;
	}

	public static String bitmap2StrByBase64(Bitmap bit) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bit.compress(Bitmap.CompressFormat.JPEG, 100, bos);// 参数100表示不压缩
		try {
			byte[] bytes = bos.toByteArray();
			return Base64.encodeToString(bytes, Base64.DEFAULT).replace("\n",
					"");
		} catch (OutOfMemoryError e) {
			if (bit != null && !bit.isRecycled()) {
				bit.recycle();
				bit = null;
				System.gc();
			}
			return null;
		}
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/** 获取屏幕分辨率宽 */
	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		// ((Activity)
		// context).getWindowManager().getDefaultDisplay().getMetrics(dm);

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		display.getMetrics(dm);

		return dm.widthPixels;
	}

	public static String String2num(String a) {
		char[] b = a.toCharArray();
		String result = "";
		for (int i = 0; i < b.length; i++) {
			if (("0123456789.").indexOf(b[i] + "") != -1) {
				result += b[i];
			}
		}
		return result;
	}

	public static String SceneList2String(List SceneList) throws IOException {
		// 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		// 然后将得到的字符数据装载到ObjectOutputStream
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				byteArrayOutputStream);
		// writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
		objectOutputStream.writeObject(SceneList);
		// 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
		String SceneListString = new String(Base64.encode(
				byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
		// 关闭objectOutputStream
		objectOutputStream.close();
		return SceneListString;
	}

	@SuppressWarnings("unchecked")
	public static List String2SceneList(String SceneListString)
			throws StreamCorruptedException, IOException,
			ClassNotFoundException {
		byte[] mobileBytes = Base64.decode(SceneListString.getBytes(),
				Base64.DEFAULT);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				mobileBytes);
		ObjectInputStream objectInputStream = new ObjectInputStream(
				byteArrayInputStream);
		List SceneList = (List) objectInputStream.readObject();
		objectInputStream.close();
		return SceneList;
	}
}
