package androidx.sframe.utils;

import android.util.Log;

import androidx.annotation.NonNull;

/**
 * @Author create by Zoran on 2019-09-22
 * @Email : 171905184@qq.com
 * @Description :
 */
public final class LoggerCompat {

	private LoggerCompat() {

	}

	public static boolean debug() {
		return SFrameManager.getInstance().getOptions()
				.isLoggerEnabled();
	}

	public static String getTag() {
		return SFrameManager.getInstance().getOptions()
				.getLoggerTag();
	}

	public static void println(@NonNull Object object) {
		if (LoggerCompat.debug()) {
			System.out.println(object);
		}
	}

	public static void i(@NonNull String message) {
		LoggerCompat.i(LoggerCompat.getTag(), message);
	}

	public static void i(@NonNull Throwable throwable) {
		LoggerCompat.i(LoggerCompat.getTag(), throwable.getMessage(), throwable);
	}

	public static void i(@NonNull String message, @NonNull Throwable throwable) {
		LoggerCompat.i(LoggerCompat.getTag(), message, throwable);
	}

	public static void i(@NonNull String tag, @NonNull String message) {
		if (LoggerCompat.debug()) {
			Log.i(tag, message);
		}
	}

	public static void i(@NonNull String tag, @NonNull String message, @NonNull Throwable throwable) {
		if (LoggerCompat.debug()) {
			Log.i(tag, message, throwable);
		}
	}

	public static void d(@NonNull String message) {
		LoggerCompat.d(LoggerCompat.getTag(), message);
	}

	public static void d(@NonNull Throwable throwable) {
		LoggerCompat.d(LoggerCompat.getTag(), throwable.getMessage(), throwable);
	}

	public static void d(@NonNull String message, @NonNull Throwable throwable) {
		LoggerCompat.d(LoggerCompat.getTag(), message, throwable);
	}

	public static void d(@NonNull String tag, @NonNull String message) {
		if (LoggerCompat.debug()) {
			Log.d(tag, message);
		}
	}

	public static void d(@NonNull String tag, @NonNull String message, @NonNull Throwable throwable) {
		if (LoggerCompat.debug()) {
			Log.d(tag, message, throwable);
		}
	}

	public static void e(@NonNull String message) {
		LoggerCompat.e(LoggerCompat.getTag(), message);
	}

	public static void e(@NonNull Throwable throwable) {
		LoggerCompat.e(LoggerCompat.getTag(), throwable.getMessage(), throwable);
	}

	public static void e(@NonNull String message, @NonNull Throwable throwable) {
		LoggerCompat.e(LoggerCompat.getTag(), message, throwable);
	}

	public static void e(@NonNull String tag, @NonNull String message) {
		if (LoggerCompat.debug()) {
			Log.e(tag, message);
		}
	}

	public static void e(@NonNull String tag, @NonNull String message, @NonNull Throwable throwable) {
		if (LoggerCompat.debug()) {
			Log.e(tag, message, throwable);
		}
	}
}
