package androidx.demon.helper;

import java.lang.reflect.Method;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.demon.annotation.RunWithAsync;

/**
 * Author create by ok on 2019-06-15
 * Email : ok@163.com.
 */
public class AnnotationHelper {

	public static boolean isShouldRunInAsyncAnn(@Nullable Object object) {
		if (object == null) {
			return false;
		}
		return isShouldRunInAsyncAnn(object.getClass());
	}

	private static boolean isShouldRunInAsyncAnn(@NonNull Class<?> preClass) {
		try {
			final Method[] methods = preClass.getMethods();
			for (Method preMethod : methods) {
				if (preMethod.isAnnotationPresent(RunWithAsync.class)) {
					final RunWithAsync preAnnotation = preMethod.getAnnotation(RunWithAsync.class);
					return preAnnotation.value();
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
}
