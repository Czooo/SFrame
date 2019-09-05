package androidx.sframe.compat;

import android.text.TextUtils;
import android.widget.Toast;

/**
 * 作者：Administrator on 2016/9/22 15:20
 * 邮箱：Zoran@kewaimiao.com
 */
public class ToastCompat {

	static public void toast(final String t) {
		if (!TextUtils.isEmpty(t)) {
			Toast.makeText(CoreCompat.getContext(), String.valueOf(t), Toast.LENGTH_SHORT).show();
		}
	}

	static public void toastDebug(final String t) {
		if (LogCompat.debug() && !TextUtils.isEmpty(t)) {
			Toast.makeText(CoreCompat.getContext(), String.format("Debug : %s", t), Toast.LENGTH_SHORT).show();
		}
	}
}
