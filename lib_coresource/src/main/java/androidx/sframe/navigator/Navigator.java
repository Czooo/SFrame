package androidx.sframe.navigator;

import android.os.Bundle;

import androidx.annotation.AnimRes;
import androidx.annotation.AnimatorRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

/**
 * @Author create by Zoran on 2019-09-25
 * @Email : 171905184@qq.com
 * @Description :
 */
public abstract class Navigator<D extends Navigator.NavDestination> {

	public static final String NAME_FRAGMENT = "fragment";

	public static final String NAME_FRAGMENT_ACTIVITY = "fragmentActivity";

	public static final String NAME_DIALOG_FRAGMENT = "dialogFragment";

	@NonNull
	public abstract D obtain();

	@Nullable
	public abstract NavDestination navigate(@NonNull D navDestination, @Nullable Bundle args, @Nullable NavOptions navOptions);

	public abstract boolean popBackStack();

	@Nullable
	public Bundle onSaveInstanceState() {
		// no-op
		return null;
	}

	public void onRestoreInstanceState(@Nullable Bundle saveInstanceState) {
		// no-op
	}

	public static abstract class NavDestination {

		private final int mDestinationId;
		private final String mNavigatorName;

		public NavDestination(@NonNull String navigatorName) {
			this.mDestinationId = ViewCompat.generateViewId();
			this.mNavigatorName = navigatorName;
		}

		@IdRes
		public final int getDestinationId() {
			return this.mDestinationId;
		}

		@NonNull
		public final String getNavigatorName() {
			return this.mNavigatorName;
		}
	}

	public static class NavOptions {

		@AnimRes
		@AnimatorRes
		private int mEnterAnim = -1;
		@AnimRes
		@AnimatorRes
		private int mExitAnim = -1;
		@AnimRes
		@AnimatorRes
		private int mPopEnterAnim = -1;
		@AnimRes
		@AnimatorRes
		private int mPopExitAnim = -1;

		@NonNull
		public NavOptions setEnterAnim(@AnimRes @AnimatorRes int enterAnim) {
			this.mEnterAnim = enterAnim;
			return this;
		}

		@NonNull
		public NavOptions setExitAnim(@AnimRes @AnimatorRes int exitAnim) {
			this.mExitAnim = exitAnim;
			return this;
		}

		@NonNull
		public NavOptions setPopEnterAnim(@AnimRes @AnimatorRes int popEnterAnim) {
			this.mPopEnterAnim = popEnterAnim;
			return this;
		}

		@NonNull
		public NavOptions setPopExitAnim(@AnimRes @AnimatorRes int popExitAnim) {
			this.mPopExitAnim = popExitAnim;
			return this;
		}

		@AnimRes
		@AnimatorRes
		public int getEnterAnim() {
			return this.mEnterAnim;
		}

		@AnimRes
		@AnimatorRes
		public int getExitAnim() {
			return this.mExitAnim;
		}

		@AnimRes
		@AnimatorRes
		public int getPopEnterAnim() {
			return this.mPopEnterAnim;
		}

		@AnimRes
		@AnimatorRes
		public int getPopExitAnim() {
			return this.mPopExitAnim;
		}
	}
}
