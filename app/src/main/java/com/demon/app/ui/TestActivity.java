package com.demon.app.ui;

import android.app.Activity;
import android.os.Bundle;

import com.demon.app.R;

import androidx.annotation.Nullable;
import androidx.sframe.annotation.RunWithAsync;
import androidx.sframe.ui.abs.AbsActivity;
import androidx.sframe.utils.Logger;

/**
 * Author create by ok on 2019-06-14
 * Email : ok@163.com.
 */
public class TestActivity extends AbsActivity {

	@Override
	public int onPageLayoutId(@Nullable Bundle savedInstanceState) {
		return R.layout.activity_test;
	}

	@Override
	public void onPageViewCreated(@Nullable Bundle savedInstanceState) {
		this.getPageController()
				.getToolbarController()
				.getToolbarMethod()
				.setPopEnabled(true)
				.setTitleText("TestActivity");
		this.setResult(Activity.RESULT_OK);
	}

	@RunWithAsync
	@Override
	public void onPageDataSourceChanged(@Nullable Object params) {
		Logger.e("运行在 ：" + Thread.currentThread());
	}
}
