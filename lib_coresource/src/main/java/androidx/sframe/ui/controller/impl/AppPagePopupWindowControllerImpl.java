package androidx.sframe.ui.controller.impl;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;

import androidx.annotation.AnimRes;
import androidx.annotation.CallSuper;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStore;
import androidx.sframe.R;
import androidx.sframe.listener.OnAnimationListener;
import androidx.sframe.ui.controller.PopupWindowPageController;
import androidx.sframe.ui.controller.UILayoutController;
import androidx.sframe.widget.AppCompatPopupWindow;

/**
 * Author create by ok on 2019-06-18
 * Email : ok@163.com.
 */
public class AppPagePopupWindowControllerImpl extends AbsPageControllerImpl<AppCompatPopupWindow> implements PopupWindowPageController<AppCompatPopupWindow> {

	private final Rect mRect = new Rect();
	private float mLayoutBackgroundAlpha = -1.F;
	private float mLayoutBackgroundViewAlpha = 0.F;
	private float mOldLayoutBackgroundAlpha = -1.F;
	private int mLayoutEnterAnimationResId;
	private int mLayoutExitAnimationResId;

	public AppPagePopupWindowControllerImpl(@NonNull PageProvider pageProvider) {
		super(pageProvider);
	}

	@CallSuper
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final AppCompatPopupWindow prePopupWindow = this.getPageOwner();

		int preWidth = -2;
		int preHeight = -2;
		View preContentView = null;

		try {
			if (savedInstanceState == null) {
				final View contentView = this.onCreateView(LayoutInflater.from(AppPageControllerHelper.requireContext(this)), null, savedInstanceState);
				if (contentView != null) {
					prePopupWindow.setContentView(contentView);
				}
			}
			preContentView = this.getLayoutController().getLayoutAt(UILayoutController.LayoutType.Content.key).getContentView();
		} catch (IllegalStateException e) {
			preContentView = this.getPageView();
		} finally {
			prePopupWindow.setFocusable(true);
			prePopupWindow.setTouchable(true);
			prePopupWindow.setOutsideTouchable(true);
			prePopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			prePopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
			prePopupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
			this.onViewCreated(savedInstanceState);
			// 生成BackgroundView
			final ColorDrawable preBackgroundDrawable = new ColorDrawable(Color.BLACK);
			preBackgroundDrawable.setAlpha((int) (this.mLayoutBackgroundViewAlpha * 255));
			prePopupWindow.setBackgroundDrawable(preBackgroundDrawable);
			// 设置Width／Height
			if (preContentView != null) {
				final ViewGroup.LayoutParams preLayoutParams = preContentView.getLayoutParams();
				preWidth = preLayoutParams.width;
				preHeight = preLayoutParams.height;
			}
			prePopupWindow.setWidth(preWidth);
			prePopupWindow.setHeight(preHeight);

			if (this.mLayoutBackgroundAlpha > 0) {
				final FragmentActivity preFragmentActivity = AppPageControllerHelper.requireActivity(this);
				final Window preWindow = preFragmentActivity.getWindow();
				if (preWindow != null) {
					WindowManager.LayoutParams preLayoutParams = preWindow.getAttributes();
					this.mOldLayoutBackgroundAlpha = preLayoutParams.alpha;
					preLayoutParams.alpha = this.mLayoutBackgroundAlpha;
					preWindow.setAttributes(preLayoutParams);
					preWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
				}
			}

			if (this.mLayoutEnterAnimationResId != 0) {
				final View mContentView = prePopupWindow.getContentView();
				prePopupWindow.setAnimationStyle(0);
				final Animation preAnimation = AnimationUtils.loadAnimation(mContentView.getContext(), this.mLayoutEnterAnimationResId);
				preAnimation.setFillEnabled(true);
				preAnimation.setFillAfter(true);
				mContentView.clearAnimation();
				mContentView.startAnimation(preAnimation);
			}
			prePopupWindow.update();
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
	public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
		super.onStateChanged(source, event);
		if (Lifecycle.Event.ON_RESUME == event) {
			this.performOnResume();
		} else if (Lifecycle.Event.ON_DESTROY == event) {
			this.performOnDestroy();
		}
	}

