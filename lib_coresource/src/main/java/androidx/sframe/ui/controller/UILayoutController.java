package androidx.sframe.ui.controller;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.AnimRes;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sframe.ui.controller.impl.UILayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStoreOwner;

/**
 * Author create by ok on 2019-06-10
 * Email : ok@163.com.
 */
public interface UILayoutController extends ViewModelStoreOwner {

	int getCurLayoutKey();

	@NonNull
	View getParentView();

	@Nullable
	UILayout getLayoutAt(int key);

	@NonNull
	UILayout requireLayoutAt(int key);

	@NonNull
	UIViewController getViewController();

	@NonNull
	UIToolbarController getToolbarController();

	UILayoutController setLifecycleOwner(@NonNull LifecycleOwner owner);

	UILayoutController addOnLayoutListener(@NonNull OnLayoutListener listener);

	UILayoutController removeOnLayoutListener(@NonNull OnLayoutListener listener);

	UILayoutController setOnDataSourceListener(@NonNull OnDataSourceListener listener);

	UILayoutController onSaveInstanceState(@NonNull Bundle savedInstanceState);

	UILayoutController restoreState(@NonNull Bundle savedInstanceState);

	UILayoutController setShouldAsyncRefreshed(boolean shouldAsyncRefreshed);

	UILayoutController setShouldManualLayoutMode(boolean shouldManualLayoutMode);

	UILayoutController setBackground(Drawable drawable);

	UILayoutController setBackgroundColor(@ColorInt int color);

	UILayoutController setBackgroundResource(@DrawableRes int resId);

	UILayoutController setToolbarLayout(@LayoutRes int layoutId);

	UILayoutController setToolbarLayout(@NonNull View preView);

	UILayoutController setContentLayout(@LayoutRes int layoutId);

	UILayoutController setContentLayout(@NonNull View preView);

	UILayoutController setLoadingLayout(@LayoutRes int layoutId);

	UILayoutController setLoadingLayout(@NonNull View preView);

	UILayoutController setErrorLayout(@LayoutRes int layoutId);

	UILayoutController setErrorLayout(@NonNull View preView);

	UILayoutController addLayoutInternal(int key, @LayoutRes int layoutId);

	UILayoutController addLayoutInternal(int key, @NonNull View preView);

	UILayoutController addLayoutInternal(@NonNull UILayout layout);

	UILayoutController setLayoutEnabledAt(int key, boolean flag);

	UILayoutController setToolbarLayoutEnabled(boolean flag);

	UILayoutController setContentLayoutEnabled(boolean flag);

	UILayoutController setLoadingLayoutEnabled(boolean flag);

	UILayoutController setErrorLayoutEnabled(boolean flag);

	UILayoutController setLayoutStableModeAt(int key, boolean flag);

	UILayoutController setToolbarLayoutStableMode(boolean flag);

	UILayoutController setContentLayoutStableMode(boolean flag);

	UILayoutController setLoadingLayoutStableMode(boolean flag);

	UILayoutController setErrorLayoutStableMode(boolean flag);

	UILayoutController setLayoutAnimationAt(int key, @AnimRes int enterAnimation, @AnimRes int exitAnimation);

	UILayoutController setContentLayoutAnimation(@AnimRes int enterAnimation, @AnimRes int exitAnimation);

	UILayoutController setLoadingLayoutAnimation(@AnimRes int enterAnimation, @AnimRes int exitAnimation);

	UILayoutController setErrorLayoutAnimation(@AnimRes int enterAnimation, @AnimRes int exitAnimation);

	UILayoutController layoutAt(int key);

	UILayoutController layoutAt(int key, @Nullable Object object);

	UILayoutController layoutAt(int key, @Nullable Object object, long delayMillis);

	UILayoutController layoutOfContent();

	UILayoutController layoutOfContent(@Nullable Object object);

	UILayoutController layoutOfContent(@Nullable Object object, long delayMillis);

	UILayoutController layoutOfLoading();

	UILayoutController layoutOfLoading(@Nullable Object object);

	UILayoutController layoutOfLoading(@Nullable Object object, long delayMillis);

	UILayoutController layoutOfError();

	UILayoutController layoutOfError(@Nullable Object object);

	UILayoutController layoutOfError(@Nullable Object object, long delayMillis);

	UILayoutController refreshed();

	UILayoutController refreshed(@Nullable Object object);

	UILayoutController refreshed(@Nullable Object object, long delayMillis);

	UILayoutController recycle();

	UILayoutController requestLayout();

	boolean hasContainsKey(int key);

	enum LayoutType {

		None(-1), Refreshed(0), Toolbar(1), Content(2), Loading(3), Error(4);

		public int key;

		LayoutType(int key) {
			this.key = key;
		}
	}

	interface OnDataSourceListener {

		void onDataSourceChanged(@NonNull UILayoutController layoutController, @Nullable Object params);
	}

	interface OnLayoutListener {

		void onLayoutChanged(@NonNull UILayoutController layoutController, @NonNull UILayout layout, @Nullable Object object);
	}
}
