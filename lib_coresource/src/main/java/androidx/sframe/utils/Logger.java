package androidx.sframe.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.sframe.manager.SFrameManager;

/**
 * @Author create by Zoran on 2019-09-22
 * @Email : 171905184@qq.com
 * @Description :
 */
public final class Logger {

	private Logger() {

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
		if (Logger.debug()) {
			System.out.println(object);
		}
	}

	public static void i(@NonNull String message) {
		Logger.i(Logger.getTag(), message);
	}

	public static void i(@NonNull Throwable throwable) {
		Logger.i(Logger.getTag(), throwable.getMessage(), throwable);
	}

	public static void i(@NonNull String message, @NonNull Throwable throwable) {
		Logger.i(Logger.getTag(), message, throwable);
	}

	public static void i(@NonNull String tag, @NonNull String message) {
		if (Logger.debug()) {
			Log.i(tag, message);
		}
	}

	public static void i(@NonNull String tag, @NonNull String message, @NonNull Throwable throwable) {
		if (Logger.debug()) {
			Log.i(tag, message, throwable);
		}
	}

	public static void d(@NonNull String message) {
		Logger.d(Logger.getTag(), message);
	}

	public static void d(@NonNull Throwable throwable) {
		Logger.d(Logger.getTag(), throwable.getMessage(), throwable);
	}

	public static void d(@NonNull String message, @NonNull Throwable throwable) {
		Logger.d(Logger.getTag(), message, throwable);
	}

	public static void d(@NonNull String tag, @NonNull String message) {
		if (Logger.debug()) {
			Log.d(tag, message);
		}
	}

	public static void d(@NonNull String tag, @NonNull String message, @NonNull Throwable throwable) {
		if (Logger.debug()) {
			Log.d(tag, message, throwable);
		}
	}

	public static void e(@NonNull String message) {
		Logger.e(Logger.getTag(), message);
	}

	public static void e(@NonNull Throwable throwable) {
		Logger.e(Logger.getTag(), throwable.getMessage(), throwable);
	}

	public static void e(@NonNull String message, @NonNull Throwable throwable) {
		Logger.e(Logger.getTag(), message, throwable);
	}

	public static void e(@NonNull String tag, @NonNull String message) {
		if (Logger.debug()) {
			Log.e(tag, message);
		}
	}

	public static void e(@NonNull String tag, @NonNull String message, @NonNull Throwable throwable) {
		if (Logger.debug()) {
			Log.e(tag, message, throwable);
		}
	}
}
