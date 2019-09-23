package com.demon.app.ui.fragment.test;

import android.os.Bundle;

import com.demon.app.R;

import androidx.annotation.Nullable;
import androidx.sframe.ui.abs.AbsFragment;

/**
 * Author create by ok on 2019-06-17
 * Email : ok@163.com.
 */
public class TempFragment3 extends AbsFragment {

	@Override
	public int onPageLayoutId(@Nullable Bundle savedInstanceState) {
		return R.layout.fragment_temp_3;
	}

	@Override
	public void onPageViewCreated(@Nullable Bundle savedInstanceState) {
		this.getPageController()
				.getToolbarController()
				.getToolbarMethod()
				.setTitleText("TempFragment3");
	}
}
