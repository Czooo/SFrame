package androidx.sframe.navigator;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

/**
 * @Author create by Zoran on 2019-10-04
 * @Email : 171905184@qq.com
 * @Description :
 */
public class NavDestination {

	@IdRes
	private final int mId;
	private final String mNavigatorName;

	public NavDestination(@NonNull Navigator<? extends NavDestination> navigator) {
		this(NavigatorProvider.getNameForNavigator(navigator.getClass()));
	}

	public NavDestination(@NonNull String navigatorName) {
		this.mId = ViewCompat.generateViewId();
		this.mNavigatorName = navigatorName;
	}

	@IdRes
	public final int getId() {
		return this.mId;
	}

	@NonNull
	public final String getNavigatorName() {
		return this.mNavigatorName;
	}
}
