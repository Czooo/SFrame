package com.demon.app.ui;

import android.os.Bundle;

import com.demon.app.R;

import androidx.annotation.Nullable;
import androidx.sframe.ui.abs.AbsActivity;

/**
 * Author create by ok on 2019-06-18
 * Email : ok@163.com.
 */
public class TempActivity extends AbsActivity {

	@Override
	public int onPageLayoutId(@Nullable Bundle savedInstanceState) {
		return R.layout.activity_temp;
	}

	@Override
	public void onPageViewCreated(@Nullable Bundle savedInstanceState) {
		this.getPageController()
				.getToolbarController()
				.getToolbarMethod()
				.setPopEnabled(true)
				.setTitle("TempActivity");
	}
}
