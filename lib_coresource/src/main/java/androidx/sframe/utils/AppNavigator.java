package androidx.sframe.utils;

import android.view.View;
import android.view.ViewParent;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.sframe.R;
import androidx.sframe.ui.abs.AbsActivity;
import androidx.sframe.ui.controller.AppNavController;
import androidx.sframe.ui.controller.AppPageController;

/**
 * Author create by ok on 2019-06-05
 * Email : ok@163.com.
 */
public class AppNavigator {

	public static <Page> void setAppNavController(@Nullable View view, @NonNull AppNavController<Page> controller) {
		if (view == null) {
			return;
		}
		view.setTag(R.id.app_controller_nav_tag, controller);
	}

	@NonNull
	public static <Page> AppNavController<FragmentActivity> findSupportNavController(@NonNull AppPageController<Page> pageController) {
		final FragmentActivity fragmentActivity = pageController.requireFragmentActivity();
		if (fragmentActivity instanceof AbsActivity) {
			return ((AbsActivity) fragmentActivity).getNavController();
		}
		throw new IllegalStateException("AppPageController " + pageController + " does not have a support AppNavController set");
	}

	@NonNull
	public static <Page> AppNavController<Page> findNavController(@Nullable View view) {
		final AppNavController<Page> controller = findNavControllerByView(view);
		if (controller == null) {
			throw new IllegalStateException("View " + view + " does not have a AppNavController set");
		}
		return controller;
	}

	@Nullable
	private static <Page> AppNavController<Page> findNavControllerByView(@Nullable View view) {
		while (view != null) {
			AppNavController<Page> controller = getNavController(view);
			if (controller != null) {
				return controller;
			}
			ViewParent parent = view.getParent();
			view = parent instanceof View ? (View) parent : null;
		}
		return null;
	}

	@Nullable
	@SuppressWarnings("unchecked")
	private static <Page> AppNavController<Page> getNavController(@NonNull View view) {
		Object tag = view.getTag(R.id.app_controller_nav_tag);
		AppNavController<Page> controller = null;
		if (tag instanceof WeakReference) {
			controller = ((WeakReference<AppNavController>) tag).get();
		} else if (tag instanceof AppNavController) {
			controller = (AppNavController) tag;
		}
		return controller;
	}

	public static <Page> void setAppPageController(@Nullable View view, @NonNull AppPageController<Page> pageController) {
		if (view == null) {
			return;
		}
		view.setTag(R.id.app_controller_page_tag, pageController);
	}

	@NonNull
	public static <Page> AppPageController<Page> findPageController(@Nullable View preView) {
		final AppPageController<Page> pageController = findPageControllerByView(preView);
		if (pageController == null) {
			throw new IllegalStateException("View " + preView + " does not have a AppPageController set");
		}
		return pageController;
	}

	@Nullable
	@SuppressWarnings("unchecked")
	private static <Controller extends AppPageController<?>> Controller findPageControllerByView(@Nullable View view) {
		while (view != null) {
			AppPageController<?> controller = getPageController(view);
			if (controller != null) {
				return (Controller) controller;
			}
			ViewParent parent = view.getParent();
			view = parent instanceof View ? (View) parent : null;
		}
		return null;
	}

	@Nullable
	@SuppressWarnings("unchecked")
	private static <Controller extends AppPageController> Controller getPageController(@NonNull View view) {
		Object tag = view.getTag(R.id.app_controller_page_tag);
		Controller controller = null;
		if (tag instanceof WeakReference) {
			controller = ((WeakReference<Controller>) tag).get();
		} else if (tag instanceof AppPageController) {
			controller = (Controller) tag;
		}
		return controller;
	}
}
