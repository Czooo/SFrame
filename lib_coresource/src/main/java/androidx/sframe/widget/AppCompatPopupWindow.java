package androidx.sframe.widget;

import android.os.Bundle;
import android.view.View;
import android.widget.PopupWindow;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

/**
 * Author create by ok on 2019-06-03
 * Email : ok@163.com.
 */
public class AppCompatPopupWindow extends PopupWindow implements
		LifecycleOwner,
		ViewModelStoreOwner {

	private final LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
	private final ViewModelStore mViewModelStore = new ViewModelStore();
	private OnDismissListener mDismissListener;
	private Bundle mArguments;

	public AppCompatPopupWindow() {
		this(true);
	}

	protected AppCompatPopupWindow(boolean performCreate) {
		super.setOnDismissListener(new OnTempDismissListener());
		if (performCreate) {
			// performCreate init
			this.performOnCreate();
		}
	}

	@CallSuper
	public void onCreate(@Nullable Bundle savedInstanceState) {

	}

	@CallSuper
	public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {

	}

	@CallSuper
	public void onStart() {

	}

	@CallSuper
	public void onResume() {

	}

	@CallSuper
	public void onPause() {

	}

	@CallSuper
	public void onStop() {

	}

	@CallSuper
	public void onDestroy() {
		this.mViewModelStore.clear();
	}

	@Override
	public final void setOnDismissListener(OnDismissListener listener) {
		this.mDismissListener = listener;
	}

	@NonNull
	@Override
	public final ViewModelStore getViewModelStore() {
		return this.mViewModelStore;
	}

	@NonNull
	@Override
	public final Lifecycle getLifecycle() {
		return this.mLifecycleRegistry;
	}

	@CallSuper
	@Override
	public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
		this.performOnStart();
		super.showAsDropDown(anchor, xoff, yoff, gravity);
		this.performOnResume();
	}

	@CallSuper
	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		this.performOnStart();
		super.showAtLocation(parent, gravity, x, y);
		this.performOnResume();
	}

	@CallSuper
	@Override
	public void dismiss() {
		this.performOnPause();
		super.dismiss();
		this.performOnStop();
	}

	public final void setArguments(@Nullable Bundle args) {
		this.mArguments = args;
	}

	@Nullable
	public final Bundle getArguments() {
		return this.mArguments;
	}

	public final void performDismiss() {
		super.dismiss();
	}

	protected final void performOnCreate() {
		this.performOnCreate(null);
	}

	private void performOnCreate(@Nullable Bundle savedInstanceState) {
		this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
		this.onCreate(savedInstanceState);
	}

	private void performOnStart() {
		this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
		this.onStart();
	}

	private void performOnResume() {
		this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
		this.onResume();
	}

	private void performOnPause() {
		this.onPause();
		this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
	}

	private void performOnStop() {
		this.onStop();
		this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
	}

	private void performOnDestroy() {
		if (this.mDismissListener != null) {
			this.mDismissListener.onDismiss();
		}
		this.onDestroy();
		this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
	}

	final class OnTempDismissListener implements PopupWindow.OnDismissListener  {

		/**
		 * Called when this popup window is dismissed.
		 */
		@Override
		public void onDismiss() {
			AppCompatPopupWindow.this.performOnDestroy();
		}
	}
}
