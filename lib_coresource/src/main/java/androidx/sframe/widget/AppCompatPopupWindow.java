package androidx.sframe.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;

import androidx.annotation.AnimRes;
import androidx.annotation.CallSuper;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.sframe.utils.Logger;

/**
 * @Author create by Zoran on 2019-09-24
 * @Email : 171905184@qq.com
 * @Description :
 */
public class AppCompatPopupWindow extends PopupWindow implements LifecycleOwner, ViewModelStoreOwner {

	private final LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
	private final ViewModelStore mViewModelStore = new ViewModelStore();

	private Bundle mArguments;
	private Bundle mSavedInstanceState;
	private Animation mLayoutEnterAnimation;
	private Animation mLayoutExitAnimation;
	private OnDismissListener mOnDismissListener;

	private final Rect mLayoutBackgroundMargin = new Rect();
	private float mLayoutBackgroundAlpha = -1.F;
	private int mLayoutEnterAnimationResource;
	private int mLayoutExitAnimationResource;
	private boolean mIsViewCreated;

	public AppCompatPopupWindow(@NonNull Context context) {
		this(context, null);
	}

	public AppCompatPopupWindow(@NonNull Context context, @Nullable Bundle savedInstanceState) {
		super(context);
		try {
			this.mSavedInstanceState = savedInstanceState;
			final LayoutInflater inflater = LayoutInflater.from(context);
			final View contentView = this.onCreateView(inflater, null, savedInstanceState);
			contentView.setClickable(true);
			contentView.setFocusable(true);
			contentView.setFocusableInTouchMode(true);
			this.setContentView(contentView);

			final ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
			if (layoutParams == null) {
				this.setWidth(-2);
				this.setHeight(-2);
			} else {
				this.setWidth(layoutParams.width);
				this.setHeight(layoutParams.height);
			}
			this.performOnCreate(savedInstanceState);
		} catch (Exception e) {
			Logger.e(e);
		}
	}

	@CallSuper
	@Override
	public void setContentView(@NonNull View contentView) {
		super.setContentView(contentView);
		this.mIsViewCreated = true;
	}

