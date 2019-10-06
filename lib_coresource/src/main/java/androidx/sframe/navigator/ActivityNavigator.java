package androidx.sframe.navigator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sframe.ui.controller.AppPageController;
import androidx.sframe.ui.controller.impl.AppPageControllerHelper;

/**
 * @Author create by Zoran on 2019-10-04
 * @Email : 171905184@qq.com
 * @Description :
 */
@Navigator.Name("activity")
public class ActivityNavigator<Page> extends Navigator<ActivityNavigator.Destination> {

	private final AppPageController<Page> mPageController;

	public ActivityNavigator(@NonNull AppPageController<Page> pageController) {
		this.mPageController = pageController;
	}

	@NonNull
	@Override
	public Destination obtain() {
		return new Destination(this);
	}

	@Nullable
	@Override
	public NavDestination navigate(@NonNull Destination navDestination, @Nullable Bundle args, @Nullable NavOptions navOptions) {
		final Intent intent = navDestination.mIntent;
		if (intent == null) {
			throw new IllegalStateException("Destination does not have an Intent set.");
		}
		if (args != null) {
			intent.putExtras(args);
		}
		if (navDestination.mRequestCode == -1) {
			AppPageControllerHelper.startActivity(this.getPageController(), intent, navDestination.mOptions);
		} else {
			AppPageControllerHelper.startActivityForResult(this.getPageController(), intent, navDestination.mOptions, navDestination.mRequestCode);
		}
		return navDestination;
	}

	@NonNull
	public final AppPageController<Page> getPageController() {
		return this.mPageController;
	}

	public static class Destination extends NavDestination {

		private Intent mIntent;
		private Bundle mOptions;
		private int mRequestCode = -1;

		Destination(@NonNull Navigator<? extends NavDestination> navigator) {
			super(navigator);
		}

		public final void setIntent(@SuppressLint("UnknownNullness") Intent intent) {
			this.mIntent = intent;
		}

		public final void setOptions(@Nullable Bundle options) {
			this.mOptions = options;
		}

		public final void setRequestCode(int requestCode) {
			this.mRequestCode = requestCode;
		}
	}
}
