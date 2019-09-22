package androidx.sframe.ui.controller.impl;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStore;
import androidx.sframe.R;
import androidx.sframe.helper.ViewModelProviders;
import androidx.sframe.helper.AnnotationHelper;
import androidx.sframe.listener.OnAnimationListener;
import androidx.sframe.tools.AsyncRequest;
import androidx.sframe.ui.controller.UILayoutController;
import androidx.sframe.ui.controller.UIToolbarController;
import androidx.sframe.ui.controller.UIViewController;
import androidx.sframe.widget.SRelativeLayout;

/**
 * Author create by ok on 2019-06-10
 * Email : ok@163.com.
 */
public class UILayoutControllerImpl implements UILayoutController, UILayoutStateHandler.Callback, LifecycleEventObserver, UIViewController.OnFindViewListener, AsyncRequest.Callback<Object> {

	private static final String KEY_CUR_LAYOUT_KEY = "androidx-layout-controller:curLayoutKey";
	private static final String KEY_FIRST_REQUEST_COMPLETED_FLAG = "androidx-layout-controller:isFirstRequestCompletedFlag";
	private static final String KEY_FIRST_LAYOUT_COMPLETED_FLAG = "androidx-layout-controller:isFirstLayoutCompletedFlag";

	private final long LAYOUT_WAIT_DELAY_TIME = 700;

	private final SparseArrayCompat<UILayout> mLayoutPool = new SparseArrayCompat<>();
	private final List<OnLayoutListener> mOnLayoutListeners = new ArrayList<>();

	private final ViewModelStore mViewModelStore = new ViewModelStore();

	private final SRelativeLayout mParentView;
	private final AsyncRequest mAsyncRequest;
	private final UIViewController mViewController;
	private final UILayoutStateHandler mLayoutStateHandler;
	private OnDataSourceListener mOnDataSourceListener;

	private boolean mIsFirstRequestCompletedFlag;
	private boolean mIsFirstLayoutCompletedFlag;
	private boolean mIsSnapStateAsStopFlag;
	private boolean mIsShouldRunWithAsync = false;
	private boolean mIsShouldAutoLayoutMode = true;

	public UILayoutControllerImpl(@NonNull SRelativeLayout parentView) {
		this.mParentView = parentView;
		this.mAsyncRequest = AsyncRequest.get();
		this.mViewController = new UIViewControllerImpl(parentView);
		this.mViewController.addOnFindViewListener(this);
		this.mLayoutStateHandler = new UILayoutStateHandler(this);
	}

	@Override
	public boolean onLayoutStateChanged(UILayoutStateHandler handler, int oldLayoutState, int nowLayoutState, @Nullable Object params) {
		if (this.shouldIgnoreLayoutChanged(nowLayoutState, params)) {
			return true;
		}
		// hide oldLayout
		final boolean mIsShouldHideAnimation = this.shouldHideLayoutAt(oldLayoutState);
		// dispatch layout changed
		if (!this.dispatchLayoutChanged(nowLayoutState, params, mIsShouldHideAnimation)) {
			// unvaild layout
			if (LayoutType.Content.key == nowLayoutState) {
				this.onLayoutChanged(nowLayoutState, params);
			} else {
				this.layoutOfContent();
			}
		}
		return false;
	}

	private boolean shouldIgnoreLayoutChanged(int nowLayoutState, @Nullable Object params) {
		if (LayoutType.Refreshed.key == nowLayoutState) {
			this.onLayoutChanged(nowLayoutState, params);
			return true;
		}
		return false;
	}

	private boolean dispatchLayoutChanged(int nowLayoutState, @Nullable Object params, boolean shouldStartAnimation) {
		final UILayout nowLayout = this.mLayoutPool.get(nowLayoutState);
		if (nowLayout == null || !nowLayout.isLayoutEnabled()) {
			return false;
		}
		final View contentView = nowLayout.getContentView();
		if (contentView.getParent() == null) {
			this.addViewInternal(nowLayout);
		}
		if (contentView.getVisibility() != View.VISIBLE) {
			contentView.setVisibility(View.VISIBLE);
		}
		if (nowLayout.getEnterAnimation() == 0
				|| !shouldStartAnimation) {
			this.onLayoutChanged(nowLayout.getLayoutKey(), params);
		} else {
			final Animation animation = AnimationUtils.loadAnimation(contentView.getContext(), nowLayout.getEnterAnimation());
			animation.setAnimationListener(new OnShowAnimationListener(nowLayout, params));
			contentView.clearAnimation();
			contentView.startAnimation(animation);
		}
		return true;
	}

