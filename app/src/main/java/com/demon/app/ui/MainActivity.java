package com.demon.app.ui;

import android.os.Bundle;
import android.view.KeyEvent;

import com.demon.app.ui.fragment.TestFragment;

import androidx.annotation.Nullable;
import androidx.sframe.annotation.AppPageInterface;
import androidx.sframe.manager.PageCacheManager;
import androidx.sframe.navigator.NavOptions;
import androidx.sframe.ui.abs.AbsActivity;

/**
 * Author create by ok on 2019-06-17
 * Email : ok@163.com.
 */
@AppPageInterface(value = false)
public class MainActivity extends AbsActivity {

	@Override
	public int onPageLayoutId(@Nullable Bundle savedInstanceState) {
		return 0;
	}

	@Override
	public void onPageViewCreated(@Nullable Bundle savedInstanceState) {
		NavOptions navOptions = new NavOptions();
		navOptions.setAddToBackStack(false);
		this.getNavController()
				.pushFragment(android.R.id.content, TestFragment.class, navOptions);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			if (this.getSupportFragmentManager().getBackStackEntryCount() <= 0) {
				PageCacheManager.getInstance().quit();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
