package androidx.sframe.utils;

import android.text.TextUtils;
import android.widget.Toast;

import androidx.sframe.manager.SFrameManager;

/**
 * @Author create by Zoran on 2019-09-22
 * @Email : 171905184@qq.com
 * @Description :
 */
public class ToastCompat {

	private ToastCompat() {

	}

	public static void toast(final String t) {
		if (!TextUtils.isEmpty(t)) {
			Toast.makeText(SFrameManager.getInstance().getContext(), String.valueOf(t), Toast.LENGTH_SHORT).show();
		}
	}

	public static void toastDebug(final String t) {
		if (Logger.debug() && !TextUtils.isEmpty(t)) {
			Toast.makeText(SFrameManager.getInstance().getContext(), String.format("Debug : %s", t), Toast.LENGTH_SHORT).show();
		}
	}
}
