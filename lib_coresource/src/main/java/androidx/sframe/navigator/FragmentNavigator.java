package androidx.sframe.navigator;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayDeque;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.sframe.ui.controller.AppPageController;
import androidx.sframe.ui.controller.impl.AppPageControllerHelper;

/**
 * @Author create by Zoran on 2019-09-25
 * @Email : 171905184@qq.com
 * @Description :
 */
public class FragmentNavigator<Page> extends Navigator<FragmentNavigator.Destination> {

	private static final String KEY_BACK_STACK_IDS = "androidx-navigator-fragment:navigator:backStackIds";

	private final AppPageController<Page> mPageController;
	private final ArrayDeque<Integer> mBackStack = new ArrayDeque<>();

	public FragmentNavigator(@NonNull AppPageController<Page> pageController) {
		this.mPageController = pageController;
	}

	@NonNull
	@Override
	public Destination obtain() {
		return new Destination(Navigator.NAME_FRAGMENT);
	}

	@Nullable
	@Override
	public NavDestination navigate(@NonNull Destination navDestination, @Nullable Bundle args, @Nullable NavOptions navOptions) {
		final FragmentManager fragmentManager = this.getFragmentManager();
		if (fragmentManager.isStateSaved()) {
			return null;
		}
		final Fragment fragment = fragmentManager.getFragmentFactory()
				.instantiate(this.getContext().getClassLoader(),
						navDestination.mFragmentClass.getName());
		fragment.setArguments(args);

		final @IdRes int destId = this.mBackStack.size();
		final int backStackIndex = this.mBackStack.size() + 1;
		final String fragmentTag = this.generateFragmentTag(backStackIndex, destId);
		final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		if (navOptions != null && !this.mBackStack.isEmpty()) {
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

		if (!this.mBackStack.isEmpty()) {
			fragmentTransaction.addToBackStack(fragmentTag);
		}

		fragmentTransaction.setReorderingAllowed(true);
		fragmentTransaction.commit();
		this.mBackStack.add(destId);
		return navDestination;
	}

	@Override
	public boolean popBackStack() {
		if (this.mBackStack.isEmpty()) {
			return false;
		}
		final FragmentManager fragmentManager = this.getFragmentManager();
		if (fragmentManager.isStateSaved()) {
			return false;
		}
		fragmentManager.popBackStack(
				this.generateFragmentTag(this.mBackStack.size(), this.mBackStack.peekLast()),
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
		this.mBackStack.removeLast();
		return true;
	}

	@Nullable
	@Override
	public Bundle onSaveInstanceState() {
		final int[] backStackIds = new int[this.mBackStack.size()];
		int index = 0;
		for (Integer destId : this.mBackStack) {
			backStackIds[index++] = destId;
		}
		Bundle saveInstanceState = new Bundle();
		saveInstanceState.putIntArray(KEY_BACK_STACK_IDS, backStackIds);
		return saveInstanceState;
	}

	@Override
	public void onRestoreInstanceState(@Nullable Bundle saveInstanceState) {
		if (saveInstanceState == null) {
			return;
		}
		final int[] backStackIds = saveInstanceState.getIntArray(KEY_BACK_STACK_IDS);
		if (backStackIds != null) {
			this.mBackStack.clear();
			for (Integer destId : backStackIds) {
				this.mBackStack.add(destId);
			}
		}
	}

	public final int getStackCount() {
		return this.mBackStack.size();
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

	@NonNull
	private String generateFragmentTag(int backStackIndex, int destId) {
		return "androidx:navigator:" + backStackIndex + ":" + destId;
	}

	public static class Destination extends Navigator.NavDestination {

		@IdRes
		private int mContainerId;
		private Class<? extends Fragment> mFragmentClass;

		public Destination(@NonNull String navigatorName) {
			super(navigatorName);
		}

		public Destination setContainerId(@IdRes int containerId) {
			this.mContainerId = containerId;
			return this;
		}

		public Destination setFragmentClass(@NonNull Class<? extends Fragment> fragmentClass) {
			this.mFragmentClass = fragmentClass;
			return this;
		}
	}
}
