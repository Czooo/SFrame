package androidx.sframe.navigator;

import android.content.Context;
import android.os.Bundle;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.sframe.ui.abs.AbsDialogFragment;
import androidx.sframe.ui.controller.AppPageController;
import androidx.sframe.ui.controller.impl.AppPageControllerHelper;

/**
 * @Author create by Zoran on 2019-09-26
 * @Email : 171905184@qq.com
 * @Description :
 */
public class DialogFragmentNavigator<Page> extends Navigator<DialogFragmentNavigator.Destination> {

	private static final String KEY_DIALOG_COUNT = "androidx-navigator:dialogCount";
	private static final String DIALOG_FRAGMENT_TAG = "androidx-navigator:dialog:";

	private final AppPageController<Page> mPageController;
	private int mDialogCount;

	private LifecycleEventObserver mObserver = new LifecycleEventObserver() {
		@Override
		public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
			if (Lifecycle.Event.ON_STOP == event) {
				final DialogFragment dialogFragment = (DialogFragment) source;
				if (!dialogFragment.requireDialog().isShowing()) {
					DialogFragmentNavigator.this.getPageController()
							.getNavController()
							.popBackStack();
				}
			}
		}
	};

	public DialogFragmentNavigator(@NonNull AppPageController<Page> pageController) {
		this.mPageController = pageController;
	}

	@NonNull
	@Override
	public Destination obtain() {
		return new Destination(Navigator.NAME_DIALOG_FRAGMENT);
	}

	@Nullable
	@Override
	public NavDestination navigate(@NonNull Destination navDestination, @Nullable Bundle args, @Nullable NavOptions navOptions) {
		final FragmentManager fragmentManager = this.getFragmentManager();
		if (fragmentManager.isStateSaved()) {
			return null;
		}
		final DialogFragment dialogFragment = this.instantiate(this.getContext(), navDestination.mDialogFragmentClass);
		dialogFragment.setArguments(args);
		dialogFragment.getLifecycle().addObserver(this.mObserver);
		dialogFragment.show(fragmentManager, DIALOG_FRAGMENT_TAG + this.mDialogCount++);
		return navDestination;
	}

	@Override
	public boolean popBackStack() {
		if (this.mDialogCount == 0) {
			return false;
		}
		final FragmentManager fragmentManager = this.getFragmentManager();
		if (fragmentManager.isStateSaved()) {
			return false;
		}
		final Fragment fragment = fragmentManager
				.findFragmentByTag(DIALOG_FRAGMENT_TAG + --this.mDialogCount);
		if (fragment != null) {
			fragment.getLifecycle()
					.removeObserver(this.mObserver);
			((DialogFragment) fragment).dismiss();
		}
		return true;
	}

	@Nullable
	@Override
	public Bundle onSaveInstanceState() {
		if (this.mDialogCount == 0) {
			return super.onSaveInstanceState();
		}
		final Bundle saveInstanceState = new Bundle();
		saveInstanceState.putInt(KEY_DIALOG_COUNT, this.mDialogCount);
		return saveInstanceState;
	}

	@Override
	public void onRestoreInstanceState(@Nullable Bundle saveInstanceState) {
		if (saveInstanceState != null) {
			this.mDialogCount = saveInstanceState.getInt(KEY_DIALOG_COUNT, 0);
			final FragmentManager fragmentManager = this.getFragmentManager();
			for (int index = 0; index < this.mDialogCount; index++) {
				final DialogFragment fragment = (DialogFragment) fragmentManager
						.findFragmentByTag(DIALOG_FRAGMENT_TAG + index);
				if (fragment != null) {
					fragment.getLifecycle().addObserver(this.mObserver);
				} else {
					throw new IllegalStateException("DialogFragment " + index
							+ " doesn't exist in the FragmentManager");
				}
			}
		}
		super.onRestoreInstanceState(saveInstanceState);
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
	private DialogFragment instantiate(@NonNull Context context, @NonNull Class<? extends DialogFragment> dialogFragmentClass) {
		try {
			final DialogFragment dialogFragment;
			if (AbsDialogFragment.class.isAssignableFrom(dialogFragmentClass)) {
				final Constructor<? extends DialogFragment> constructor = dialogFragmentClass.getConstructor(AppPageController.class);
				constructor.setAccessible(true);
				dialogFragment = constructor.newInstance(AppPageControllerHelper.getHostPageController(this.getPageController()));
			} else {
				final Constructor<? extends DialogFragment> constructor = dialogFragmentClass.getConstructor();
				constructor.setAccessible(true);
				dialogFragment = constructor.newInstance();
			}
			return dialogFragment;
		} catch (IllegalAccessException e) {
			throw new Fragment.InstantiationException("Unable to instantiate fragment " + dialogFragmentClass
					+ ": make sure class name exists, is public, and has an"
					+ " empty constructor that is public", e);
		} catch (InstantiationException e) {
			throw new Fragment.InstantiationException("Unable to instantiate fragment " + dialogFragmentClass
					+ ": make sure class name exists, is public, and has an"
					+ " empty constructor that is public", e);
		} catch (NoSuchMethodException e) {
			throw new Fragment.InstantiationException("Unable to instantiate fragment " + dialogFragmentClass
					+ ": could not find Fragment constructor", e);
		} catch (InvocationTargetException e) {
			throw new Fragment.InstantiationException("Unable to instantiate fragment " + dialogFragmentClass
					+ ": calling Fragment constructor caused an exception", e);
		}
	}

	public static class Destination extends Navigator.NavDestination {

		private Class<? extends DialogFragment> mDialogFragmentClass;

		public Destination(@NonNull String navigatorName) {
			super(navigatorName);
		}

		public Destination setDialogFragmentClass(@NonNull Class<? extends DialogFragment> dialogFragmentClass) {
			this.mDialogFragmentClass = dialogFragmentClass;
			return this;
		}
	}
}
