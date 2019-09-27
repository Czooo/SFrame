package androidx.sframe.ui.controller.impl;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.sframe.R;
import androidx.sframe.ui.controller.DialogFragmentPageController;
import androidx.sframe.ui.controller.UILayoutController;

/**
 * Author create by ok on 2019-06-18
 * Email : ok@163.com.
 */
public class AppPageDialogFragmentControllerImpl extends AppPageFragmentControllerImpl<AppCompatDialogFragment> implements DialogFragmentPageController<AppCompatDialogFragment> {

	private int mLayoutWidth = 0;
	private int mLayoutHeight = 0;
	private int mLayoutGravity = Gravity.CENTER;
	private int mLayoutAnimStyle = R.style.DialogAnimation;
	private float mLayoutBackgroundAlpha = 0.22f;
	private OnDismissListener mOnDismissListener;

	public AppPageDialogFragmentControllerImpl(@NonNull PageProvider pageProvider) {
		super(pageProvider);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getPageOwner().setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme_AppCompat);
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
	public final AppCompatDialogFragment getPageOwner() {
		return (AppCompatDialogFragment) this.getPageProvider();
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
		} catch (IllegalStateException e) {
			contentView = this.getPageView();
		} finally {
			final AppCompatDialogFragment pageOwner = this.getPageOwner();
			final Window window = pageOwner.requireDialog().getWindow();
			if (window != null) {
				if (contentView != null) {
					final ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
					if (layoutParams == null) {
						window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					} else {
						window.setLayout(layoutParams.width, layoutParams.height);
					}
				}
				window.setWindowAnimations(this.mLayoutAnimStyle);
				window.setDimAmount(this.mLayoutBackgroundAlpha);
				window.setGravity(this.mLayoutGravity);

				final WindowManager.LayoutParams attributes = window.getAttributes();
				if (this.mLayoutWidth != 0) {
					window.setLayout(this.mLayoutWidth, attributes.height);
				}
				if (this.mLayoutHeight != 0) {
					window.setLayout(attributes.width, this.mLayoutHeight);
				}
				if (ViewGroup.LayoutParams.MATCH_PARENT == attributes.height) {
					final UILayoutController layoutController = this.getPreLayoutController();
					if (layoutController != null) {
						layoutController
								.getToolbarController()
								.getToolbarMethod()
								.setStateBarEnabled(true);
					}
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (this.mOnDismissListener != null) {
			this.mOnDismissListener.onDismiss();
		}
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
	public final DialogFragmentPageController<AppCompatDialogFragment> setWindowBackgroundAlpha(float alpha) {
		this.mLayoutBackgroundAlpha = alpha;
		return this;
	}

	@Override
	public final DialogFragmentPageController<AppCompatDialogFragment> setOnDismissListener(@NonNull OnDismissListener listener) {
		this.mOnDismissListener = listener;
		return this;
	}

	@Override
	public final DialogFragmentPageController<AppCompatDialogFragment> show() {
		this.getPageOwner().show(AppPageControllerHelper.requireChildFragmentManager(AppPageControllerHelper.getHostPageController(this)), this.getClass().getName());
		return this;
	}

	@Override
	public final DialogFragmentPageController<AppCompatDialogFragment> show(@NonNull View anchor) {
		this.mLayoutWidth = ViewGroup.LayoutParams.MATCH_PARENT;
		this.mLayoutHeight = this.calculateAnchorViewHeight(anchor);
		this.setGravity(Gravity.BOTTOM);
		return this.show();
	}

	@Override
	public final DialogFragmentPageController<AppCompatDialogFragment> showNow() {
		this.getPageOwner().showNow(AppPageControllerHelper.requireChildFragmentManager(AppPageControllerHelper.getHostPageController(this)), this.getClass().getName());
		return this;
	}

	@Override
	public final DialogFragmentPageController<AppCompatDialogFragment> showNow(@NonNull View anchor) {
		this.mLayoutWidth = ViewGroup.LayoutParams.MATCH_PARENT;
		this.mLayoutHeight = this.calculateAnchorViewHeight(anchor);
		this.setGravity(Gravity.BOTTOM);
		return this.showNow();
	}

	private int calculateAnchorViewHeight(@NonNull View anchor) {
		final int[] outLocation = new int[2];
		anchor.getLocationOnScreen(outLocation);
		final int y = outLocation[1] + anchor.getHeight();
		final DisplayMetrics mDisplayMetrics = anchor.getResources().getDisplayMetrics();
		final int screenHeight = mDisplayMetrics.heightPixels;
		return screenHeight - y;
	}
}
