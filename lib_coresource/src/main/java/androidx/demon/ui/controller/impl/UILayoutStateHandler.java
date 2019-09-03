package androidx.demon.ui.controller.impl;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.demon.ui.controller.UILayoutController;

/**
 * Author create by ok on 2019-06-10
 * Email : ok@163.com.
 */
class UILayoutStateHandler implements Handler.Callback {

	private final int MESSAGE_WHAT_FLAG = 1;
	private final Callback mCallback;

	private int mOldLayoutState = UILayoutController.LayoutType.None.key;
	private int mNowLayoutState = UILayoutController.LayoutType.None.key;
	private int mTempLayoutState = UILayoutController.LayoutType.None.key;
	private Handler mLayoutStateHandler;

	UILayoutStateHandler(@NonNull Callback callback) {
		this.mCallback = callback;
	}

	@Override
	public boolean handleMessage(Message msg) {
		final int oldLayoutState = msg.arg1;
		final int nowLayoutState = msg.arg2;
		if (this.notifyLayoutStateSetChanged(oldLayoutState, nowLayoutState, msg.obj)) {
			this.mOldLayoutState = mTempLayoutState;
			this.mNowLayoutState = oldLayoutState;
		}
		return true;
	}

	public void post(@NonNull Runnable runnable) {
		this.getLayoutStateHandler().post(runnable);
	}

	public void postDelayed(@NonNull Runnable runnable, long delayMillis) {
		this.getLayoutStateHandler().postDelayed(runnable, delayMillis);
	}

	public int getOldLayoutState() {
		return this.mOldLayoutState;
	}

	public int getNowLayoutState() {
		return this.mNowLayoutState;
	}

	public Handler getLayoutStateHandler() {
		if (this.mLayoutStateHandler == null) {
			this.mLayoutStateHandler = new Handler(Looper.getMainLooper(), this);
		}
		return this.mLayoutStateHandler;
	}

	public synchronized void dispatchMessage(int layoutState, @Nullable Object params, long delayMillis) {
		final int oldLayoutState = this.mNowLayoutState;

		if (oldLayoutState != layoutState) {
			this.mTempLayoutState = this.mOldLayoutState;
			this.mOldLayoutState = oldLayoutState;
			this.mNowLayoutState = layoutState;

			final Handler handler = this.getLayoutStateHandler();
			handler.removeMessages(MESSAGE_WHAT_FLAG);

			Message message = handler.obtainMessage(MESSAGE_WHAT_FLAG);
			message.arg1 = oldLayoutState; // old
			message.arg2 = layoutState; // new
			message.obj = params;

			if (delayMillis > 0) {
				handler.sendMessageDelayed(message, delayMillis);
			} else {
				handler.sendMessage(message);
			}
		}
	}

	public void cancel() {
		if (this.mLayoutStateHandler != null) {
			Message message = this.mLayoutStateHandler.obtainMessage(MESSAGE_WHAT_FLAG);
			message.recycle();
			this.mLayoutStateHandler.removeMessages(message.what);
		}
	}

	public void recycle() {
		if (this.mLayoutStateHandler != null) {
			Message message = this.mLayoutStateHandler.obtainMessage(MESSAGE_WHAT_FLAG);
			message.recycle();

			this.mLayoutStateHandler.removeMessages(message.what);
			this.mLayoutStateHandler.removeCallbacksAndMessages(null);
			this.mLayoutStateHandler = null;
		}
	}

	private synchronized boolean notifyLayoutStateSetChanged(int oldLayoutState, int nowLayoutState, @Nullable Object params) {
		return this.mCallback.onLayoutStateChanged(this, oldLayoutState, nowLayoutState, params);
	}

	public interface Callback {

		boolean onLayoutStateChanged(UILayoutStateHandler handler, int oldLayoutState, int nowLayoutState, @Nullable Object params);
	}
}
