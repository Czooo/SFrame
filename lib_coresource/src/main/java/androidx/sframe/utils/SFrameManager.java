package androidx.sframe.utils;

import android.content.Context;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;

/**
 * @Author create by Zoran on 2019-09-22
 * @Email : 171905184@qq.com
 * @Description :
 */
public final class SFrameManager {

	private static final class Helper {
		private static final SFrameManager INSTANCE = new SFrameManager();
	}

	public static void init(@NonNull Context context) {
		SFrameManager.getInstance().preInit(context);
	}

	@NonNull
	public static SFrameManager getInstance() {
		return Helper.INSTANCE;
	}

	private WeakReference<Context> mReference;
	private SFrameOptions mSFrameOptions;

	private SFrameManager() {
		this.mSFrameOptions = new SFrameOptions.Builder()
				.setLoggerTag("androidx.sframe")
				.setLogger(false)
				.build();
	}

	private void preInit(@NonNull Context context) {
		if (this.mReference != null) {
			this.mReference.clear();
		}
		this.mReference = new WeakReference<>(context);
	}

	public void setOptions(@NonNull SFrameOptions options) {
		this.mSFrameOptions = options;
	}

	public Context getContext() {
		this.assertShouldApplicationInit();
		return this.mReference.get();
	}

	@NonNull
	public SFrameOptions getOptions() {
		return this.mSFrameOptions;
	}

	private void assertShouldApplicationInit() {
		if (this.mReference == null) {
			throw new IllegalStateException("Application not context init");
		}
	}
}
