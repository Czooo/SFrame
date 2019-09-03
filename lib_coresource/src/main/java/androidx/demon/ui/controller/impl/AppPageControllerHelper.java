package androidx.demon.ui.controller.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.demon.ui.abs.AbsDialogFragment;
import androidx.demon.ui.abs.AbsPopupWindow;
import androidx.demon.ui.controller.AppPageController;
import androidx.demon.widget.AppCompatPopupWindow;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * Author create by ok on 2019-06-18
 * Email : ok@163.com.
 */
final class AppPageControllerHelper {

	@NonNull
	static <Page> AppPageController<?> getHostPageController(@NonNull AppPageController<Page> pageController) {
		final Page prePage = pageController.getPageOwner();
		if (prePage instanceof AppPageController.WindowPageProvider) {
			return ((AppPageController.WindowPageProvider) prePage).getHostPageController();
		}
		return pageController;
	}

	@Nullable
	static <Page> Bundle getArguments(@NonNull AppPageController<Page> pageController) {
		final Page prePageOwner = pageController.getPageOwner();
		final Bundle preArguments;
		if (prePageOwner instanceof AppCompatPopupWindow) {
			preArguments = ((AppCompatPopupWindow) prePageOwner).getArguments();
		} else if (prePageOwner instanceof Fragment) {
			preArguments = ((Fragment) prePageOwner).getArguments();
		} else {
			preArguments = ((FragmentActivity) prePageOwner).getIntent().getExtras();
		}
		return preArguments;
	}

	@NonNull
	static <Page> Bundle requireArguments(@NonNull AppPageController<Page> pageController) {
		final Bundle preArguments = getArguments(pageController);
		if (preArguments == null) {
			throw new IllegalStateException("Page " + pageController.getPageOwner() + " does not have a args set");
		}
		return preArguments;
	}

	@NonNull
	static <Page> Context requireContext(@NonNull AppPageController<Page> pageController) {
		final Page prePageOwner = pageController.getPageOwner();
		final Context preContext;
		if (prePageOwner instanceof AbsPopupWindow) {
			preContext = requireContext(((AbsPopupWindow) prePageOwner).getHostPageController());
		} else if (prePageOwner instanceof Fragment) {
			preContext = ((Fragment) prePageOwner).getContext();
		} else {
			preContext = ((FragmentActivity) prePageOwner);
		}
		if (preContext == null) {
			throw new IllegalStateException("Page " + prePageOwner + " not attached to an context.");
		}
		return preContext;
	}

	@NonNull
	static <Page> FragmentActivity requireActivity(@NonNull AppPageController<Page> pageController) {
		final Page prePageOwner = pageController.getPageOwner();
		final FragmentActivity preFragmentActivity;
		if (prePageOwner instanceof AbsPopupWindow) {
			preFragmentActivity = requireActivity(((AbsPopupWindow) prePageOwner).getHostPageController());
		} else if (prePageOwner instanceof Fragment) {
			preFragmentActivity = ((Fragment) prePageOwner).getActivity();
		} else {
			preFragmentActivity = ((FragmentActivity) prePageOwner);
		}
		if (preFragmentActivity == null) {
			throw new IllegalStateException("Page " + prePageOwner + " not attached to an activity.");
		}
		return preFragmentActivity;
	}

	static <Page> FragmentManager requireFragmentManager(@NonNull AppPageController<Page> pageController) {
		final Page prePageOwner = pageController.getPageOwner();
		final FragmentManager preFragmentManager;
		if (prePageOwner instanceof AbsPopupWindow) {
			preFragmentManager = requireFragmentManager(((AbsPopupWindow) prePageOwner).getHostPageController());
		} else if (prePageOwner instanceof AbsDialogFragment) {
			preFragmentManager = requireFragmentManager(((AbsDialogFragment) prePageOwner).getHostPageController());
		} else if (prePageOwner instanceof Fragment) {
			preFragmentManager = ((Fragment) prePageOwner).getChildFragmentManager();
		} else {
			preFragmentManager = ((FragmentActivity) prePageOwner).getSupportFragmentManager();
		}
		if (preFragmentManager == null) {
			throw new IllegalStateException("Page " + prePageOwner + " not attached to an FragmentManager.");
		}
		return preFragmentManager;
	}

	static <Page> void startActivity(@NonNull AppPageController<Page> pageController, @NonNull Intent intent, @Nullable Bundle options) {
		final Page prePageOwner = pageController.getPageOwner();
		if (prePageOwner instanceof AbsPopupWindow) {
			startActivity(((AbsPopupWindow) prePageOwner).getHostPageController(), intent, options);
		} else if (prePageOwner instanceof Fragment) {
			((Fragment) prePageOwner).startActivity(intent, options);
		} else {
			((FragmentActivity) prePageOwner).startActivity(intent, options);
		}
	}

	static <Page> void startActivityForResult(@NonNull AppPageController<Page> pageController, @NonNull Intent intent, @Nullable Bundle options, int requestCode) {
		final Page prePageOwner = pageController.getPageOwner();
		if (prePageOwner instanceof AbsPopupWindow) {
			startActivityForResult(((AbsPopupWindow) prePageOwner).getHostPageController(), intent, options, requestCode);
		} else if (prePageOwner instanceof Fragment) {
			((Fragment) prePageOwner).startActivityForResult(intent, requestCode, options);
		} else {
			((FragmentActivity) prePageOwner).startActivityForResult(intent, requestCode, options);
		}
	}
}
