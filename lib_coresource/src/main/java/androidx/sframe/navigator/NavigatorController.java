package androidx.sframe.navigator;

import android.os.Bundle;
import android.os.Parcelable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.sframe.ui.controller.AppPageController;

/**
 * @Author create by Zoran on 2019-09-26
 * @Email : 171905184@qq.com
 * @Description :
 */
public class NavigatorController<Page> implements LifecycleEventObserver {

	private final NavigatorProvider mNavigatorProvider = new NavigatorProvider();

	private final AppPageController<Page> mPageController;
	private final ArrayList<Navigator.NavDestination> mNavDestinationPool = new ArrayList<>();
	private final ArrayDeque<NavigatorBackStackEntry> mNavigatorBackStack = new ArrayDeque<>();
	private final OnPageBackPressedCallback mOnBackPressedCallback = new OnPageBackPressedCallback();

	public NavigatorController(@NonNull AppPageController<Page> pageController) {
		this.mPageController = pageController;
		this.mPageController.getLifecycle()
				.addObserver(this);

		this.mOnBackPressedCallback.remove();
		pageController.requireFragmentActivity()
				.getOnBackPressedDispatcher()
				.addCallback(pageController, this.mOnBackPressedCallback);

		this.addNavigator(Navigator.NAME_FRAGMENT, new FragmentNavigator<>(pageController));
		this.addNavigator(Navigator.NAME_DIALOG_FRAGMENT, new DialogFragmentNavigator<>(pageController));
		this.addNavigator(Navigator.NAME_FRAGMENT_ACTIVITY, new FragmentActivityNavigator<>(pageController));
	}

