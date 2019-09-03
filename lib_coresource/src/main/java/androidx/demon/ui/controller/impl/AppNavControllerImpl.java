package androidx.demon.ui.controller.impl;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import java.lang.reflect.InvocationTargetException;

import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.demon.R;
import androidx.demon.model.AgentNavModel;
import androidx.demon.ui.AppCompatNavAgentActivity;
import androidx.demon.ui.abs.AbsDialogFragment;
import androidx.demon.ui.controller.AppCompatNavHostController;
import androidx.demon.ui.controller.AppNavController;
import androidx.demon.ui.controller.AppNavigation;
import androidx.demon.ui.controller.AppPageController;
import androidx.demon.ui.dialog.ProgressDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.FragmentNavigator;

/**
 * Author create by ok on 2019-06-18
 * Email : ok@163.com.
 */
final class AppNavControllerImpl<Page> implements AppNavController<Page> {

	private final AppPageController<Page> mPageController;
	private final NavOptions mNavOptions;

	private ProgressDialogFragment mProgressDialogFragment;

	AppNavControllerImpl(@NonNull AppPageController<Page> pageController) {
		this.mPageController = pageController;
		// default options
		this.mNavOptions = new NavOptions.Builder()
				.setEnterAnim(R.anim.slide_in_from_right)
				.setExitAnim(R.anim.slide_out_to_left)
				.setPopEnterAnim(R.anim.slide_in_from_left)
				.setPopExitAnim(R.anim.slide_out_to_right)
				.build();
	}

	@NonNull
	@Override
	public final AppPageController<Page> getPageController() {
		return this.mPageController;
	}

	@Override
	public AppNavController<Page> showProgressPage() {
		if (this.mProgressDialogFragment != null) {
			if (this.mProgressDialogFragment.getDialog() != null &&
					this.mProgressDialogFragment.getDialog().isShowing()) {
				return this;
			}
			this.mProgressDialogFragment.dismiss();
		}
		this.mProgressDialogFragment = new ProgressDialogFragment(AppPageControllerHelper.getHostPageController(this.getPageController()));
		this.mProgressDialogFragment.getPageController().show();
		return this;
	}

	@Override
	public AppNavController<Page> dismissProgressPage() {
		if (this.mProgressDialogFragment != null) {
			this.mProgressDialogFragment.dismiss();
		}
		return this;
	}

	@Override
	public AppNavController<Page> showPage(@NonNull Class<? extends DialogFragment> pageClass) {
		return this.showPage(pageClass, null);
	}

	@Override
	public AppNavController<Page> showPage(@NonNull Class<? extends DialogFragment> pageClass, @Nullable Bundle args) {
		return this.navigateDia(pageClass, args);
	}

	@Override
	public AppNavController<Page> pushPage(@NonNull Class<? extends Fragment> pageClass) {
		return this.pushPage(pageClass, null);
	}

	@Override
	public AppNavController<Page> pushPage(@NonNull Class<? extends Fragment> pageClass, @Nullable Bundle args) {
		return this.pushPage(pageClass, args, this.mNavOptions);
	}

	@Override
	public AppNavController<Page> pushPage(@NonNull Class<? extends Fragment> pageClass, @Nullable Bundle args, @Nullable NavOptions navOptions) {
		return this.pushPage(pageClass, args, navOptions, null);
	}

	@Override
	public AppNavController<Page> pushPage(@NonNull Class<? extends Fragment> pageClass, @Nullable Bundle args, @Nullable NavOptions navOptions, @Nullable FragmentNavigator.Extras navigatorExtras) {
		this.getNavHostController().navigateFra(pageClass, args, navOptions, navigatorExtras);
		return this;
	}

	@Override
	public AppNavController<Page> startPage(@NonNull Class<? extends Fragment> pageClass) {
		return this.startPage(pageClass, null);
	}

	@Override
	public AppNavController<Page> startPage(@NonNull Class<? extends Fragment> pageClass, @Nullable Bundle args) {
		return this.startPage(pageClass, args, null);
	}

