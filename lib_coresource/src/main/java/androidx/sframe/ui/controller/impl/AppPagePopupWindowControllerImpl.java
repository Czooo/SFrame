package androidx.sframe.ui.controller.impl;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStore;
import androidx.sframe.R;
import androidx.sframe.ui.controller.PopupWindowPageController;
import androidx.sframe.ui.controller.UILayoutController;
import androidx.sframe.widget.AppCompatPopupWindow;

/**
 * Author create by ok on 2019-06-18
 * Email : ok@163.com.
 */
public class AppPagePopupWindowControllerImpl extends AbsPageControllerImpl<AppCompatPopupWindow> implements PopupWindowPageController<AppCompatPopupWindow> {

	private float mWindowBackgroundAlpha = -1.F;

	public AppPagePopupWindowControllerImpl(@NonNull PageProvider pageProvider) {
		super(pageProvider);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppCompatPopupWindow popupWindow = this.getPageOwner();
		popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
	}

	@Override
	protected void onPreViewCreated(@Nullable Bundle savedInstanceState) throws Exception {
		super.onPreViewCreated(savedInstanceState);
		final UILayoutController layoutController = this.getPreLayoutController();
		if (layoutController != null) {
			layoutController
					.setToolbarLayoutStableMode(true)
					.getToolbarController()
					.getToolbarMethod()
					.setPopEnabled(true);
		}
	}

	@NonNull
	@Override
	public final AppCompatPopupWindow getPageOwner() {
		return (AppCompatPopupWindow) this.getPageProvider();
	}

	/**
	 * Returns the Lifecycle of the provider.
	 *
	 * @return The lifecycle of the provider.
	 */
	@NonNull
	@Override
	public Lifecycle getLifecycle() {
		return this.getPageOwner().getLifecycle();
	}

	/**
	 * Returns owned {@link ViewModelStore}
	 *
	 * @return a {@code ViewModelStore}
	 */
	@NonNull
	@Override
	public ViewModelStore getViewModelStore() {
		return this.getPageOwner().getViewModelStore();
	}

	@Override
	public void onPopClick(@NonNull View view) {
		this.getPageOwner().dismiss();
	}

	@Override
	protected void onStart() {
		super.onStart();
		View contentView = null;
		try {
			contentView = this.getLayoutController().requireLayoutAt(UILayoutController.LayoutType.Content.key).getContentView();
		} catch (Exception e) {
			contentView = this.getPageView();
		} finally {
			if (contentView != null) {
				final AppCompatPopupWindow popupWindow = this.getPageOwner();
				final ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
				if (layoutParams == null) {
					popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
					popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
				} else {
					popupWindow.setWidth(layoutParams.width);
					popupWindow.setHeight(layoutParams.height);
				}
			}
			if (this.mWindowBackgroundAlpha >= 0.F) {
				this.requestWindowBackgroundAlpha(this.mWindowBackgroundAlpha);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.requireFragmentActivity().getLifecycle()
				.addObserver(new HostLifecycleEventObserver());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.requestWindowBackgroundAlpha(1.F);
	}

	@Override
	public PopupWindowPageController<AppCompatPopupWindow> setWindowBackgroundAlpha(@FloatRange(from = 0.F, to = 1.F) float alpha) {
		this.mWindowBackgroundAlpha = alpha;
		return this;
	}

	private void requestWindowBackgroundAlpha(@FloatRange(from = 0.F, to = 1.F) float alpha) {
		final Window window = this.requireFragmentActivity().getWindow();
		if (window == null) {
			return;
		}
		WindowManager.LayoutParams layoutParams = window.getAttributes();
		layoutParams.alpha = alpha;
		window.setAttributes(layoutParams);
		this.getPageOwner().update();
	}

	final class HostLifecycleEventObserver implements LifecycleEventObserver {

		@Override
		public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
			if (Lifecycle.Event.ON_DESTROY == event) {
				AppPagePopupWindowControllerImpl.this.getPageOwner().dismiss();
			}
		}
	}
}