	@CallSuper
	@Override
	public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
		if (!this.isShowing()) {
			this.performOnStart();
			super.showAsDropDown(anchor, xoff, yoff, gravity);
			this.performOnResume();
		}
	}

	@CallSuper
	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		if (!this.isShowing()) {
			this.performOnStart();
			super.showAtLocation(parent, gravity, x, y);
			this.performOnResume();
		}
	}

	@CallSuper
	@Override
	public void dismiss() {
		if (this.isShowing()) {
			if (this.mLayoutExitAnimation != null
					&& this.mLayoutExitAnimation.hasStarted()
					&& !this.mLayoutExitAnimation.hasEnded()) {
				return;
			}
			this.performOnPause();
			if (this.mLayoutExitAnimation == null
					&& this.mLayoutExitAnimationResource > 0) {
				this.mLayoutExitAnimation = AnimationUtils.loadAnimation(this.getContentView().getContext(), this.mLayoutExitAnimationResource);
			}
			// exit animation
			if (this.mLayoutExitAnimation != null) {
				this.setAnimationStyle(0);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					this.setExitTransition(null);
				}
				this.mLayoutExitAnimation.setAnimationListener(new OnExitAnimationListener());
				this.mLayoutExitAnimation.setFillEnabled(true);
				this.mLayoutExitAnimation.setFillAfter(true);
				this.getContentView().startAnimation(this.mLayoutExitAnimation);
				this.update();
				return;
			}
			this.performOnStop();
			super.dismiss();
		}
	}

	@CallSuper
	@Override
	public void setOnDismissListener(@Nullable OnDismissListener listener) {
		this.mOnDismissListener = listener;
	}

	@NonNull
	@Override
	public final Lifecycle getLifecycle() {
		return this.mLifecycleRegistry;
	}

	@NonNull
	@Override
	public final ViewModelStore getViewModelStore() {
		return this.mViewModelStore;
	}

	@CallSuper
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		final ColorDrawable backgroundDrawable = new ColorDrawable(Color.BLACK);
		backgroundDrawable.setAlpha((int) (0.F * 255));
		this.setFocusable(true);
		this.setTouchable(true);
		this.setOutsideTouchable(true);
		this.setBackgroundDrawable(backgroundDrawable);
		this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		final OnTempDismissListener mOnTempDismissListener = new OnTempDismissListener();
		super.setOnDismissListener(mOnTempDismissListener);
	}

	@NonNull
	protected View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return null;
	}

	@CallSuper
	protected void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

	}

	@CallSuper
	public void onSaveInstanceState(@NonNull Bundle outState) {

	}

	@CallSuper
	protected void onStart() {
		if (this.mIsViewCreated) {
			this.onViewCreated(this.getContentView(), this.mSavedInstanceState);
			if (this.mLayoutBackgroundAlpha >= 0.F) {
				Drawable backgroundDrawable = this.getBackground();
				if (backgroundDrawable == null) {
					backgroundDrawable = new ColorDrawable(Color.BLACK);
				}
				backgroundDrawable.setAlpha((int) (this.mLayoutBackgroundAlpha * 255));
				this.setBackgroundDrawable(backgroundDrawable);
			}
			if (this.mLayoutEnterAnimation == null
					&& this.mLayoutEnterAnimationResource > 0) {
				this.mLayoutEnterAnimation = AnimationUtils.loadAnimation(this.getContentView().getContext(), this.mLayoutEnterAnimationResource);
			}
			// enter animation
			if (this.mLayoutEnterAnimation != null) {
				// no finish animation
				if (this.mLayoutEnterAnimation.hasStarted()
						&& !this.mLayoutEnterAnimation.hasEnded()) {
					return;
				}
				this.setAnimationStyle(0);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					this.setEnterTransition(null);
				}
				this.mLayoutEnterAnimation.setFillEnabled(true);
				this.mLayoutEnterAnimation.setFillAfter(true);
				this.getContentView().startAnimation(this.mLayoutEnterAnimation);
			}
		}
	}

	@CallSuper
	protected void onResume() {
		final Drawable backgroundDrawable = this.getBackground();
		if (backgroundDrawable != null) {
			final View contentView = this.getContentView();
			final View backgroundView = (View) contentView.getParent();
			backgroundView.setOnClickListener(new OnBackgroundViewClickListener());
			backgroundView.setPadding(this.mLayoutBackgroundMargin.left, this.mLayoutBackgroundMargin.top, this.mLayoutBackgroundMargin.right, this.mLayoutBackgroundMargin.bottom);
		}
		this.update();
	}

	@CallSuper
	protected void onPause() {

	}

	@CallSuper
	protected void onStop() {

	}

	@CallSuper
	protected void onDestroy() {
		this.mViewModelStore.clear();
		if (this.mOnDismissListener != null) {
			this.mOnDismissListener.onDismiss();
			this.mOnDismissListener = null;
		}
		this.mLayoutEnterAnimation = null;
		this.mLayoutExitAnimation = null;
		this.mLayoutEnterAnimationResource = -1;
		this.mLayoutExitAnimationResource = -1;
		this.mLayoutBackgroundAlpha = -1.F;
		this.mArguments = null;
		this.mSavedInstanceState = null;
		this.mIsViewCreated = false;
	}

	// 非全屏透明度(结合：Margin)
	public void setBackgroundAlpha(@FloatRange(from = 0, to = 1.f) float alpha) {
		this.mLayoutBackgroundAlpha = alpha;
	}

	public void setBackgroundMarginLeft(int marginLeft) {
		this.mLayoutBackgroundMargin.left = marginLeft;
	}

	public void setBackgroundMarginTop(int marginTop) {
		this.mLayoutBackgroundMargin.top = marginTop;
	}

	public void setBackgroundMarginRight(int marginRight) {
		this.mLayoutBackgroundMargin.right = marginRight;
	}

	public void setBackgroundMarginBottom(int marginBottom) {
		this.mLayoutBackgroundMargin.bottom = marginBottom;
	}

	public final void setLayoutEnterAnimation(@AnimRes int resId) {
		this.mLayoutEnterAnimationResource = resId;
	}

	public final void setLayoutEnterAnimation(@Nullable Animation animation) {
		this.mLayoutEnterAnimation = animation;
	}

	public final void setLayoutExitAnimation(@AnimRes int resId) {
		this.mLayoutExitAnimationResource = resId;
	}

	public final void setLayoutExitAnimation(@Nullable Animation animation) {
		this.mLayoutExitAnimation = animation;
	}

	public final void setArguments(@Nullable Bundle arguments) {
		this.mArguments = arguments;
	}

	@Nullable
	public final Bundle getArguments() {
		return this.mArguments;
	}

	@NonNull
	public final Bundle requireArguments() {
		Bundle arguments = this.getArguments();
		if (arguments == null) {
			throw new IllegalStateException("PopupWindow " + this + " does not have any arguments.");
		}
		return arguments;
	}

	public final void performOnCreate(@Nullable Bundle savedInstanceState) {
		this.onCreate(savedInstanceState);
		this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
	}

	public final void performOnStart() {
		this.onStart();
		this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
	}

	public final void performOnResume() {
		this.onResume();
		this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
	}

	public final void performOnPause() {
		this.onPause();
		this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
	}

	public final void performOnStop() {
		this.onStop();
		this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
	}

	public final void performOnDestroy() {
		this.onDestroy();
		this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
	}

	private final class OnBackgroundViewClickListener implements View.OnClickListener {

		/**
		 * Called when a view has been clicked.
		 *
		 * @param view The view that was clicked.
		 */
		@Override
		public void onClick(View view) {
			AppCompatPopupWindow.this.dismiss();
		}
	}

	private final class OnTempDismissListener implements PopupWindow.OnDismissListener {

		/**
		 * Called when this popup window is dismissed.
		 */
		@Override
		public void onDismiss() {
			AppCompatPopupWindow.this.performOnDestroy();
		}
	}

	private final class OnExitAnimationListener implements Animation.AnimationListener {

		/**
		 * <p>Notifies the start of the animation.</p>
		 *
		 * @param animation The started animation.
		 */
		@Override
		public void onAnimationStart(Animation animation) {
			AppCompatPopupWindow.this.performOnStop();
		}

		/**
		 * <p>Notifies the end of the animation. This callback is not invoked
		 * for animations with repeat count set to INFINITE.</p>
		 *
		 * @param animation The animation which reached its end.
		 */
		@Override
		public void onAnimationEnd(Animation animation) {
			AppCompatPopupWindow.super.dismiss();
		}

		/**
		 * <p>Notifies the repetition of the animation.</p>
		 *
		 * @param animation The animation which was repeated.
		 */
		@Override
		public void onAnimationRepeat(Animation animation) {

		}
	}
}
