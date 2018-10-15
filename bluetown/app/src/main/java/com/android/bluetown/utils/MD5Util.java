package com.android.bluetown.utils;

import java.security.MessageDigest;

public class MD5Util {

	public static String encoder(String str){
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] data = digest.digest(str.getBytes());
			StringBuilder sb = new StringBuilder();
			for(int i = 0;i< data.length;i++){
				String result = Integer.toHexString(data[i]&0xff);
				String temp = null;
				if(result.length() == 1){
					temp = "0" + result;
				}else{
					temp = result;
				}
				sb.append(temp);
			}
			return sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


}
