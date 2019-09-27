package androidx.sframe.navigator;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Author create by Zoran on 2019-09-26
 * @Email : 171905184@qq.com
 * @Description :
 */
final class NavigatorBackStackEntry {

	private final Navigator.NavDestination mDestination;
	private final Bundle mArguments;

	NavigatorBackStackEntry(@NonNull Navigator.NavDestination destination, @Nullable Bundle arguments) {
		this.mDestination = destination;
		this.mArguments = arguments;
	}

	@NonNull
	public Navigator.NavDestination getDestination() {
		return this.mDestination;
	}

	@Nullable
	public Bundle getArguments() {
		return this.mArguments;
	}
}
