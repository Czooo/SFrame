package com.demon.app.ui.dialog;

import android.os.Bundle;

import com.demon.app.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.demon.ui.abs.AbsDialogFragment;
import androidx.demon.ui.controller.AppPageController;

/**
 * Author create by ok on 2019-06-14
 * Email : ok@163.com.
 */
public class TestDialogFragment3 extends AbsDialogFragment {

	public TestDialogFragment3(@NonNull AppPageController<?> supperPageController) {
		super(supperPageController);
	}

	@Override
	public int onPageLayoutId(@Nullable Bundle savedInstanceState) {
		return R.layout.dialog_fragment_test_3;
	}

	@Override
	public void onPageViewCreated(@Nullable Bundle savedInstanceState) {
	}
}
