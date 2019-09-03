package androidx.demon.ui.controller.impl;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.demon.R;
import androidx.demon.ui.controller.DialogFragmentPageController;
import androidx.demon.ui.controller.UILayoutController;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

/**
 * Author create by ok on 2019-06-18
 * Email : ok@163.com.
 */
public class AppPageDialogFragmentControllerImpl extends AppPageFragmentControllerImpl<AppCompatDialogFragment> implements DialogFragmentPageController<AppCompatDialogFragment> {

	private int mLayoutWidth;
	private int mLayoutHeight;
	private int mLayoutGravity = Gravity.CENTER;
	private int mLayoutAnimStyle = R.style.DialogAnimation;
	private float mLayoutBackgroundAlpha = 0.22f;

	private OnDismissListener mDismissListener;

	public AppPageDialogFragmentControllerImpl(@NonNull PageProvider pageProvider) {
		super(pageProvider);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getPageOwner().setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme_AppCompat);
	}

	@Override
	public void onPopClick(@NonNull View view) {
		this.getPageOwner().dismiss();
	}

	@Override
	public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
		super.onStateChanged(source, event);
		if (Lifecycle.Event.ON_START == event) {
			int preWidth = -2;
			int preHeight = -2;
			View preView = null;
			try {
				preView = this.getLayoutController().getLayoutAt(UILayoutController.LayoutType.Content.key).getContentView();
			} catch (IllegalStateException e) {
				preView = this.getPageView();
			} finally {
				if (preView != null) {
					final ViewGroup.LayoutParams preLayoutParams = preView.getLayoutParams();
					preWidth = preLayoutParams.width;
					preHeight = preLayoutParams.height;
				}

				if (this.mLayoutWidth != 0) {
					preWidth = this.mLayoutWidth;
				}

				if (this.mLayoutHeight != 0) {
					preHeight = this.mLayoutHeight;
				}

				final Window preWindow = this.getPageOwner().requireDialog().getWindow();
				if (preWindow != null) {
					preWindow.setWindowAnimations(this.mLayoutAnimStyle);
					preWindow.setDimAmount(this.mLayoutBackgroundAlpha);
					preWindow.setLayout(preWidth, preHeight);
					preWindow.setGravity(this.mLayoutGravity);
				}
				if (ViewGroup.LayoutParams.MATCH_PARENT == preHeight) {
					try {
						this.getToolbarController()
								.getToolbarMethod()
								.setStatusEnabled(true);
					} catch (IllegalStateException e) {
						e.printStackTrace();
					}
				}
			}
		} else if (Lifecycle.Event.ON_DESTROY == event) {
			if (this.mDismissListener != null) {
				this.mDismissListener.onDismiss();
			}
		}
	}

	@NonNull
	@Override
	public final AppCompatDialogFragment getPageOwner() {
		return (AppCompatDialogFragment) this.getPageProvider();
	}

	@Override
	public final DialogFragmentPageController<AppCompatDialogFragment> setWidth(int width) {
		this.mLayoutWidth = width;
		return this;
	}

	@Override
	public final DialogFragmentPageController<AppCompatDialogFragment> setHeight(int height) {
		this.mLayoutHeight = height;
		return this;
	}

	@Override
	public final DialogFragmentPageController<AppCompatDialogFragment> setGravity(int gravity) {
		this.mLayoutGravity = gravity;
		return this;
	}

	@Override
	public final DialogFragmentPageController<AppCompatDialogFragment> setAnimationStyle(@StyleRes int animationStyleResId) {
		this.mLayoutAnimStyle = animationStyleResId;
		return this;
	}

	@Override
	public final DialogFragmentPageController<AppCompatDialogFragment> setBackgroundAlpha(float alpha) {
		this.mLayoutBackgroundAlpha = alpha;
		return this;
	}

	@Override
	public final DialogFragmentPageController<AppCompatDialogFragment> setOnDismissListener(@NonNull OnDismissListener listener) {
		this.mDismissListener = listener;
		return this;
	}

	@Override
	public final DialogFragmentPageController<AppCompatDialogFragment> show() {
		this.getPageOwner().show(AppPageControllerHelper.requireFragmentManager(this), this.getClass().getName());
		return this;
	}

	@Override
	public final DialogFragmentPageController<AppCompatDialogFragment> show(@NonNull View anchor) {
		this.setGravity(Gravity.BOTTOM);
		this.setWidth(-1);
		this.setHeight(this.calculatAnchorViewHeight(anchor));
		return this.show();
	}

	@Override
	public final DialogFragmentPageController<AppCompatDialogFragment> showNow() {
		this.getPageOwner().showNow(AppPageControllerHelper.requireFragmentManager(this), this.getClass().getName());
		return this;
	}

	@Override
	public final DialogFragmentPageController<AppCompatDialogFragment> showNow(@NonNull View anchor) {
		this.setGravity(Gravity.BOTTOM);
		this.setWidth(-1);
		this.setHeight(this.calculatAnchorViewHeight(anchor));
		return this.showNow();
	}

	private int calculatAnchorViewHeight(@NonNull View anchor) {
		final int[] outLocation = new int[2];
		anchor.getLocationOnScreen(outLocation);
		final int y = outLocation[1] + anchor.getHeight();
		final DisplayMetrics mDisplayMetrics = anchor.getResources().getDisplayMetrics();
		final int screenHeight = mDisplayMetrics.heightPixels;
		return screenHeight - y;
	}
}