	@Override
	public PopupWindowPageController<AppCompatPopupWindow> setAnimationStyle(int animationStyle) {
		this.getPageOwner().setAnimationStyle(animationStyle);
		return this;
	}

	@Override
	public PopupWindowPageController<AppCompatPopupWindow> setCustomAnimation(@AnimRes int enterAnimResId, @AnimRes int exitAnimResId) {
		this.mLayoutEnterAnimationResId = enterAnimResId;
		this.mLayoutExitAnimationResId = exitAnimResId;
		return this;
	}

	@Override
	public PopupWindowPageController<AppCompatPopupWindow> setBackgroundViewAlpha(@FloatRange(from = 0, to = 1.f) float alpha) {
		this.mLayoutBackgroundViewAlpha = alpha;
		return this;
	}

	@Override
	public final PopupWindowPageController<AppCompatPopupWindow> setBackgroundAlpha(@FloatRange(from = 0, to = 1.f) float alpha) {
		this.mLayoutBackgroundAlpha = alpha;
		return this;
	}

	@Override
	public PopupWindowPageController<AppCompatPopupWindow> setMarginLeft(int marginLeft) {
		this.mRect.left = marginLeft;
		return this;
	}

	@Override
	public PopupWindowPageController<AppCompatPopupWindow> setMarginTop(int marginTop) {
		this.mRect.top = marginTop;
		return this;
	}

	@Override
	public PopupWindowPageController<AppCompatPopupWindow> setMarginRight(int marginRight) {
		this.mRect.right = marginRight;
		return this;
	}

	@Override
	public PopupWindowPageController<AppCompatPopupWindow> setMarginBottom(int marginBottom) {
		this.mRect.bottom = marginBottom;
		return this;
	}

	private Animation mExitAnimation;

	public boolean performDismiss() {
		final AppCompatPopupWindow prePopupWindow = this.getPageOwner();
		if (this.mLayoutExitAnimationResId == 0) {
			return false;
		}
		if (this.mExitAnimation != null && this.mExitAnimation.hasStarted() && !this.mExitAnimation.hasEnded()) {
			return true;
		}
		final View preContentView = prePopupWindow.getContentView();
		this.mExitAnimation = AnimationUtils.loadAnimation(preContentView.getContext(), this.mLayoutExitAnimationResId);
		this.mExitAnimation.setFillEnabled(true);
		this.mExitAnimation.setFillAfter(true);
		this.mExitAnimation.setAnimationListener(new OnAnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				prePopupWindow.performDismiss();
			}
		});
		preContentView.clearAnimation();
		preContentView.startAnimation(this.mExitAnimation);
		return true;
	}

	private void performOnResume() {
		final AppCompatPopupWindow prePopupWindow = this.getPageOwner();
		final View preContentView = prePopupWindow.getContentView();
		final View preBackgroundView = (View) preContentView.getParent();
		preBackgroundView.setPadding(this.mRect.left, this.mRect.top, this.mRect.right, this.mRect.bottom);
		preBackgroundView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				prePopupWindow.dismiss();
			}
		});
		prePopupWindow.update();
	}

	private void performOnDestroy() {
		if (this.mOldLayoutBackgroundAlpha != -1.F) {
			final FragmentActivity preFragmentActivity = AppPageControllerHelper.requireActivity(this);
			final Window preWindow = preFragmentActivity.getWindow();
			if (preWindow != null) {
				WindowManager.LayoutParams preLayoutParams = preWindow.getAttributes();
				preLayoutParams.alpha = this.mOldLayoutBackgroundAlpha;
				preWindow.setAttributes(preLayoutParams);
				preWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
			}
			this.mOldLayoutBackgroundAlpha = -1.F;
		}
	}
}
