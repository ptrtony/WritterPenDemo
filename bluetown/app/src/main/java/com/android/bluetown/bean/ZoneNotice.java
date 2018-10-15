package com.android.bluetown.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ZoneNotice implements Parcelable {
	private String content;
	private String nid;
	private String img;
	private String createTime;
	private String noticeName;

	public ZoneNotice() {
		// TODO Auto-generated constructor stub
	}

	public ZoneNotice(String content, String nid, String img,
			String createTime, String noticeName) {
		super();
		this.content = content;
		this.nid = nid;
		this.img = img;
		this.createTime = createTime;
		this.noticeName = noticeName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getNoticeName() {
		return noticeName;
	}

	public void setNoticeName(String noticeName) {
		this.noticeName = noticeName;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		/*
		 * 将ZoneNotice的成员写入Parcel， 注：Parcel中的数据是按顺序写入和读取的，即先被写入的就会先被读取出来
		 */
		dest.writeString(content);
		dest.writeString(nid);
		dest.writeString(img);
		dest.writeString(createTime);
		dest.writeString(noticeName);
	}

	// 该静态域是必须要有的，而且名字必须是CREATOR，否则会出错
	public static final Creator<ZoneNotice> CREATOR = new Creator<ZoneNotice>() {

		@Override
		public ZoneNotice createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			// 从Parcel读取通过writeToParcel方法写入的ZoneNotice的相关成员信息
			String content = source.readString();
			String nid = source.readString();
			String img = source.readString();
			String createTime = source.readString();
			String noticeName = source.readString();
			// 更加读取到的信息，创建返回ZoneNotice对象
			return new ZoneNotice(content, nid, img, createTime, noticeName);
		}

		@Override
		public ZoneNotice[] newArray(int size) {
			// TODO Auto-generated method stub
			// 返回ZoneNotice对象数组
			return new ZoneNotice[size];
		}
	};

}
