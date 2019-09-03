package androidx.demon.compat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.ref.WeakReference;
import java.security.MessageDigest;

import androidx.annotation.NonNull;
import androidx.demon.BuildConfig;
import androidx.demon.tools.DeviceUuidFactory;

/**
 * Author create by ok on 2018/12/29
 * Email : ok@163.com.
 */
public class CoreCompat {

	private static WeakReference<Context> sContextReference;

	public static Context getContext() {
		if (sContextReference != null) {
			if (sContextReference.get() != null) {
				return sContextReference.get();
			}
		}
		throw new NullPointerException("CoreCompat.Context is null");
	}

	public static void init(Application application) {
		sContextReference = new WeakReference<>(application.getApplicationContext());

		LogCompat.enableDebugLogging(BuildConfig.DEBUG);
		FileCompat.init(application);
	}

	/**
	 * 获取包名
	 */
	public static String getPackageName() {
		return getContext().getPackageName();
	}

	/**
	 * 获取软件版本号
	 */
	public static int getVersionCode() {
		try {
			String packageName = getContext().getPackageName();
			PackageManager mPackageManager = getContext().getPackageManager();
			return mPackageManager.getPackageInfo(packageName, 0).versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			return 0;
		}
	}

	/**
	 * 获取软件版本名称
	 */
	public static String getVersionName() {
		try {
			String packageName = getContext().getPackageName();
			PackageManager mPackageManager = getContext().getPackageManager();
			return mPackageManager.getPackageInfo(packageName, 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			return "1.0.0";
		}
	}

	public static void assertMainThread() {
		if (!isOnMainThread()) {
			throw new IllegalArgumentException("You must call this findAtParent on the main thread");
		}
	}

	public static void assertBackgroundThread() {
		if (!isOnBackgroundThread()) {
			throw new IllegalArgumentException("You must call this findAtParent on a background thread");
		}
	}

	public static boolean isOnMainThread() {
		return Looper.myLooper() == Looper.getMainLooper();
	}

	public static boolean isOnBackgroundThread() {
		return !isOnMainThread();
	}

	/**
	 * 获取屏幕度量(屏幕宽高度等)
	 */
	public static DisplayMetrics getScreenMetrics() {
		WindowManager mManager = (WindowManager) CoreCompat.getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		assert mManager != null;
		DisplayMetrics mMetrics = new DisplayMetrics();
		mManager.getDefaultDisplay().getMetrics(mMetrics);
		return mMetrics;
	}

	/**
	 * 判断当前手机是否是全屏
	 */
	public static boolean isFullScreen(Activity activity) {
		return (activity.getWindow().getAttributes().flags &
				WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
	}

	/**
	 * 从父容器移除View
	 */
	public static View removeInParentView(View view) {
		if (view != null && view.getParent() != null) {
			((ViewGroup) view.getParent()).removeView(view);
		}
		return view;
	}

	/**
	 * 获取状态栏高度
	 */
	public static int getStatusBarHeight() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
			if (resourceId > 0) {
				return getContext().getResources().getDimensionPixelSize(resourceId);
			}
		}
		return 0;
	}

	/**
	 * 获取导航栏高度
	 */
	public static int getNavigationBarHeight() {
		int resourceId = getContext().getResources().getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0) {
			return getContext().getResources().getDimensionPixelSize(resourceId);
		}
		return 0;
	}

	/**
	 * 转换MD5
	 * 对字符串md5加密(小写+字母)
	 */
	@NonNull
	static public String MD5(String s) {
		char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@SuppressLint({"MissingPermission", "HardwareIds"})
	static public String getDeviceId() {
//		return Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
		StringBuilder mBuilder = new StringBuilder();
		mBuilder
				.append("BangU")
				.append("Android")
				.append("Uuid")
				.append(new DeviceUuidFactory(getContext()).getDeviceUuid().toString())
				.append("End");
		LogCompat.e("DeviceId > " + mBuilder.toString() + " MD5 > " + MD5(mBuilder.toString()));
		return MD5(mBuilder.toString());
	}

	public static void openSoftKeyboard(EditText editText) {
		editText.setFocusable(true);
		editText.setFocusableInTouchMode(true);
		editText.requestFocus();
		InputMethodManager mInputMethodManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (mInputMethodManager != null) {
			mInputMethodManager.showSoftInput(editText, 0);
//			mInputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
//			mInputMethodManager.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
//			mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
		}
	}

	public static void closeSoftKeyboard(Context context) {
		if (context == null || !(context instanceof Activity) || ((Activity) context).getCurrentFocus() == null) {
			return;
		}
		try {
			View children = ((Activity) context).getCurrentFocus();
			if (children != null) {
				children.clearFocus();
				InputMethodManager mInputMethodManager = (InputMethodManager) children.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				if (mInputMethodManager != null) {
					mInputMethodManager.hideSoftInputFromWindow(children.getWindowToken(), 0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭软键盘
	 * 当使用全屏主题的时候,View屏蔽了焦点.关闭软键盘时,直接指定 closeSoftKeyboard(EditView)
	 */
	public static void closeSoftKeyboard(View view) {
		if (view == null || view.getWindowToken() == null) {
			return;
		}
		InputMethodManager mInputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (mInputMethodManager != null) {
			mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
}
