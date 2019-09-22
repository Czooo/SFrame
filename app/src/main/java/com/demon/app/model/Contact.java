package com.demon.app.model;

import androidx.annotation.NonNull;

/**
 * @Author create by Zoran on 2019-09-15
 * @Email : 171905184@qq.com
 * @Description :
 */
public class Contact {

	private int identiy;
	private String userName;
	private String phoneNo;

	public Contact(@NonNull String userName, @NonNull String phoneNo) {
		this.userName = userName;
		this.phoneNo = phoneNo;
	}

	public void setIdentiy(int identiy) {
		this.identiy = identiy;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public int getIdentiy() {
		return this.identiy;
	}

	public String getUserName() {
		return this.userName;
	}

	public String getPhoneNo() {
		return this.phoneNo;
	}

	@NonNull
	@Override
	public String toString() {
		return userName + " : " + phoneNo;
	}
}
