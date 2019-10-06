package androidx.sframe.ui.controller.impl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.sframe.R;
import androidx.sframe.model.AgentNavModel;
import androidx.sframe.navigator.ActivityNavigator;
import androidx.sframe.navigator.DialogFragmentNavigator;
import androidx.sframe.navigator.FragmentNavigator;
import androidx.sframe.navigator.NavDestination;
import androidx.sframe.navigator.NavGraph;
import androidx.sframe.navigator.NavOptions;
import androidx.sframe.navigator.Navigator;
import androidx.sframe.ui.NavAgentActivity;
import androidx.sframe.ui.controller.AppNavController;
import androidx.sframe.ui.controller.AppPageController;
import androidx.sframe.utils.Logger;

/**
 * @Author create by Zoran on 2019-09-25
 * @Email : 171905184@qq.com
 * @Description :
 */
public class AppNavControllerImpl<Page> implements AppNavController<Page> {

	private final AppPageController<Page> mPageController;

	public AppNavControllerImpl(@NonNull AppPageController<Page> pageController) {
		this.mPageController = pageController;
	}

	@Override
	public AppNavController<Page> finishActivity() {
		ActivityCompat.finishAfterTransition(this.getPageController().requireFragmentActivity());
		return this;
	}

	@Override
	public final NavDestination startActivity(@SuppressLint("UnknownNullness") Intent intent) {
		return this.startActivity(intent, null);
	}

	@Override
	public NavDestination startActivity(@SuppressLint("UnknownNullness") Intent intent, @Nullable Bundle options) {
		ActivityNavigator<Page> navigator = this.getNavGraph().getNavigator(ActivityNavigator.class);
		ActivityNavigator.Destination destination = navigator.obtain();
		destination.setOptions(options);
		destination.setIntent(intent);
		return this.navigate(destination);
	}

	@Override
	public final NavDestination startActivityForResult(@SuppressLint("UnknownNullness") Intent intent, int requestCode) {
		return this.startActivityForResult(intent, requestCode, null);
	}

	@Override
	public NavDestination startActivityForResult(@SuppressLint("UnknownNullness") Intent intent, int requestCode, @Nullable Bundle options) {
		ActivityNavigator<Page> navigator = this.getNavGraph().getNavigator(ActivityNavigator.class);
		ActivityNavigator.Destination destination = navigator.obtain();
		destination.setRequestCode(requestCode);
		destination.setOptions(options);
		destination.setIntent(intent);
		return this.navigate(destination);
	}

	@Override
	public final NavDestination startActivity(@NonNull Class<? extends FragmentActivity> activity) {
		return this.startActivity(activity, null);
	}

	@Override
	public final NavDestination startActivity(@NonNull Class<? extends FragmentActivity> activity, @Nullable Bundle args) {
		return this.startActivity(activity, args, null);
	}

	@Override
	public NavDestination startActivity(@NonNull Class<? extends FragmentActivity> activity, @Nullable Bundle args, @Nullable Bundle options) {
		ActivityNavigator<Page> navigator = this.getNavGraph().getNavigator(ActivityNavigator.class);
		AppPageController<Page> pageController = navigator.getPageController();
		Intent intent = new Intent(pageController.requireContext(), activity);
		ActivityNavigator.Destination destination = navigator.obtain();
		destination.setOptions(options);
		destination.setIntent(intent);
		return this.navigate(destination, args);
	}

	@Override
	public final NavDestination startActivityForResult(@NonNull Class<? extends FragmentActivity> activity, int requestCode) {
		return this.startActivityForResult(activity, requestCode, null);
	}

	@Override
	public final NavDestination startActivityForResult(@NonNull Class<? extends FragmentActivity> activity, int requestCode, @Nullable Bundle args) {
		return this.startActivityForResult(activity, requestCode, args, null);
	}

	@Override
	public NavDestination startActivityForResult(@NonNull Class<? extends FragmentActivity> activity, int requestCode, @Nullable Bundle args, @Nullable Bundle options) {
		ActivityNavigator<Page> navigator = this.getNavGraph().getNavigator(ActivityNavigator.class);
		AppPageController<Page> pageController = navigator.getPageController();
		Intent intent = new Intent(pageController.requireContext(), activity);
		ActivityNavigator.Destination destination = navigator.obtain();
		destination.setRequestCode(requestCode);
		destination.setOptions(options);
		destination.setIntent(intent);
		return this.navigate(destination, args);
	}

	@Override
	public final NavDestination showFragment(@NonNull Class<? extends DialogFragment> dialogFragment) {
		return this.showFragment(dialogFragment, null);
	}

	@Override
	public NavDestination showFragment(@NonNull Class<? extends DialogFragment> dialogFragment, @Nullable Bundle args) {
		DialogFragmentNavigator<Page> navigator = this.getNavGraph().getNavigator(DialogFragmentNavigator.class);
		DialogFragmentNavigator.Destination destination = navigator.obtain();
		destination.setDialogFragmentClass(dialogFragment);
		return this.navigate(destination, args);
	}

