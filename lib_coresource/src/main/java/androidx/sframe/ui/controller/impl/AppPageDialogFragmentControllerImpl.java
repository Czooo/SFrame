package androidx.sframe.ui.controller.impl;

import android.app.Dialog;
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
import androidx.fragment.app.DialogFragment;
import androidx.sframe.R;
import androidx.sframe.ui.controller.AppPageController;
import androidx.sframe.ui.controller.DialogFragmentPageController;
import androidx.sframe.ui.controller.UILayoutController;

/**
 * Author create by ok on 2019-06-18
 * Email : ok@163.com.
 */
public class AppPageDialogFragmentControllerImpl extends AppPageFragmentControllerImpl<AppCompatDialogFragment> implements DialogFragmentPageController<AppCompatDialogFragment> {

	private final AppPageController<?> mHostPageController;

	private int mLayoutWidth;
	private int mLayoutHeight;
	private int mLayoutGravity = Gravity.CENTER;
	private int mLayoutAnimStyle = R.style.DialogAnimation;
	private float mLayoutBackgroundAlpha = 0.22f;

	private OnDismissListener mOnDismissListener;

	public AppPageDialogFragmentControllerImpl(@NonNull PageProvider pageProvider, @NonNull AppPageController<?> hostPageController) {
		super(pageProvider);
		this.mHostPageController = hostPageController;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getPageOwner().setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme_AppCompat);
	}

	@Override
	protected void onPreViewCreated(@Nullable Bundle savedInstanceState) throws Exception {
		super.onPreViewCreated(savedInstanceState);
		this.getLayoutController()
				.setToolbarLayoutStableMode(true)
				.getToolbarController()
				.getToolbarMethod()
				.setPopEnabled(true);
	}

	@NonNull
	@Override
	public final AppCompatDialogFragment getPageOwner() {
		return (AppCompatDialogFragment) this.getPageProvider();
	}

	@Override
	public final AppPageController<?> getHostPageController() {
		if (this.mHostPageController == null) {
			throw new IllegalStateException("Your page " + this + " is not yet attached to the HostPage instance. ");
		}
		return this.mHostPageController;
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
			if (contentView == null) {
				return;
			}
			final ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
			int width = layoutParams.width;
			int height = layoutParams.height;

			if (this.mLayoutWidth != 0 && this.mLayoutHeight != 0) {
				width = this.mLayoutWidth;
				height = this.mLayoutHeight;
			}

			final AppCompatDialogFragment pageOwner = this.getPageOwner();
			final Dialog requireDialog = pageOwner.requireDialog();
			final Window window = requireDialog.getWindow();
			if (window == null) {
				return;
			}
			window.setWindowAnimations(this.mLayoutAnimStyle);
			window.setDimAmount(this.mLayoutBackgroundAlpha);
			window.setGravity(this.mLayoutGravity);
			window.setLayout(width, height);

			if (ViewGroup.LayoutParams.MATCH_PARENT == height) {
				try {
					this.getToolbarController()
							.getToolbarMethod()
							.setStateBarEnabled(true);
				} catch (IllegalStateException e) {
					e.printStackTrace();
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
		this.mOnDismissListener = listener;
		return this;
	}

	@Override
	public final DialogFragmentPageController<AppCompatDialogFragment> show() {
		this.getPageOwner().show(AppPageControllerHelper.requireSupportFragmentManager(this), this.getClass().getName());
		return this;
	}

	@Override
	public final DialogFragmentPageController<AppCompatDialogFragment> show(@NonNull View anchor) {
		this.setGravity(Gravity.BOTTOM);
		this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		this.setHeight(this.calculatAnchorViewHeight(anchor));
		return this.show();
	}

	@Override
	public final DialogFragmentPageController<AppCompatDialogFragment> showNow() {
		this.getPageOwner().showNow(AppPageControllerHelper.requireSupportFragmentManager(this), this.getClass().getName());
		return this;
	}

	@Override
	public final DialogFragmentPageController<AppCompatDialogFragment> showNow(@NonNull View anchor) {
		this.setGravity(Gravity.BOTTOM);
		this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
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
