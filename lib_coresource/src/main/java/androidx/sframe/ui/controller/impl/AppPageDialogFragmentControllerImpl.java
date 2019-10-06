package androidx.sframe.ui.controller.impl;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
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
public class AppPageDialogFragmentControllerImpl extends AppPageFragmentControllerImpl<AppCompatDialogFragment> implements DialogFragmentPageController<AppCompatDialogFragment>, Dialog.OnKeyListener, KeyEvent.Callback {

	private int mLayoutWidth = 0;
	private int mLayoutHeight = 0;
	private int mLayoutGravity = Gravity.CENTER;
	private int mLayoutAnimStyle = R.style.DialogAnimation;
	private float mLayoutBackgroundAlpha = 0.22f;
	private OnKeyDownListener mOnKeyDownListener;
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
		this.getPageOwner().requireDialog()
				.setOnKeyListener(this);
	}

	@NonNull
	@Override
	public final AppCompatDialogFragment getPageOwner() {
		return (AppCompatDialogFragment) this.getPageProvider();
	}

	@Override
	public void onPopClick(@NonNull View view) {
		if (!this.getNavController().popBackStack()) {
			this.getPageOwner().dismiss();
		}
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
	public final DialogFragmentPageController<AppCompatDialogFragment> setOnKeyDownListener(@NonNull OnKeyDownListener listener) {
		this.mOnKeyDownListener = listener;
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

	@Override
	public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent event) {
		final Dialog dialog = this.getPageOwner().requireDialog();
		final Window window = dialog.getWindow();
		if (window != null) {
			if (window.superDispatchKeyEvent(event)) {
				return true;
			}
			return event.dispatch(this, window.getDecorView()
					.getKeyDispatcherState(), this);
		}
		return event.dispatch(this, null, this);
	}

	/**
	 * Called when a key down event has occurred.  If you return true,
	 * you can first call {@link KeyEvent#startTracking()
	 * KeyEvent.startTracking()} to have the framework track the event
	 * through its {@link #onKeyUp(int, KeyEvent)} and also call your
	 * {@link #onKeyLongPress(int, KeyEvent)} if it occurs.
	 *
	 * @param keyCode The value in event.getKeyCode().
	 * @param event   Description of the key event.
	 * @return If you handled the event, return true.  If you want to allow
	 * the event to be handled by the next receiver, return false.
	 */
	@Override
	public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
		if (this.mOnKeyDownListener != null
				&& this.mOnKeyDownListener.onKeyDown(this.getPageOwner(), keyCode, event)) {
			return true;
		}
		if (KeyEvent.KEYCODE_BACK == keyCode
				|| KeyEvent.KEYCODE_ESCAPE == keyCode) {
			if (!this.getNavController().popBackStack()) {
				event.startTracking();
			}
			return true;
		}
		return false;
	}

	/**
	 * Called when a long press has occurred.  If you return true,
	 * the final key up will have {@link KeyEvent#FLAG_CANCELED} and
	 * {@link KeyEvent#FLAG_CANCELED_LONG_PRESS} set.  Note that in
	 * order to receive this callback, someone in the event change
	 * <em>must</em> return true from {@link #onKeyDown} <em>and</em>
	 * call {@link KeyEvent#startTracking()} on the event.
	 *
	 * @param keyCode The value in event.getKeyCode().
	 * @param event   Description of the key event.
	 * @return If you handled the event, return true.  If you want to allow
	 * the event to be handled by the next receiver, return false.
	 */
	@Override
	public boolean onKeyLongPress(int keyCode, @NonNull KeyEvent event) {
		return false;
	}

	/**
	 * Called when a key up event has occurred.
	 *
	 * @param keyCode The value in event.getKeyCode().
	 * @param event   Description of the key event.
	 * @return If you handled the event, return true.  If you want to allow
	 * the event to be handled by the next receiver, return false.
	 */
	@Override
	public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_ESCAPE)
				&& event.isTracking()
				&& !event.isCanceled()) {
			final AppCompatDialogFragment pageOwner = this.getPageOwner();
			if (pageOwner.isCancelable() && pageOwner.getDialog() != null) {
				pageOwner.getDialog().cancel();
			}
			return true;
		}
		return false;
	}

	/**
	 * Called when a user's interaction with an analog control, such as
	 * flinging a trackball, generates simulated down/up events for the same
	 * key multiple times in quick succession.
	 *
	 * @param keyCode The value in event.getKeyCode().
	 * @param count   Number of pairs as returned by event.getRepeatCount().
	 * @param event   Description of the key event.
	 * @return If you handled the event, return true.  If you want to allow
	 * the event to be handled by the next receiver, return false.
	 */
	@Override
	public boolean onKeyMultiple(int keyCode, int count, @NonNull KeyEvent event) {
		return false;
	}
}
