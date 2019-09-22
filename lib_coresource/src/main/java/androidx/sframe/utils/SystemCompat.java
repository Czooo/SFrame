package androidx.sframe.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import java.security.MessageDigest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sframe.tools.DeviceUuidFactory;

/**
 * @Author create by Zoran on 2019-09-22
 * @Email : 171905184@qq.com
 * @Description :
 */
public class SystemCompat {

	/**
	 * 获取包名
	 */
	public static String getPackageName(@NonNull Context context) {
		return context.getPackageName();
	}

	/**
	 * 获取软件版本名称
	 */
	public static String getVersionName(@NonNull Context context) {
		try {
			String packageName = context.getPackageName();
			PackageManager mPackageManager = context.getPackageManager();
			return mPackageManager.getPackageInfo(packageName, 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			return "1.0.0";
		}
	}

	/**
	 * 获取软件版本号
	 */
	public static int getVersionCode(@NonNull Context context) {
		try {
			String packageName = context.getPackageName();
			PackageManager mPackageManager = context.getPackageManager();
			return mPackageManager.getPackageInfo(packageName, 0).versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			return 0;
		}
	}

	/**
	 * 获取状态栏高度
	 */
	public static int getStatusBarHeight(@NonNull Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
			if (resourceId > 0) {
				return context.getResources().getDimensionPixelSize(resourceId);
			}
		}
		return 0;
	}

	/**
	 * 获取导航栏高度
	 */
	public static int getNavigationBarHeight(@NonNull Context context) {
		int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0) {
			return context.getResources().getDimensionPixelSize(resourceId);
		}
		return 0;
	}

	/**
	 * 获取屏幕度量(屏幕宽高度等)
	 */
	public static DisplayMetrics getScreenMetrics(@NonNull Context context) {
		WindowManager mManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		assert mManager != null;
		DisplayMetrics mMetrics = new DisplayMetrics();
		mManager.getDefaultDisplay().getMetrics(mMetrics);
		return mMetrics;
	}

	/**
	 * 打开软键盘
	 */
	public static void openSoftKeyboard(@NonNull EditText editText) {
		editText.setFocusable(true);
		editText.setFocusableInTouchMode(true);
		editText.requestFocus();
		InputMethodManager mInputMethodManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (mInputMethodManager != null) {
			mInputMethodManager.showSoftInput(editText, 0);
		}
	}

	/**
	 * 关闭软键盘
	 */
	public static void closeSoftKeyboard(@NonNull Context context) {
		if (!(context instanceof Activity) || ((Activity) context).getCurrentFocus() == null) {
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
	public static void closeSoftKeyboard(@Nullable View view) {
		if (view == null || view.getWindowToken() == null) {
			return;
		}
		InputMethodManager mInputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (mInputMethodManager != null) {
			mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	/**
	 * 判断当前手机是否是全屏
	 */
	public static boolean isFullScreen(@NonNull Activity activity) {
		return (activity.getWindow().getAttributes().flags &
				WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
	}

	/**
	 * 确保在主线程
	 */
	public static void assertMainThread() {
		if (!isOnMainThread()) {
			throw new IllegalArgumentException("You must call this findAtParent on the main thread");
		}
	}

	/**
	 * 确保在后台线程
	 */
	public static void assertBackgroundThread() {
		if (!isOnBackgroundThread()) {
			throw new IllegalArgumentException("You must call this findAtParent on a background thread");
		}
	}

	/**
	 * 是否在主线程
	 */
	public static boolean isOnMainThread() {
		return Looper.myLooper() == Looper.getMainLooper();
	}

	/**
	 * 是否在后台线程
	 */
	public static boolean isOnBackgroundThread() {
		return !isOnMainThread();
	}

	/**
	 * 从父容器移除View
	 */
	public static void removeInParentView(@Nullable View view) {
		if (view != null && view.getParent() != null) {
			((ViewGroup) view.getParent()).removeView(view);
		}
	}

	@SuppressLint({"MissingPermission", "HardwareIds"})
	public static String getDeviceId(@NonNull Context context) {
		StringBuilder mBuilder = new StringBuilder();
		mBuilder
				.append("SFrame")
				.append("Android")
				.append("Uuid")
				.append(new DeviceUuidFactory(context).getDeviceUuid().toString())
				.append("End");
		return md5(mBuilder.toString());
	}

	/**
	 * 转换MD5
	 * 对字符串md5加密(小写+字母)
	 */
	@NonNull
	public static String md5(@NonNull String s) {
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
}
