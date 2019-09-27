package androidx.sframe.helper;

import java.lang.reflect.Method;

import androidx.annotation.NonNull;
import androidx.sframe.annotation.AppPageInterface;
import androidx.sframe.annotation.RunWithAsync;

/**
 * Author create by ok on 2019-06-15
 * Email : ok@163.com.
 */
public class AnnotationHelper {

	public static boolean isAppPageInterface(@NonNull Class<?> pageClass) {
		if (pageClass.isAnnotationPresent(AppPageInterface.class)) {
			AppPageInterface annotation = pageClass.getAnnotation(AppPageInterface.class);
			return annotation != null && annotation.value();
		}
		return true;
	}

	public static boolean isRunWithAsync(@NonNull Class<?> objectClass) {
		final Method[] methods = objectClass.getMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(RunWithAsync.class)) {
				RunWithAsync annotation = method.getAnnotation(RunWithAsync.class);
				return annotation != null && annotation.value();
			}
		}
		return false;
	}
}
