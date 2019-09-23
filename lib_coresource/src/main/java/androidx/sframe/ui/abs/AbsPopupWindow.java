package androidx.sframe.ui.abs;

import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sframe.ui.controller.AppNavController;
import androidx.sframe.ui.controller.AppPageController;
import androidx.sframe.ui.controller.PopupWindowPageController;
import androidx.sframe.ui.controller.UILayoutController;
import androidx.sframe.ui.controller.UIToolbarController;
import androidx.sframe.ui.controller.UIViewController;
import androidx.sframe.ui.controller.impl.AppPagePopupWindowControllerImpl;
import androidx.sframe.widget.AppCompatPopupWindow;

/**
 * Author create by ok on 2019-06-03
 * Email : ok@163.com.
 */
public abstract class AbsPopupWindow extends AppCompatPopupWindow implements AppPageController.WindowPageProvider {

	// host pageController
	private AppPageController<?> mHostPageController;
	// this pageController
	private PopupWindowPageController<AppCompatPopupWindow> mPageController;

	public AbsPopupWindow(@NonNull AppPageController<?> hostPageController) {
		super(false);
		this.mHostPageController = hostPageController;
		this.performOnCreate();
	}

	@CallSuper
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getPageController().onCreate(savedInstanceState);
	}

	@CallSuper
	@Override
	public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		this.getPageController().onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void dismiss() {
		final PopupWindowPageController<AppCompatPopupWindow> pageController = this.getPageController();
		if (pageController instanceof AppPagePopupWindowControllerImpl) {
			if (((AppPagePopupWindowControllerImpl) this.getPageController()).performDismiss()) {
				return;
			}
		}
		super.dismiss();
	}

	@Override
	public void onPageDataSourceChanged(@Nullable Object params) {

	}

	@NonNull
	public final UIViewController getViewController() {
		return this.getPageController().getViewController();
	}

	@NonNull
	public final UILayoutController getLayoutController() {
		return this.getPageController().getLayoutController();
	}

	@NonNull
	public final UIToolbarController getToolbarController() {
		return this.getPageController().getToolbarController();
	}

	@NonNull
	public final AppNavController<AppCompatPopupWindow> getAppNavController() {
		return this.getPageController().getAppNavController();
	}

	@NonNull
	public PopupWindowPageController<AppCompatPopupWindow> getPageController() {
		if (this.mPageController == null) {
			this.mPageController = new AppPagePopupWindowControllerImpl(this);
		}
		return this.mPageController;
	}

	@NonNull
	@Override
	public final AppPageController<?> getHostPageController() {
		if (this.mHostPageController == null) {
			throw new IllegalStateException("Your page " + this + " is not yet attached to the HostPage instance. ");
		}
		return this.mHostPageController;
	}
}
