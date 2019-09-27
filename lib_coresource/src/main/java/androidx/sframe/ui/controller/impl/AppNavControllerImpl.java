package androidx.sframe.ui.controller.impl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.sframe.R;
import androidx.sframe.model.AgentNavModel;
import androidx.sframe.ui.NavAgentActivity;
import androidx.sframe.ui.controller.AppNavController;
import androidx.sframe.ui.controller.AppPageController;
import androidx.sframe.ui.dialog.ProgressDialogFragment;
import androidx.sframe.navigator.FragmentActivityNavigator;
import androidx.sframe.navigator.DialogFragmentNavigator;
import androidx.sframe.navigator.FragmentNavigator;
import androidx.sframe.navigator.Navigator;
import androidx.sframe.navigator.NavigatorController;

/**
 * @Author create by Zoran on 2019-09-25
 * @Email : 171905184@qq.com
 * @Description :
 */
public class AppNavControllerImpl<Page> implements AppNavController<Page> {

	private final NavigatorController<Page> mNavigatorController;
	private final Navigator.NavOptions mNavOptions;

	public AppNavControllerImpl(@NonNull AppPageController<Page> pageController) {
		this.mNavigatorController = new NavigatorController<>(pageController);
		// default options
		this.mNavOptions = new Navigator.NavOptions()
				.setEnterAnim(R.anim.slide_in_from_right)
				.setExitAnim(R.anim.slide_out_to_left)
				.setPopEnterAnim(R.anim.slide_in_from_left)
				.setPopExitAnim(R.anim.slide_out_to_right);
	}

	@Override
	public AppNavController<Page> onSaveInstanceState(@NonNull Bundle saveInstanceState) {
		this.getNavigatorController().onSaveInstanceState(saveInstanceState);
		return this;
	}

	@Override
	public AppNavController<Page> onRestoreInstanceState(@Nullable Bundle saveInstanceState) {
		this.getNavigatorController().onRestoreInstanceState(saveInstanceState);
		return this;
	}

	@Override
	public AppNavController<Page> showProgressFragment() {
		return this.showFragment(ProgressDialogFragment.class);
	}

	@Override
	public AppNavController<Page> hideProgressFragment() {
		this.popBackStack();
		return this;
	}

	@Override
	public final AppNavController<Page> startActivity(@SuppressLint("UnknownNullness") Intent intent) {
		return this.startActivity(intent, null);
	}

	@Override
	public AppNavController<Page> startActivity(@SuppressLint("UnknownNullness") Intent intent, @Nullable Bundle options) {
		FragmentActivityNavigator<Page> navigator = this.getNavigator(Navigator.NAME_FRAGMENT_ACTIVITY);
		FragmentActivityNavigator.Destination destination = navigator.obtain();
		destination.setOptions(options);
		destination.setIntent(intent);
		this.getNavigatorController().navigate(destination);
		return this;
	}

	@Override
	public final AppNavController<Page> startActivityForResult(@SuppressLint("UnknownNullness") Intent intent, int requestCode) {
		return this.startActivityForResult(intent, requestCode, null);
	}

	@Override
	public AppNavController<Page> startActivityForResult(@SuppressLint("UnknownNullness") Intent intent, int requestCode, @Nullable Bundle options) {
		FragmentActivityNavigator<Page> navigator = this.getNavigator(Navigator.NAME_FRAGMENT_ACTIVITY);
		FragmentActivityNavigator.Destination destination = navigator.obtain();
		destination.setRequestCode(requestCode);
		destination.setOptions(options);
		destination.setIntent(intent);
		this.getNavigatorController().navigate(destination);
		return this;
	}

	@Override
	public final AppNavController<Page> startActivity(@NonNull Class<? extends FragmentActivity> activity) {
		return this.startActivity(activity, null);
	}

	@Override
	public final AppNavController<Page> startActivity(@NonNull Class<? extends FragmentActivity> activity, @Nullable Bundle args) {
		return this.startActivity(activity, args, null);
	}

	@Override
	public AppNavController<Page> startActivity(@NonNull Class<? extends FragmentActivity> activity, @Nullable Bundle args, @Nullable Bundle options) {
		FragmentActivityNavigator<Page> navigator = this.getNavigator(Navigator.NAME_FRAGMENT_ACTIVITY);
		AppPageController<Page> pageController = navigator.getPageController();
		Intent intent = new Intent(pageController.requireContext(), activity);
		FragmentActivityNavigator.Destination destination = navigator.obtain();
		destination.setOptions(options);
		destination.setIntent(intent);
		this.getNavigatorController().navigate(destination, args);
		return this;
	}

	@Override
	public final AppNavController<Page> startActivityForResult(@NonNull Class<? extends FragmentActivity> activity, int requestCode) {
		return this.startActivityForResult(activity, requestCode, null);
	}