	@SuppressLint("StaticFieldLeak")
	private void onLayoutChanged(int key, @Nullable Object params) {
		if (this.mIsSnapStateAsStopFlag) {
			return;
		}
		final UILayout nowLayout = this.mLayoutPool.get(key);
		if (nowLayout != null) {
			for (OnLayoutListener listener : this.mOnLayoutListeners) {
				listener.onLayoutChanged(this, nowLayout, params);
			}
		}
		if (LayoutType.Content.key == key) {
			this.mIsFirstLayoutCompletedFlag = true;
		}
		if (this.hasAsyncLoadDataSourceFlag(key)) {
			if (this.mIsShouldRunWithAsync
					|| AnnotationHelper.isShouldRunInAsyncAnn(this.mOnDataSourceListener)) {
				this.mAsyncRequest.execute(params, this);
			} else {
				this.performOnDataSourceChanged(params);
				if (this.mIsShouldAutoLayoutMode) {
					// auto layoutComplete
					this.layoutOfContent(null, LAYOUT_WAIT_DELAY_TIME);
				}
			}
			this.mIsFirstRequestCompletedFlag = true;
		}
	}

	private boolean hasAsyncLoadDataSourceFlag(int key) {
		if (LayoutType.Loading.key == key) {
			return true;
		}
		if (LayoutType.Refreshed.key == key && this.mIsFirstLayoutCompletedFlag) {
			return true;
		}
		return !this.mIsFirstRequestCompletedFlag;
	}

	private boolean shouldHideLayoutAt(int layoutState) {
		final UILayout nowLayout = this.mLayoutPool.get(layoutState);
		if (nowLayout == null || nowLayout.getContentView().getParent() == null) {
			return false;
		}
		final View contentView = nowLayout.getContentView();
		if (contentView.getVisibility() == View.VISIBLE) {
			if (nowLayout.getExitAnimation() == 0
					|| !nowLayout.isLayoutEnabled()) {
				this.shouldHideLayout(nowLayout);
			} else {
				final Animation animation = AnimationUtils.loadAnimation(contentView.getContext(), nowLayout.getExitAnimation());
				animation.setAnimationListener(new OnHideAnimationListener(nowLayout));
				contentView.clearAnimation();
				contentView.startAnimation(animation);
			}
		}
		return true;
	}

	private void shouldHideLayout(@NonNull UILayout nowLayout) {
		final View contentView = nowLayout.getContentView();
		if (LayoutType.Content.key == nowLayout.getLayoutKey()) {
			contentView.setVisibility(View.INVISIBLE);
		} else {
			contentView.setVisibility(View.GONE);
			this.removeViewInParent(contentView);
		}
	}

	@Override
	public int getCurLayoutKey() {
		return this.mLayoutStateHandler.getNowLayoutState();
	}

	@NonNull
	@Override
	public View getParentView() {
		return this.mParentView;
	}

	@Nullable
	@Override
	public UILayout getLayoutAt(int key) {
		return this.mLayoutPool.get(key);
	}

	@NonNull
	@Override
	public UILayout requireLayoutAt(int key) {
		final UILayout nowLayout = this.getLayoutAt(key);
		if (nowLayout == null) {
			throw new IllegalStateException("UILayoutController " + this + " Key(" + key + ") does not have a layout set.");
		}
		return nowLayout;
	}

	@NonNull
	@Override
	public UIViewController getViewController() {
		return this.mViewController;
	}

	@NonNull
	@Override
	public UIToolbarController getToolbarController() {
		return ViewModelProviders.of(this).get(UIToolbarControllerImpl.class);
	}

	@Override
	public UILayoutController setLifecycleOwner(@NonNull LifecycleOwner owner) {
		owner.getLifecycle().addObserver(this);
		return this;
	}

	@Override
	public UILayoutController addOnLayoutListener(@NonNull OnLayoutListener listener) {
		this.mOnLayoutListeners.add(listener);
		return this;
	}

