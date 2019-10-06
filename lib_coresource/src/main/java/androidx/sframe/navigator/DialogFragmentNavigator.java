package androidx.sframe.navigator;

import android.os.Bundle;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleObserver;
import androidx.sframe.ui.abs.AbsDialogFragment;
import androidx.sframe.ui.controller.AppPageController;
import androidx.sframe.ui.controller.impl.AppPageControllerHelper;

/**
 * @Author create by Zoran on 2019-10-04
 * @Email : 171905184@qq.com
 * @Description :
 */
@Navigator.Name("dialog")
public class DialogFragmentNavigator<Page> extends Navigator<DialogFragmentNavigator.Destination> implements LifecycleObserver {

	private static final String DIALOG_FRAGMENT_TAG = "androidx-navigator-dialogFragment:tag:";

	private final AppPageController<Page> mPageController;
	private int mDialogFragmentCount = 0;

	public DialogFragmentNavigator(@NonNull AppPageController<Page> pageController) {
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
		final String fragmentTag = DIALOG_FRAGMENT_TAG + this.mDialogFragmentCount++;
		final DialogFragment dialogFragment = this.instantiate(navDestination.mDialogFragmentClass);
		dialogFragment.setArguments(args);
		dialogFragment.show(fragmentManager, fragmentTag);
		return navDestination;
	}

	@NonNull
	public final FragmentManager getFragmentManager() {
		return AppPageControllerHelper.requireChildFragmentManager(
				AppPageControllerHelper.getHostPageController(this.getPageController())
		);
	}

	@NonNull
	public final AppPageController<Page> getPageController() {
		return this.mPageController;
	}

	@NonNull
	private DialogFragment instantiate(@NonNull Class<? extends DialogFragment> dialogFragmentClass) {
		try {
			final DialogFragment dialogFragment;
			if (AbsDialogFragment.class.isAssignableFrom(dialogFragmentClass)) {
				final Constructor<? extends DialogFragment> constructor = dialogFragmentClass
						.getConstructor(AppPageController.class);
				constructor.setAccessible(true);
				dialogFragment = constructor.newInstance(AppPageControllerHelper.getHostPageController(this.getPageController()));
			} else {
				final Constructor<? extends DialogFragment> constructor = dialogFragmentClass
						.getConstructor();
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

	public static class Destination extends NavDestination {

		private Class<? extends DialogFragment> mDialogFragmentClass;

		Destination(@NonNull Navigator<? extends NavDestination> navigator) {
			super(navigator);
		}

		public final void setDialogFragmentClass(@NonNull Class<? extends DialogFragment> dialogFragmentClass) {
			this.mDialogFragmentClass = dialogFragmentClass;
		}
	}
}
