package androidx.sframe.ui.controller.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.sframe.ui.abs.AbsDialogFragment;
import androidx.sframe.ui.abs.AbsPopupWindow;
import androidx.sframe.ui.controller.AppPageController;

/**
 * Author create by ok on 2019-06-18
 * Email : ok@163.com.
 */
public final class AppPageControllerHelper {

	@NonNull
	public static <Page> AppPageController<?> getHostPageController(@NonNull AppPageController<Page> pageController) {
		final Page pageOwner = pageController.getPageOwner();
		if (pageOwner instanceof AbsPopupWindow) {
			return ((AbsPopupWindow) pageOwner).getHostPageController();
		} else if (pageOwner instanceof AbsDialogFragment) {
			return ((AbsDialogFragment) pageOwner).getHostPageController();
		}
		return pageController;
	}

	@Nullable
	public static <Page> Bundle getArguments(@NonNull AppPageController<Page> pageController) {
		final Page pageOwner = pageController.getPageOwner();
		Bundle arguments = null;
		if (pageOwner instanceof AbsPopupWindow) {
			arguments = ((AbsPopupWindow) pageOwner).getArguments();
		} else if (pageOwner instanceof Fragment) {
			arguments = ((Fragment) pageOwner).getArguments();
		} else if (pageOwner instanceof FragmentActivity) {
			Intent intent = ((FragmentActivity) pageOwner).getIntent();
			if (intent != null) {
				arguments = intent.getExtras();
			}
		}
		return arguments;
	}

	@NonNull
	public static <Page> Bundle requireArguments(@NonNull AppPageController<Page> pageController) {
		final Bundle arguments = getArguments(pageController);
		if (arguments == null) {
			throw new IllegalStateException("Page " + pageController.getPageOwner() + " does not have a args set");
		}
		return arguments;
	}

	@Nullable
	public static <Page> Context getContext(@NonNull AppPageController<Page> pageController) {
		final Page pageOwner = pageController.getPageOwner();
		Context context = null;
		if (pageOwner instanceof AbsPopupWindow) {
			context = getContext(((AbsPopupWindow) pageOwner).getHostPageController());
		} else if (pageOwner instanceof Fragment) {
			context = ((Fragment) pageOwner).getContext();
		} else if (pageOwner instanceof FragmentActivity) {
			context = ((FragmentActivity) pageOwner);
		}
		return context;
	}

	@NonNull
	public static <Page> Context requireContext(@NonNull AppPageController<Page> pageController) {
		final Context context = getContext(pageController);
		if (context == null) {
			throw new IllegalStateException("Page " + pageController.getPageOwner() + " not attached to an context.");
		}
		return context;
	}

	@Nullable
	public static <Page> FragmentActivity getFragmentActivity(@NonNull AppPageController<Page> pageController) {
		final Page pageOwner = pageController.getPageOwner();
		FragmentActivity fragmentActivity = null;
		if (pageOwner instanceof AbsPopupWindow) {
			fragmentActivity = getFragmentActivity(((AbsPopupWindow) pageOwner).getHostPageController());
		} else if (pageOwner instanceof Fragment) {
			fragmentActivity = ((Fragment) pageOwner).getActivity();
		} else if (pageOwner instanceof FragmentActivity) {
			fragmentActivity = ((FragmentActivity) pageOwner);
		}
		return fragmentActivity;
	}

	@NonNull
	public static <Page> FragmentActivity requireFragmentActivity(@NonNull AppPageController<Page> pageController) {
		final FragmentActivity fragmentActivity = getFragmentActivity(pageController);
		if (fragmentActivity == null) {
			throw new IllegalStateException("Page " + pageController.getPageOwner() + " not attached to an activity.");
		}
		return fragmentActivity;
	}

	@NonNull
	public static <Page> FragmentManager requireSupportFragmentManager(@NonNull AppPageController<Page> pageController) {
		final Page pageOwner = pageController.getPageOwner();
		FragmentManager fragmentManager = null;
		if (pageOwner instanceof AbsPopupWindow) {
			fragmentManager = requireSupportFragmentManager(((AbsPopupWindow) pageOwner).getHostPageController());
		} else if (pageOwner instanceof Fragment) {
			fragmentManager = ((Fragment) pageOwner).getFragmentManager();
		} else if (pageOwner instanceof FragmentActivity) {
			fragmentManager = ((FragmentActivity) pageOwner).getSupportFragmentManager();
		}
		if (fragmentManager == null) {
			throw new IllegalStateException("Page " + pageOwner + " not attached to an FragmentManager.");
		}
		return fragmentManager;
	}

	@NonNull
	public static <Page> FragmentManager requireChildFragmentManager(@NonNull AppPageController<Page> pageController) {
		final Page pageOwner = pageController.getPageOwner();
		FragmentManager fragmentManager = null;
		if (pageOwner instanceof AbsPopupWindow) {
			fragmentManager = requireChildFragmentManager(((AbsPopupWindow) pageOwner).getHostPageController());
		} else if (pageOwner instanceof Fragment) {
			fragmentManager = ((Fragment) pageOwner).getChildFragmentManager();
		} else if (pageOwner instanceof FragmentActivity) {
			fragmentManager = ((FragmentActivity) pageOwner).getSupportFragmentManager();
		}
		if (fragmentManager == null) {
			throw new IllegalStateException("Page " + pageOwner + " not attached to an ChildFragmentManager.");
		}
		return fragmentManager;
	}

	public static <Page> void startActivity(@NonNull AppPageController<Page> pageController, @NonNull Intent intent, @Nullable Bundle options) {
		final Page pageOwner = pageController.getPageOwner();
		if (pageOwner instanceof AbsPopupWindow) {
			startActivity(((AbsPopupWindow) pageOwner).getHostPageController(), intent, options);
		} else if (pageOwner instanceof Fragment) {
			((Fragment) pageOwner).startActivity(intent, options);
		} else if (pageOwner instanceof FragmentActivity) {
			((FragmentActivity) pageOwner).startActivity(intent, options);
		}
	}

	public static <Page> void startActivityForResult(@NonNull AppPageController<Page> pageController, @NonNull Intent intent, @Nullable Bundle options, int requestCode) {
		final Page pageOwner = pageController.getPageOwner();
		if (pageOwner instanceof AbsPopupWindow) {
			startActivityForResult(((AbsPopupWindow) pageOwner).getHostPageController(), intent, options, requestCode);
		} else if (pageOwner instanceof Fragment) {
			((Fragment) pageOwner).startActivityForResult(intent, requestCode, options);
		} else if (pageOwner instanceof FragmentActivity) {
			((FragmentActivity) pageOwner).startActivityForResult(intent, requestCode, options);
		}
	}
}