	@Override
	public UILayoutController removeOnLayoutListener(@NonNull OnLayoutListener listener) {
		this.mOnLayoutListeners.remove(listener);
		return this;
	}

	@Override
	public UILayoutController setOnDataSourceListener(@NonNull OnDataSourceListener listener) {
		this.mOnDataSourceListener = listener;
		return this;
	}

	@Override
	public UILayoutController onSaveInstanceState(@NonNull Bundle savedInstanceState) {
		savedInstanceState.putInt(KEY_CUR_LAYOUT_KEY, this.getCurLayoutKey());
		savedInstanceState.putBoolean(KEY_FIRST_REQUEST_COMPLETED_FLAG, this.mIsFirstRequestCompletedFlag);
		savedInstanceState.putBoolean(KEY_FIRST_LAYOUT_COMPLETED_FLAG, this.mIsFirstLayoutCompletedFlag);
		return this;
	}

	@Override
	public UILayoutController onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
		final int curLayoutKey = savedInstanceState.getInt(KEY_CUR_LAYOUT_KEY, this.getCurLayoutKey());
		this.mIsFirstRequestCompletedFlag = savedInstanceState.getBoolean(KEY_FIRST_REQUEST_COMPLETED_FLAG, this.mIsFirstRequestCompletedFlag);
		this.mIsFirstLayoutCompletedFlag = savedInstanceState.getBoolean(KEY_FIRST_LAYOUT_COMPLETED_FLAG, this.mIsFirstLayoutCompletedFlag);
		return this.layoutAt(curLayoutKey);
	}

	@Override
	public UILayoutController setShouldRunWithAsync(boolean shouldRunWithAsync) {
		this.mIsShouldRunWithAsync = shouldRunWithAsync;
		return this;
	}

	@Override
	public UILayoutController setShouldAutoLayoutMode(boolean shouldAutoLayoutMode) {
		this.mIsShouldAutoLayoutMode = shouldAutoLayoutMode;
		return this;
	}

	@Override
	public UILayoutController setBackground(Drawable drawable) {
		this.mParentView.setBackground(drawable);
		return this;
	}

	@Override
	public UILayoutController setBackgroundColor(@ColorInt int color) {
		this.mParentView.setBackgroundColor(color);
		return this;
	}

	@Override
	public UILayoutController setBackgroundResource(@DrawableRes int resId) {
		this.mParentView.setBackgroundResource(resId);
		return this;
	}

	@Override
	public UILayoutController setToolbarLayout(int layoutId) {
		final View preView = this.getViewController().findAtParent().inflate(layoutId, this.mParentView, false);
		return this.setToolbarLayout(preView);
	}

	@Override
	public UILayoutController setToolbarLayout(@NonNull View preView) {
		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		return this.addLayoutInternal(new UILayout.Builder(LayoutType.Toolbar.key)
				.setLayoutSpareId(R.id.app_layout_toolbar_id)
				.setContentView(preView)
				.build(), layoutParams);
	}

	@Override
	public UILayoutController setContentLayout(@LayoutRes int layoutId) {
		final View preView = this.getViewController().findAtParent().inflate(layoutId, this.mParentView, false);
		return this.setContentLayout(preView);
	}

	@Override
	public UILayoutController setContentLayout(@NonNull View preView) {
		ViewGroup.LayoutParams layoutParams = preView.getLayoutParams();
		if (layoutParams == null) {
			layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		}
		return this.addLayoutInternal(new UILayout.Builder(LayoutType.Content.key)
				.setLayoutEnterAnimation(R.anim.anim_alpha_action_v)
				.setLayoutSpareId(R.id.app_layout_content_id)
				.setContentView(preView)
				.build(), layoutParams);
	}

	@Override
	public UILayoutController setLoadingLayout(@LayoutRes int layoutId) {
		final View preView = this.getViewController().findAtParent().inflate(layoutId, this.mParentView, false);
		return this.setLoadingLayout(preView);
	}

	@Override
	public UILayoutController setLoadingLayout(@NonNull View preView) {
		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		return this.addLayoutInternal(new UILayout.Builder(LayoutType.Loading.key)
				.setLayoutEnterAnimation(R.anim.anim_alpha_action_v)
				.setLayoutSpareId(R.id.app_layout_loading_id)
				.setContentView(preView)
				.build(), layoutParams);
	}

	@Override
	public UILayoutController setErrorLayout(@LayoutRes int layoutId) {
		final View preView = this.getViewController().findAtParent().inflate(layoutId, this.mParentView, false);
		return this.setErrorLayout(preView);
	}

	@Override
	public UILayoutController setErrorLayout(@NonNull View preView) {
		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		return this.addLayoutInternal(new UILayout.Builder(LayoutType.Error.key)
				.setLayoutEnterAnimation(R.anim.anim_alpha_action_v)
				.setLayoutSpareId(R.id.app_layout_error_id)
				.setContentView(preView)
				.build(), layoutParams);
	}

	@Override
	public UILayoutController addLayoutInternal(@NonNull UILayout layout) {
		return this.addLayoutInternal(layout, -1);
	}

	@Override
	public UILayoutController addLayoutInternal(@NonNull UILayout layout, int index) {
		return this.addLayoutInternal(layout, index, null);
	}

	@Override
	public UILayoutController addLayoutInternal(@NonNull UILayout layout, @NonNull ViewGroup.LayoutParams params) {
		return this.addLayoutInternal(layout, -1, params);
	}

	@Override
	public UILayoutController addLayoutInternal(@NonNull UILayout layout, int index, @Nullable ViewGroup.LayoutParams params) {
		if (layout.ignoreLayoutDirectPreview()) {
			return this.addViewInternal(layout, index, params);
		}
		final UILayout oldLayout = this.mLayoutPool.get(layout.getLayoutKey());
		final UILayout nowLayout = layout.inflate(this.mParentView);
		this.removeViewInParent(nowLayout.getContentView());
		if (oldLayout == null) {
			this.mLayoutPool.put(nowLayout.getLayoutKey(), nowLayout);
		} else {
			this.mLayoutPool.replace(oldLayout.getLayoutKey(), oldLayout, nowLayout);
			// remove oldLayout
			this.removeViewInParent(oldLayout.getContentView());
			this.getViewController().gc(oldLayout.getContentView());
		}
		if ((nowLayout.getLayoutKey() == LayoutType.Toolbar.key
				|| nowLayout.getLayoutKey() == LayoutType.Content.key
				|| nowLayout.getLayoutKey() == this.getCurLayoutKey()) && nowLayout.isLayoutEnabled()) {
			this.addViewInternal(nowLayout, index, params);
		}
		if (nowLayout.getLayoutKey() == LayoutType.Content.key
				&& nowLayout.getLayoutKey() != this.getCurLayoutKey()) {
			this.shouldHideLayout(nowLayout);
		}
		return this.requestLayout();
	}

	private UILayoutController addViewInternal(@NonNull UILayout nowLayout) {
		return this.addViewInternal(nowLayout, -1, null);
	}

	private UILayoutController addViewInternal(@NonNull UILayout nowLayout, int index, @Nullable ViewGroup.LayoutParams params) {
		if (LayoutType.Content.key == nowLayout.getLayoutKey()) {
			this.mParentView.addViewInternal(nowLayout.getContentView(), 0, params);
		} else {
			this.mParentView.addViewInternal(nowLayout.getContentView(), index, params);
		}
		return this.requestLayout();
	}

	@Override
	public UILayoutController setLayoutEnabledAt(int key, boolean flag) {
		final UILayout oldlayout = this.requireLayoutAt(key);
		final UILayout nowLayout = oldlayout.build().setLayoutEnabled(flag).build();
		this.mLayoutPool.replace(key, oldlayout, nowLayout);
		return this.requestLayout();
	}

	@Override
	public UILayoutController setToolbarLayoutEnabled(boolean flag) {
		final UILayout nowLayout = this.requireLayoutAt(LayoutType.Toolbar.key);
		if (flag) {
			nowLayout.getContentView().setVisibility(View.VISIBLE);
		} else {
			nowLayout.getContentView().setVisibility(View.GONE);
		}
		return this;
	}

	@Override
	public UILayoutController setContentLayoutEnabled(boolean flag) {
		return this.setLayoutEnabledAt(LayoutType.Content.key, flag);
	}

	@Override
	public UILayoutController setLoadingLayoutEnabled(boolean flag) {
		return this.setLayoutEnabledAt(LayoutType.Loading.key, flag);
	}

	@Override
	public UILayoutController setErrorLayoutEnabled(boolean flag) {
		return this.setLayoutEnabledAt(LayoutType.Error.key, flag);
	}

	@Override
	public UILayoutController setLayoutStableModeAt(int key, boolean flag) {
		final UILayout oldLayout = this.getLayoutAt(key);
		if (oldLayout != null) {
			final UILayout nowLayout = oldLayout.build().setLayoutStableMode(flag).build();
			this.mLayoutPool.replace(key, oldLayout, nowLayout);
			return this.requestLayout();
		}
		return this;
	}

	@Override
	public UILayoutController setToolbarLayoutStableMode(boolean flag) {
		return this.setLayoutStableModeAt(LayoutType.Toolbar.key, flag);
	}

	@Override
	public UILayoutController setContentLayoutStableMode(boolean flag) {
		return this.setLayoutStableModeAt(LayoutType.Content.key, flag);
	}

	@Override
	public UILayoutController setLoadingLayoutStableMode(boolean flag) {
		return this.setLayoutStableModeAt(LayoutType.Loading.key, flag);
	}

	@Override
	public UILayoutController setErrorLayoutStableMode(boolean flag) {
		return this.setLayoutStableModeAt(LayoutType.Error.key, flag);
	}

	@Override
	public UILayoutController setLayoutAnimationAt(int key, int enterAnimation, int exitAnimation) {
		final UILayout oldLayout = this.requireLayoutAt(key);
		final UILayout nowLayout = oldLayout.build()
				.setLayoutEnterAnimation(enterAnimation)
				.setLayoutExitAnimation(exitAnimation)
				.build();
		this.mLayoutPool.replace(key, oldLayout, nowLayout);
		return this;
	}

	@Override
	public UILayoutController setContentLayoutAnimation(int enterAnimation, int exitAnimation) {
		return this.setLayoutAnimationAt(LayoutType.Content.key, enterAnimation, exitAnimation);
	}

	@Override
	public UILayoutController setLoadingLayoutAnimation(int enterAnimation, int exitAnimation) {
		return this.setLayoutAnimationAt(LayoutType.Loading.key, enterAnimation, exitAnimation);
	}

	@Override
	public UILayoutController setErrorLayoutAnimation(int enterAnimation, int exitAnimation) {
		return this.setLayoutAnimationAt(LayoutType.Error.key, enterAnimation, exitAnimation);
	}

	@Override
	public UILayoutController layoutAt(int key) {
		return this.layoutAt(key, null);
	}

	@Override
	public UILayoutController layoutAt(int key, Object object) {
		return this.layoutAt(key, object, 0);
	}

	@Override
	public UILayoutController layoutAt(int key, Object object, long delayMillis) {
		if (this.mIsSnapStateAsStopFlag) {
			return this;
		}
		this.mLayoutStateHandler.dispatchMessage(key, object, delayMillis);
		return this;
	}

	@Override
	public UILayoutController layoutOfContent() {
		return this.layoutOfContent(null);
	}

	@Override
	public UILayoutController layoutOfContent(Object object) {
		return this.layoutOfContent(object, 0);
	}

	@Override
	public UILayoutController layoutOfContent(Object object, long delayMillis) {
		return this.layoutAt(LayoutType.Content.key, object, delayMillis);
	}

	@Override
	public UILayoutController layoutOfLoading() {
		return this.layoutOfLoading(null);
	}

	@Override
	public UILayoutController layoutOfLoading(Object object) {
		return this.layoutOfLoading(object, 0);
	}

	@Override
	public UILayoutController layoutOfLoading(Object object, long delayMillis) {
		return this.layoutAt(LayoutType.Loading.key, object, delayMillis);
	}

	@Override
	public UILayoutController layoutOfError() {
		return this.layoutOfError(null);
	}

	@Override
	public UILayoutController layoutOfError(Object object) {
		return this.layoutOfError(object, 0);
	}

	@Override
	public UILayoutController layoutOfError(Object object, long delayMillis) {
		return this.layoutAt(LayoutType.Error.key, object, delayMillis);
	}

	@Override
	public UILayoutController refreshed() {
		return this.refreshed(null);
	}

	@Override
	public UILayoutController refreshed(@Nullable Object object) {
		return this.refreshed(object, 0);
	}

	@Override
	public UILayoutController refreshed(@Nullable Object object, long delayMillis) {
		return this.layoutAt(LayoutType.Refreshed.key, object, delayMillis);
	}

	@Override
	public UILayoutController recycle() {
		this.mAsyncRequest.cancel();
		this.mViewModelStore.clear();
		this.mOnLayoutListeners.clear();
		this.mLayoutStateHandler.recycle();
		this.mOnDataSourceListener = null;
		this.mViewController.removeOnFindViewListener(this);
		this.mViewController.gc();
		this.mLayoutPool.clear();
		return this;
	}

	@Override
	public UILayoutController requestLayout() {
		for (int index = 0; index < this.mLayoutPool.size(); index++) {
			final UILayout nowLayout = this.mLayoutPool.valueAt(index);
			nowLayout.requestLayout(this);
		}
		this.mParentView.requestLayout();
		return this;
	}

	@Override
	public boolean hasContainsKey(int key) {
		return this.mLayoutPool.containsKey(key);
	}

	@NonNull
	@Override
	public String toString() {
		return "\n\n==========UILayoutController Start==========\n" +
				"LayoutSize : " + this.mLayoutPool.size() + "\n" +
				"CurLayoutKey : " + this.getCurLayoutKey() + "\n" +
				"ViewModelStore : " + this.mViewModelStore.toString() + "\n" +
				"==========UILayoutController End==========\n\n";
	}

	/**
	 * Called when a state transition event happens.
	 *
	 * @param source The source of the event
	 * @param event  The event
	 */
	@Override
	public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
		if (Lifecycle.Event.ON_STOP == event) {
			this.mIsSnapStateAsStopFlag = true;
			this.mAsyncRequest.cancel();
			this.mLayoutStateHandler.cancel();
		} else if (Lifecycle.Event.ON_DESTROY != event) {
			this.mIsSnapStateAsStopFlag = false;
		}
	}

	@Override
	public <T extends View> T findViewById(@IdRes int id) {
		View preView = this.mParentView.findViewById(id);
		for (int index = this.mLayoutPool.size() - 1; index >= 0 && preView == null; index--) {
			UILayout uiLayout = this.mLayoutPool.valueAt(index);
			if (uiLayout == null) {
				continue;
			}
			preView = uiLayout.getContentView().findViewById(id);
		}
		return (T) preView;
	}

	@Override
	public void doInBackground(@Nullable Object params) {
		this.performOnDataSourceChanged(params);
	}

	@Override
	public void onPostExecute(@Nullable Object params) {
		if (this.mIsShouldAutoLayoutMode) {
			this.layoutOfContent(null, LAYOUT_WAIT_DELAY_TIME);
		}
	}

	@NonNull
	@Override
	public ViewModelStore getViewModelStore() {
		return this.mViewModelStore;
	}

	private synchronized void performOnDataSourceChanged(@Nullable Object params) {
		if (this.mOnDataSourceListener != null) {
			this.mOnDataSourceListener.onDataSourceChanged(this, params);
		}
	}

	private void removeViewInParent(@Nullable View preView) {
		if (preView == null || preView.getParent() == null) {
			return;
		}
		final ViewGroup parent = (ViewGroup) preView.getParent();
		parent.removeView(preView);
	}

	private final class OnShowAnimationListener extends OnAnimationListener {

		private final UILayout nowLayout;
		private final Object params;

		OnShowAnimationListener(@NonNull UILayout nowLayout, @Nullable Object params) {
			this.nowLayout = nowLayout;
			this.params = params;
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			onLayoutChanged(this.nowLayout.getLayoutKey(), this.params);
		}
	}

	private final class OnHideAnimationListener extends OnAnimationListener implements Runnable {

		private final UILayout nowLayout;

		OnHideAnimationListener(@NonNull UILayout nowLayout) {
			this.nowLayout = nowLayout;
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			UILayoutControllerImpl.this.mLayoutStateHandler.post(this);
		}

		@Override
		public void run() {
			UILayoutControllerImpl.this.shouldHideLayout(this.nowLayout);
		}
	}
}
