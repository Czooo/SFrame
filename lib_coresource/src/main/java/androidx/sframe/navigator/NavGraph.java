package androidx.sframe.navigator;

import androidx.annotation.NonNull;

/**
 * @Author create by Zoran on 2019-10-04
 * @Email : 171905184@qq.com
 * @Description :
 */
public class NavGraph extends NavDestination {

	private static final String NAME = "navigator";

	private final NavigatorProvider mNavigatorProvider =
			new NavigatorProvider();

	public NavGraph() {
		super(NAME);
	}

	public NavGraph(@NonNull String navigatorName) {
		super(navigatorName);
	}

	public final <T extends Navigator<? extends NavDestination>> void addNavigator(@NonNull T navigator) {
		this.mNavigatorProvider.addNavigator(NavigatorProvider.getNameForNavigator(navigator.getClass()), navigator);
	}

	public final <T extends Navigator<? extends NavDestination>> void addNavigator(@NonNull String name, @NonNull T navigator) {
		this.mNavigatorProvider.addNavigator(name, navigator);
	}

	@NonNull
	public final <T extends Navigator<? extends NavDestination>> T getNavigator(@NonNull Class<? extends Navigator> navigator) {
		return this.mNavigatorProvider.getNavigator(NavigatorProvider.getNameForNavigator(navigator));
	}

	@NonNull
	public final <T extends Navigator<? extends NavDestination>> T getNavigator(@NonNull String name) {
		return this.mNavigatorProvider.getNavigator(name);
	}
}
