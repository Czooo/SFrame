package androidx.sframe.navigator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.sframe.ui.controller.AppPageController;
import androidx.sframe.ui.controller.impl.AppPageControllerHelper;

/**
 * @Author create by Zoran on 2019-09-25
 * @Email : 171905184@qq.com
 * @Description :
 */
public class FragmentActivityNavigator<Page> extends Navigator<FragmentActivityNavigator.Destination> {

	private final AppPageController<Page> mPageController;

	public FragmentActivityNavigator(@NonNull AppPageController<Page> pageController) {
		this.mPageController = pageController;
	}

	@NonNull
	@Override
	public Destination obtain() {
		return new Destination(Navigator.NAME_FRAGMENT_ACTIVITY);
	}

	@Nullable
	@Override
	public NavDestination navigate(@NonNull Destination navDestination, @Nullable Bundle args, @Nullable NavOptions navOptions) {
		if (navDestination.mIntent == null) {
			throw new IllegalStateException("Destination does not have an Intent set.");
		}
		if (args != null) {
			navDestination.mIntent.putExtras(args);
		}
		if (navDestination.mRequestCode == -1) {
			AppPageControllerHelper.startActivity(this.mPageController, navDestination.mIntent, navDestination.mOptions);
		} else {
			AppPageControllerHelper.startActivityForResult(this.mPageController, navDestination.mIntent, navDestination.mOptions, navDestination.mRequestCode);
		}
		return navDestination;
	}

	@Override
	public boolean popBackStack() {
		final FragmentActivity fragmentActivity = this.mPageController.getFragmentActivity();
		if (fragmentActivity != null) {
			ActivityCompat.finishAfterTransition(fragmentActivity);
			return true;
		}
		return false;
	}

	@NonNull
	public final AppPageController<Page> getPageController() {
		return this.mPageController;
	}

	public static class Destination extends Navigator.NavDestination {

		@SuppressLint("UnknownNullness")
		private Intent mIntent;
		private Bundle mOptions;
		private int mRequestCode = -1;

		public Destination(@NonNull String navigatorName) {
			super(navigatorName);
		}

		public Destination setIntent(@SuppressLint("UnknownNullness") Intent intent) {
			this.mIntent = intent;
			return this;
		}

		public Destination setOptions(@Nullable Bundle options) {
			this.mOptions = options;
			return this;
		}

		public Destination setRequestCode(int requestCode) {
			this.mRequestCode = requestCode;
			return this;
		}
	}
}