	@Override
	public AppNavController<Page> startPage(@NonNull Class<? extends Fragment> pageClass, @Nullable Bundle args, @Nullable Bundle options) {
		return this.startActivity(AppCompatNavAgentActivity.class, AppCompatNavAgentActivity.create(new AgentNavModel(pageClass, args)), options);
	}

	@Override
	public AppNavController<Page> startPageForResult(@NonNull Class<? extends Fragment> pageClass, int requestCode) {
		return this.startPageForResult(pageClass, null, requestCode);
	}

	@Override
	public AppNavController<Page> startPageForResult(@NonNull Class<? extends Fragment> pageClass, @Nullable Bundle args, int requestCode) {
		return this.startPageForResult(pageClass, args, null, requestCode);
	}

	@Override
	public AppNavController<Page> startPageForResult(@NonNull Class<? extends Fragment> pageClass, @Nullable Bundle args, @Nullable Bundle options, int requestCode) {
		return this.startActivityForResult(AppCompatNavAgentActivity.class, AppCompatNavAgentActivity.create(new AgentNavModel(pageClass, args)), options, requestCode);
	}

	@Override
	public AppNavController<Page> startActivity(@NonNull Intent intent) {
		return this.startActivity(intent, null);
	}

	@Override
	public AppNavController<Page> startActivity(@NonNull Intent intent, @Nullable Bundle options) {
		return this.navigateAct(intent, options);
	}

	@Override
	public AppNavController<Page> startActivity(@NonNull Class<? extends FragmentActivity> pageClass) {
		return this.startActivity(pageClass, null);
	}

	@Override
	public AppNavController<Page> startActivity(@NonNull Class<? extends FragmentActivity> pageClass, @Nullable Bundle args) {
		return this.startActivity(pageClass, args, null);
	}

	@Override
	public AppNavController<Page> startActivity(@NonNull Class<? extends FragmentActivity> pageClass, @Nullable Bundle args, @Nullable Bundle options) {
		return this.navigateAct(pageClass, args, options);
	}

	@Override
	public AppNavController<Page> startActivityForResult(@NonNull Intent intent, int requestCode) {
		return this.startActivityForResult(intent, null, requestCode);
	}

	@Override
	public AppNavController<Page> startActivityForResult(@NonNull Intent intent, @Nullable Bundle options, int requestCode) {
		return this.navigateActForResult(intent, options, requestCode);
	}

	@Override
	public AppNavController<Page> startActivityForResult(@NonNull Class<? extends FragmentActivity> pageClass, int requestCode) {
		return this.startActivityForResult(pageClass, null, requestCode);
	}

	@Override
	public AppNavController<Page> startActivityForResult(@NonNull Class<? extends FragmentActivity> pageClass, @Nullable Bundle args, int requestCode) {
		return this.startActivityForResult(pageClass, args, null, requestCode);
	}

	@Override
	public AppNavController<Page> startActivityForResult(@NonNull Class<? extends FragmentActivity> pageClass, @Nullable Bundle args, @Nullable Bundle options, int requestCode) {
		return this.navigateActForResult(pageClass, args, options, requestCode);
	}

	@Override
	public AppNavController<Page> navigate(@NavigationRes int resId) {
		return this.navigate(resId, null);
	}

	@Override
	public AppNavController<Page> navigate(@NavigationRes int resId, @Nullable Bundle args) {
		return this.navigate(resId, args, this.mNavOptions);
	}

	@Override
	public AppNavController<Page> navigate(@NavigationRes int resId, @Nullable Bundle args, @Nullable NavOptions navOptions) {
		return this.navigate(resId, args, navOptions, null);
	}

	@Override
	public AppNavController<Page> navigate(@NavigationRes int resId, @Nullable Bundle args, @Nullable NavOptions navOptions, @Nullable Navigator.Extras navigatorExtras) {
		this.getNavHostController().navigate(resId, args, navOptions, navigatorExtras);
		return this;
	}

	@Override
	public AppNavController<Page> addGraph(@NavigationRes int navResId) {
		return this.addGraph(navResId, null);
	}

