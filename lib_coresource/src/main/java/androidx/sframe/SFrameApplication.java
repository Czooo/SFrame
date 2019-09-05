package androidx.sframe;

import android.app.Application;
import android.content.Context;

import androidx.sframe.compat.CoreCompat;
import androidx.multidex.MultiDex;

/**
 * Author create by ok on 2019-06-02
 * Email : ok@163.com.
 */
public class SFrameApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		CoreCompat.init(this);
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
}
