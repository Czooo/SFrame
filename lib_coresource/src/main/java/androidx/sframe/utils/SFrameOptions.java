package androidx.sframe.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.Serializable;

import androidx.annotation.NonNull;

/**
 * @Author create by Zoran on 2019-09-22
 * @Email : 171905184@qq.com
 * @Description :
 */
public class SFrameOptions implements Serializable {

	public static final String DIR_FILE = "f_file";
	public static final String DIR_VIDEO = "f_video";
	public static final String DIR_AUDIO = "f_audio";
	public static final String DIR_IMAGE = "f_image";

	@NonNull
	public static SFrameOptions.Builder obtain() {
		return SFrameManager.getInstance().getOptions().rebuild();
	}

	private final String mCachePath;
	private final String mLoggerTag;
	private final boolean mIsLoggerEnabled;

	private SFrameOptions(@NonNull Builder builder) {
		this.mCachePath = builder.mCachePath;
		this.mLoggerTag = builder.mLoggerTag;
		this.mIsLoggerEnabled = builder.mIsLoggerEnabled;
	}

	@NonNull
	public final String getFileCachePath() {
		return this.getCachePathOf(DIR_FILE);
	}

	@NonNull
	public final String getVideoCachePath() {
		return this.getCachePathOf(DIR_VIDEO);
	}

	@NonNull
	public final String getAudioCachePath() {
		return this.getCachePathOf(DIR_AUDIO);
	}

	@NonNull
	public final String getImageCachePath() {
		return this.getCachePathOf(DIR_IMAGE);
	}

	@NonNull
	public final String getCachePath() {
		if (TextUtils.isEmpty(this.mCachePath)) {
			throw new IllegalStateException("not cache path set");
		}
		final File cacheFile = new File(this.mCachePath);
		return FileCompat.getAbsolutePath(cacheFile);
	}

	@NonNull
	public final String getCachePathOf(String dirName) {
		final String cachePath = this.getCachePath();
		final File cacheFileDir = new File(cachePath + File.separator + dirName);
		return FileCompat.getAbsolutePath(cacheFileDir);
	}

	@NonNull
	public final String getLoggerTag() {
		return this.mLoggerTag;
	}

	public final boolean isLoggerEnabled() {
		return this.mIsLoggerEnabled;
	}

	public final Builder rebuild() {
		return new Builder(this);
	}

	public static class Builder {

		private String mCachePath;
		private String mLoggerTag = "androidx.sframe";
		private boolean mIsLoggerEnabled;

		public Builder() {

		}

		public Builder(@NonNull SFrameOptions options) {
			this.mCachePath = options.mCachePath;
			this.mLoggerTag = options.mLoggerTag;
			this.mIsLoggerEnabled = options.mIsLoggerEnabled;
		}

		public Builder setCachePath(String cachePath) {
			this.mCachePath = cachePath;
			return this;
		}

		public Builder setLogger(boolean enabled) {
			this.mIsLoggerEnabled = enabled;
			return this;
		}

		public Builder setLoggerTag(@NonNull String tag) {
			if (!TextUtils.isEmpty(tag)) {
				this.mLoggerTag = tag;
			}
			return this;
		}

		public SFrameOptions build() {
			return new SFrameOptions(this);
		}
	}
}
