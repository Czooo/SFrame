package androidx.sframe.ui.abs;

import android.view.KeyEvent;

import java.util.concurrent.CopyOnWriteArrayList;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

/**
 * @Author create by Zoran on 2019-09-28
 * @Email : 171905184@qq.com
 * @Description :
 */
public abstract class OnKeyDownCallback {

	private final CopyOnWriteArrayList<OnKeyDownDispatcher.Cancellable> mCancellables =
			new CopyOnWriteArrayList<>();
	private boolean enabled = false;

	@MainThread
	public final void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public final boolean isEnabled() {
		return enabled;
	}

	@MainThread
	public final void remove() {
		for (OnKeyDownDispatcher.Cancellable cancellable : this.mCancellables) {
			cancellable.cancel();
		}
	}

	@MainThread
	public abstract boolean onKeyDown(int keyCode, KeyEvent event);

	void addCancellable(@NonNull OnKeyDownDispatcher.Cancellable cancellable) {
		this.mCancellables.add(cancellable);
	}

	void removeCancellable(@NonNull OnKeyDownDispatcher.Cancellable cancellable) {
		this.mCancellables.remove(cancellable);
	}
}
