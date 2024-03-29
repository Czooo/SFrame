package androidx.sframe.ui.abs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.FloatRange;
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
public abstract class AbsPopupWindow extends AppCompatPopupWindow implements AppPageController.PageProvider {

	private final AppPageController<?> mHostPageController;
	private PopupWindowPageController<AppCompatPopupWindow> mPageController;

	public AbsPopupWindow(@NonNull AppPageController<?> hostPageController) {
		this(hostPageController, null);
	}

	public AbsPopupWindow(@NonNull AppPageController<?> hostPageController, @Nullable Bundle savedInstanceState) {
		super(hostPageController.requireContext(), savedInstanceState);
		this.mHostPageController = hostPageController;
	}

	@CallSuper
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getPageController().onCreate(savedInstanceState);
	}

	@NonNull
	@Override
	protected View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return this.getPageController().onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		this.getPageController().onViewCreated(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		this.getPageController().onSaveInstanceState(outState);
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
	public final AppNavController<AppCompatPopupWindow> getNavController() {
		return this.getPageController().getNavController();
	}

	@NonNull
	public final AppPageController<?> getHostPageController() {
		if (this.mHostPageController == null) {
			throw new IllegalStateException("Your page " + this + " is not yet attached to the HostPage instance. ");
		}
		return this.mHostPageController;
	}

	@NonNull
	public PopupWindowPageController<AppCompatPopupWindow> getPageController() {
		if (this.mPageController == null) {
			this.mPageController = new AppPagePopupWindowControllerImpl(this);
		}
		return this.mPageController;
	}

	// 全屏透明度
	public void setWindowBackgroundAlpha(@FloatRange(from = 0, to = 1.f) float alpha) {
		this.getPageController().setWindowBackgroundAlpha(alpha);
	}
}
