package androidx.sframe.navigator;

import android.text.TextUtils;
import java.util.HashMap;
import androidx.annotation.NonNull;

/**
 * @Author create by Zoran on 2019-09-29
 * @Email : 171905184@qq.com
 * @Description :
 */
public class NavigatorProvider {

	private static final HashMap<Class<? extends Navigator>, String> sAnnotationNames =
			new HashMap<>();

	@NonNull
	static String getNameForNavigator(@NonNull Class<? extends Navigator> navigator) {
		String mNavigatorName = sAnnotationNames.get(navigator);
		if (TextUtils.isEmpty(mNavigatorName)) {
			final Navigator.Name annotation = navigator.getAnnotation(Navigator.Name.class);
			mNavigatorName = annotation == null ? null : annotation.value();
			if (annotation == null || TextUtils.isEmpty(mNavigatorName)) {
				throw new IllegalArgumentException("No @NavigatorName annotation found for "
						+ navigator.getSimpleName());
			}
			sAnnotationNames.put(navigator, mNavigatorName);
		}
		return mNavigatorName;
	}

	private final HashMap<String, Navigator<? extends NavDestination>> mNavigators =
			new HashMap<>();

	public <T extends Navigator<? extends NavDestination>> void addNavigator(@NonNull String name, @NonNull T navigator) {
		if (TextUtils.isEmpty(name)) {
			throw new IllegalArgumentException("navigator name cannot be an empty string");
		}
		this.mNavigators.put(name, navigator);
	}

	@NonNull
	public <T extends Navigator<? extends NavDestination>> T getNavigator(@NonNull String name) {
		if (TextUtils.isEmpty(name)) {
			throw new IllegalArgumentException("navigator name cannot be an empty string");
		}
		final Navigator<? extends NavDestination> navigator = this.mNavigators.get(name);
		if (navigator == null) {
			throw new IllegalStateException("Could not find Navigator with name \"" + name
					+ "\". You must call addNavigator() for each navigation type.");
		}
		return (T) navigator;
	}
}
