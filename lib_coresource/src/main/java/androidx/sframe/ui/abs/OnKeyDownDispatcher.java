package androidx.sframe.ui.abs;

import android.annotation.SuppressLint;
import android.view.KeyEvent;

import java.util.ArrayDeque;
import java.util.Iterator;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

/**
 * @Author create by Zoran on 2019-09-28
 * @Email : 171905184@qq.com
 * @Description :
 */
public class OnKeyDownDispatcher {

	public interface Cancellable {
		void cancel();
	}

	private final ArrayDeque<OnKeyDownCallback> mOnKeyDownCallbacks = new ArrayDeque<>();
	private final OnKeyDownCallback mOnKeyDownCallback;

	public OnKeyDownDispatcher() {
		this(null);
	}

	public OnKeyDownDispatcher(@Nullable OnKeyDownCallback callback) {
		this.mOnKeyDownCallback = callback;
	}

	@MainThread
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		final Iterator<OnKeyDownCallback> iterator = this.mOnKeyDownCallbacks.descendingIterator();
		while (iterator.hasNext()) {
			final OnKeyDownCallback callback = iterator.next();
			if (callback.isEnabled()) {
				if (callback.onKeyDown(keyCode, event)) {
					return true;
				}
			}
		}
		if (this.mOnKeyDownCallback != null) {
			return this.mOnKeyDownCallback.onKeyDown(keyCode, event);
		}
		return false;
	}

	@MainThread
	@NonNull
	private Cancellable addCancellableCallback(@NonNull OnKeyDownCallback onKeyDownCallback) {
		this.mOnKeyDownCallbacks.add(onKeyDownCallback);
		final OnKeyDownCancellable cancellable = new OnKeyDownCancellable(onKeyDownCallback);
		onKeyDownCallback.addCancellable(cancellable);
		return cancellable;
	}

	@MainThread
	public void addCallback(@NonNull OnKeyDownCallback onKeyDownCallback) {
		this.addCancellableCallback(onKeyDownCallback);
	}

	@SuppressLint("LambdaLast")
	@MainThread
	public void addCallback(@NonNull LifecycleOwner owner, @NonNull OnKeyDownCallback onKeyDownCallback) {
		final Lifecycle lifecycle = owner.getLifecycle();
		if (lifecycle.getCurrentState() == Lifecycle.State.DESTROYED) {
			return;
		}
		onKeyDownCallback.addCancellable(
				new LifecycleOnKeyDownCancellable(lifecycle, onKeyDownCallback));
	}

	public class OnKeyDownCancellable implements Cancellable {

		private final OnKeyDownCallback mOnKeyDownCallback;

		OnKeyDownCancellable(@NonNull OnKeyDownCallback callback) {
			this.mOnKeyDownCallback = callback;
		}

		@Override
		public void cancel() {
			OnKeyDownDispatcher.this.mOnKeyDownCallbacks.remove(this.mOnKeyDownCallback);
			this.mOnKeyDownCallback.removeCancellable(this);
		}
	}

	private class LifecycleOnKeyDownCancellable implements LifecycleEventObserver, Cancellable {

		private final Lifecycle mLifecycle;
		private final OnKeyDownCallback mOnKeyDownCallback;
		@Nullable
		private Cancellable mCurrentCancellable;

		LifecycleOnKeyDownCancellable(@NonNull Lifecycle lifecycle, @NonNull OnKeyDownCallback callback) {
			this.mLifecycle = lifecycle;
			this.mOnKeyDownCallback = callback;
			lifecycle.addObserver(this);
		}

		@Override
		public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
			if (event == Lifecycle.Event.ON_START) {
				this.mCurrentCancellable = addCancellableCallback(this.mOnKeyDownCallback);
			} else if (event == Lifecycle.Event.ON_STOP) {
				if (this.mCurrentCancellable != null) {
					this.mCurrentCancellable.cancel();
				}
			} else if (event == Lifecycle.Event.ON_DESTROY) {
				this.cancel();
			}
		}

		@Override
		public void cancel() {
			this.mLifecycle.removeObserver(this);
			this.mOnKeyDownCallback.removeCancellable(this);
			if (this.mCurrentCancellable != null) {
				this.mCurrentCancellable.cancel();
				this.mCurrentCancellable = null;
			}
		}
	}
}