	@Override
	public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
		if (Lifecycle.Event.ON_DESTROY == event) {
			this.mNavDestinationPool.clear();
			this.mNavigatorProvider.clear();
		}
	}

	@Nullable
	public final Navigator.NavDestination navigate(@NonNull Navigator.NavDestination navDestination) {
		return this.navigate(navDestination, null);
	}

	@Nullable
	public final Navigator.NavDestination navigate(@NonNull Navigator.NavDestination navDestination, @Nullable Bundle args) {
		return this.navigate(navDestination, args, null);
	}

	@Nullable
	public Navigator.NavDestination navigate(@NonNull Navigator.NavDestination navDestination, @Nullable Bundle args, @Nullable Navigator.NavOptions navOptions) {
		final Navigator<Navigator.NavDestination> navigator = this.getNavigator(navDestination.getNavigatorName());
		final Navigator.NavDestination destination = navigator.navigate(navDestination, args, navOptions);
		if (destination != null && !Objects.equals(destination.getNavigatorName(), Navigator.NAME_FRAGMENT_ACTIVITY)) {
			final NavigatorBackStackEntry navigatorBackStackEntry = new NavigatorBackStackEntry(destination, args);
			this.mNavigatorBackStack.add(navigatorBackStackEntry);
			this.mNavDestinationPool.add(destination);
		}
		this.updateOnBackPressedCallbackEnabled();
		return destination;
	}

	public boolean popBackStack() {
		if (this.mNavigatorBackStack.isEmpty()) {
			return false;
		}
		final Navigator.NavDestination currentDestination = this.getCurrentDestination();
		if (currentDestination == null) {
			return false;
		}
		return this.popBackStackInternal(currentDestination.getDestinationId(), true);
	}

	public boolean popBackStack(@IdRes int destinationId, boolean inclusive) {
		return this.popBackStackInternal(destinationId, inclusive);
	}

	private boolean popBackStackInternal(@IdRes int destinationId, boolean inclusive) {
		if (this.mNavigatorBackStack.isEmpty()) {
			return false;
		}
		boolean foundDestination = false;
		final ArrayList<Navigator<?>> navigators = new ArrayList<>();
		final Iterator<NavigatorBackStackEntry> iterator = this.mNavigatorBackStack.descendingIterator();
		while (iterator.hasNext()) {
			Navigator.NavDestination destination = iterator.next().getDestination();
			Navigator<?> navigator = this.getNavigator(destination.getNavigatorName());
			if (inclusive || destination.getDestinationId() != destinationId) {
				navigators.add(navigator);
			}
			if (destination.getDestinationId() == destinationId) {
				foundDestination = true;
				break;
			}
		}
		if (!foundDestination) {
			return false;
		}
		boolean popBackStack = false;
		for (Navigator<?> navigator : navigators) {
			if (navigator.popBackStack()) {
				this.mNavigatorBackStack.removeLast();
				popBackStack = true;
			} else {
				break;
			}
		}
		this.updateOnBackPressedCallbackEnabled();
		return popBackStack;
	}

	private static final String KEY_NAVIGATOR_NAMES = "androidx:navigator:state:navigatorNames";
	private static final String KEY_NAVIGATOR_STATE = "androidx:navigator:state:navigatorState";
	private static final String KEY_BACK_STACK_STATE = "androidx:navigator:state:backStackState";

	public void onSaveInstanceState(@NonNull Bundle saveInstanceState) {
		final Bundle navigatorInstanceState = new Bundle();
		final ArrayList<String> navigatorNames = new ArrayList<>();
		for (Map.Entry<String, Navigator<? extends Navigator.NavDestination>> entry :
				this.mNavigatorProvider.getNavigators().entrySet()) {
			Bundle navigatorSaveInstanceState = entry.getValue().onSaveInstanceState();
			if (navigatorSaveInstanceState != null) {
				String navigatorName = entry.getKey();
				navigatorNames.add(navigatorName);
				navigatorInstanceState.putBundle(navigatorName, navigatorSaveInstanceState);
			}
		}
		navigatorInstanceState.putStringArrayList(KEY_NAVIGATOR_NAMES, navigatorNames);
		saveInstanceState.putBundle(KEY_NAVIGATOR_STATE, navigatorInstanceState);

		int backStackIndex = 0;
		final Parcelable[] parcelables = new Parcelable[this.mNavigatorBackStack.size()];
		for (NavigatorBackStackEntry entry : this.mNavigatorBackStack) {
			parcelables[backStackIndex++] = new NavigatorBackStackState(entry);
		}
		saveInstanceState.putParcelableArray(KEY_BACK_STACK_STATE, parcelables);
	}

	public void onRestoreInstanceState(@Nullable Bundle saveInstanceState) {
		if (saveInstanceState == null) {
			return;
		}
		saveInstanceState.setClassLoader(this.getPageController().requireContext().getClassLoader());

		final Bundle navigatorInstanceState = saveInstanceState.getBundle(KEY_NAVIGATOR_STATE);
		if (navigatorInstanceState != null) {
			final ArrayList<String> navigatorNames = navigatorInstanceState.getStringArrayList(KEY_NAVIGATOR_NAMES);
			if (navigatorNames != null) {
				for (String navigatorName : navigatorNames) {
					final Bundle navigatorSaveInstanceState = navigatorInstanceState.getBundle(navigatorName);
					final Navigator<?> navigator = this.getNavigator(navigatorName);
					navigator.onRestoreInstanceState(navigatorSaveInstanceState);
				}
			}
		}

		final Parcelable[] parcelables = saveInstanceState.getParcelableArray(KEY_BACK_STACK_STATE);
		if (parcelables != null) {
			for (Parcelable parcelable : parcelables) {
				final NavigatorBackStackState navigatorBackStackState = (NavigatorBackStackState) parcelable;
				Navigator.NavDestination destination = null;
				for (Navigator.NavDestination navDestination : this.mNavDestinationPool) {
					if (navDestination.getDestinationId() == navigatorBackStackState.getDestinationId()) {
						destination = navDestination;
						break;
					}
				}
				if (destination == null) {
					throw new IllegalStateException("unknown destination during restore: " + navigatorBackStackState.getDestinationId());
				}
				final Bundle arguments = navigatorBackStackState.getArguments();
				if (arguments != null) {
					arguments.setClassLoader(this.getPageController().requireContext().getClassLoader());
				}
				final NavigatorBackStackEntry navigatorBackStackEntry = new NavigatorBackStackEntry(destination, arguments);
				this.mNavigatorBackStack.add(navigatorBackStackEntry);
			}
			this.updateOnBackPressedCallbackEnabled();
		}
	}

	public void addNavigator(@NonNull String name, @NonNull Navigator<? extends Navigator.NavDestination> navigator) {
		this.mNavigatorProvider.addNavigator(name, navigator);
	}

	@NonNull
	public <T extends Navigator<?>> T getNavigator(@NonNull String name) {
		return this.mNavigatorProvider.getNavigator(name);
	}

	public final int getNavigatorBackStackCount() {
		return this.mNavigatorBackStack.size();
	}

	@Nullable
	public final Navigator.NavDestination getCurrentDestination() {
		if (this.mNavigatorBackStack.isEmpty()) {
			return null;
		} else {
			return this.mNavigatorBackStack.getLast().getDestination();
		}
	}

	@NonNull
	public final AppPageController<Page> getPageController() {
		return this.mPageController;
	}

	private void updateOnBackPressedCallbackEnabled() {
		this.mOnBackPressedCallback.setEnabled(this.getNavigatorBackStackCount() > 1);
	}

	final class OnPageBackPressedCallback extends OnBackPressedCallback {

		public OnPageBackPressedCallback() {
			super(false);
		}

		@Override
		public void handleOnBackPressed() {
			NavigatorController.this.popBackStack();
		}
	}
}
