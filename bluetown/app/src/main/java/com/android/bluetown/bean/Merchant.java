package com.android.bluetown.bean; /**
 * 
 */

import org.litepal.crud.DataSupport;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 商家信息
 * 
 * @author shenyz
 * 
 */
public class Merchant extends DataSupport implements Parcelable {
	// 收藏商家的参数
	private String type;

	private String km;
	private String latitude;
	private String longitude;
	private String meid;
	private String headImg;
	private String merchantName;
	private String logoImg;
	/**
	 * 本地自定义参数查找该商家的时间
	 */
	public String opDate;

	public Merchant() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Merchant(String type, String km, String latitude, String longitude,
			String meid, String headImg, String merchantName, String logoImg) {
		super();
		this.type = type;
		this.km = km;
		this.latitude = latitude;
		this.longitude = longitude;
		this.meid = meid;
		this.headImg = headImg;
		this.merchantName = merchantName;
		this.logoImg = logoImg;
	}

	public Merchant(Parcel source) {
		this.type = source.readString();
		this.km = source.readString();
		this.latitude = source.readString();
		this.longitude = source.readString();
		this.meid = source.readString();
		this.headImg = source.readString();
		this.merchantName = source.readString();
		this.logoImg = source.readString();
	}

	public static final Creator<Merchant> CREATOR = new Creator<Merchant>() {
		@Override
		public Merchant createFromParcel(Parcel in) {
			return new Merchant(in);
		}

		@Override
		public Merchant[] newArray(int size) {
			return new Merchant[size];
		}
	};

	public String getKm() {
		return km;
	}

	public void setKm(String km) {
		this.km = km;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getMeid() {
		return meid;
	}

	public void setMeid(String meid) {
		this.meid = meid;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getLogoImg() {
		return logoImg;
	}

	public void setLogoImg(String logoImg) {
		this.logoImg = logoImg;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOpDate() {
		return opDate;
	}

	public void setOpDate(String opDate) {
		this.opDate = opDate;
	}

	public static Creator<Merchant> CREATE = new Creator<Merchant>() {

		@Override
		public Merchant[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Merchant[size];
		}

		@Override
		public Merchant createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Merchant(source);
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.type);
		dest.writeString(this.km);
		dest.writeString(this.latitude);
		dest.writeString(this.longitude);
		dest.writeString(this.meid);
		dest.writeString(this.headImg);
		dest.writeString(this.merchantName);
		dest.writeString(this.logoImg);
	}
}
