package androidx.sframe.manager;

import android.content.Context;
import android.text.TextUtils;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.sframe.utils.FileCompat;

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
	private SFrameOptions mOptions;

	private SFrameManager() {
		this.mOptions = new SFrameOptions.Builder()
				.build();
	}

	private void preInit(@NonNull Context context) {
		if (this.mReference != null) {
			this.mReference.clear();
		}
		this.mReference = new WeakReference<>(context);
		// init cache path
		if (TextUtils.isEmpty(this.mOptions.getCachePath())) {
			this.setOptions(this.mOptions.rebuild()
					.setCachePath(FileCompat.getLocatDir(context))
					.build());
		}
		// init glide
		Glide.get(context);
	}

	public void setOptions(@NonNull SFrameOptions options) {
		this.mOptions = options;
	}

	public Context getContext() {
		this.assertShouldApplicationInit();
		return this.mReference.get();
	}

	@NonNull
	public SFrameOptions getOptions() {
		return this.mOptions;
	}

	private void assertShouldApplicationInit() {
		if (this.mReference == null) {
			throw new IllegalStateException("Application not context init");
		}
	}
}
