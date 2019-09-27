package androidx.sframe.navigator;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * @Author create by Zoran on 2019-09-25
 * @Email : 171905184@qq.com
 * @Description :
 */
public class NavigatorProvider {

	private final HashMap<String, Navigator<? extends Navigator.NavDestination>> mNavigators = new HashMap<>();

	public void addNavigator(@NonNull String name, @NonNull Navigator<? extends Navigator.NavDestination> navigator) {
		if (TextUtils.isEmpty(name)) {
			throw new IllegalArgumentException("navigator name cannot be an empty string");
		}
		this.mNavigators.put(name, navigator);
	}

	@NonNull
	public <T extends Navigator<?>> T getNavigator(@NonNull String name) {
		if (TextUtils.isEmpty(name)) {
			throw new IllegalArgumentException("navigator name cannot be an empty string");
		}
		final Navigator<? extends Navigator.NavDestination> navigator = this.mNavigators.get(name);
		if (navigator == null) {
			throw new IllegalStateException("Could not find Navigator with name \"" + name
					+ "\". You must call addNavigator() for each navigation type.");
		}
		return (T) navigator;
	}

	@NonNull
	public final Map<String, Navigator<? extends Navigator.NavDestination>> getNavigators() {
		return this.mNavigators;
	}

	public final void clear() {
		this.mNavigators.clear();
	}
}
