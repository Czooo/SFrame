package androidx.sframe.ui.controller;

import android.view.View;
import android.view.ViewParent;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sframe.R;
import androidx.fragment.app.FragmentActivity;

/**
 * Author create by ok on 2019-06-05
 * Email : ok@163.com.
 */
public class AppNavigation {

	public static void setViewNavHostController(@NonNull FragmentActivity fragmentActivity, @NonNull AppCompatNavHostController controller) {
		setViewNavHostController(fragmentActivity.getWindow().getDecorView(), controller);
	}

	public static void setViewNavHostController(@Nullable View preView, @NonNull AppCompatNavHostController navHostController) {
		if (preView == null) {
			return;
		}
		preView.setTag(R.id.app_controller_nav_tag, navHostController);
	}

	public static AppCompatNavHostController findNavHostController(@NonNull FragmentActivity fragmentActivity) {
		return findNavHostController(fragmentActivity.getWindow().getDecorView());
	}

	public static AppCompatNavHostController findNavHostController(@NonNull View preView) {
		final AppCompatNavHostController preNavHostController = findViewNavHostController(preView);
		if (preNavHostController == null) {
			throw new IllegalStateException("View " + preView + " does not have a AppCompatNavHostController set");
		}
		return preNavHostController;
	}

	@Nullable
	private static AppCompatNavHostController findViewNavHostController(@Nullable View view) {
		while (view != null) {
			AppCompatNavHostController controller = getViewNavHostController(view);
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
	private static AppCompatNavHostController getViewNavHostController(@NonNull View view) {
		Object tag = view.getTag(R.id.app_controller_nav_tag);
		AppCompatNavHostController controller = null;
		if (tag instanceof WeakReference) {
			controller = ((WeakReference<AppCompatNavHostController>) tag).get();
		} else if (tag instanceof AppCompatNavHostController) {
			controller = (AppCompatNavHostController) tag;
		}
		return controller;
	}

	public static <Page> void setViewPageController(@Nullable View preView, @NonNull AppPageController<Page> pageController) {
		if (preView == null) {
			return;
		}
		preView.setTag(R.id.app_controller_page_tag, pageController);
	}

	@NonNull
	public static <Page> AppPageController<Page> findPageController(@Nullable View preView) {
		final AppPageController<Page> pageController = findViewPageController(preView);
		if (pageController == null) {
			throw new IllegalStateException("View " + preView + " does not have a AppPageController set");
		}
		return pageController;
	}

	@Nullable
	private static <Controller extends AppPageController<?>> Controller findViewPageController(@Nullable View view) {
		while (view != null) {
			AppPageController<?> controller = getViewPageController(view);
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
	private static <Controller extends AppPageController> Controller getViewPageController(@NonNull View view) {
		Object tag = view.getTag(R.id.app_controller_page_tag);
		Controller controller = null;
		if (tag instanceof WeakReference) {
			controller = ((WeakReference<Controller>) tag).get();
		} else if (tag instanceof AppPageController) {
			controller = (Controller) tag;
		}
		return controller;
	}

//	public static boolean isTopLevelDestination(@NonNull FragmentActivity fragmentActivity) {
//		final NavHostController preNavHostController = findNavHostController(fragmentActivity).getNavHostController();
//		final NavDestination currentDestination = preNavHostController.getCurrentDestination();
//		if (currentDestination != null) {
//			final Set<Integer> mTopLevelDestinations = new HashSet<>();
//			mTopLevelDestinations.add(findStartDestination(preNavHostController.getGraph()).getLayoutId());
//			return matchDestinations(currentDestination, mTopLevelDestinations);
//		}
//		return false;
//	}
//
//	static NavDestination findStartDestination(@NonNull NavGraph graph) {
//		NavDestination startDestination = graph;
//		while (startDestination instanceof NavGraph) {
//			NavGraph parent = (NavGraph) startDestination;
//			startDestination = parent.findNode(parent.getStartDestination());
//		}
//		return startDestination;
//	}
//
//	static boolean matchDestinations(@NonNull NavDestination destination, @NonNull Set<Integer> destinationIds) {
//		NavDestination currentDestination = destination;
//		do {
//			if (destinationIds.contains(currentDestination.getLayoutId())) {
//				return true;
//			}
//			currentDestination = currentDestination.getParent();
//		} while (currentDestination != null);
//		return false;
//	}
}
