package androidx.sframe.navigator;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.sframe.ui.controller.AppPageController;
import androidx.sframe.ui.controller.impl.AppPageControllerHelper;

/**
 * @Author create by Zoran on 2019-10-04
 * @Email : 171905184@qq.com
 * @Description :
 */
@Navigator.Name("fragment")
public class FragmentNavigator<Page> extends Navigator<FragmentNavigator.Destination> {

	private static final String FRAGMENT_TAG = "androidx-navigator-fragment:tag:";

	private final AppPageController<Page> mPageController;
	private int mFragmentCount = 0;

	public FragmentNavigator(@NonNull AppPageController<Page> pageController) {
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
		final FragmentManager fragmentManager = this.getFragmentManager();
		if (fragmentManager.isStateSaved()) {
			return null;
		}
		final Fragment fragment = fragmentManager.getFragmentFactory()
				.instantiate(this.getContext().getClassLoader(), navDestination.mFragmentClass.getName());
		fragment.setArguments(args);

		final String fragmentTag = FRAGMENT_TAG + this.mFragmentCount++;
		final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (navOptions != null) {
			int enterAnim = navOptions.getEnterAnim();
			int exitAnim = navOptions.getExitAnim();
			int popEnterAnim = navOptions.getPopEnterAnim();
			int popExitAnim = navOptions.getPopExitAnim();
			if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
				enterAnim = enterAnim != -1 ? enterAnim : 0;
				exitAnim = exitAnim != -1 ? exitAnim : 0;
				popEnterAnim = popEnterAnim != -1 ? popEnterAnim : 0;
				popExitAnim = popExitAnim != -1 ? popExitAnim : 0;
				fragmentTransaction.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim);
			}
		}
		fragmentTransaction.replace(navDestination.mContainerId, fragment, fragmentTag);
		fragmentTransaction.setPrimaryNavigationFragment(fragment);
		final boolean shouldAddToBackStack = navOptions == null
				|| navOptions.isAddToBackStack();
		if (shouldAddToBackStack) {
			fragmentTransaction.addToBackStack(fragment.getTag());
		}
		fragmentTransaction.setReorderingAllowed(true);
		fragmentTransaction.commit();
		return navDestination;
	}

	@NonNull
	public final Context getContext() {
		return this.getPageController().requireContext();
	}

	@NonNull
	public final FragmentManager getFragmentManager() {
		return AppPageControllerHelper.requireChildFragmentManager(this.getPageController());
	}

	@NonNull
	public final AppPageController<Page> getPageController() {
		return this.mPageController;
	}

	public static class Destination extends NavDestination {

		@IdRes
		private int mContainerId;
		private Class<? extends Fragment> mFragmentClass;

		Destination(@NonNull Navigator<? extends NavDestination> navigator) {
			super(navigator);
		}

		public final void setContainerId(@IdRes int containerId) {
			this.mContainerId = containerId;
		}

		public final void setFragmentClass(@NonNull Class<? extends Fragment> fragmentClass) {
			this.mFragmentClass = fragmentClass;
		}
	}
}
