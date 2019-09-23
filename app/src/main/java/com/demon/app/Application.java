package com.demon.app;

import androidx.sframe.BuildConfig;
import androidx.sframe.SFrameApplication;
import androidx.sframe.utils.FileCompat;
import androidx.sframe.manager.SFrameManager;
import androidx.sframe.manager.SFrameOptions;

/**
 * Author create by ok on 2019-06-14
 * Email : ok@163.com.
 */
public class Application extends SFrameApplication {

	@Override
	public void onCreate() {
		super.onCreate();

		SFrameManager.init(this);
		//  default options
		SFrameManager.getInstance()
				.setOptions(SFrameOptions.obtain()
						.setLogger(BuildConfig.DEBUG)
						.setLoggerTag(this.getPackageName())
						.setCachePath(FileCompat.getLocatDir(this))
						.build());
	}
}
