package androidx.sframe.ui.controller.impl;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.sframe.ui.controller.UIViewController;
import androidx.demon.widget.RefreshLayout;
import androidx.sframe.widget.RefreshLoadView;
import androidx.demon.widget.RefreshMode;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Author create by ok on 2019/5/10
 * Email : ok@163.com.
 */
public class UIViewMethod<V extends View> extends AbsUIViewMethod<V> {

	protected UIViewMethod(@NonNull UIViewController controller, @Nullable V preView) {
		super(controller, preView);
	}

	public UIViewMethod<V> gc() {
		return this.gc(this.getPreView());
	}

	public UIViewMethod<V> gc(@NonNull View preView) {
		this.getViewController().gc(preView);
		return this;
	}

	public UIViewMethod<V> post(@NonNull Runnable run) {
		this.getViewController().post(run);
		return this;
	}

	public UIViewMethod<V> postDelayed(@NonNull Runnable run, long delayMillis) {
		this.getViewController().postDelayed(run, delayMillis);
		return this;
	}

	public UIViewMethod<V> setFitsSystemWindows(boolean fitSystemWindows) {
		this.getPreView().setFitsSystemWindows(fitSystemWindows);
		return this;
	}

	public UIViewMethod<V> setVisibility(int visibility) {
		this.getPreView().setVisibility(visibility);
		return this;
	}

	public UIViewMethod<V> setBackground(Drawable drawable) {
		this.getPreView().setBackground(drawable);
		return this;
	}

	public UIViewMethod<V> setBackgroundColor(@ColorInt int color) {
		this.getPreView().setBackgroundColor(color);
		return this;
	}

	public UIViewMethod<V> setBackgroundResource(@DrawableRes int resid) {
		this.getPreView().setBackgroundResource(resid);
		return this;
	}

	public UIViewMethod<V> setLayoutParams(ViewGroup.LayoutParams params) {
		this.getPreView().setLayoutParams(params);
		return this;
	}

	public UIViewMethod<V> setAnimation(Animation animation) {
		this.getPreView().setAnimation(animation);
		return this;
	}

	public UIViewMethod<V> setPadding(int left, int top, int right, int bottom) {
		this.getPreView().setPadding(left, top, right, bottom);
		return this;
	}