	@Override
	public final NavDestination pushFragment(@IdRes int containerId, @NonNull Class<? extends Fragment> fragment) {
		return this.pushFragment(containerId, fragment, null, null);
	}

	@Override
	public final NavDestination pushFragment(@IdRes int containerId, @NonNull Class<? extends Fragment> fragment, @Nullable Bundle args) {
		return this.pushFragment(containerId, fragment, args, null);
	}

	@Override
	public final NavDestination pushFragment(@IdRes int containerId, @NonNull Class<? extends Fragment> fragment, @Nullable NavOptions navOptions) {
		return this.pushFragment(containerId, fragment, null, navOptions);
	}

	@Override
	public NavDestination pushFragment(@IdRes int containerId, @NonNull Class<? extends Fragment> fragment, @Nullable Bundle args, @Nullable NavOptions navOptions) {
		FragmentNavigator<Page> navigator = this.getNavGraph().getNavigator(FragmentNavigator.class);
		FragmentNavigator.Destination destination = navigator.obtain();
		destination.setFragmentClass(fragment);
		destination.setContainerId(containerId);
		return this.navigate(destination, args, navOptions);
	}

	@Override
	public final NavDestination startFragment(@NonNull Class<? extends Fragment> fragment) {
		return this.startFragment(fragment, null);
	}

	@Override
	public final NavDestination startFragment(@NonNull Class<? extends Fragment> fragment, @Nullable Bundle args) {
		return this.startFragment(fragment, args, null);
	}

	@Override
	public final NavDestination startFragment(@NonNull Class<? extends Fragment> fragment, @Nullable Bundle args, @Nullable Bundle options) {
		return this.startActivity(NavAgentActivity.class, NavAgentActivity.create(new AgentNavModel(fragment, args)), options);
	}

	@Override
	public final NavDestination startFragmentForResult(@NonNull Class<? extends Fragment> fragment, int requestCode) {
		return this.startFragmentForResult(fragment, requestCode, null);
	}

	@Override
	public final NavDestination startFragmentForResult(@NonNull Class<? extends Fragment> fragment, int requestCode, @Nullable Bundle args) {
		return this.startFragmentForResult(fragment, requestCode, args, null);
	}

	@Override
	public final NavDestination startFragmentForResult(@NonNull Class<? extends Fragment> fragment, int requestCode, @Nullable Bundle args, @Nullable Bundle options) {
		return this.startActivityForResult(NavAgentActivity.class, requestCode, NavAgentActivity.create(new AgentNavModel(fragment, args)), options);
	}

	@Override
	public final NavDestination navigate(@NonNull NavDestination navDestination) {
		return this.navigate(navDestination, null);
	}

	@Override
	public final NavDestination navigate(@NonNull NavDestination navDestination, @Nullable Bundle args) {
		return this.navigate(navDestination, args, null);
	}

	@Override
	public NavDestination navigate(@NonNull NavDestination navDestination, @Nullable Bundle args, @Nullable NavOptions navOptions) {
		Navigator<NavDestination> navigator = this.getNavGraph().getNavigator(navDestination.getNavigatorName());
		return navigator.navigate(navDestination, args, navOptions);
	}

	@NonNull
	@Override
	public NavOptions getNavOptions() {
		return new NavOptions()
				.setEnterAnim(R.anim.slide_in_from_right)
				.setExitAnim(R.anim.slide_out_to_left)
				.setPopEnterAnim(R.anim.slide_in_from_left)
				.setPopExitAnim(R.anim.slide_out_to_right);
	}

	private NavGraph mNavGraph;

	@NonNull
	@Override
	public NavGraph getNavGraph() {
		if (this.mNavGraph == null) {
			this.mNavGraph = new NavGraph();
			this.mNavGraph.addNavigator(new ActivityNavigator<>(this.getPageController()));
			this.mNavGraph.addNavigator(new FragmentNavigator<>(this.getPageController()));
			this.mNavGraph.addNavigator(new DialogFragmentNavigator<>(this.getPageController()));
		}
		return this.mNavGraph;
	}

	@Override
	public boolean navigateUp() {
		if (this.popBackStack()) {
			return true;
		}
		FragmentManager fragmentManager = AppPageControllerHelper
				.requireSupportFragmentManager(this.getPageController());
		if (fragmentManager.isStateSaved() || !fragmentManager.popBackStackImmediate()) {
			Logger.e("navigateUp false");
			return false;
		}
		Logger.e("navigateUp true");
		return true;
	}

	@Override
	public boolean popBackStack() {
		final FragmentManager childFragmentManager = AppPageControllerHelper
				.requireChildFragmentManager(this.getPageController());
		if (childFragmentManager.isStateSaved() || !childFragmentManager.popBackStackImmediate()) {
			Logger.e("popBackStack false");
			return false;
		}
		Logger.e("popBackStack true");
		return true;
	}

	@NonNull
	private AppPageController<Page> getPageController() {
		return this.mPageController;
	}
}
