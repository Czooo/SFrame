package androidx.sframe.utils;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

/**
 * @Author create by Zoran on 2019-09-18
 * @Email : 171905184@qq.com
 * @Description :
 */
public class Collections {

	private Collections() {

	}

	@SafeVarargs
	public static <T> List<T> toList(@NonNull T... values) {
		return Arrays.asList(values);
	}
}
