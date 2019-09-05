package androidx.sframe.ui.controller;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;

import androidx.annotation.IdRes;
import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavHostController;
import androidx.navigation.NavInflater;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.DialogFragmentNavigator;
import androidx.navigation.fragment.FragmentNavigator;

/**
 * Author create by ok on 2019-06-18
 * Email : ok@163.com.
 */
public class AppCompatNavHostController {

	private static final String KEY_NAV_CONTROLLER_STATE = "android-support-nav:pageController:navControllerState";

	private final HashMap<String, Integer> mDestinationIds = new HashMap<>();
	private final NavHostController mNavHostController;
	private final AppPageController<FragmentActivity> mPageController;

	public AppCompatNavHostController(@NonNull AppPageController<FragmentActivity> pageController) {
		this.mPageController = pageController;

		this.mNavHostController = new NavHostController(this.requireContext());
		this.mNavHostController.setLifecycleOwner(pageController);
		this.mNavHostController.setViewModelStore(pageController.getViewModelStore());
		this.mNavHostController.setOnBackPressedDispatcher(this.requireActivity().getOnBackPressedDispatcher());

		final NavigatorProvider preNavigatorProvider = this.mNavHostController.getNavigatorProvider();
		preNavigatorProvider.addNavigator(new DialogFragmentNavigator(this.requireContext(), this.requireActivity().getSupportFragmentManager()));
		preNavigatorProvider.addNavigator(new FragmentNavigator(this.requireContext(), this.requireActivity().getSupportFragmentManager(), android.R.id.content));
	}

