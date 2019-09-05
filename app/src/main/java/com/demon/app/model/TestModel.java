package com.demon.app.model;

import androidx.sframe.model.AbsModel;

/**
 * Author create by ok on 2019-06-17
 * Email : ok@163.com.
 */
public class TestModel extends AbsModel {

	private final int id;
	private final String title;

	public TestModel(int id, String title) {
		this.id = id;
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}
}
