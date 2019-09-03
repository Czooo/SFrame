package com.demon.app.ui.dialog;

import android.os.Bundle;

import com.demon.app.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.demon.ui.abs.AbsPopupWindow;
import androidx.demon.ui.controller.AppPageController;

/**
 * Author create by ok on 2019-06-19
 * Email : ok@163.com.
 */
public class TestPopupWindow4 extends AbsPopupWindow {

	public TestPopupWindow4(@NonNull AppPageController<?> supperPageController) {
		super(supperPageController);
	}

	@Override
	public int onPageLayoutId(@Nullable Bundle savedInstanceState) {
		return R.layout.dialog_popupwindow_test_4;
	}

	@Override
	public void onPageViewCreated(@Nullable Bundle savedInstanceState) {

	}
}