	public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
		final Bundle navOutState = this.mNavHostController.saveState();
		if (navOutState != null) {
			savedInstanceState.putBundle(KEY_NAV_CONTROLLER_STATE, navOutState);
		}
	}

	public void restoreState(@Nullable Bundle savedInstanceState) {
		this.mNavHostController.restoreState(savedInstanceState);
	}

	public void addGraph(@NavigationRes int navResId, @Nullable Bundle args) {
		try {
			final NavHostController preNavHostController = this.mNavHostController;
			final NavGraph preNavGraph = preNavHostController.getGraph();
			final NavGraph preAddNavGraph = preNavHostController.getNavInflater().inflate(navResId);
			preNavGraph.addAll(preAddNavGraph);
		} catch (IllegalStateException e) {
			this.setGraph(navResId, args);
		}
	}

	public void setGraph(@NavigationRes int navResId, @Nullable Bundle args) {
		this.mNavHostController.setGraph(navResId, args);
	}

	public void navigateFra(@NonNull Class<? extends Fragment> pageClass, @Nullable Bundle args, @Nullable NavOptions navOptions, @Nullable FragmentNavigator.Extras navigatorExtras) {
		final NavHostController preNavHostController = this.mNavHostController;
		try {
			final NavGraph navGraph = preNavHostController.getGraph();
			final @IdRes int destinationId = this.findDestinationIdAt(pageClass);
			if (navGraph.findNode(destinationId) == null) {
				navGraph.addDestination(this.createFragmentDestinationAt(pageClass));
			}
			preNavHostController.navigate(destinationId, args, navOptions, navigatorExtras);
		} catch (Exception e) {
			final NavDestination fragmentDestination = this.createFragmentDestinationAt(pageClass);
			final NavGraph navGraph = this.createNavGraph(fragmentDestination);
			preNavHostController.setGraph(navGraph, args);
		}
	}

	public void navigate(@IdRes int resId, @Nullable Bundle args, @Nullable NavOptions navOptions, @Nullable Navigator.Extras navigatorExtras) {
		this.mNavHostController.navigate(resId, args, navOptions, navigatorExtras);
	}

	public boolean navigateUp() {
		return this.mNavHostController.navigateUp();
	}

	public boolean popBackStack() {
		return this.mNavHostController.popBackStack();
	}

	public boolean popBackStack(@IdRes int destinationId, boolean inclusive) {
		return this.mNavHostController.popBackStack(destinationId, inclusive);
	}

	public void addNavigator(@NonNull Navigator<? extends NavDestination> navigator) {
		this.mNavHostController.getNavigatorProvider().addNavigator(navigator);
	}

	public void addNavigator(@NonNull String name, @NonNull Navigator<? extends NavDestination> navigator) {
		this.mNavHostController.getNavigatorProvider().addNavigator(name, navigator);
	}

	public void addOnDestinationChangedListener(@NonNull NavController.OnDestinationChangedListener listener) {
		this.mNavHostController.addOnDestinationChangedListener(listener);
	}

	public void removeOnDestinationChangedListener(@NonNull NavController.OnDestinationChangedListener listener) {
		this.mNavHostController.removeOnDestinationChangedListener(listener);
	}

	public void setViewNavHostController(@NonNull FragmentActivity fragmentActivity) {
		AppNavigation.setViewNavHostController(fragmentActivity, this);
	}

	public void setViewNavHostController(@NonNull View preView) {
		AppNavigation.setViewNavHostController(preView, this);
	}

	public void enableOnBackPressed(boolean enabled) {
		this.mNavHostController.enableOnBackPressed(enabled);
	}

	public NavInflater getNavInflater() {
		return this.mNavHostController.getNavInflater();
	}

	public NavDestination getCurrentDestination() {
		return this.mNavHostController.getCurrentDestination();
	}

	public <T extends Navigator<?>> T getNavigator(@NonNull Class<T> navigatorClass) {
		return this.mNavHostController.getNavigatorProvider().getNavigator(navigatorClass);
	}

	public <T extends Navigator<?>> T getNavigator(@NonNull String name) {
		return this.mNavHostController.getNavigatorProvider().getNavigator(name);
	}

	@NonNull
	private NavDestination createActivityDestinationAt(@NonNull Class<? extends FragmentActivity> fragmentActivityClass) {
		final @IdRes Integer destinationId = this.findDestinationIdAt(fragmentActivityClass);
		final NavigatorProvider preNavigatorProvider = this.mNavHostController.getNavigatorProvider();
		final ActivityNavigator preNavigator = preNavigatorProvider.getNavigator(ActivityNavigator.class);
		final ActivityNavigator.Destination preDestination = preNavigator.createDestination();
		preDestination.setComponentName(new ComponentName(this.requireContext(), fragmentActivityClass));
		preDestination.setLabel(fragmentActivityClass.getName());
		preDestination.setId(destinationId);
		return preDestination;
	}

	@NonNull
	private NavDestination createFragmentDestinationAt(@NonNull Class<? extends Fragment> fragmentClass) {
		final @IdRes Integer destinationId = this.findDestinationIdAt(fragmentClass);
		final NavigatorProvider preNavigatorProvider = this.mNavHostController.getNavigatorProvider();
		final FragmentNavigator preNavigator = preNavigatorProvider.getNavigator(FragmentNavigator.class);
		final FragmentNavigator.Destination preDestination = preNavigator.createDestination();
		preDestination.setClassName(fragmentClass.getName());
		preDestination.setLabel(fragmentClass.getName());
		preDestination.setId(destinationId);
		return preDestination;
	}

	@NonNull
	private NavDestination createDialogFragmentDestinationAt(@NonNull Class<? extends DialogFragment> dialogFragmentClass) {
		final @IdRes Integer destinationId = this.findDestinationIdAt(dialogFragmentClass);
		final NavigatorProvider preNavigatorProvider = this.mNavHostController.getNavigatorProvider();
		final DialogFragmentNavigator preNavigator = preNavigatorProvider.getNavigator(DialogFragmentNavigator.class);
		final DialogFragmentNavigator.Destination preDestination = preNavigator.createDestination();
		preDestination.setClassName(dialogFragmentClass.getName());
		preDestination.setLabel(dialogFragmentClass.getName());
		preDestination.setId(destinationId);
		return preDestination;
	}

	@NonNull
	private NavGraph createNavGraph(@NonNull NavDestination navDestination) {
		final NavigatorProvider preNavigatorProvider = this.mNavHostController.getNavigatorProvider();
		final NavGraphNavigator preNavigator = preNavigatorProvider.getNavigator(NavGraphNavigator.class);
		final NavGraph navGraph = preNavigator.createDestination();
		navGraph.addDestination(navDestination);
		navGraph.setStartDestination(navDestination.getId());
		return navGraph;
	}

	@NonNull
	public final AppPageController<FragmentActivity> getPageController() {
		return this.mPageController;
	}

	@NonNull
	public final NavHostController getNavHostController() {
		return this.mNavHostController;
	}

	@IdRes
	@NonNull
	private Integer findDestinationIdAt(Class<?> pageClass) {
		Integer destinationId = this.mDestinationIds.get(pageClass.getName());
		if (destinationId == null) {
			destinationId = View.generateViewId();
			this.mDestinationIds.put(pageClass.getName(), destinationId);
		}
		return destinationId;
	}

	@NonNull
	private FragmentActivity requireActivity() {
		return this.getPageController().getPageOwner();
	}

	@NonNull
	private Context requireContext() {
		return this.getPageController().getPageOwner();
	}
}
