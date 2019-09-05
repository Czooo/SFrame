package com.demon.app.ui;

import android.os.Bundle;

import com.demon.app.ui.fragment.TestFragment;

import androidx.annotation.Nullable;
import androidx.sframe.ui.abs.AbsActivity;

/**
 * Author create by ok on 2019-06-17
 * Email : ok@163.com.
 */
public class MainActivity extends AbsActivity {

	@Override
	public int onPageLayoutId(@Nullable Bundle savedInstanceState) {
		return 0;
	}

	@Override
	public void onPageViewCreated(@Nullable Bundle savedInstanceState) {
		this.getPageController()
				.getAppNavController()
				.pushPage(TestFragment.class);
	}
}
