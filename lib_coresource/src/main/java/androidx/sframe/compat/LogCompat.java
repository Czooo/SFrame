package androidx.sframe.compat;

/**
 * 作者：Administrator on 2016/9/21 17:48
 * 邮箱：Zoran@kewaimiao.com
 */
public class LogCompat {

	private static boolean debug = false;

	private static String tag = "Project";

	static public void enableDebugLogging(boolean debug) {
		LogCompat.enableDebugLogging(tag, debug);
	}

	static public void enableDebugLogging(String tag, boolean debug) {
		LogCompat.tag = tag;
		LogCompat.debug = debug;
	}

	static public boolean debug() {
		return debug;
	}

	static public String tag() {
		return tag;
	}

	static public void i(String i) {
		if (debug()) {
			android.util.Log.i(tag(), i, null);
		}
	}

	public static void i(String tag, String i) {
		if (debug()) {
			android.util.Log.i(tag, i, null);
		}
	}

	static public void i(Throwable tr) {
		if (debug()) {
			android.util.Log.i(tag(), tr.getMessage(), tr);
		}
	}

	static public void i(String i, Throwable tr) {
		if (debug()) {
			android.util.Log.i(tag(), i, tr);
		}
	}

	static public void d(String i) {
		if (debug()) {
			android.util.Log.d(tag(), i, null);
		}
	}

	public static void d(String tag, String i) {
		if (debug()) {
			android.util.Log.d(tag, i, null);
		}
	}

	static public void d(Throwable tr) {
		if (debug()) {
			android.util.Log.d(tag(), tr.getMessage(), tr);
		}
	}

	static public void d(String i, Throwable tr) {
		if (debug()) {
			android.util.Log.d(tag(), i, tr);
		}
	}

	static public void e(String i) {
		if (debug()) {
			android.util.Log.e(tag(), i, null);
		}
	}

	public static void e(String tag, String i) {
		if (debug()) {
			android.util.Log.e(tag, i, null);
		}
	}

	static public void e(Throwable tr) {
		if (debug()) {
			android.util.Log.e(tag(), tr.getMessage(), tr);
		}
	}

	static public void e(String i, Throwable tr) {
		if (debug()) {
			android.util.Log.e(tag(), i, tr);
		}
	}

	public static void format(Object... objects) {
		format("Index", objects);
	}

	public static void format(String key, Object... objects) {
		if (debug()) {
			final StringBuilder mBuilder = new StringBuilder();
			mBuilder.append(String.format("\n\n=========%s Start=========\n", key));

			for (int index = 0; index < objects.length; index++) {
				if (objects[index] == null) {
					continue;
				}
				mBuilder.append(String.format("[%s] %s : ", objects[index].getClass().getSimpleName(), (index + 1))).append(String.valueOf(objects[index])).append("\n");
			}
			mBuilder.append(String.format("=========%s End=========\n\n", key));
			e(mBuilder.toString());
		}
	}
}
