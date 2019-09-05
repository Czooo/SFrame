package androidx.sframe.model;

import android.text.TextUtils;

import androidx.annotation.DrawableRes;

/**
 * Author create by ok on 2019-06-15
 * Email : ok@163.com.
 */
public class AppMenuModel extends AbsModel {

	private int id;

	@DrawableRes
	private int resId;

	private String title;

	private int position;

	public AppMenuModel(int id, @DrawableRes int resId) {
		this.id = id;
		this.resId = resId;
	}

	public AppMenuModel(int id, String title) {
		this.id = id;
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public int getPosition() {
		return position;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public int getResId() {
		return resId;
	}

	public boolean isImage() {
		return resId != 0;
	}

	public boolean isText() {
		return !TextUtils.isEmpty(title);
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
