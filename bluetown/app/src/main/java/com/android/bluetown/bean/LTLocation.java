package com.android.bluetown.bean;

import java.io.Serializable;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

public class LTLocation implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5784712120993600849L;
	public String city;
	public String street;
	public String district;
	public double longitude;
	public String province;
	public double latitude;
	public String streetNumber;
	public float radius;
	
	

	public static LTLocation convertJSONObject(String result) {
		if (result == null || result.length() == 0)
			return null;
		LTLocation object = new LTLocation();
		try {
			JSONObject jsonObj = new JSONObject(result);
			Iterator<String> keyIter1 = null;
			for (keyIter1 = jsonObj.keys(); keyIter1.hasNext();) {
				String key1 = keyIter1.next();
				Object valueObj1 = jsonObj.get(key1);
				if (key1.equals("city")) {
					object.city = (String) valueObj1;
				} else if (key1.equals("street")) {
					object.street = (String) valueObj1;
				} else if (key1.equals("district")) {
					object.district = (String) valueObj1;
				} else if (key1.equals("longitude")) {
					object.longitude = Double.valueOf(valueObj1 + "");
				} else if (key1.equals("province")) {
					object.province = (String) valueObj1;
				} else if (key1.equals("streetNumber")) {
					object.streetNumber = (String) valueObj1;
				} else if (key1.equals("latitude")) {
					object.latitude = Double.valueOf(valueObj1 + "");
				} else if (key1.equals("radius")) {
					object.radius = Float.valueOf(valueObj1 + "");
				}
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}
}