	@Override
	public AppNavController<Page> addGraph(@NavigationRes int navResId, @Nullable Bundle args) {
		this.getNavHostController().addGraph(navResId, args);
		return this;
	}

	@Override
	public AppNavController<Page> setGraph(@NavigationRes int navResId) {
		return this.setGraph(navResId, null);
	}

	@Override
	public AppNavController<Page> setGraph(@NavigationRes int navResId, @Nullable Bundle args) {
		this.getNavHostController().setGraph(navResId, args);
		return this;
	}

	@Override
	public boolean navigateUp() {
		return this.getNavHostController().navigateUp();
	}

	@Override
	public boolean popBackStack() {
		return this.getNavHostController().popBackStack();
	}

	@Override
	public boolean popBackStack(int destinationId, boolean inclusive) {
		return this.getNavHostController().popBackStack(destinationId, inclusive);
	}

	private AppNavController<Page> navigateAct(@NonNull Class<? extends FragmentActivity> pageClass, @Nullable Bundle args, @Nullable Bundle options) {
		final Intent preIntent = new Intent();
		preIntent.setComponent(new ComponentName(AppPageControllerHelper.requireContext(this.getPageController()), pageClass));
		if (args != null) {
			preIntent.putExtras(args);
		}
		return this.navigateAct(preIntent, options);
	}

	private AppNavController<Page> navigateAct(@NonNull Intent intent, @Nullable Bundle options) {
		AppPageControllerHelper.startActivity(this.getPageController(), intent, options);
		return this;
	}

	private AppNavController<Page> navigateActForResult(@NonNull Class<? extends FragmentActivity> pageClass, @Nullable Bundle args, @Nullable Bundle options, int requestCode) {
		final Intent preIntent = new Intent();
		preIntent.setComponent(new ComponentName(AppPageControllerHelper.requireContext(this.getPageController()), pageClass));
		if (args != null) {
			preIntent.putExtras(args);
		}
		return this.navigateActForResult(preIntent, options, requestCode);
	}

	private AppNavController<Page> navigateActForResult(@NonNull Intent intent, @Nullable Bundle options, int requestCode) {
		AppPageControllerHelper.startActivityForResult(this.getPageController(), intent, options, requestCode);
		return this;
	}

	private AppNavController<Page> navigateDia(@NonNull Class<? extends DialogFragment> pageClass, @Nullable Bundle args) {
//		this.getNavHostController().navigateDia(pageClass, args);
		try {
			final DialogFragment preDialogFragment;
			if (AbsDialogFragment.class.isAssignableFrom(pageClass)) {
				preDialogFragment = pageClass.getConstructor(AppPageController.class).newInstance(AppPageControllerHelper.getHostPageController(this.getPageController()));
			} else {
				preDialogFragment = pageClass.getConstructor().newInstance();
			}
			preDialogFragment.setArguments(args);

			if (preDialogFragment instanceof AbsDialogFragment) {
				((AbsDialogFragment) preDialogFragment).getPageController().show();
			} else {
				preDialogFragment.show(AppPageControllerHelper.requireFragmentManager(this.getPageController()), pageClass.getName());
			}
		} catch (IllegalAccessException e) {
			throw new Fragment.InstantiationException("Unable to instantiate fragment " + pageClass
					+ ": make sure class name exists, is public, and has an"
					+ " empty constructor that is public", e);
		} catch (InstantiationException e) {
			throw new Fragment.InstantiationException("Unable to instantiate fragment " + pageClass
					+ ": make sure class name exists, is public, and has an"
					+ " empty constructor that is public", e);
		} catch (NoSuchMethodException e) {
			throw new Fragment.InstantiationException("Unable to instantiate fragment " + pageClass
					+ ": could not find Fragment constructor", e);
		} catch (InvocationTargetException e) {
			throw new Fragment.InstantiationException("Unable to instantiate fragment " + pageClass
					+ ": calling Fragment constructor caused an exception", e);
		}
		return this;
	}

	@NonNull
	private AppCompatNavHostController getNavHostController() {
		return AppNavigation.findNavHostController(AppPageControllerHelper.requireActivity(this.getPageController()));
	}
}