	public UIViewMethod<V> setAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
		this.getPreView().setAlpha(alpha);
		return this;
	}

	public UIViewMethod<V> setEnabled(boolean enabled) {
		this.getPreView().setEnabled(enabled);
		return this;
	}

	public UIViewMethod<V> setClickable(boolean clickable) {
		this.getPreView().setClickable(clickable);
		return this;
	}

	public UIViewMethod<V> setPressed(boolean pressed) {
		this.getPreView().setPressed(pressed);
		return this;
	}

	public UIViewMethod<V> setSelected(boolean selected) {
		this.getPreView().setSelected(selected);
		return this;
	}

	public UIViewMethod<V> setActivated(boolean activated) {
		this.getPreView().setActivated(activated);
		return this;
	}

	public UIViewMethod<V> setFocusable(boolean focusable) {
		this.getPreView().setFocusable(focusable);
		return this;
	}

	public UIViewMethod<V> setFocusableInTouchMode(boolean focusableInTouchMode) {
		this.getPreView().setFocusableInTouchMode(focusableInTouchMode);
		return this;
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	public UIViewMethod<V> setNestedScrollingEnabled(boolean enabled) {
		this.getPreView().setNestedScrollingEnabled(enabled);
		return this;
	}

	public UIViewMethod<V> setOnKeyListener(View.OnKeyListener listener) {
		this.getPreView().setOnKeyListener(listener);
		return this;
	}

	@SuppressLint("ClickableViewAccessibility")
	public UIViewMethod<V> setOnTouchListener(View.OnTouchListener listener) {
		this.getPreView().setOnTouchListener(listener);
		return this;
	}

	public UIViewMethod<V> setOnClickListener(View.OnClickListener listener) {
		this.getPreView().setOnClickListener(listener);
		return this;
	}

	public UIViewMethod<V> setOnLongClickListener(View.OnLongClickListener listener) {
		this.getPreView().setOnLongClickListener(listener);
		return this;
	}

	public UIViewMethod<V> addOnLayoutChangeListener(View.OnLayoutChangeListener listener) {
		this.getPreView().addOnLayoutChangeListener(listener);
		return this;
	}

	public UIViewMethod<V> removeOnLayoutChangeListener(View.OnLayoutChangeListener listener) {
		this.getPreView().removeOnLayoutChangeListener(listener);
		return this;
	}

	public UIViewMethod<V> addOnAttachStateChangeListener(View.OnAttachStateChangeListener listener) {
		this.getPreView().addOnAttachStateChangeListener(listener);
		return this;
	}

	public UIViewMethod<V> removeOnAttachStateChangeListener(View.OnAttachStateChangeListener listener) {
		this.getPreView().removeOnAttachStateChangeListener(listener);
		return this;
	}

	public LayoutInflater getLayoutInflater() {
		return LayoutInflater.from(getContext());
	}

	public View inflate(int resource, @Nullable ViewGroup root) {
		return inflate(resource, root, root != null);
	}

	public View inflate(int resource, @Nullable ViewGroup root, boolean attachToRoot) {
		return getLayoutInflater().inflate(resource, root, attachToRoot);
	}

	public UITextViewMethod methodAtTextView() {
		return this.get(UITextViewMethod.class);
	}

	public UIImageViewMethod methodAtImageView() {
		return this.get(UIImageViewMethod.class);
	}

	public UIViewPagerMethod methodAtViewPager() {
		return this.get(UIViewPagerMethod.class);
	}

	public UIAppBarLayoutMethod methodAtAppBarLayout() {
		return this.get(UIAppBarLayoutMethod.class);
	}

	public UIRecyclerViewMethod methodAtRecyclerView() {
		return this.get(UIRecyclerViewMethod.class);
	}

	public UIRefreshLayoutMethod methodAtRefreshLayout() {
		return this.get(UIRefreshLayoutMethod.class);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static class UITextViewMethod extends UIViewMethod<TextView> {

		protected UITextViewMethod(@NonNull UIViewController controller, @Nullable TextView preView) {
			super(controller, preView);
		}

		public UITextViewMethod setText(@StringRes int resId) {
			this.getPreView().setText(resId);
			return this;
		}

		public UITextViewMethod setText(@StringRes int resId, TextView.BufferType type) {
			this.getPreView().setText(resId, type);
			return this;
		}

		public UITextViewMethod setText(@NonNull CharSequence text) {
			this.getPreView().setText(text);
			return this;
		}

		public UITextViewMethod setText(@NonNull CharSequence text, TextView.BufferType type) {
			this.getPreView().setText(text, type);
			return this;
		}

		public UITextViewMethod setTextSize(float size) {
			this.getPreView().setTextSize(size);
			return this;
		}

		public UITextViewMethod setTextSize(int unit, float size) {
			this.getPreView().setTextSize(unit, size);
			return this;
		}

		public UITextViewMethod setTextColor(@ColorInt int color) {
			this.getPreView().setTextColor(color);
			return this;
		}

		public UITextViewMethod setGravity(int gravity) {
			this.getPreView().setGravity(gravity);
			return this;
		}
	}

	public static class UIImageViewMethod extends UIViewMethod<ImageView> {

		protected UIImageViewMethod(@NonNull UIViewController controller, @Nullable ImageView preView) {
			super(controller, preView);
		}

		public UIImageViewMethod setImage(Object url) {
			Glide.with(this.getContext())
					.load(url).into(this.getPreView());
			return this;
		}

		public UIImageViewMethod setImageBitmap(Bitmap bitmap) {
			this.getPreView().setImageBitmap(bitmap);
			return this;
		}

		public UIImageViewMethod setImageDrawable(Drawable drawable) {
			this.getPreView().setImageDrawable(drawable);
			return this;
		}

		public UIImageViewMethod setImageResource(@DrawableRes int resId) {
			this.getPreView().setImageResource(resId);
			return this;
		}

		@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
		public UIImageViewMethod setImageTintList(@Nullable ColorStateList tint) {
			this.getPreView().setImageTintList(tint);
			return this;
		}

		public UIImageViewMethod setImageAlpha(int alpha) {
			this.getPreView().setImageAlpha(alpha);
			return this;
		}
	}

	public static class UIViewPagerMethod extends UIViewMethod<ViewPager> {

		protected UIViewPagerMethod(@NonNull UIViewController controller, @Nullable ViewPager preView) {
			super(controller, preView);
		}

		public UIViewPagerMethod setAdapter(@Nullable PagerAdapter adapter) {
			this.getPreView().setAdapter(adapter);
			return this;
		}

		public UIViewPagerMethod setPageTransformer(boolean reverseDrawingOrder, @Nullable ViewPager.PageTransformer transformer) {
			this.getPreView().setPageTransformer(reverseDrawingOrder, transformer);
			return this;
		}

		public UIViewPagerMethod setPageTransformer(boolean reverseDrawingOrder, @Nullable ViewPager.PageTransformer transformer, int pageLayerType) {
			this.getPreView().setPageTransformer(reverseDrawingOrder, transformer, pageLayerType);
			return this;
		}

		public UIViewPagerMethod setCurrentItem(int item) {
			this.getPreView().setCurrentItem(item);
			return this;
		}

		public UIViewPagerMethod setCurrentItem(int item, boolean smoothScroll) {
			this.getPreView().setCurrentItem(item, smoothScroll);
			return this;
		}

		public UIViewPagerMethod setOffscreenPageLimit(int pageLimit) {
			this.getPreView().setOffscreenPageLimit(pageLimit);
			return this;
		}

		public UIViewPagerMethod addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
			this.getPreView().addOnPageChangeListener(listener);
			return this;
		}

		public UIViewPagerMethod removeOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
			this.getPreView().removeOnPageChangeListener(listener);
			return this;
		}

		public UIViewPagerMethod addOnAdapterChangeListener(ViewPager.OnAdapterChangeListener listener) {
			this.getPreView().addOnAdapterChangeListener(listener);
			return this;
		}

		public UIViewPagerMethod removeOnAdapterChangeListener(ViewPager.OnAdapterChangeListener listener) {
			this.getPreView().removeOnAdapterChangeListener(listener);
			return this;
		}
	}

	public static class UIAppBarLayoutMethod extends UIViewMethod<AppBarLayout> {

		protected UIAppBarLayoutMethod(@NonNull UIViewController controller, @Nullable AppBarLayout preView) {
			super(controller, preView);
		}

		public UIAppBarLayoutMethod addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener listener) {
			this.getPreView().addOnOffsetChangedListener(listener);
			return this;
		}

		public UIAppBarLayoutMethod removeOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener listener) {
			this.getPreView().removeOnOffsetChangedListener(listener);
			return this;
		}
	}

	public static class UIRecyclerViewMethod extends UIViewMethod<RecyclerView> {

		protected UIRecyclerViewMethod(@NonNull UIViewController controller, @Nullable RecyclerView preView) {
			super(controller, preView);
		}

		public UIRecyclerViewMethod setLayoutManager(RecyclerView.LayoutManager layoutManager) {
			this.getPreView().setLayoutManager(layoutManager);
			return this;
		}

		public UIRecyclerViewMethod setItemAnimator(@Nullable RecyclerView.ItemAnimator animator) {
			this.getPreView().setItemAnimator(animator);
			return this;
		}

		public UIRecyclerViewMethod swapAdapter(@Nullable RecyclerView.Adapter adapter, boolean removeAndRecycleExistingViews) {
			this.getPreView().swapAdapter(adapter, removeAndRecycleExistingViews);
			return this;
		}

		public UIRecyclerViewMethod setAdapter(@Nullable RecyclerView.Adapter adapter) {
			this.getPreView().setAdapter(adapter);
			return this;
		}

		public UIRecyclerViewMethod setHasFixedSize(boolean hasFixedSize) {
			this.getPreView().setHasFixedSize(hasFixedSize);
			return this;
		}

		public UIRecyclerViewMethod setItemViewCacheSize(int size) {
			this.getPreView().setItemViewCacheSize(size);
			return this;
		}

		public UIRecyclerViewMethod setClipToPadding(boolean clipToPadding) {
			this.getPreView().setClipToPadding(clipToPadding);
			return this;
		}

		public UIRecyclerViewMethod setOnFlingListener(@Nullable RecyclerView.OnFlingListener listener) {
			this.getPreView().setOnFlingListener(listener);
			return this;
		}

		public UIRecyclerViewMethod setRecyclerListener(@Nullable RecyclerView.RecyclerListener listener) {
			this.getPreView().setRecyclerListener(listener);
			return this;
		}

		public UIRecyclerViewMethod addOnScrollListener(@NonNull RecyclerView.OnScrollListener listener) {
			this.getPreView().addOnScrollListener(listener);
			return this;
		}

		public UIRecyclerViewMethod removeOnScrollListener(@NonNull RecyclerView.OnScrollListener listener) {
			this.getPreView().removeOnScrollListener(listener);
			return this;
		}

		public UIRecyclerViewMethod addOnItemTouchListener(@NonNull RecyclerView.OnItemTouchListener listener) {
			this.getPreView().addOnItemTouchListener(listener);
			return this;
		}

		public UIRecyclerViewMethod removeOnItemTouchListener(@NonNull RecyclerView.OnItemTouchListener listener) {
			this.getPreView().removeOnItemTouchListener(listener);
			return this;
		}

		public UIRecyclerViewMethod addItemDecoration(@NonNull RecyclerView.ItemDecoration decor) {
			this.getPreView().addItemDecoration(decor);
			return this;
		}

		public UIRecyclerViewMethod addItemDecoration(@NonNull RecyclerView.ItemDecoration decor, int index) {
			this.getPreView().addItemDecoration(decor, index);
			return this;
		}

		public UIRecyclerViewMethod removeItemDecoration(@NonNull RecyclerView.ItemDecoration decor) {
			this.getPreView().removeItemDecoration(decor);
			return this;
		}

		public UIRecyclerViewMethod removeItemDecorationAt(int index) {
			this.getPreView().removeItemDecorationAt(index);
			return this;
		}
	}

	public static class UIRefreshLayoutMethod extends UIViewMethod<RefreshLayout> {

		protected UIRefreshLayoutMethod(@NonNull UIViewController controller, @Nullable RefreshLayout preView) {
			super(controller, preView);
		}

		public UIRefreshLayoutMethod setRefreshing(boolean refreshing, long delayMillis) {
			this.getPreView().setRefreshing(refreshing, delayMillis);
			return this;
		}

		public UIRefreshLayoutMethod setRefreshing(boolean refreshing) {
			this.getPreView().setRefreshing(refreshing);
			return this;
		}

		public UIRefreshLayoutMethod setDraggingToStart(boolean dragEnabled) {
			this.getPreView().setDraggingToStart(dragEnabled);
			return this;
		}

		public UIRefreshLayoutMethod setDraggingToEnd(boolean dragEnabled) {
			this.getPreView().setDraggingToEnd(dragEnabled);
			return this;
		}

		public UIRefreshLayoutMethod setOrientation(int orientation) {
			this.getPreView().setOrientation(orientation);
			return this;
		}

		public UIRefreshLayoutMethod setRefreshMode(RefreshMode refreshMode) {
			this.getPreView().setRefreshMode(refreshMode);
			return this;
		}

		public UIRefreshLayoutMethod setOnRefreshListener(RefreshLayout.OnRefreshListener listener) {
			this.getPreView().setOnRefreshListener(listener);
			return this;
		}

		public UIRefreshLayoutMethod addOnScrollListener(RefreshLayout.OnScrollListener listener) {
			this.getPreView().addOnScrollListener(listener);
			return this;
		}

		public UIRefreshLayoutMethod removeOnScrollListener(RefreshLayout.OnScrollListener listener) {
			this.getPreView().removeOnScrollListener(listener);
			return this;
		}

		public UIRefreshLayoutMethod setOnChildScrollCallback(RefreshLayout.OnChildScrollCallback callback) {
			this.getPreView().setOnChildScrollCallback(callback);
			return this;
		}

		public UIRefreshLayoutMethod setFrictionRatio(@FloatRange(from = 0.1f, to = 1.f) float frictionRatio) {
			this.getPreView().setFrictionRatio(frictionRatio);
			return this;
		}

		public <LoadView extends RefreshLoadView> UIRefreshLayoutMethod setHeaderLoadView(@NonNull LoadView loadView) {
			this.getPreView().setHeaderLoadView(loadView);
			return this;
		}

		public <LoadView extends RefreshLoadView> UIRefreshLayoutMethod setFooterLoadView(@NonNull LoadView loadView) {
			this.getPreView().setFooterLoadView(loadView);
			return this;
		}

		public UIRefreshLayoutMethod setHeaderScrollStyleMode(int scrollStyleMode) {
			this.getPreView().setHeaderScrollStyleMode(scrollStyleMode);
			return this;
		}

		public UIRefreshLayoutMethod setFooterScrollStyleMode(int scrollStyleMode) {
			this.getPreView().setFooterScrollStyleMode(scrollStyleMode);
			return this;
		}

		public UIRefreshLayoutMethod setHeaderRefreshTips(boolean refreshTips) {
			final RefreshLayout.LoadView loadView = this.getPreView().getHeaderLoadView();
			if (loadView instanceof RefreshLoadView) {
				((RefreshLoadView) loadView).setRefreshTips(refreshTips);
			}
			return this;
		}

		public UIRefreshLayoutMethod setFooterRefreshTips(boolean refreshTips) {
			final RefreshLayout.LoadView loadView = this.getPreView().getFooterLoadView();
			if (loadView instanceof RefreshLoadView) {
				((RefreshLoadView) loadView).setRefreshTips(refreshTips);
			}
			return this;
		}
	}
}
