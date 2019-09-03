package com.demon.app;

import androidx.demon.compat.LogCompat;
import androidx.demon.ui.abs.AbsApplication;

/**
 * Author create by ok on 2019-06-14
 * Email : ok@163.com.
 */
public class Application extends AbsApplication {

	@Override
	public void onCreate() {
		super.onCreate();

		LogCompat.enableDebugLogging(true);
	}
}
