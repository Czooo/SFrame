package androidx.sframe.ui.controller.impl;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;
import androidx.sframe.ui.controller.UIViewController;

/**
 * Author create by ok on 2019-06-14
 * Email : ok@163.com.
 */
public class UIViewControllerImpl implements UIViewController {

	private final ArrayList<OnFindViewListener> mFindViewListeners = new ArrayList<>();
	private final SparseArrayCompat<View> mViewPool = new SparseArrayCompat<>();
	private final Handler mViewHandler = new Handler(Looper.getMainLooper());
	private final View mParentView;
	private UIViewMethod<View> mViewMethod;

	public UIViewControllerImpl(@NonNull View parentView) {
		this.mParentView = parentView;
	}

	@Override
	public <V extends View> V getView() {
		return (V) this.mParentView;
	}

	@Override
	public <V extends View> V set(@NonNull Class<V> viewClass) {
		try {
			if (viewClass.isInstance(this.mParentView)) {
				return (V) this.mParentView;
			}
			throw new ClassCastException(this.mParentView.getClass().getName() + " cannot be cast to " + viewClass.getName());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <V extends View> V findViewById(@IdRes int id) {
		View preView = this.mViewPool.get(id);
		if (preView == null) {
			preView = this.mParentView.findViewById(id);
			int index = 0;
			while (preView == null && index < this.mFindViewListeners.size()) {
				preView = this.mFindViewListeners.get(index).findViewById(id);
				index++;
			}
			if (preView != null) {
				this.mViewPool.put(id, preView);
			}
		}
		return (V) preView;
	}

	@Override
	public Context getContext() {
		return this.mParentView.getContext();
	}

	@Override
	public UIViewMethod<View> findAtParent() {
		return this.findAt(this.mParentView);
	}

	@Override
	public UIViewMethod<View> findAt(@IdRes int id) {
		return this.findAt(this.findViewById(id));
	}

	@Override
	public UIViewMethod<View> findAt(@NonNull View preView) {
		if (this.mViewMethod != null) {
			this.mViewMethod.recycled();
		}
		this.mViewMethod = new UIViewMethod<>(this, preView);
		return this.mViewMethod;
	}

	@Override
	public UIViewController addOnFindViewListener(@NonNull OnFindViewListener listener) {
		this.mFindViewListeners.add(listener);
		return this;
	}

	@Override
	public UIViewController removeOnFindViewListener(@NonNull OnFindViewListener listener) {
		this.mFindViewListeners.remove(listener);
		return this;
	}

	@Override
	public UIViewController post(@NonNull Runnable runnable) {
		this.mViewHandler.post(runnable);
		return this;
	}

	@Override
	public UIViewController postDelayed(@NonNull Runnable runnable, long delayMillis) {
		this.mViewHandler.postDelayed(runnable, delayMillis);
		return this;
	}

	@Override
	public UIViewController gc() {
		return this.gc(this.mParentView);
	}

	@Override
	public UIViewController gc(@Nullable View preView) {
		if (preView == null) {
			return this;
		}
		this.mViewPool.remove(preView.getId());
		if (preView instanceof ViewGroup) {
			ViewGroup preViewGroup = (ViewGroup) preView;
			for (int index = 0; index < preViewGroup.getChildCount(); index++) {
				this.gc(preViewGroup.getChildAt(index));
			}
		}
		return this;
	}

	@Override
	public UIViewController recycled() {
		this.mViewHandler.removeCallbacksAndMessages(null);
		if (this.mViewMethod != null) {
			this.mViewMethod.recycled();
			this.mViewMethod = null;
		}
		this.gc();
		return this;
	}
}