	@Override
	public final AppNavController<Page> startActivityForResult(@NonNull Class<? extends FragmentActivity> activity, int requestCode, @Nullable Bundle args) {
		return this.startActivityForResult(activity, requestCode, args, null);
	}

	@Override
	public AppNavController<Page> startActivityForResult(@NonNull Class<? extends FragmentActivity> activity, int requestCode, @Nullable Bundle args, @Nullable Bundle options) {
		FragmentActivityNavigator<Page> navigator = this.getNavigator(Navigator.NAME_FRAGMENT_ACTIVITY);
		AppPageController<Page> pageController = navigator.getPageController();
		Intent intent = new Intent(pageController.requireContext(), activity);
		FragmentActivityNavigator.Destination destination = navigator.obtain();
		destination.setRequestCode(requestCode);
		destination.setOptions(options);
		destination.setIntent(intent);
		this.getNavigatorController().navigate(destination, args);
		return this;
	}

	@Override
	public final AppNavController<Page> showFragment(@NonNull Class<? extends DialogFragment> dialogFragment) {
		return this.showFragment(dialogFragment, null);
	}

	@Override
	public AppNavController<Page> showFragment(@NonNull Class<? extends DialogFragment> dialogFragment, @Nullable Bundle args) {
		DialogFragmentNavigator<Page> navigator = this.getNavigator(Navigator.NAME_DIALOG_FRAGMENT);
		DialogFragmentNavigator.Destination destination = navigator.obtain();
		destination.setDialogFragmentClass(dialogFragment);
		this.getNavigatorController().navigate(destination, args);
		return this;
	}

	@Override
	public final AppNavController<Page> pushFragment(@IdRes int containerId, @NonNull Class<? extends Fragment> fragment) {
		return this.pushFragment(containerId, fragment, this.mNavOptions);
	}

	@Override
	public final AppNavController<Page> pushFragment(@IdRes int containerId, @NonNull Class<? extends Fragment> fragment, @Nullable Bundle args) {
		return this.pushFragment(containerId, fragment, args, this.mNavOptions);
	}

	@Override
	public final AppNavController<Page> pushFragment(int containerId, @NonNull Class<? extends Fragment> fragment, @Nullable Navigator.NavOptions options) {
		return this.pushFragment(containerId, fragment, null, options);
	}

	@Override
	public AppNavController<Page> pushFragment(@IdRes int containerId, @NonNull Class<? extends Fragment> fragment, @Nullable Bundle args, @Nullable Navigator.NavOptions options) {
		FragmentNavigator<Page> navigator = this.getNavigator(Navigator.NAME_FRAGMENT);
		FragmentNavigator.Destination destination = navigator.obtain();
		destination.setFragmentClass(fragment);
		destination.setContainerId(containerId);
		this.getNavigatorController().navigate(destination, args, options);
		return this;
	}

	@Override
	public final AppNavController<Page> startFragment(@NonNull Class<? extends Fragment> fragment) {
		return this.startFragment(fragment, null);
	}

	@Override
	public final AppNavController<Page> startFragment(@NonNull Class<? extends Fragment> fragment, @Nullable Bundle args) {
		return this.startFragment(fragment, args, null);
	}

	@Override
	public final AppNavController<Page> startFragment(@NonNull Class<? extends Fragment> fragment, @Nullable Bundle args, @Nullable Bundle options) {
		return this.startActivity(NavAgentActivity.class, NavAgentActivity.create(new AgentNavModel(fragment, args)), options);
	}

	@Override
	public final AppNavController<Page> startFragmentForResult(@NonNull Class<? extends Fragment> fragment, int requestCode) {
		return this.startFragmentForResult(fragment, requestCode, null);
	}

	@Override
	public final AppNavController<Page> startFragmentForResult(@NonNull Class<? extends Fragment> fragment, int requestCode, @Nullable Bundle args) {
		return this.startFragmentForResult(fragment, requestCode, args, null);
	}

	@Override
	public final AppNavController<Page> startFragmentForResult(@NonNull Class<? extends Fragment> fragment, int requestCode, @Nullable Bundle args, @Nullable Bundle options) {
		return this.startActivityForResult(NavAgentActivity.class, requestCode, NavAgentActivity.create(new AgentNavModel(fragment, args)), options);
	}

	@Override
	public AppNavController<Page> addNavigator(@NonNull String name, @NonNull Navigator<? extends Navigator.NavDestination> navigator) {
		this.getNavigatorController().addNavigator(name, navigator);
		return this;
	}

	@Override
	public <T extends Navigator<?>> T getNavigator(@NonNull String name) {
		return this.getNavigatorController().getNavigator(name);
	}

	@NonNull
	@Override
	public Navigator.NavOptions getDefaultOptions() {
		return this.mNavOptions;
	}

	@Override
	public boolean popBackStack() {
		return this.getNavigatorController().popBackStack();
	}

	@NonNull
	private NavigatorController<Page> getNavigatorController() {
		return this.mNavigatorController;
	}
}
